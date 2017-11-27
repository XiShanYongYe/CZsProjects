package org.nust.wsong.algorithm;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections4.CollectionUtils;
import org.nust.wsong.Recovery.Recovery;
import org.nust.wsong.constant.Constant;
import org.nust.wsong.model.Graph;
import org.nust.wsong.model.Loop;
import org.nust.wsong.model.Trace;
import org.nust.wsong.util.GraphViz;
import org.nust.wsong.util.GraphVizUtil;
import org.nust.wsong.util.TraceUtil;

/**
 * @description 老师的修复类
 * @author xxx
 * @date 2016-10-1
 */

public class Algorithm2 {
	
	
	/**
	 * trace 进行聚类
	 * @param cTraces
	 * @return
	 */
	public static List<List<Trace>> traceCluster(List<Trace> cTraces){
		TraceUtil.traceUtilMap.put(Constant.Sheel_CompliantTraces, cTraces);
		List<List<Trace>> clusters = TraceUtil.traceCluster(cTraces);
		TraceUtil.traceUtilMap.put(Constant.Sheel_TraceClusters, clusters);
		System.out.println("Algorithm2:"+clusters.size());
		for(List<Trace> cluster:clusters){
			System.out.println("cluster:"+cluster);
		}
		return clusters;
	}
	
	/**
	 * 
	 * @param deviated trace dtrace
	 * @param compliant trace traces
	 */
	@SuppressWarnings("unchecked")
	public static Trace recovery(Trace dtrace){
		Trace repaired;
		List<Trace> cTraces = (List<Trace>) TraceUtil.traceUtilMap.get(Constant.Sheel_CompliantTraces);
		List<List<Trace>> clusters;
		if(TraceUtil.traceUtilMap.get(Constant.Sheel_TraceClusters)==null){
			clusters = TraceUtil.traceCluster(cTraces);
		}else{
			clusters = (List<List<Trace>>) TraceUtil.traceUtilMap.get(Constant.Sheel_TraceClusters);
		}
		
		List<Trace> referenceTraceCluster = Recovery.heuristics1(dtrace, clusters);
		
		//找到拥有活动最多的trace
		int max = Integer.MIN_VALUE;
		Trace maxEleTrace = referenceTraceCluster.iterator().next();
		for(Trace t:referenceTraceCluster){
			Set<String> temp = new HashSet<>();
			CollectionUtils.addAll(temp, t.getEvents());
			if(temp.size()>max||(temp.size()==max&&t.getEvents().size()>maxEleTrace.size())){
				max = temp.size();
				maxEleTrace = t;
			}
		}
		
		TraceUtil.traceUtilMap.put(Constant.Sheel_maxEleTrace, maxEleTrace);
//		System.out.println("拥有活动最多的trace是"+maxEleTrace);
		
		
		//投影  
		List<Trace> projectedTrace = new ArrayList<>();
		CollectionUtils.addAll(projectedTrace, referenceTraceCluster);
		for(List<Trace> cluster:clusters){
			if(cluster!=referenceTraceCluster){
				for(Trace trace:cTraces){
					Set<Trace> traces = TraceUtil.projection(trace, maxEleTrace.getEvents());
					for(Trace t:traces){
						if(!projectedTrace.contains(t))
							projectedTrace.add(t);
					}
					
				}
			}
		}
		
		List<Trace> traces1 = new ArrayList<>();//每个活动仅发生一次
		List<Trace> traces2 = new ArrayList<>();//有发生大于一次
		for(Trace t:projectedTrace){
			if(TraceUtil.detectOccurrences(t)){
				traces2.add(t);
			}else
				traces1.add(t);
		}
		
		
		 TraceUtil.traceUtilMap.put(Constant.Sheel_St1,traces1);
		
	
		
		
		
		//包含循环
		if(TraceUtil.detectOccurrences(maxEleTrace)){
			
			//trace分割
			TraceUtil.traceSegmentation2(traces2);

			Set<Loop> loops= (Set<Loop>) TraceUtil.traceUtilMap.get(Constant.Sheel_St1);
			
			System.out.println("包含的循环为: "+loops);
			List<Set<Trace>> segmentCluster = TraceUtil.segmentCluster();
			System.out.println("segmentCluster为："+segmentCluster);
			
//			long start = System.nanoTime();
			repaired = Recovery.repair2(dtrace, segmentCluster, loops);
//			long end =System.nanoTime();
//					System.out.println("测试："+(end-start)/1000000.0+"ms");
			
		}else{//不包含循环
			Graph  g= Recovery.mining(projectedTrace);
//			GraphVizUtil.show(g);
			repaired = Recovery.repair(dtrace, g);
			
		}
		return repaired;
	}
	
	/**
	 * 
	 * @param deviated trace dtrace
	 * @param compliant trace traces
	 */
	@SuppressWarnings("unchecked")
	public static List<Trace> recovery(List<Trace> dtraces){
		List<Trace> repairedTraces = new LinkedList<>();
		for(Trace trace:dtraces){
			Trace repairedTrace = recovery(trace);
			repairedTraces.add(repairedTrace);
		}
		
		return repairedTraces;
		
		
	}
}
