package org.nust.heroine.basicstruct;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Stroke;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * Petri网中节点数据结构
 * 
 * @author Administrator
 * 
 */
public class Node implements Serializable, Cloneable {

	private static final long serialVersionUID = 6761956841328082611L;

	protected int DX = 25;
	protected int DY = 25;
	protected String Name;
	protected String ID;
	protected String type;
	protected int indegree = 0;// 入度
	protected int outdegree = 0;// 出度
	protected int PX;
	protected int PY;

	protected int token = 0;
	private NodePrintType nodePrintType;

	public int missingNum = 0;

	protected boolean petriNet = false;

	protected ArrayList<Node> predecessors = new ArrayList<Node>();// 前继
	protected ArrayList<Node> successors = new ArrayList<Node>();// 后继

	public ArrayList<Node> getPredecessors() {
		return predecessors;
	}

	public ArrayList<Node> getSuccessors() {
		return successors;
	}

	public int getMissingNum() {
		return missingNum;
	}

	public void setMissingNum(int missingNum) {
		this.missingNum = missingNum;
	}

	public void initMissingNum() {
		this.missingNum = 0;
	}

	public Node() {

	}

	public Node(String id) {
		this.ID = id;
	}

	public Node(String Name, String type) {
		this.Name = Name;
		this.type = type;
	}

	public String getName() {
		return Name;
	}

	public void setName(String Name) {
		this.Name = Name;
	}

	public String getID() {
		return ID;
	}

	public void setID(String ID) {
		this.ID = ID;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public NodePrintType getNodePrintType() {
		return nodePrintType;
	}

	public void setNodePrintType(NodePrintType nodePrintType) {
		this.nodePrintType = nodePrintType;
	}

	public int getIndegree() {
		return indegree;
	}

	public void setIndegree(int indegree) {
		this.indegree = indegree;
	}

	public int getOutdegree() {
		return outdegree;
	}

	public void setOutdegree(int outdegree) {
		this.outdegree = outdegree;
	}

	public int getPX() {
		return PX;
	}

	public void setPX(int PX) {
		this.PX = PX;
	}

	public int getPY() {
		return PY;
	}

	public void setPY(int PY) {
		this.PY = PY;
	}

	public int getDX() {
		return DX;
	}

	public void setDX(int DX) {
		this.DX = DX;
	}

	public int getDY() {
		return DY;
	}

	public void setDY(int DY) {
		this.DY = DY;
	}

	public int gettoken() {
		return token;
	}

	public void settoken(int i) {
		this.token = i;
	}

	/**
	 * token减少1
	 */
	public void multoken() {
		this.token = this.token - 1;
	}

	/**
	 * token增加1
	 */
	public void addtoken() {
		this.token = this.token + 1;
	}

	public boolean isPetriNet() {
		return petriNet;
	}

	public void setPetriNet(boolean petriNet) {
		this.petriNet = petriNet;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == this)
			return true;
		if (obj == null)
			return false;
		// if(obj instanceof String)
		// return this.ID.equalsIgnoreCase((String)obj);
		// if (!(obj instanceof Node))
		// return false;
		Node n = (Node) obj;
		return this.ID.equals(n.ID);
	}

	// @Override
	public int hashCode() {
		// TODO Auto-generated method stub
		// return super.hashCode();
		return ID.hashCode();
	}

	public boolean isIn(ArrayList<Node> Node) {
		for (int i = 0; i < Node.size(); i++) {
			if (Node.get(i) == this) {
				return true;
			}
		}
		return false;
	}

	@Override
	public String toString() {
		return ID;
	}

	public Rectangle getRect() {
		return new Rectangle(PX, PY, DX, DY);
	}

	public void draw(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		Color c = g2d.getColor();
		Stroke s = new BasicStroke(2.0f);
		g2d.setColor(Color.BLACK);
		g2d.setStroke(s);

		if (this.nodePrintType == NodePrintType.START) {
			g2d.fillOval(PX, PY, DX, DY);
			g2d.setColor(c);
		} else if (this.nodePrintType == NodePrintType.END) {
			g2d.drawOval(PX, PY, DX, DY);
			g2d.fillOval(PX + 2, PY + 2, DX - 4, DY - 4);
			g2d.setColor(c);
		} else if (this.nodePrintType == NodePrintType.PLACE) {
			g2d.fillOval(PX, PY, DX, DY);
			g2d.setColor(Color.RED);
			Font f = new Font("time new romans", Font.BOLD, 16);
			g2d.setFont(f);
			g2d.drawString(ID, PX + 7, PY + 13);
			// System.out.println(ID);
			g2d.setColor(c);
		} else if (this.nodePrintType == NodePrintType.TRANSITION) {
			g2d.fillRect(PX, PY, (int) (DX + DX / 2), DY / 2);
			g2d.setColor(Color.RED);
			Font f = new Font("time new romans", Font.BOLD, 16);
			g2d.setFont(f);
			g2d.drawString(ID, PX + 7, PY + 13);
			// System.out.println(ID);
			g2d.setColor(c);
		} else if (this.nodePrintType == NodePrintType.PARALLEL) {
			g2d.fillRect(PX - DX / 2, PY, 2 * DX, DY / 2);
			g2d.setColor(c);
		} else if (this.nodePrintType == NodePrintType.CHOICE) {
			g2d.drawLine(PX + DX / 2, PY, PX, PY + DY / 2);
			g2d.drawLine(PX + DX / 2, PY, PX + DX, PY + DY / 2);
			g2d.drawLine(PX, PY + DY / 2, PX + DX / 2, PY + DY);
			g2d.drawLine(PX + DX / 2, PY + DY, PX + DX, PY + DY / 2);
			g2d.setColor(Color.RED);
			Font f = new Font("time new romans", Font.BOLD, 16);
			g2d.setFont(f);
			g2d.drawString(ID, PX + 7, PY + 13);
			g2d.setColor(c);
		} else if (this.nodePrintType == NodePrintType.BASIC) {
			g2d.drawRect(PX, PY, (int) (DX + DX / 4), DY);
			g2d.setColor(Color.RED);
			Font f = new Font("time new romans", Font.BOLD, 16);
			g2d.setFont(f);
			g2d.drawString(ID, PX + 7, PY + 13);
			g2d.setColor(c);
		}
	}

	public Node clone() {
		Node o = null;
		try {
			o = (Node) super.clone();
			// for(Node node:this.predecessors)
			// o.predecessors.add(node.clone());
			// for(Node node:this.successors)
			// o.successors.add(node.clone());
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		return o;
	}

}
