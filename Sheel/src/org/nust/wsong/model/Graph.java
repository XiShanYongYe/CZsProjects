package org.nust.wsong.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 
 * @author xxx
 * 
 * 
 */
public class Graph implements Serializable, Cloneable {

	private static final long serialVersionUID = 6712637811832637633L;

	/**
	 * 顶点
	 */
	protected Set<Node> nodes;

	/**
	 * 入边
	 */
	protected Map<Node, Set<Edge>> inEdges;

	/**
	 * 出边
	 */
	protected Map<Node, Set<Edge>> outEdges;

	/**
	 * 边
	 */
	protected Set<Edge> edges;

	/**
	 * 根据label得到节点(可能存在重名节点)
	 */
	protected Map<String, Set<Node>> label2Node;

	public Graph() {
		nodes = new HashSet<>();
		edges = new HashSet<>();
		inEdges = new HashMap<>();
		outEdges = new HashMap<>();
		label2Node = new HashMap<>();
	}

	public Collection<Node> getNodes() {
		return new ArrayList<Node>(nodes);
	}

	public void setNodes(Set<Node> nodes) {
		this.nodes = nodes;
	}

	public Collection<Edge> getEdges() {
		return new ArrayList<Edge>(edges);
	}

	public void setEdges(Set<Edge> edges) {
		this.edges = edges;
	}

	public void addNode(Node node) {
		if (label2Node.get(node.getLabel()) == null)
			label2Node.put(node.getLabel(), new HashSet<Node>());
		label2Node.get(node.getLabel()).add(node);
		nodes.add(node);

		inEdges.put(node, new HashSet<Edge>());
		outEdges.put(node, new HashSet<Edge>());
	}

	public List<Node> getNodeByLabel(String label) {
		if (!label2Node.containsKey(label))
			return null;
		return new ArrayList<>(label2Node.get(label));
	}

	public void removeNode(Node node) {
		nodes.remove(node);
		label2Node.get(node.getLabel()).remove(node);
	}

	public void addEdge(Edge edge) {
		Node source = edge.getSource();
		Node target = edge.getTarget();
		addEdge(source, target);
	}

	public void addEdge(Node source, Node target) {
		Edge e = new Edge(source, target);
		edges.add(e);
		inEdges.get(target).add(e);
		outEdges.get(source).add(e);
	}

	/**
	 * 删除边
	 * 
	 * @param edge
	 */
	public void removeEdge(Edge edge) {
		Node source = edge.getSource();
		Node target = edge.getTarget();
		removeEdge(source, target);
	}

	public void removeEdge(Node source, Node target) {
		Edge e = new Edge(source, target);
		edges.remove(e);
		inEdges.get(target).remove(e);
		outEdges.get(source).remove(e);
	}

	public Collection<Edge> getInEdges(Node node) {
		if (inEdges.get(node) == null)
			return null;
		else
			return new ArrayList<>(inEdges.get(node));
	}

	public int getInEdgesCount(Node node) {
		if (inEdges.get(node) == null)
			return 0;
		else
			return inEdges.get(node).size();
	}

	public Collection<Edge> getOutEdges(Node node) {
		if (outEdges.get(node) == null)
			return null;
		else
			return new ArrayList<>(outEdges.get(node));
	}

	public int getOutEdgesCount(Node node) {
		if (outEdges.get(node) == null)
			return 0;
		else
			return outEdges.get(node).size();
	}

	@Override
	protected Graph clone() throws CloneNotSupportedException {
		Graph o = null;
		o = (Graph) super.clone();
		for (Node node : o.getNodes())
			node.clone();
		for (Edge edge : o.getEdges())
			edge.clone();
		return o;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		if (!(obj instanceof Graph))
			return false;
		if (obj == this)
			return true;
		Graph g = (Graph) obj;
		if (!getNodes().equals(g.getNodes()))
			return false;
		if (!getEdges().equals(g.getEdges()))
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		int hash = 0;
		for (Node n : getNodes())
			hash += 37 * n.hashCode();
		for (Edge e : getEdges())
			hash += 51 * e.hashCode();
		return hash;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Graph[" + "\n");
		for (Node node : nodes) {
			sb.append(node + "\n" + inEdges.get(node) + "\n"
					+ outEdges.get(node) + "\n\n");
		}
		sb.append("]");
		return sb.toString();
	}
}
