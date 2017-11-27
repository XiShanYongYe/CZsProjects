package org.nust.wsong.test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.nust.wsong.Recovery.Recovery;
import org.nust.wsong.model.Graph;
import org.nust.wsong.model.Trace;
import org.nust.wsong.util.GraphVizUtil;


public class Test123 {
	public static void main(String[] args) {		
		Trace t1 = new Trace();
		Trace t2 = new Trace();
		Trace t3 = new Trace();
		Collections.addAll(t1.getEvents(), "t1","t2","t3","t4","t5","t6");
		Collections.addAll(t2.getEvents(), "t1","t4","t5","t2","t3","t6","t9");
		Collections.addAll(t3.getEvents(), "t1","t4","t2","t5","t3","t6","t9");
		List<Trace> traces = new ArrayList<>();
		Collections.addAll(traces, t1,t2,t3);
		
	    Graph graph = Recovery.mining(traces);
	    GraphVizUtil.show(graph);
		
//		DirectedGraphVisualizer vv = new DirectedGraphVisualizer(g);
//		
//		JFrame frame = new JFrame("test");
//		frame.setSize(600, 400);
//		frame.setVisible(true);
//		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//		frame.getContentPane().add(vv.getvv());
//		
		
		
		
//		System.out.println(graph.getVertexs());
//		System.out.println(graph.getEdges());
		
		
		
//		System.out.println(graph);
		
//		graph.addVertex(new vertex);
	}
}
