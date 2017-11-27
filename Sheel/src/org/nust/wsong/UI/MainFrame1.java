package org.nust.wsong.UI;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.text.DecimalFormat;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import org.nust.wsong.UI.experiment.MyJFileChooser;
import org.nust.wsong.algorithm.Algorithm1;
import org.nust.wsong.model.Trace;
import org.nust.wsong.util.LogUtils;
import org.nust.wsong.util.StringDistance;

/**
 * @description sheel法
 * @author xxx
 * 
 */
public class MainFrame1 extends JFrame implements ActionListener {

	private static final long serialVersionUID = 1L;

	JButton jbtclog;
	JButton jbtdlog;
	JButton jbtrecovery;
	JTextArea jtaclog;
	JTextArea jtadlog;
	List<Trace> dTrace = null;// 偏差日志
	List<Trace> cTraces = null;// 正确日志
	List<Trace> repairedTraces = null;// 修复日志
	String path;
	String dTraceFileName;

	public MainFrame1() {
		JPanel p1 = new JPanel(new GridLayout(2, 3, 5, 5));
		p1.add(new JLabel("Compliant Log:"));

		jtaclog = new JTextArea(1, 15);
		jtaclog.setEditable(false);
		p1.add(jtaclog);

		jbtclog = new JButton("Open");
		jbtclog.addActionListener(this);
		p1.add(jbtclog);

		p1.add(new JLabel("Deviated Log:"));

		jtadlog = new JTextArea(1, 15);
		jtadlog.setEditable(false);
		p1.add(jtadlog);

		jbtdlog = new JButton("Open");
		jbtdlog.addActionListener(this);
		p1.add(jbtdlog);

		jbtrecovery = new JButton("Start Recovery");
		jbtrecovery.addActionListener(this);
		add(p1, BorderLayout.NORTH);
		add(jbtrecovery, BorderLayout.SOUTH);
	}

	public static void main(String[] args) {
		MainFrame1 window = new MainFrame1();
		window.setTitle("Sheel");
		window.setSize(400, 300);
		window.setVisible(true);
		window.setLocationRelativeTo(null);// 窗口居中
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == jbtclog) {
			File file = logImport();
			cTraces = LogUtils.xesParse(file);
			System.out
					.println("--------------------------------------------正确的日志--------------------------------------------");
			for (int i = 0; i < cTraces.size(); i++) {
				System.out.println("第" + i + "条：" + cTraces.get(i));
			}
			jtaclog.setText(file.getName());

		}
		if (e.getSource() == jbtdlog) {
			File file = logImport();
			dTrace = LogUtils.xesParse(file);
			jtadlog.setText(file.getName());
			System.out
					.println("--------------------------------------------偏差日志--------------------------------------------");
			for (int i = 0; i < dTrace.size(); i++) {
				System.out.println("第" + i + "条：" + dTrace.get(i));
			}
			// path = file.getParent();
			// dTraceFileName = file.getName();
		}
		if (e.getSource() == jbtrecovery) {
			recovery();
		}
	}

	/**
	 * 日志导入
	 * 
	 * @return
	 */
	public File logImport() {
		File file = null;
		MyJFileChooser fc = new MyJFileChooser();
		int state = fc.showDialog();
		if (state == JFileChooser.APPROVE_OPTION) {
			try {
				file = fc.getSelectedFile();
				return file;
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}
		return null;
	}

	public void recovery() {
		Algorithm1 algorithm1 = new Algorithm1();
		algorithm1.traceCluster(cTraces);
		long start = System.nanoTime();
		repairedTraces = algorithm1.recovery(dTrace);
		long end = System.nanoTime();
		long timeConsumed = end - start;

		System.out
				.println("--------------------------------------------修复好的日志--------------------------------------------");
		for (int i = 0; i < repairedTraces.size(); i++) {
			System.out.println("第" + i + "条：" + repairedTraces.get(i));
		}

		System.out.println("修复完成。耗时：" + timeConsumed / 1000000.0 + "ms");
		calculateAccuracy(repairedTraces);
		// String fileName =
		// dTraceFileName.substring(0,dTraceFileName.lastIndexOf("."))+"-repaired.xes";
		// String destination = path+"\\"+fileName;
		// LogUtils.xesWrite(repairedTraces, new File(destination));
	}

	/**
	 * 正确率计算
	 * 
	 * @param repairedTraces
	 *            修复之后的traces
	 */
	private void calculateAccuracy(List<Trace> repairedTraces) {
		int right = 0;
		double rate;
		for (int i = 0; i < dTrace.size(); i++) {
			int editcost1 = StringDistance.editDistance(cTraces.get(i)
					.getEvents(), dTrace.get(i).getEvents());
			int editcost2 = StringDistance.editDistance(repairedTraces.get(i)
					.getEvents(), dTrace.get(i).getEvents());
			System.out.println("num" + i + ":" + "editcost1 = " + editcost1
					+ "        editcost2 = " + editcost2);
			if (editcost1 == editcost2) {
				right++;
			}
		}

		System.out.println("right = " + right + "     all = " + dTrace.size());
		DecimalFormat format = new DecimalFormat("#0.0000");
		rate = right * 1.0 / dTrace.size();
		// rate = Double.parseDouble(format.format(rate));
		// rate = rate*100;
		// System.out.println("正确率:"+rate+"%");
		System.out.println("正确率:" + format.format(rate));
	}
}
