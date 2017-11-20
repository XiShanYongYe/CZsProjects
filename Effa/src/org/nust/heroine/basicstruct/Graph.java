package org.nust.heroine.basicstruct;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * 有向图
 * 
 * @author Administrator
 * 
 */
public class Graph implements Serializable, Cloneable {

	private static final long serialVersionUID = 6712637811832637633L;

	protected int id;

	protected ArrayList<Node> nodes;// 所有节点的集合
	protected ArrayList<Arc> arcs;// 所有边的集合

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Graph() {
		nodes = new ArrayList<Node>();
		arcs = new ArrayList<Arc>();
	}

	public ArrayList<Node> getNodes() {
		return nodes;
	}

	public void setNodes(ArrayList<Node> nodes) {
		this.nodes = nodes;
	}

	public ArrayList<Arc> getArcs() {
		return arcs;
	}

	public void setEdges(ArrayList<Arc> arcs) {
		this.arcs = arcs;
	}

	public void addNode(Node node) {
		nodes.add(node);
	}

	public void deleteNode(Node node) {
		nodes.remove(node);
	}

	public void addArc(Arc arc) {
		arcs.add(arc);
	}

	public void addArc(Node n1, Node n2) {
		Arc a = new Arc();
		a.setSource(n1);
		a.setTarget(n2);
		arcs.add(a);
	}

	public void deleteArc(Arc arc) {
		arcs.remove(arc);
	}

	public void deleteArc(Node n1, Node n2) {
		deleteArc(new Arc(n1, n2));
	}

	/**
	 * 通过Id（不区分大小写）获得Node节点
	 * 
	 * @param id
	 * @return
	 */
	public Node getNodeById(String id) {
		for (Node node : nodes) {
			if (node.getID().equalsIgnoreCase(id))
				return node;
		}
		return null;
	}

	/**
	 * 通过source和target获得arc边
	 * 
	 * @param source
	 *            源节点
	 * @param target
	 *            目的节点
	 * @return
	 */
	public Arc getArcByST(String source, String target) {
		for (Arc arc : arcs) {
			if (arc.getSource().getID().equalsIgnoreCase(source)
					&& arc.getTarget().getID().equalsIgnoreCase(target))
				return arc;
		}
		return null;
	}

	@Override
	public String toString() {
		return nodes.toString() + "|" + arcs.toString();
	}

	@Override
	public Graph clone() throws CloneNotSupportedException {
		Graph g = new Graph();
		for (Node node : getNodes())
			g.addNode(node.clone());
		for (Arc arc : getArcs())
			g.addArc(arc.clone());
		return g;
	}

	/**
	 * 获得节点的入度
	 */
	public void findIndegree() {
		for (Node node : getNodes()) {
			node.setIndegree(0);
		}
		for (Arc arc : getArcs()) {
			Node target = arc.getTarget();
			target.setIndegree(target.getIndegree() + 1);
		}
	}

	/**
	 * 获得邻接矩阵，即所有节点直接的向
	 * 
	 * @return 范围所有节点之间可连接向的二维数组
	 */
	public boolean[][] getAdjacentMatrix() {
		boolean[][] adjacentMatrix = new boolean[getNodes().size()][getNodes()
				.size()];
		for (int i = 0; i < getNodes().size(); i++) {
			for (int j = 0; j < getNodes().size(); j++) {
				if (i == j)
					adjacentMatrix[i][j] = false;
				else {
					for (Arc arc : getArcs()) {
						if (arc.getSource().getID()
								.equalsIgnoreCase(getNodes().get(i).getID())
								&& arc.getTarget()
										.getID()
										.equalsIgnoreCase(
												getNodes().get(j).getID())) {
							adjacentMatrix[i][j] = true;
						}
					}
				}
			}
		}
		return adjacentMatrix;
	}
}
