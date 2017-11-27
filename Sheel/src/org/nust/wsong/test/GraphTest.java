package org.nust.wsong.test;

import org.nust.wsong.UI.GraphJungVisualization;
import org.nust.wsong.model.Graph;
import org.nust.wsong.model.Node;

public class GraphTest {
	public static void main(String[] args) {
		
		
		Graph graph = new Graph();
		Node node1 = new Node("hello");
		Node node2 = new Node("world");
		Node node3 = new Node("hello");
		
		graph.addNode(node1);
		graph.addNode(node2);
		graph.addNode(node3);
		graph.addEdge(node1, node2);
		graph.addEdge(node2, node3);
		
		
		GraphJungVisualization visualization = new GraphJungVisualization(graph);
		visualization.show();

		System.out.println(graph.getInEdges(node3));
		System.out.println(graph.getOutEdges(node3));
	}
}
