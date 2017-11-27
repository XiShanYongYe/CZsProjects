package org.nust.wsong.UI.experiment;

import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.filechooser.FileFilter;

public class TestJFileSelectionBox {
	public static void main(String[] args) {
		JFrame frame = new JFrame();
		frame.setSize(600, 400);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		MyJFileChooser chooser = new MyJFileChooser();
		int showDialog = chooser.showDialog();
		if(showDialog==JFileChooser.APPROVE_OPTION){
			File file = chooser.getSelectedFile();
			System.out.println(file.getName());
			
		}
		
		
//		frame.getContentPane().add(new JFileSelectionBox(new FileFilter() {
//			
//			@Override
//			public String getDescription() {
//				return "log";
//			}
//			
//			@Override
//			public boolean accept(File f) {
//				if(f.getName().endsWith("xes"))
//					return true;
//				return false;
//			}
//		}));
		
	}
}
