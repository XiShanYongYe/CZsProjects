package org.nust.wsong.algorithm;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections4.CollectionUtils;
import org.nust.wsong.constant.Constant;
import org.nust.wsong.mining.Mining;
import org.nust.wsong.model.Edge;
import org.nust.wsong.model.Graph;
import org.nust.wsong.model.Loop;
import org.nust.wsong.model.Node;
import org.nust.wsong.model.Trace;
import org.nust.wsong.repair.Repair;
import org.nust.wsong.util.StringDistance;
import org.nust.wsong.util.TraceUtils;

/**
 * @description sheel 修复法 正确日志聚类
 * @author xxx
 * 
 */

public class Algorithm1 {

	List<List<Trace>> clusters;

	static {
		Graph test = new Graph();
		Node helloNode = new Node("hello");
		Node worldNoe = new Node("world");
		Edge edge = new Edge(helloNode, worldNoe);
		test.addNode(helloNode);
		test.addNode(worldNoe);
		test.addEdge(edge);
	}

	/**
	 * trace 进行聚类
	 * 
	 * @param cTraces
	 * @return
	 */
	public List<List<Trace>> traceCluster(List<Trace> cTraces) {
		TraceUtils.traceUtilMap.put(Constant.Sheel_CompliantTraces, cTraces);
		clusters = TraceUtils.traceCluster(cTraces);// 聚类
		TraceUtils.traceUtilMap.put(Constant.Sheel_TraceClusters, clusters);
		// System.out.println("Algorithm1 cluster size:" + clusters.size());
		// for (List<Trace> cluster : clusters) {
		// System.out.println("cluster:" + cluster);
		// }
		return clusters;
	}

	/**
	 * 
	 * @param deviated
	 *            trace dtrace
	 * @param compliant
	 *            trace traces
	 */
	@SuppressWarnings("unchecked")
	public List<Trace> repair(Trace dtrace) {

		// System.out
		// .println("-----------------------------偏差日志进入修复-----------------------------");
		List<Trace> repairedtraces = new ArrayList<>();
		Trace repaired;

		// 确认参考聚类（不管处理的是带循环还是不带循环的修复，这一步都一样）
		List<List<Trace>> referenceTraceCluster = new ArrayList<>();
		if (clusters.size() > 1) {// 如果有多于一个分支
			referenceTraceCluster = Repair.heuristics1(dtrace, clusters);// 找到参考trace聚类（占时间很少，占2.68%）
		} else {// 修改：如果只有一个分支，直接确定参考聚类
			referenceTraceCluster.add(clusters.get(0));
		}

		for (List<Trace> subReferenceTraceCluster : referenceTraceCluster) {
			// 找到拥有活动最多的trace（之前聚类时每个聚类按照trace从长到到短排序
			Trace maxEleTrace = subReferenceTraceCluster.get(0);

			TraceUtils.traceUtilMap
					.put(Constant.Sheel_maxEleTrace, maxEleTrace);

			/**
			 * 投影之后的traces
			 */
			List<Trace> projectedTrace = new ArrayList<>();
			CollectionUtils.addAll(projectedTrace, subReferenceTraceCluster);
			for (List<Trace> cluster : clusters) {
				if (cluster != subReferenceTraceCluster) {
					for (Trace trace : cluster) {
						Set<Trace> traces = TraceUtils.projection(trace,
								maxEleTrace.getEvents());
						for (Trace t : traces) {
							if (!projectedTrace.contains(t))
								projectedTrace.add(t);
						}
					}
				}
			}

			List<Trace> traces1 = new ArrayList<>();// trace中每个活动仅发生一次
			List<Trace> traces2 = new ArrayList<>();// trace中有活动发生大于一次
			for (Trace t : projectedTrace) {
				if (TraceUtils.detectOccurrences(t)) {
					traces2.add(t);
				} else
					traces1.add(t);
			}
			TraceUtils.traceUtilMap.put(Constant.Sheel_St1, traces1);

			// 包含循环
			if (TraceUtils.detectOccurrences(maxEleTrace)) {

				// trace分割
				TraceUtils.segmentation(traces2);

				Set<Loop> loops = (Set<Loop>) TraceUtils.traceUtilMap
						.get(Constant.Sheel_St1);

				List<Set<Trace>> segmentCluster = TraceUtils.segmentCluster();

				repaired = Repair.repair2(dtrace, segmentCluster, loops);

			} else {// 不包含循环

				Graph g = Mining.mining(projectedTrace);
				// GraphJungVisualization visualization = new
				// GraphJungVisualization(g);
				// visualization.show();

				repaired = Repair.repair(dtrace, g);
			}
			repairedtraces.add(repaired);
		}

		return repairedtraces;
	}

	/**
	 * 
	 * @param deviated
	 *            trace dtrace
	 * @param compliant
	 *            trace traces
	 */
	public List<Trace> recovery(List<Trace> dtraces) {

		List<Trace> repairedTraces = new ArrayList<Trace>();

		for (Trace trace : dtraces) {

			List<Trace> repairedsubTraces = repair(trace);

			// 找出最小编辑距离的修复
			if (repairedsubTraces.size() == 1) {
				repairedTraces.add(repairedsubTraces.get(0));
			} else {
				int minEditDistance = StringDistance
						.editDistance(repairedsubTraces.get(0).getEvents(),
								trace.getEvents());
				Trace minRepairTrace = repairedsubTraces.get(0);
				for (int i = 1; i < repairedsubTraces.size(); i++) {
					int comparedEditDistance = StringDistance.editDistance(
							repairedsubTraces.get(i).getEvents(),
							trace.getEvents());
					if (comparedEditDistance < minEditDistance) {
						minEditDistance = comparedEditDistance;
						minRepairTrace = repairedsubTraces.get(i);
					}
				}
				repairedTraces.add(minRepairTrace);
			}
		}
		return repairedTraces;
	}
}
