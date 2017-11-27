package org.nust.wsong.UI;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.nust.wsong.algorithm.Algorithm3;
import org.nust.wsong.model.Trace;
import org.nust.wsong.util.LogUtils;

/**
 * @description 穷举法 调用算法3 计算 editDistance
 * @author xxx
 * @since 2016-10-1
 */
public class MainFrame3 extends JFrame implements ActionListener {

	private static final long serialVersionUID = 1L;
	JButton jbtclog;
	JButton jbtdlog;
	JButton jbtrecovery;
	JTextArea jtaclog;
	JTextArea jtadlog;
	List<Trace> dTrace = null;
	List<Trace> cTraces = null;
	String path;
	String dTraceFileName;

	public MainFrame3() {

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
		MainFrame3 window = new MainFrame3();
		window.setTitle("穷举法");
		window.setSize(400, 300);
		window.setVisible(true);
		window.setLocationRelativeTo(null);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == jbtclog) {
			File file = logImport();
			cTraces = LogUtils.xesParse(file);
			jtaclog.setText(file.getName());
			// System.out.println("compliant trace"+cTraces);
		}
		if (e.getSource() == jbtdlog) {
			File file = logImport();
			dTrace = LogUtils.xesParse(file);
			jtadlog.setText(file.getName());
			// System.out.println("deviated trace"+dTrace);
			path = file.getParent();
			dTraceFileName = file.getName();
		}
		if (e.getSource() == jbtrecovery) {
			recovery();
		}
	}

	public File logImport() {
		File file = null;
		JFileChooser fc = new JFileChooser();
		fc.setFileFilter(new FileNameExtensionFilter("Log File(XES)", "XES"));// ,
																				// openType
		fc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
		fc.setMultiSelectionEnabled(true);
		// 路径
		fc.setCurrentDirectory(new File("E:\\赵宁\\shuju-3.14"));
		fc.setVisible(true);
		int state = fc.showOpenDialog(this);
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
		// Algorithm1.traceCluster(cTraces);
		long start = System.nanoTime();
		List<Trace> repairedTraces = Algorithm3.recovery(dTrace, cTraces);
		long end = System.nanoTime();
		long timeConsumed = end - start;
		for (int i = 0; i < dTrace.size(); i++) {
			System.out.println(repairedTraces.get(i));
			// System.out.println("修复前"+dTrace.get(i));
			// System.out.println("修复后："+repairedTraces.get(i));
			// System.out.println("------------------------------------------------");
		}
		// System.out.println("修复完成。耗时：" + timeConsumed/1000000.0 + "ms");
		System.out.println(timeConsumed / 1000000.0);

		String fileName = dTraceFileName.substring(0,
				dTraceFileName.lastIndexOf("."))
				+ "-repaired.xes";
		String destination = path + "\\" + fileName;
		LogUtils.xesWrite(repairedTraces, new File(destination));
		// calculateAccuracy(repairedTraces);
	}

	// private void calculateAccuracy(List<Trace> repairedTraces){
	// int right = 0;
	// double rate ;
	// for(int i=0;i<cTraces.size();i++){
	// int editcost1 = StringDistance.editDistance(cTraces.get(i).getEvents(),
	// dTrace.get(i).getEvents());
	// int editcost2 =
	// StringDistance.editDistance(repairedTraces.get(i).getEvents(),
	// dTrace.get(i).getEvents());
	// // System.out.println("editcost1:"+editcost1+", editcost2:"+editcost2);
	// //等价
	// if(editcost1 == editcost2){
	// right++;
	// }
	//
	// }
	// System.out.println("right = "+right+"     all = "+cTraces.size());
	// DecimalFormat format = new DecimalFormat("#0.0000");
	// rate = right*1.0/cTraces.size();
	// // rate = Double.parseDouble(format.format(rate));
	// // rate = rate*100;
	// // System.out.println("正确率:"+rate+"%");
	// System.out.println("正确率:"+format.format(rate));
	// }

}
