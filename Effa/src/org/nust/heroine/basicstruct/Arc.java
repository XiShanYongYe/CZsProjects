package org.nust.heroine.basicstruct;

import java.awt.Color;
import java.awt.Graphics;
import java.io.Serializable;

/**
 * Petri网中节点数据结构
 * 
 * @author Administrator
 * 
 */
public class Arc implements Serializable, Cloneable {

	private static final long serialVersionUID = 3486802786745182175L;
	private Node Source;// 源
	private Node Target;// 目的
	private String ID;
	private String Type;// 类型
	private String label;// 名称
	private int[] splineX = new int[100];
	private int[] splineY = new int[100];
	private int endX;
	private int endY;
	private boolean visited = false;
	private boolean printVisited = false;
	private boolean petriNet = false;
	public boolean isDeleted = false;

	public Arc() {

	}

	public Arc(Node Source, Node Target) {
		this.Source = Source;
		this.Target = Target;
	}

	public Arc(Node Source, Node Target, String label, String Type) {
		this.Source = Source;
		this.Target = Target;
		this.label = label;
		this.Type = Type;
	}

	public Node getSource() {
		return Source;
	}

	public void setSource(Node Source) {
		this.Source = Source;
	}

	public Node getTarget() {
		return Target;
	}

	public void setTarget(Node Target) {
		this.Target = Target;
	}

	public String getID() {
		return ID;
	}

	public void setID(String ID) {
		this.ID = ID;
	}

	public String getType() {
		return Type;
	}

	public void setType(String Type) {
		this.Type = Type;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public int[] getSplineX() {
		return splineX;
	}

	public int[] getSplineY() {
		return splineY;
	}

	public void setSplineX(int i, int j) {
		splineX[i] = j;
	}

	public void setSplineY(int i, int j) {
		splineY[i] = j;
	}

	public int getEndX() {
		return endX;
	}

	public int getEndY() {
		return endY;
	}

	public void setEndX(int endX) {
		this.endX = endX;
	}

	public void setEndY(int endY) {
		this.endY = endY;
	}

	public Boolean getVisited() {
		return visited;
	}

	public void setVisited(Boolean visited) {
		this.visited = visited;
	}

	public Boolean getprintVisited() {
		return printVisited;
	}

	public void setprintVisited(Boolean printVisited) {
		this.printVisited = printVisited;
	}

	public boolean isPetriNet() {
		return petriNet;
	}

	public void setPetriNet(boolean petriNet) {
		this.petriNet = petriNet;
	}

	/**
	 * 显示边的信息
	 */
	public void show() {
		System.out.print(Source.getID() + Target.getID() + " label: "
				+ this.label + " type: " + this.Type);
		System.out.println();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == this)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof Arc))
			return false;
		Arc a = (Arc) obj;
		return this.getSource().equals(a.getSource())
				&& this.getTarget().equals(a.getTarget());
	}

	public boolean isControlArc() {
		if (this.getSource().getType().equalsIgnoreCase("entry")
				|| this.getSource().getType().equalsIgnoreCase("while")
				|| this.getSource().getType().equalsIgnoreCase("if")
				|| this.getSource().getType().equalsIgnoreCase("switch")) {
			return true;
		}
		return false;
	}

	public void draw(Graphics g) {
		g.setColor(Color.BLACK);
		g.drawLine(Source.getPX() + 9, Source.getPY() + 12, Target.getPX() + 9,
				Target.getPY());

		double d = Math.sqrt((Target.getPY() - Source.getPY())
				* (Target.getPY() - Source.getPY())
				+ (Target.getPX() - Source.getPX())
				* (Target.getPX() - Source.getPX()));
		double xa = Target.getPX()
				+ 9
				+ 10
				* ((Source.getPX() - Target.getPX()) + (Source.getPY() - Target
						.getPY()) / 2) / d;
		double ya = Target.getPY()
				+ 10
				* ((Source.getPY() - Target.getPY()) - (Source.getPX() - Target
						.getPX()) / 2) / d;
		double xb = Target.getPX()
				+ 9
				+ 10
				* ((Source.getPX() - Target.getPX()) - (Source.getPY() - Target
						.getPY()) / 2) / d;
		double yb = Target.getPY()
				+ 10
				* ((Source.getPY() - Target.getPY()) + (Source.getPX() - Target
						.getPX()) / 2) / d;

		g.drawLine(Target.getPX() + 9, Target.getPY(), (int) xa, (int) ya);
		g.drawLine(Target.getPX() + 9, Target.getPY(), (int) xb, (int) yb);
		g.setColor(Color.BLACK);
	}

	@Override
	public String toString() {
		return Source.toString() + "-" + Target.toString();
	}

	public Arc clone() {
		Arc o = null;
		try {
			o = (Arc) super.clone();
			o.Source = (Node) Source.clone();
			o.Target = (Node) Target.clone();

		} catch (CloneNotSupportedException e) {
			e.printStackTrace();

		}
		return o;
	}

	@Override
	public int hashCode() {
		return (7 * getSource().hashCode() + 2 * getTarget().hashCode());
	}
}
