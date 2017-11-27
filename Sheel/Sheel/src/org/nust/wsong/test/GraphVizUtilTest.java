package org.nust.wsong.test;

import static org.junit.Assert.*;

import org.junit.Test;
import org.nust.wsong.model.Edge;
import org.nust.wsong.model.Graph;
import org.nust.wsong.model.Node;
import org.nust.wsong.util.GraphVizUtil;

public class GraphVizUtilTest {
	@Test
	public void testVisual() throws Exception {
		Graph graph  = new Graph();
		Node n1 = new Node("A");
		Node n2 = new Node("B");
		Node n3 = new Node("C");
		Edge e1 = new Edge(n1,n2);
		graph.addNode(n1);
		graph.addNode(n2);
		graph.addNode(n3);
//		graph.addEdge(n1, n2);
		graph.addEdge(e1);
		GraphVizUtil.show(graph);
		while(true){
			
		}
//		GraphVizUtil.save(graph, GraphVizUtil.GraphVizUtil_Save_Png);
	}
}
