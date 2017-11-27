package org.nust.wsong.main;

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

import org.apache.commons.collections4.Bag;
import org.apache.commons.collections4.bag.HashBag;
import org.nust.wsong.algorithm.Algorithm1;
import org.nust.wsong.algorithm.Algorithm2;
import org.nust.wsong.model.Trace;
import org.nust.wsong.util.LogUtil;
import org.nust.wsong.util.TraceUtil;

/**
 * @description 调用算法1
 * @author xxx
 * @since 2016-10-1
 */
public class MainFrame2 extends JFrame implements ActionListener {

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
	public MainFrame2() {

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
		MainFrame2 window = new MainFrame2();
		window.setTitle("自己");
		window.setSize(400, 300);
		window.setVisible(true);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == jbtclog) {
			File file = logImport();
			cTraces = LogUtil.xesParse(file);
			jtaclog.setText(file.getName());
//			System.out.println("compliant trace"+cTraces);
			
		}
		if (e.getSource() == jbtdlog) {
			File file = logImport();
			dTrace = LogUtil.xesParse(file);
			jtadlog.setText(file.getName());
			//System.out.println("deviated trace"+dTrace);
			
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
		Algorithm1.traceCluster(cTraces);
		long start = System.nanoTime();
		List<Trace> repairedTraces = Algorithm2.recovery(dTrace);
		long end = System.nanoTime();
		long timeConsumed = end - start;
		
		for(int i=0;i<dTrace.size();i++){
			System.out.println("修复前"+dTrace.get(i));
			System.out.println("修复后："+repairedTraces.get(i));
			System.out.println("------------------------------------------------");
		}
		System.out.println("修复完成。耗时：" + timeConsumed/1000000.0 + "ms");
		String fileName = dTraceFileName.substring(0,dTraceFileName.lastIndexOf("."))+"-repaired.xes";
		String destination = path+"\\"+fileName;
		LogUtil.xesWrite(repairedTraces, new File(destination));
	}



}
