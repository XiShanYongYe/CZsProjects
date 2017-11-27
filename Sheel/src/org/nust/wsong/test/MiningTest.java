package org.nust.wsong.test;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.junit.Test;
import org.nust.wsong.UI.GraphJungVisualization;
import org.nust.wsong.mining.Mining;
import org.nust.wsong.model.Graph;
import org.nust.wsong.model.Trace;


public class MiningTest {
	
	@Test
	public void test() {
		Trace trace1 = new Trace();
		Trace trace2 = new Trace();
		Trace trace3 = new Trace();
		
		Collections.addAll(trace1.getEvents(), "t1","t2","t3","t4","t5","t6");
		Collections.addAll(trace2.getEvents(), "t1","t4","t5","t2","t3","t6","t9");
		Collections.addAll(trace3.getEvents(), "t1","t4","t2","t5","t3","t6","t9");
		
		Set<Trace> traces = new HashSet<>();
		Collections.addAll(traces, trace1,trace2,trace3);
		
		Graph g = Mining.mining(traces);
		
		System.out.println(g.getInEdgesCount(g.getNodeByLabel("t9").get(0)));
		System.out.println(g.getOutEdgesCount(g.getNodeByLabel("t9").get(0)));
		
		GraphJungVisualization visualization = new GraphJungVisualization(g);
		visualization.show();
	}
}
