package org.nust.wsong.test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.Test;
import org.nust.wsong.UI.GraphJungVisualization;
import org.nust.wsong.algorithm.Algorithm1;
import org.nust.wsong.model.Graph;
import org.nust.wsong.model.Node;
import org.nust.wsong.model.Trace;
import org.nust.wsong.repair.Repair;

public class RepairTest {
	
	
	/**
	 * example 3
	 * @throws Exception
	 */
	@Test
	public void recoveryTest1() throws Exception {
		Trace dtrace = new Trace();
		dtrace.append("t1");
		dtrace.append("t3");
		dtrace.append("t2");
	    dtrace.append("t4");
		dtrace.append("t6");
		dtrace.append("t8");
		dtrace.append("t9");
		
		Trace t1 = new Trace();
		Trace t2 = new Trace();
		Trace t3 = new Trace();
		Collections.addAll(t1.getEvents(), "t1","t2","t3","t4","t5","t6","t7","t8");
		Collections.addAll(t2.getEvents(), "t1","t4","t5","t2","t3","t6","t9");
		Collections.addAll(t3.getEvents(), "t1","t4","t2","t5","t3","t6","t9");
		
		List<Trace> traces = new ArrayList<>();
		Collections.addAll(traces, t1,t2,t3);
		
//		Algorithm4.traceCluster(traces);
		
		Algorithm1 algorithm1 = new Algorithm1();
		
		List<Trace> result = algorithm1.repair(dtrace);
		System.out.println("修复结果"+result);
	}
	
	
	
	/**
	 * example 6
	 * @throws Exception
	 */
	@Test
	public void recoveryTest2() throws Exception {
		Trace trace1 = new Trace();
		Trace trace2 = new Trace();
		Trace trace3 = new Trace();
		Collections.addAll(trace1.getEvents(), "t1","t2","t3","t4","t5","t6","t7","t3","t5","t4","t6","t7","t3","t5","t4","t6","t8");
		Collections.addAll(trace2.getEvents(), "t1","t2","t3","t5","t4","t6","t7","t3","t4","t5","t6","t8");
		Collections.addAll(trace3.getEvents(), "t1","t2","t3","t5","t4","t6","t8");
		
		List<Trace> traces = new ArrayList<>();
		Collections.addAll(traces, trace1,trace2,trace3);
		
		Trace dTrace = new Trace();
		Collections.addAll(dTrace.getEvents(), "t1","t3","t4","t6","t5","t7","t3","t5","t6","t9","t8");
		Algorithm1 algorithm1 = new Algorithm1();
		algorithm1.traceCluster(traces);
		List<Trace> recovery = algorithm1.repair(dTrace);
//		
//		Trace recovery = Algorithm1.repair(dTrace);
		System.out.println(recovery);
	}
	
	/**
	 * example 8
	 * @throws Exception
	 */
	@Test
	public void recoveryTest3() throws Exception {
		Trace trace1 = new Trace();
		Trace trace2 = new Trace();
		Collections.addAll(trace1.getEvents(), "t1","t2","t3","t4","t5","t6","t7","t6","t7","t6","t8","t9","t10","t2","t4","t3","t5","t6","t8","t9","t11");
		Collections.addAll(trace2.getEvents(), "t1","t2","t3","t4","t5","t6","t8","t9","t10","t2","t3","t4","t5","t6","t8","t9","t10","t2","t4","t3","t5","t6","t8","t9","t11");
		List<Trace> traces = new ArrayList<>();
		Collections.addAll(traces, trace1,trace2);
		
		Trace dTrace = new Trace();
		Collections.addAll(dTrace.getEvents(), "t1","t2","t3","t5","t6","t7","t6","t8","t9","t10","t2","t3","t5","t4","t6","t8","t9","t11");
		
		Algorithm1 algorithm1 = new Algorithm1();
		algorithm1.traceCluster(traces);
		List<Trace> recovery = algorithm1.repair(dTrace);
		
		System.out.println(recovery);
	}
	
	
	
	@Test
	public void DAGcatTest() throws Exception {
		Graph g1 = new Graph();
		Node node1 = new Node("g1:node1");
		Node node2 = new Node("g1:node2");
		Node node3 = new Node("g1:node3");
		Node node4 = new Node("g1:node4");
		
		g1.addNode(node1);
		g1.addNode(node2);
		g1.addNode(node3);
		g1.addNode(node4);
		
		g1.addEdge(node1, node2);
		g1.addEdge(node1, node3);
		g1.addEdge(node2, node4);
		g1.addEdge(node3, node4);
		
		Graph g2 = new Graph();
		Node node5 = new Node("g2:node5");
		Node node6 = new Node("g2:node6");
		Node node7 = new Node("g2:node7");
		Node node8 = new Node("g2:node8");
		
		g2.addNode(node5);
		g2.addNode(node6);
		g2.addNode(node7);
		g2.addNode(node8);
		
		g2.addEdge(node5, node6);
		g2.addEdge(node5, node7);
		g2.addEdge(node6, node8);
		g2.addEdge(node7, node8);
		
		Graph mergedGraph = Repair.DAGcat(g1, g2);
		
		//图显
		GraphJungVisualization visualization = new GraphJungVisualization(mergedGraph);
		visualization.show();
	}
}
