package org.nust.wsong.repair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.nust.wsong.constant.Constant;
import org.nust.wsong.mining.Mining;
import org.nust.wsong.model.Edge;
import org.nust.wsong.model.Graph;
import org.nust.wsong.model.Loop;
import org.nust.wsong.model.Node;
import org.nust.wsong.model.Trace;
import org.nust.wsong.util.MyCollectionsUtils;
import org.nust.wsong.util.TraceUtils;

public class Repair {

	/**
	 * Algorithm 3 不带循环的修复
	 * 
	 * @param trace
	 *            偏差日志
	 * @param g
	 *            修复模型
	 * @return
	 */
	public static Trace repair(Trace trace, Graph g) {

		/**
		 * 修改： 修改人：常震 修改时间：2017.11.29
		 */
		// //////////////////////////////////////////////////////////////////////
		Trace newtrace = new Trace();// 新建一个trace的克隆，为了之后的操作不影响原操作

		ArrayList<String> currentEvents = new ArrayList<>();
		for (String string : trace.getEvents())
			currentEvents.add(string);
		newtrace.setEvents(currentEvents);
		// //////////////////////////////////////////////////////////////////////

		/**
		 * 修改： 修改人：常震 修改时间：2017.11.27
		 */
		// ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		HashMap<String, Integer> exists = new HashMap<String, Integer>();// 记录trace中各个事件出现的次数
		List<String> traceEvents = trace.getEvents();
		for (String event : traceEvents) {
			if (!exists.containsKey(event))
				exists.put(event, 1);
			else
				exists.put(event, exists.get(event) + 1);
		}

		String boundary = null;
		for (int i = trace.size() - 1; i >= 0; i--) {
			if (g.getNodeByLabel(trace.get(i)) != null && g.getNodeByLabel(trace.get(i)).size() > 1) {
				boundary = trace.get(i);
				break;
			}
		}

		List<Integer> segments = new ArrayList<Integer>();// 记录trace分段信息
		for (int i = 0; i < traceEvents.size(); i++) {
			if (traceEvents.get(i).equals(boundary))
				segments.add(i);
		}
		segments.add(traceEvents.size() - 1);
		// ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

		// 入度为0的顶点
		List<Node> zeroList = new ArrayList<>();
		for (Node node : g.getNodes()) {
			if (g.getInEdgesCount(node) == 0)
				zeroList.add(node);
		}

		int i = 0;
		Trace repairedTrace = new Trace();
		String s = null;
		while (g.getNodes().size() > 0) {

			boolean flag = false;// 能够重现

			if (i < newtrace.size()) {
				s = newtrace.get(i);
				// if (i < trace.size()) {
				// s = trace.get(i);
				// dita[i]不属于V，冗余元素
				if (g.getNodeByLabel(s) == null) {
					i++;
					continue;
				}
				for (Node node : g.getNodeByLabel(s)) {
					if (zeroList.contains(node)) {
						flag = true;
						repairedTrace.append(s);
						/**
						 * 修改： 修改人：常震 修改时间：2017.11.27
						 */
						// /////////////////////////////////////////////////////////////////////////////////////////////////////////
						exists.put(s, exists.get(s) - 1);
						// /////////////////////////////////////////////////////////////////////////////////////////////////////////
						g.removeNode(node);
						zeroList.remove(node);
						for (Edge e : g.getOutEdges(node)) {
							g.removeEdge(e);
							if (g.getInEdgesCount(e.getTarget()) == 0) {
								zeroList.add(e.getTarget());
							}
						}
						break;
					}
				}
				if (flag) {
					i++;
					continue;
				}
			}

			/**
			 * 修改： 修改人：常震 修改时间：2017.11.27 去掉冗余
			 */
			// ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
			if (boundary != null && exists.get(s) > g.getNodeByLabel(s).size()) {
				int current = 0;
				for (int m = 0; m < segments.size(); m++) {
					if (i < segments.get(m)) {
						current = m;
						break;
					}
				}
				List<String> sublist = new ArrayList<String>();
				if (current == 0)
					sublist = traceEvents.subList(0, i);
				else {
					sublist = traceEvents.subList(segments.get(current - 1), i);
				}
				int before = sublist.lastIndexOf(s);
				if (before != -1) {
					i++;
					exists.put(s, exists.get(s) - 1);
					continue;
				}
			}
			// ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

			// 任找一个不属于Ts的能够重现的重现
			for (Node node : zeroList) {
				if (!newtrace.getEvents().subList(i, newtrace.getEvents().size()).contains(node.getLabel())) {
					// if (!trace.getEvents().subList(i,
					// trace.getEvents().size()).contains(node.getLabel())) {
					flag = true;
					repairedTrace.append(node.getLabel());
					g.removeNode(node);
					zeroList.remove(node);
					for (Edge e : g.getOutEdges(node)) {
						g.removeEdge(e);
						if (g.getInEdgesCount(e.getTarget()) == 0) {
							zeroList.add(e.getTarget());
						}
					}
					break;
				}
			}
			if (flag) {
				continue;
			}

			// 往后找一个能重现的
			for (int j = i + 1; j < newtrace.size(); j++) {
				String sj = newtrace.get(j);
				// for (int j = i + 1; j < trace.size(); j++) {
				// String sj = trace.get(j);
				if (null == g.getNodeByLabel(sj))
					continue;
				if (flag) {
					break;
				}
				for (Node node : g.getNodeByLabel(sj)) {
					if (zeroList.contains(node)) {
						flag = true;
						repairedTrace.append(sj);
						g.removeNode(node);
						zeroList.remove(node);
						for (Edge e : g.getOutEdges(node)) {
							g.removeEdge(e);
							if (g.getInEdgesCount(e.getTarget()) == 0) {
								zeroList.add(e.getTarget());
							}
						}
						/**
						 * 修改： 修改人：常震 修改时间：2017.11.29
						 */
						// //////////////////////////////////////////////////////////////////////
						if (exists.get(sj) != null && exists.get(sj) <= g.getNodeByLabel(sj).size()) {// 这样的情况认定为缺失
						} else// 在trace中去掉乱序掉的事件
							newtrace.getEvents().remove(j);
						// //////////////////////////////////////////////////////////////////////
						break;
					}
				}
			}
		}

		return repairedTrace;
	}

	/**
	 * Algorithm 6 有循环的修复
	 * 
	 * @param t
	 * @param clusters
	 * @param loops
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public static Trace repair2(Trace t, List<Set<Trace>> clusters, Set<Loop> loops) {
		// long start = System.nanoTime();
		List<Graph> graphs = new ArrayList<>();
		for (Set<Trace> c : clusters) {
			Graph g = Mining.mining(c);
			graphs.add(g);
		}

		// long end1 = System.nanoTime();
		// 计算N2的发生次数 先假定就一个嵌套循环
		Map<Set<String>, Integer> rN2Map = new HashMap<>();
		Map<Set<String>, Set<String>> nested = (Map<Set<String>, Set<String>>) TraceUtils.traceUtilMap
				.get(Constant.Sheel_nested);
		Map<Set<String>, Integer> position = new HashMap<>();

		for (int i = clusters.size() - 1; i >= 0; i--) {
			Trace next = clusters.get(i).iterator().next();
			for (Loop loop : loops) {
				if (loop.getN1().containsAll(next.getEvents())) {
					position.put(loop.getN2(), i);
				}
			}
		}
		// 这里结束--------------------------------------------------------------------
		// System.out.println("position:"+position);
		List<Trace> segments = new LinkedList<>();

		if (nested.size() > 0) {
			Loop outer = null;
			Loop inner = null;
			for (Loop loop : loops) {
				if (nested.containsKey(loop.getN2())) {
					outer = loop;
				} else if (nested.containsValue(loop.getN2())) {
					inner = loop;
				}
			}
			Map<String, Object> resultMap = TraceUtils.mode(t, outer, inner);
			int mode = (int) resultMap.get(Constant.Sheel_Mode);
			String s = (String) resultMap.get(Constant.Sheel_ModeElement);
			System.out.println("mode:" + mode);

			segments = TraceUtils.splitWithEle(t, s);
			rN2Map.put(outer.getN2(), mode - 1);
		} else {
			segments.add(t);
		}

		int currentIndex = 0;
		Trace currentSegment = segments.get(currentIndex);
		// 没有嵌套的循环
		for (Loop loop : loops) {
			if (nested.containsKey(loop.getN2()))
				continue;
			int rN1 = TraceUtils.mode(currentSegment, loop);
			int rN2 = rN1 - 1;
			rN2Map.put(loop.getN2(), rN2);
		}
		// int j =0;
		Graph temp = graphs.get(0);
		// Trace currentSegment = segments.get(0);
		for (int i = 1; i < graphs.size(); i++) {
			Set<String> nodes = new HashSet<>();
			for (Node n : graphs.get(i).getNodes())
				nodes.add(n.getLabel());
			// 不是N2
			if (!rN2Map.containsKey(nodes)) {
				temp = DAGcat(temp, graphs.get(i));
				continue;
			}
			if (rN2Map.get(nodes) == 0) {
				continue;
			}
			if (rN2Map.get(nodes) > 0) {

				/*
				 * if(nested.containsKey(graphs.get(i))){ //找到 }
				 */

				// 找到相应的位置-1 下次循环就是对应的n1的位置
				temp = DAGcat(temp, graphs.get(i));
				i = position.get(nodes) - 1;
				rN2Map.put(nodes, rN2Map.get(nodes) - 1);

				// 嵌套循环跳入下个分段
				if (nested.containsKey(nodes)) {
					currentIndex++;
					currentSegment = segments.get(currentIndex);
					// 没有嵌套的循环
					for (Loop loop : loops) {
						if (nested.containsKey(loop.getN2()))
							continue;
						int rN1 = TraceUtils.mode(currentSegment, loop);
						int rN2 = rN1 - 1;
						rN2Map.put(loop.getN2(), rN2);
					}
				}
				continue;
			}
		}
		// long end2 = System.nanoTime();
		// 图显
		// GraphJungVisualization visualization = new
		// GraphJungVisualization(temp);
		// visualization.show();
		/*
		 * System.out.println("t是什么："+t); System.out.println("temp是什么："+temp);
		 */
		// t是偏差日志，temp是修复模型
		Trace repaired = repair(t, temp);
		// System.out.println("修复好的日志"+repaired);
		// long end3 = System.nanoTime();

		// System.out.println("第71部分" + (end1 - start) / 1000000.0);
		// System.out.println("第72部分" + (end2 - start) / 1000000.0);
		// System.out.println("第73部分" + (end3 - start) / 1000000.0);
		return repaired;
	}

	/**
	 * 启发式规则1 选出与trace相似性最大的cluster
	 * 
	 * @param t
	 * @param clusters
	 * @return
	 */
	public static List<List<Trace>> heuristics1(Trace t, List<List<Trace>> clusters) {
		double d = Double.MIN_VALUE;
		// List<Trace> maxCluster = clusters.get(0);

		Map<Double, List<List<Trace>>> maxClusters = new HashMap<Double, List<List<Trace>>>();
		for (List<Trace> cluster : clusters) {
			// 交集
			Set<String> intersection = MyCollectionsUtils.intersection(t.getEvents(), cluster.get(0).getEvents());
			// 并集
			Set<String> union = MyCollectionsUtils.union(t.getEvents(), cluster.get(0).getEvents());
			double value = intersection.size() * 1.0 / union.size();
			/*
			 * System.out.println(
			 * "***********************************************************");
			 * System.out.println("value:"+value); System.out.println(cluster);
			 * System.out.println(
			 * "***********************************************************");
			 */
			if (maxClusters.keySet().contains(value)) {
				maxClusters.get(value).add(cluster);
			} else {
				List<List<Trace>> tempClusters = new ArrayList<>();
				tempClusters.add(cluster);
				maxClusters.put(value, tempClusters);
			}
			if (d < value) {
				d = value;
				// maxCluster = cluster;
			}
		}

		return maxClusters.get(d);
		// return maxCluster;
	}

	/**
	 * 合并g1 g2 将g1中出度为0的节点与g2中入度为0的相连
	 * 
	 * @param g1
	 * @param g2
	 * @param i
	 * @return
	 */
	public static Graph DAGcat(Graph g1, Graph g2) {
		Node source = null;
		Node target = null;

		Map<String, Node> nodeMap = new HashMap<>();
		for (Node n : g1.getNodes()) {
			if (g1.getOutEdgesCount(n) == 0) {
				source = n;
				break;
			}
		}
		for (Node n : g2.getNodes()) {
			if (g2.getInEdgesCount(n) == 0) {
				target = n;
			}
			nodeMap.put(n.getLabel(), new Node(n.getLabel()));
			g1.addNode(nodeMap.get(n.getLabel()));
		}
		for (Edge e : g2.getEdges()) {
			Node newSource = nodeMap.get(e.getSource().getLabel());
			Node newTarget = nodeMap.get(e.getTarget().getLabel());
			Edge newEdge = new Edge(newSource, newTarget);
			g1.addEdge(newEdge);
		}
		g1.addEdge(source, nodeMap.get(target.getLabel()));
		return g1;
	}
}
