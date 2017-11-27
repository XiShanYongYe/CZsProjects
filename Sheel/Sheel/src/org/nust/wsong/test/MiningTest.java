package org.nust.wsong.test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Test;
import org.nust.wsong.Recovery.Recovery;
import org.nust.wsong.model.Graph;
import org.nust.wsong.model.Loop;
import org.nust.wsong.model.Trace;

public class MiningTest {
	public static void main(String[] args) {
		List<String> trace1 = new ArrayList<>();
		trace1.add("t1");
		trace1.add("t2");
		trace1.add("t3");
		trace1.add("t4");
		trace1.add("t5");
		trace1.add("t6");
		trace1.add("t7");
		trace1.add("t8");
		
		List<String> trace2 = new ArrayList<>();
		trace2.add("t1");
		trace2.add("t4");
		trace2.add("t5");
		trace2.add("t2");
		trace2.add("t3");
		trace2.add("t6");
		trace2.add("t9");
		
		List<String> trace3 = new ArrayList<>();
		trace3.add("t1");
		trace3.add("t4");
		trace3.add("t2");
		trace3.add("t5");
		trace3.add("t3");
		trace3.add("t6");
		trace3.add("t9");
		
		Set<Trace> traces = new HashSet<>();
		Trace t1 = new Trace(trace1);
		Trace t2 = new Trace(trace2);
		Trace t3 = new Trace(trace3);
		
		traces.add(t1);
		traces.add(t2);
		traces.add(t3);
		
		
		
		
		
		Trace trace = new Trace();
		List<String> list = new ArrayList<>();
		list.add("t1");
/*		list.add("t3");
		list.add("t2");
		list.add("t4");
		list.add("t6");
		list.add("t8");
		list.add("t9");*/
		trace.setEvents(list);
		
		Graph g = Recovery.mining(traces);
		Trace repairedStr = Recovery.repair(trace, g);
		System.out.println("修复前"+trace);
		System.out.println("修复后"+repairedStr);
	}
	
	/**
	 * 带循环的修复
	 * @throws Exception
	 */
	@Test
	public void testRepair2() throws Exception {
		
		Set<Trace> c1 = new HashSet<>();
		Trace trace1 = new Trace();
		trace1.append("t1");
		trace1.append("t2");
		c1.add(trace1);
		
		Set<Trace> c2 = new HashSet<>();
		Trace trace2 = new Trace();
		trace2.append("t3");
		trace2.append("t4");
		trace2.append("t5");
		trace2.append("t6");
		
		Trace trace3 = new Trace();
		trace3.append("t3");
		trace3.append("t5");
		trace3.append("t4");
		trace3.append("t6");
		c2.add(trace2);
		c2.add(trace3);
		
		Set<Trace> c4 = new HashSet<>();
		Trace trace4 = new Trace();
		trace4.append("t7");
		//trace4.append("t10");
		c4.add(trace4);
		
		Set<Trace> c5 = new HashSet<>();
		Trace trace5 = new Trace();
		trace5.append("t8");
		//trace5.append("t9");
		c5.add(trace5);
		
		List<Set<Trace>> clusters = new ArrayList<>();
		clusters.add(c1);
		clusters.add(c2);
		clusters.add(c4);
		clusters.add(c5);
		
		Set<Loop> loops = new HashSet<>();
		Loop loop = new Loop();
		//loop.setN1(Arrays.asList("t3","t4","t5","t6"));
		Set<String> N1 = new HashSet<>();
		Set<String> N2 = new HashSet<>();
		N1.addAll(Arrays.asList("t3","t4","t5","t6"));
		N2.addAll(Arrays.asList("t7"));
		loop.setN1(N1);
		loop.setN2(N2);
		loops.add(loop);
		
		Trace t  = new Trace();
		t.append("t1");
		t.append("t3");
		t.append("t4");
		t.append("t6");
		t.append("t5");
		t.append("t7");
		t.append("t3");
		t.append("t5");
		t.append("t6");
		t.append("t9");
		t.append("t8");
		Recovery.repair2(t,clusters,loops);
	}
	
	@Test
	public void testHeuristic1() throws Exception {
		Trace t  = new Trace();
		t.append("t1");
		t.append("t3");
		t.append("t2");
		t.append("t4");
		t.append("t6");
		t.append("t8");
		t.append("t9");

		Trace t1 = new Trace();
		t1.append("t1");
		t1.append("t2");
		t1.append("t3");
		t1.append("t4");
		t1.append("t5");
		t1.append("t6");
		t1.append("t7");
		t1.append("t8");

		Trace t2 = new Trace();
		t2.append("t1");
		t2.append("t4");
		t2.append("t5");
		t2.append("t2");
		t2.append("t3");
		t2.append("t6");
		t2.append("t9");
		
		Trace t3 = new Trace();
		t3.append("t1");
		t3.append("t4");
		t3.append("t2");
		t3.append("t5");
		t3.append("t3");
		t3.append("t6");
		t3.append("t9");
		
		List<Set<Trace>> clusters = new ArrayList<>();
		Set<Trace> c1 = new HashSet<>();
		c1.add(t1);
		
		Set<Trace> c2 = new HashSet<>();
		c2.add(t2);
		
		clusters.add(c1);
		clusters.add(c2);
		
		Recovery.heuristics1(t, clusters);
		
		
	}
	

}
