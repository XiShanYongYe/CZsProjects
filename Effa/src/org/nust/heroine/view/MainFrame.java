package org.nust.heroine.view;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import org.nust.heroine.basicstruct.PetriNet;
import org.nust.heroine.basicstruct.Trace;
import org.nust.heroine.recovery.Recovery;
import org.nust.heroine.util.Decompose;
import org.nust.heroine.util.LogUtil;
import org.nust.heroine.util.PnmlParser;
import org.nust.heroine.util.StringDistance;

public class MainFrame extends JFrame implements ActionListener {
	/**
	 * author:xxx
	 */
	private static final long serialVersionUID = 1L;
	JTextArea jta_CTraces;
	JButton jbt_CTraces;

	JTextArea jta_DTraces;
	JButton jbt_DTraces;

	JTextArea jta_Spe;
	JButton jbt_Specification;

	JButton jbt_Recovery;

	List<Trace> cTraces;
	List<Trace> dTraces;
	ArrayList<Trace> repairedTraces;
	File logFile;

	PetriNet petriNet;
	List<PetriNet> petriNets;

	public MainFrame() {
		JPanel p1 = new JPanel(new GridLayout(3, 3, 5, 5));

		p1.add(new JLabel("Deviated Log:"));
		jta_DTraces = new JTextArea(1, 15);
		jta_DTraces.setEditable(false);
		p1.add(jta_DTraces);

		jbt_DTraces = new JButton("Open");
		jbt_DTraces.addActionListener(this);
		p1.add(jbt_DTraces);

		JLabel spe = new JLabel("Specification:");
		p1.add(spe);

		jta_Spe = new JTextArea(1, 15);
		jta_Spe.setEditable(false);
		p1.add(jta_Spe);

		jbt_Specification = new JButton("Open");
		jbt_Specification.addActionListener(this);
		p1.add(jbt_Specification);

		p1.add(new JLabel("Compliant Log:"));
		jta_CTraces = new JTextArea(1, 15);
		jta_CTraces.setEditable(false);
		p1.add(jta_CTraces);

		jbt_CTraces = new JButton("Open");
		jbt_CTraces.addActionListener(this);
		p1.add(jbt_CTraces);

		jbt_Recovery = new JButton("Start Recovery");
		jbt_Recovery.addActionListener(this);
		add(p1, BorderLayout.NORTH);
		add(jbt_Recovery, BorderLayout.SOUTH);

		this.setTitle("Effa");
		this.setSize(400, 300);
		this.setVisible(true);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == jbt_CTraces) {// 正确日志
			File file = logImport();
			cTraces = logParse(file);
			jta_CTraces.setText(file.getName());
			// 正确日志输出
			System.out.println("compliant traces:");
			for (int i = 0; i < cTraces.size(); i++) {
				Trace trace = cTraces.get(i);
				System.out.println(trace.getEvents());
			}
		}
		if (e.getSource() == jbt_DTraces) {// 错误日志
			File file = logImport();
			logFile = file;
			dTraces = logParse(file);
			jta_DTraces.setText(file.getName());
			// 错误日志输出
			System.out.println("deviated traces:");
			for (int i = 0; i < dTraces.size(); i++) {
				Trace trace = dTraces.get(i);
				System.out.println(trace.getEvents());
			}
		}
		if (e.getSource() == jbt_Specification) {// 模型
			pnmlParse();
			System.out.println("pnml解析完成");
		}
		if (e.getSource() == jbt_Recovery) {// 开始修复
			System.out.println("开始修复");
			recovery();
			System.out.println("修复完成");
		}
	}

	/**
	 * 读取日志
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

	/**
	 * 解析日志
	 * 
	 * @param file
	 *            选择的日志
	 * @return 日志中包含的trace
	 */
	public List<Trace> logParse(File file) {
		List<Trace> traceSet = LogUtil.xesParse(file);
		return traceSet;
	}

	/**
	 * 模型解析
	 */
	public void pnmlParse() {
		MyJFileChooser fc = new MyJFileChooser();
		int state = fc.showDialog();
		if (state == JFileChooser.APPROVE_OPTION) {
			try {
				File file = fc.getSelectedFile();
				petriNet = PnmlParser.parse(file);// 读入Petri网中的所有节点和边
				petriNet.computePetri();
				petriNet.initTransMap();

				System.out.println("pnml开始解析");
				String fileName = file.getName();
				jta_Spe.setText(fileName);

				long start = System.nanoTime();
				petriNets = Decompose.dec(PnmlParser.parse(file));// 业务过程分解
				long end = System.nanoTime();
				System.out.println("过程分解时间" + (end - start) / 1000000.0);

				for (int i = 0; i < petriNets.size(); i++) {
					petriNets.get(i).computePetri();
					petriNets.get(i).initTransMap();

				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public void recovery() {

		Recovery recovery = new Recovery(petriNet, petriNets);
		long start = System.nanoTime();
		// System.out.println("dtrace size = " + dTraces.size());
		// System.out.println(dTraces.get(0).getEvents());
		// recovery.recovery(dTraces);
		repairedTraces = recovery.recovery(dTraces);

		long end = System.nanoTime();
		long time = end - start;

		System.out.println("修复后的日志");
		for (int i = 0; i < repairedTraces.size(); i++) {
			System.out.println(repairedTraces.get(i));
		}
		System.out.println("修复总时间" + time / 1000000.0);

		calculateAccuracy(repairedTraces);
	}

	// 待修改
	private void calculateAccuracy(ArrayList<Trace> repairedTraces) {
		int right = 0;
		double rate;
		for (int i = 0; i < dTraces.size(); i++) {
			List<String> ctrace = cTraces.get(i).getEvents();
			List<String> dtrace = dTraces.get(i).getEvents();
			List<String> rtrace = repairedTraces.get(i).getEvents();
			int editcost1 = StringDistance.editDistance(ctrace, dtrace);
			int editcost2 = StringDistance.editDistance(rtrace, dtrace);
			System.out.println("num" + i + ":" + "editcost1=" + editcost1
					+ "  editcost2=" + editcost2);
			if (editcost1 == editcost2) {
				right++;
			}
		}
		System.out.println("right = " + right + "     all = " + cTraces.size());
		DecimalFormat format = new DecimalFormat("#0.0000");
		rate = right * 1.0 / cTraces.size();
		System.out.println("正确率:" + format.format(rate));

	}
}
