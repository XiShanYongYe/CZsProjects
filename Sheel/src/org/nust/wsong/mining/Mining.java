package org.nust.wsong.mining;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.nust.wsong.model.Graph;
import org.nust.wsong.model.Node;
import org.nust.wsong.model.Trace;

public class Mining {
	/**
	 * Algorithm 2 sub-process discovery 根据traces获得DAG
	 * 
	 * @param traces
	 */
	public static Graph mining(Collection<Trace> traces) {
		Set<String> nodes = new HashSet<>();// set中会去掉重复的元素
		Map<String, Set<String>> indirectRelation = new HashMap<>();// 间接关系
		for (Trace trace : traces) {
			for (int i = 0; i < trace.size(); i++) {
				String node1 = trace.get(i);
				nodes.add(node1);
				if (indirectRelation.get(node1) == null) {
					indirectRelation.put(node1, new HashSet<String>());
				}
				for (int j = i + 1; j < trace.size(); j++) {
					String node2 = trace.get(j);
					nodes.add(node2);
					indirectRelation.get(node1).add(node2);
				}
			}
		}

		Graph g = new Graph();
		// 加入点集
		for (String s : nodes) {
			Node node = new Node(s);
			g.addNode(node);
		}

		for (Entry<String, Set<String>> entry : indirectRelation.entrySet()) {
			String key = entry.getKey();
			for (String value : entry.getValue()) {
				if (!indirectRelation.get(value).contains(key)) {
					Node source = g.getNodeByLabel(key).get(0);
					Node target = g.getNodeByLabel(value).get(0);
					g.addEdge(source, target);
				}
			}
		}
		return g;
	}
}
