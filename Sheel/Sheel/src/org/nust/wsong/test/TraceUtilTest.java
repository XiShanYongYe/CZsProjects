package org.nust.wsong.test;
import static org.junit.Assert.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.nust.wsong.constant.Constant;
import org.nust.wsong.model.Loop;
import org.nust.wsong.model.Trace;
import org.nust.wsong.util.TraceUtil;

public class TraceUtilTest {
	//private List<Trace> traces;
	Map<String, Collection<? extends Object>> map;
	@Before
	public void init(){
		
	}
	


	@Test
	public void traceCluster() throws Exception {
		Trace t1 = new Trace();
		Trace t2 = new Trace();
		Trace t3 = new Trace();
		Collections.addAll(t1.getEvents(), "t1","t2","t3","t4","t5","t6");
		Collections.addAll(t2.getEvents(), "t1","t4","t5","t2","t3","t6","t9");
		Collections.addAll(t3.getEvents(), "t1","t4","t2","t5","t3","t6","t9");

	
		List<Trace> traces = new ArrayList<>();
		traces.add(t1);
		traces.add(t2);
		traces.add(t3);
		List<Set<Trace>> traceCluster = TraceUtil.traceCluster(traces);
		System.out.println(traceCluster.size());
		System.out.println(t2.contains(t3));
		for(Set<Trace> ts:traceCluster){
			System.out.println(ts);
		}
	}
	
	@Test
	public void traceSegmentation() throws Exception {
		 List<Trace> traces = new ArrayList<>();
		 Trace t1 = new Trace();
		 Trace t2 = new Trace();
		 Trace t3 = new Trace();
		 
		 Collections.addAll(t1.getEvents(), "t1","t2","t3","t4","t5","t6","t7","t6","t7","t6","t8","t9","t10","t2","t4","t3","t5","t6","t8","t9","t11");
		 Collections.addAll(t2.getEvents(), "t1","t2","t3","t4","t5","t6","t8","t9","t10","t2","t3","t4","t5","t6","t8","t9","t10","t2","t4","t3","t5","t6","t8","t9","t11");
//		 Collections.addAll(t1.getEvents(), "t1","t2","t3","t4","t5","t6","t7","t3","t5","t4","t6","t7","t3","t5","t4","t6","t8");
//		 Collections.addAll(t2.getEvents(), "t1","t2","t3","t4","t5","t6","t7","t3","t5","t4","t6","t8");
//		 Collections.addAll(t3.getEvents(), "t1","t2","t3","t5","t4","t6","t8");

//		 Collections.addAll(traces, t1,t2,t3);
		 Collections.addAll(traces, t1,t2);
		 TraceUtil.traceSegmentation(traces);
		 System.out.println("test"+TraceUtil.traceUtilMap);
		// return null;
	}
	

	
	@Test
	public void segmentCluster(){
		 List<Trace> traces = new ArrayList<>();
		 Trace t1 = new Trace();
		 Trace t2 = new Trace();
		 Trace t3 = new Trace();
		 Trace t4 = new Trace();
		 Trace t5 = new Trace();
		 Trace t6 = new Trace();
		 Trace t7 = new Trace();
		 
		 Collections.addAll(t1.getEvents(), "t1","t2","t3","t4","t5","t6","t7");
		 Collections.addAll(t2.getEvents(), "t6","t7");
		 Collections.addAll(t3.getEvents(), "t6","t8","t9","t10","t2","t4","t3","t5");
		 Collections.addAll(t4.getEvents(), "t6","t8","t9","t11");
		 Collections.addAll(t5.getEvents(), "t1","t2","t3","t4","t5","t6","t8","t9","t10");
		 Collections.addAll(t6.getEvents(), "t2","t3","t4","t5","t6","t8","t9","t10");
		 Collections.addAll(t7.getEvents(), "t2","t4","t3","t5","t6","t8","t9","t11");
//		 Collections.addAll(t1.getEvents(), "t1","t2","t3","t4","t5","t6","t7");
//		 Collections.addAll(t2.getEvents(), "t3","t5","t4","t6","t7");
//		 Collections.addAll(t3.getEvents(), "t3","t5","t4","t6","t8");
//		 Collections.addAll(t4.getEvents(), "t1","t2","t3","t5","t4","t6","t7");
//		 Collections.addAll(t5.getEvents(), "t3","t4","t5","t6","t8");
//		 Collections.addAll(t6.getEvents(), "t1","t2","t3","t5","t4","t6","t8");
		 
		 Collections.addAll(traces, t1,t2,t3,t4,t5,t6,t7);
		 
		 Map<String, Collection<? extends Object>> map = new HashMap<>();
		 map.put("Ss", traces);
		 Set<Loop> loops = new HashSet<>();
		 
		 Loop loop = new Loop();
		 HashSet<String> set1 = new HashSet<>();
		 Collections.addAll(set1,"t2","t4","t3","t5","t6","t8","t9");
		 HashSet<String> set2 = new HashSet<>();
		 Collections.addAll(set2,"t10");
		 loop.setN1(set1);
		 loop.setN2(set2);
		 loops.add(loop);
		 
		 Loop loop1 = new Loop();
		 HashSet<String> set11 = new HashSet<>();
		 Collections.addAll(set11,"t6");
		 HashSet<String> set22 = new HashSet<>();
		 Collections.addAll(set22,"t7");
		 loop1.setN1(set11);
		 loop1.setN2(set22);
		 loops.add(loop1);
		 
		 map.put("Stl", loops);
		try {
			System.out.println("map:"+map);
			TraceUtil.segmentCluster2(map);
		} catch (Exception e) {
			e.printStackTrace();
		}
		 
		
		
		
	}
	
	@Test
	public void editDistance() throws Exception {
		Trace t1 = new Trace();
		Collections.addAll(t1.getEvents(), "A","B","C","D","E","F","E","b");
		Trace t2 = new Trace();
		Collections.addAll(t2.getEvents(), "A","B","D","C","E");
		
		System.out.println(TraceUtil.editDistance(t1,t2));
	}
	
	
	@Test
	public void traceSegmentation2() throws Exception {
		 Trace t1 = new Trace();
//		 Collections.addAll(t1.getEvents(), "t1","t2","t3","t4","t5","t6","t7","t6","t7","t6","t8","t9","t10","t2","t4","t3","t5","t6","t8","t9","t11");
//		 Collections.addAll(t1.getEvents(), "t0","t1","t2","t5","t2","t3","t6","t1","t2","t3","t4","t10","t1","t2","t3","t4","t7","t9","t7","t9","t7","t9","t7","t8");
		 Collections.addAll(t1.getEvents(), "t1","t2","t4","t3","t4","t5","t6","t7","t6","t8","t9","t10","t2","t4","t5","t6","t8","t9","t11");
//		 Collections.addAll(t1.getEvents(),"t0","t1","t2","t5","t1","t2","t5","t1","t2","t3","t6","t2","t5","t1","t2","t3","t4");
//		 Collections.addAll(t1.getEvents(),"t0","t1","t2","t5","t1","t2","t5","t1","t2","t3","t6","t2","t3","t4");
//		 Collections.addAll(t1.getEvents(), "t0","t1","t2","t1","t3","t4","t3","t5");
		 List<Trace> list = new ArrayList<>();
		 list.add(t1);
		 TraceUtil.traceSegmentation2(list);
	}
	@Test
	public void testName() throws Exception {
		assertEquals(new Double(1.1), new Double(1.1));
	}
	@Test(expected=NumberFormatException.class)
	public void testName1() throws Exception {
		BigDecimal x = new BigDecimal("Not a nunber");
	}
	
	@Test
	public void testProjection() throws Exception {
		List<String> list1 = new ArrayList<>();
		Collections.addAll(list1, "t1","t2","t3","t4","t5","t6");
		
		List<String> list2 = new ArrayList<>();
		Collections.addAll(list2, "t2","t4","t1","t5","t7");
		
		System.out.println(TraceUtil.projection(list1, list2));
	}
	
	@Test
	public void testMode() throws Exception {
		Loop loop = new Loop();
		Set<String> tn1 = new HashSet<>();
		Collections.addAll(tn1, "t3","t4","t5","t6");
		
		Set<String> tn2 = new HashSet<>();
		Collections.addAll(tn2, "t7");
		loop.setN1(tn1);
		loop.setN2(tn2);
		Trace t = new Trace();
		Collections.addAll(t.getEvents(), "t1","t3","t4","t6","t5","t7","t3","t5","t6","t9","t8");
		
		TraceUtil.mode(t,loop);
	}
	
	@Test
	public void testMode2() throws Exception {
		Loop loop1 = new Loop();
		Collections.addAll(loop1.getN1(), "t6");
		Collections.addAll(loop1.getN2(), "t7");
		Loop loop2 = new Loop();
		Collections.addAll(loop2.getN1(), "t6","t2","t3","t4","t5","t8","t9");
		Collections.addAll(loop2.getN2(), "t10");
		List<Loop> loops = new ArrayList<>();
		loops.add(loop1);
		loops.add(loop2);
		
		Trace t = new Trace();
		Collections.addAll(t.getEvents(), "t1","t2","t3","t5","t6","t7","t6","t8","t9","t10","t2","t3","t5","t4","t6","t8","t9","t11");
		
		Map<String, Object> resultMap = TraceUtil.mode(t, loop2, loop1);
		int mode = (int) resultMap.get(Constant.Sheel_Mode);
		String s = (String) resultMap.get(Constant.Sheel_ModeElement);
		System.out.println(mode);
		System.out.println(s);
		
	}
	
	
	@Test
	public void splitWithEle() throws Exception {
		Trace t = new Trace();
		Collections.addAll(t.getEvents(), "t1","t2","t3","t5","t6","t7","t6","t8","t9","t10","t2","t3","t5","t4","t6","t8","t9","t11");
		String ele = "t2";
		
		List<Trace> result = TraceUtil.splitWithEle(t, ele);
		System.out.println(result);
		
		
	}
	
}
