package org.nust.heroine.view;

import java.awt.Dimension;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.border.TitledBorder;

import org.nust.heroine.basicstruct.PetriNet;

/**
 * 
 * ������PetriNetFrame ��;��SubProcess��PetriNetͼ�ԣ�������
 * 
 */
public class PetriNetFrame extends JFrame {

	private static final long serialVersionUID = 1L;
	private String title  = "default";
	PetriNet petriNet;
	PetriNetPanel pp;
	JScrollPane jsp;

	public PetriNetFrame(PetriNet petriNet) {
		this.petriNet = petriNet;
	}
	
	public PetriNetFrame(String title,PetriNet petriNet) {
		this.title = title;
		this.petriNet = petriNet;
	}

	/**
	 * ��ͼ�Դ��ڽ��г�ʼ��
	 */
	public void launchFrame() {
		this.setTitle(title);
		this.setBounds(0, 0, 600, 500);
		pp = new PetriNetPanel(petriNet);
		pp.setPreferredSize(new Dimension(3000, 3000));
		jsp = new JScrollPane(pp);
		this.add(jsp);
		this.setVisible(true);
//		this.setResizable(false);
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				dispose();
			}
		});
	}
}
