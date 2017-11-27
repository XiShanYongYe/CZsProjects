package org.nust.heroine.test;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;

import org.nust.heroine.basicstruct.PetriNet;
import org.nust.heroine.util.Decompose;
import org.nust.heroine.util.PnmlParser;
import org.nust.heroine.view.PetriNetFrame;

public class TestDecompose extends JFrame{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	JButton jButton;
	public TestDecompose() {
		 jButton = new JButton("Open");
		 jButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser chooser = new JFileChooser();
				int value = chooser.showOpenDialog(TestDecompose.this);
				if(value == JFileChooser.APPROVE_OPTION){
					File file = chooser.getSelectedFile();
					
					PnmlParser parser = new PnmlParser();
					PetriNet PN;
					try {
						PN = parser.parse(file);
						ArrayList<PetriNet> petriNets = Decompose.dec(PN);
						for(PetriNet p:petriNets){
							PetriNetFrame frame = new PetriNetFrame(p);
							frame.launchFrame();
						}
						
						System.out.println(petriNets.size());
						
					} catch (Exception e1) {
						e1.printStackTrace();
					}
					
				}
			}
		});
		 getContentPane().add(jButton,BorderLayout.NORTH);
	}
	public static void main(String ags[]){
		TestDecompose test = new TestDecompose();
		test.setSize(400,300);
		test.setVisible(true);
		test.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		test.setTitle("test");
	}

}
