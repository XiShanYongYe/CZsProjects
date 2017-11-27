package org.nust.wsong.model;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * 
 * @author xxx
 * @since 2016-10-1
 *
 */
public class Graph implements Serializable,Cloneable{

	private static final long serialVersionUID = 6712637811832637633L;

	/**
	 * 顶点
	 */
	protected Set<Node> nodes;
	/**
	 * 边
	 */
	protected Set<Edge> edges;
	
	public Graph() {
		nodes = new HashSet<Node>();
		edges = new HashSet<Edge>();
	}
	
	public  Set<Node> getNodes() {
		return nodes;
	}

	public void setNodes(Set<Node> nodes) {
		this.nodes = nodes;
	}

	public Set<Edge> getEdges() {
		return edges;
	}

	public void setEdges(Set<Edge> edges) {
		this.edges = edges;
	}
	

	public void addNode(Node node) {
		nodes.add(node);
	}
	
	public void deleteNode(Node node) {
		nodes.remove(node);
	}
	
	public void addEdge(Edge edge) {
		Node source = edge.getSource();
		Node target = edge.getTarget();
		addEdge(source,target);


	}
	
	public void addEdge(Node source,Node target) {
		Edge e= new Edge();
		e.setSource(source);
		e.setTarget(target);
		if(edges.add(e)){
			source.setOutDegree(source.getOutDegree()+1);
			target.setInDegree(target.getInDegree()+1);
		}
	}

	/**
	 * 删除边
	 * @param edge
	 */
	public void deleteEdge(Edge edge) {
		Node source = edge.getSource();
		Node target = edge.getTarget();
		if(edges.remove(edge)){
			source.setOutDegree(source.getOutDegree()-1);
			target.setInDegree(target.getInDegree()-1);
		}
	}
	
	public void deleteEdge(Node n1,Node n2) {
		deleteEdge(new Edge(n1,n2));

	}

	public Node getNodeById(String id){
		for(Node node:nodes){
			if(node.getName().equalsIgnoreCase(id))
				return node;
		}
		return null;
	}
	
	@Override
	protected Graph clone() throws CloneNotSupportedException {
		Graph o = null;
		o = (Graph)super.clone();
		for(Node node:o.getNodes())
			node.clone();
		for(Edge edge:o.getEdges())
			edge.clone();
		return o;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj == this)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof Graph))
			return false;
		Graph g = (Graph) obj;
		if(getNodes().equals(g.getNodes()))
			return false;
		if(getEdges().equals(g.getEdges()))
			return false;
		return true;
	}
	
	@Override
	public int hashCode() {
		// TODO Auto-generated method stub
		int hash=0;
		for(Node n:getNodes())
			hash+=37*n.hashCode();
		for(Edge e:getEdges())
			hash+=51*e.hashCode();
		return hash;
	}
	
	
	

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Graph [nodes=" + nodes + ", edges=" + edges +"\n");
		for(Node node:nodes){
			sb.append(node.getName()+"  "+"indegree： "+node.getInDegree()+"    outDegree: "+node.getOutDegree()+"\n");
		}
		sb.append("]");
		return sb.toString();
	}
	
	
	/**
	 * ���Graph���ɽ��n1��n2���ɵı�
	 * @param n1 ����ߵĵ�һ�����
	 * @param n2 ����ߵĵڶ������
	 * @return ������n1��n2����ı�Edge����
	 */
/*	public Edge getEdge(Node n1, Node n2) {
		Iterator<Edge> iter = edges.iterator();
		while(iter.hasNext()){
			Edge e = iter.next();
			if (e.equals(new Edge(n1, n2))) {
				return e;
			}
		}
		return null;
	}*/
	
	
}
