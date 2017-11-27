package org.nust.wsong.algorithm;

import java.util.ArrayList;
import java.util.List;

import org.nust.wsong.model.Trace;
import org.nust.wsong.util.TraceUtil;

/**
 * @description editdistance计算法 修复
 * @author xxx
 *
 */
public class Algorithm3 {
	/**
	 * 
	 * @param deviated trace dtrace
	 * @param compliant trace traces
	 */
	public static Trace recovery(Trace dtrace,List<Trace> ctraces){
//		List<Trace> result = new ArrayList<>();
		Trace result =ctraces.get(0);
		int minimumDistance = Integer.MAX_VALUE;
		int distance;
		for(Trace trace:ctraces){
//			distance = TraceUtil.editDistance(dtrace, trace);
			distance = TraceUtil.editDistance(trace,dtrace);
			if(distance<minimumDistance){
				/*result.clear();
				result.add(trace);*/
				result = trace;
				minimumDistance = distance;
			}/*else if(distance==minimumDistance){
				result.add(trace);
			}*/
		}
			System.out.println("距离为:"+minimumDistance);
		return result;
	}
	
	/**
	 * 
	 * @param deviated trace dtrace
	 * @param compliant trace traces
	 */
	public static List<Trace> recovery(List<Trace> dtraces,List<Trace> ctraces){
		 List<Trace> result = new ArrayList<>();
		 for(Trace dTrace:dtraces){
			 result.add(recovery(dTrace, ctraces));
		 }
		
		return result;
	
	}
	

}
