package org.nust.heroine.view;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Stroke;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

import javax.swing.JPanel;

import org.nust.heroine.basicstruct.Arc;
import org.nust.heroine.basicstruct.Node;
import org.nust.heroine.basicstruct.PetriNet;

/**
 * 
 * ������PetriNetView ��;��PetriNet��ͼ��
 * 
 */
public class PetriNetPanel extends JPanel implements MouseMotionListener{

	private static final long serialVersionUID = 1L;
	private Node chosedNode = new Node();
	private PetriNet PetriNet;
	public PetriNetPanel(PetriNet petriNet){
		this.PetriNet = petriNet;
		addMouseMotionListener(this);
	}
	@Override
	public void paintComponent(Graphics g) {
		g.setColor(this.getBackground());
		g.fillRect(0, 0, this.getHeight(), this.getWidth());
		for (int i = 0; i < PetriNet.getNodes().size(); i++) {
			Node n = PetriNet.getNodes().get(i);
			if (n.getType() != "transition") {
				Pdraw(g, n);
			} else {
				Tdraw(g, n);
			}
		}

		for (int i = 0; i < PetriNet.getArcs().size(); i++) {
			Arc e = PetriNet.getArcs().get(i);
			e.draw(g);
		}

	}

	/**
	 * ����ͼ��
	 */
	public void Pdraw(Graphics g, Node n) {
		Graphics2D g2d = (Graphics2D) g;
		Color c = g2d.getColor();
		Stroke s = new BasicStroke(2.0f);
		g2d.setColor(Color.BLACK);
		g2d.setStroke(s);
		g2d.drawOval(n.getPX(), n.getPY(), n.getDX(), n.getDY());
		g2d.setColor(Color.RED);
		Font f = new Font("time new romans", Font.BOLD, 14);
		g2d.setFont(f);
		g2d.drawString(n.getID(), n.getPX() + 7, n.getPY() + 13);
		g2d.setColor(c);
	}

	/**
	 * ��Ǩͼ��
	 */
	public void Tdraw(Graphics g, Node n) {
		Graphics2D g2d = (Graphics2D) g;
		Color c = g2d.getColor();
		Stroke s = new BasicStroke(2.0f);
		g2d.setColor(Color.BLACK);
		g2d.setStroke(s);
		g2d.drawRect(n.getPX(), n.getPY(),(int) (n.getDX() + n.getDY() / 2), n.getDY() / 2);
		g2d.setColor(Color.RED);
		Font f = new Font("time new romans", Font.BOLD, 14);
		g2d.setFont(f);
		g2d.drawString(n.getID(), n.getPX() + 7, n.getPY() + 13);
		g2d.setColor(c);
	}

	@Override
	public void mouseDragged(MouseEvent e) {
//		System.out.println("123");
		int x = e.getX();
		int y = e.getY();
		for (Node node : PetriNet.getNodes()) {
			if (node.getRect().contains(new Point(x, y))) {
				chosedNode = node;
				// System.out.println("123");
			}
		}
		for (Node node : PetriNet.getNodes()) {
			if (node == chosedNode) {
				node.setPX(x);
				node.setPY(y);
//				PetriNet.initArcLocation();
				repaint();
			}
		}

	}

	@Override
	public void mouseMoved(MouseEvent e) {
		int x = e.getX();
		int y = e.getY();
		for (int i = 0; i < PetriNet.getNodes().size(); i++) {
			if (PetriNet.getNodes().get(i).getRect().contains(
					new Point(x, y))) {
				if (PetriNet.getNodes().get(i).getType().equalsIgnoreCase(
						"transition")) {
					/*this.setToolTipText("Input: "
							+ PetriNet.getNodes().get(i).getInput() + "   "
							+ "Output: "
							+ PetriNet.getNodes().get(i).getOutput());*/
				} else if (PetriNet.getNodes().get(i).getType()
						.equalsIgnoreCase("place")) {
					this.setToolTipText("");
				}
			}
		}
		repaint();
	}

}
