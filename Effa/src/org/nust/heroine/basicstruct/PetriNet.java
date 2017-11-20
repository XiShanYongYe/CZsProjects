package org.nust.heroine.basicstruct;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.normalisiert.utils.ElementaryCyclesSearch;

/**
 * Petri网结构
 * 
 * @author Administrator
 * 
 */
public class PetriNet extends Graph implements Serializable, Cloneable {

	private static final long serialVersionUID = 3201629470197559118L;

	public Node sourcePlace;// 开始节点
	public Node sinkPlace;// 终止节点
	public ArrayList<Node> places;// 库所集合
	public ArrayList<Node> transitions;// 变迁集合
	public ArrayList<Node> backs;// 循环中的回边节点集合
	public ArrayList<Node> exits;// 退出循环语义
	public ArrayList<List<Node>> f1;// 循环中的正序节点组的集合
	public ArrayList<List<Node>> f2;// 循环中的反序节点组的集合

	private HashMap<String, Node> transMap = new HashMap<String, Node>();// 变迁名称与节点map
	// private HashMap<String, Node> placesMap = new HashMap<String, Node>();
	public boolean hasCycle = false;
	public boolean isHasIntersection = false;

	public ArrayList<Node> getBack_transitions() {
		return backs;
	}

	public PetriNet() {
		super();
		initPetri();
	}

	public void initPetri() {
		sourcePlace = new Node();
		sinkPlace = new Node();
		transitions = new ArrayList<Node>();
		places = new ArrayList<Node>();
		backs = new ArrayList<Node>();
		exits = new ArrayList<Node>();
		f1 = new ArrayList<List<Node>>();
		f2 = new ArrayList<List<Node>>();
	}

	public ArrayList<Node> getPlaces() {
		return places;
	}

	public ArrayList<Node> getTransitons() {
		return transitions;
	}

	/**
	 * 判断该节点是否为选择节点
	 * 
	 * @param n
	 * @return
	 */
	public boolean isChoiceNode(Node n) {
		if (n.getType().equals("transition"))
			return false;
		int j = 0;
		for (int i = 0; i < arcs.size(); i++) {
			if (arcs.get(i).getSource() == n) {
				j++;
			}
		}
		if (j > 1) {
			return true;
		}
		return false;
	}

	/**
	 * 初始化变迁名称与节点map表
	 */
	public void initTransMap() {
		for (Node node : getTransitons())
			transMap.put(node.getID(), node);
	}

	public HashMap<String, Node> getTransMap() {
		return transMap;
	}

	/**
	 * 判断该节点是否为选择节点
	 */
	public boolean isChoicePlace(Place n) {
		int j = 0;
		for (int i = 0; i < arcs.size(); i++) {
			if (arcs.get(i).getSource() == n) {
				j++;
			}
		}
		if (j > 1) {
			return true;
		}
		return false;
	}

	/**
	 * 判断当前petri网中是否存在选择
	 */
	public boolean HasChoiceNode() {
		for (int i = 0; i < nodes.size(); i++) {
			if (isChoiceNode(nodes.get(i))) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 判断该节点能否被点火
	 */
	public boolean enabled(Node n) {
		for (Node node : n.getPredecessors())
			if (node.gettoken() <= 0)
				return false;
		return true;
	}

	/**
	 * 将该节点点火
	 */
	public void fire(Node n) {
		for (Node node : n.getPredecessors())
			node.multoken();
		for (Node node : n.getSuccessors())
			node.addtoken();
	}

	/**
	 * token初始化
	 */
	public void initToken() {
		for (Node node : getPlaces())
			if (node.equals(getSourcePlace()))
				node.settoken(1);
			else
				node.settoken(0);
	}

	/**
	 * 显示度信息
	 */
	public void showDegree() {
		for (Node node : nodes) {
			System.out.println(node + "\t" + " " + node.getIndegree() + " "
					+ node.getOutdegree());
		}

	}

	public Node getSourcePlace() {
		return sourcePlace;
	}

	public Node getSinkPlace() {
		return sinkPlace;
	}

	public void computePetri() {
		// System.out.println(getNodes()+"||||||||"+getArcs());
		// initToken();
		initPetri();
		for (Node node : getNodes()) {
			if (node != null) {
				node.setIndegree(0);
				node.setOutdegree(0);
				if (node.getType().equalsIgnoreCase("place"))
					places.add(node);
				else
					transitions.add(node);
				node.predecessors = new ArrayList<Node>();
				node.successors = new ArrayList<Node>();
			}
		}

		for (Arc arc : getArcs()) {
			Node source = getNodeById(arc.getSource().getID());
			Node target = getNodeById(arc.getTarget().getID());
			// Node source = arc.getSource();
			// Node target = arc.getTarget();
			source.setOutdegree(source.getOutdegree() + 1);
			target.setIndegree(target.getIndegree() + 1);
			source.getSuccessors().add(target);
			target.getPredecessors().add(source);
		}

		// 获得开始和结束节点
		for (Node node : getPlaces()) {
			if (node.getIndegree() == 0)
				this.sourcePlace = node;
			if (node.getOutdegree() == 0)
				this.sinkPlace = node;

		}

		List<PetriNet> test = getAllCycle();
		if (test.size() > 0)
			hasCycle = true;

		// 将所有循环重新排列，以循环开始节点为开始
		List<List<Node>> circuits = new ArrayList<List<Node>>();
		for (PetriNet p : test) {
			List<Node> circuit = new ArrayList<Node>();
			for (int i = 0; i < p.getNodes().size(); i++) {
				Node n = p.getNodes().get(i);
				if (n.getOutdegree() > 1 && n.getType().equals("place")) {
					circuit.clear();
					circuit.addAll(p.getNodes().subList(i, p.getNodes().size()));
					circuit.addAll(p.getNodes().subList(0, i));
				}
			}
			circuits.add(circuit);
		}

		// System.out.println("circuits" + circuits);

		for (List<Node> circuit : circuits) {
			Node loop_start = circuit.get(0);
			Node loop_end = null;
			// 找到循环返回向结束的节点
			for (int i = 1; i < circuit.size(); i++) {
				Node temp = circuit.get(i);
				if (temp.getIndegree() > 1
						&& temp.getType().equalsIgnoreCase("place"))
					loop_end = temp;
			}
			/**
			 * 修改： 修改人：常震 时间：2017.11.06
			 */
			// /////////////////////////////////////////////////////////
			// List<Node> f2s = circuit.subList(1, circuit.indexOf(loop_end));
			// List<Node> f1s = circuit.subList(circuit.indexOf(loop_end) + 1,
			// circuit.size());
			List<Node> f2s = new ArrayList<Node>();
			List<Node> f1s = new ArrayList<Node>();
			for (int i = 1; i < circuit.indexOf(loop_end); i++) {
				f2s.add(circuit.get(i));
			}
			for (int i = circuit.indexOf(loop_end) + 1; i < circuit.size(); i++) {
				f1s.add(circuit.get(i));
			}
			// /////////////////////////////////////////////////////////
			int index;
			if ((index = f2.indexOf(f2s)) != -1) {
				f1.get(index).addAll(f1s);
			} else {
				f1.add(f1s);
				f2.add(f2s);
				backs.add(circuit.get(1));

				for (Node n : loop_start.getSuccessors()) {
					if (!backs.contains(n)) {
						exits.add(n);
						break;
					}
				}
			}
		}
		isHasIntersection = hasIntersection(f1);
	}

	public void showInfo() {
		System.out.println("##########petriNet info##########");
		System.out.println("sourcePlace:" + sourcePlace);
		System.out.println("sinkPlace:" + sinkPlace);
		System.out.println("transitions:" + transitions);
		System.out.println("places:" + places);
		System.out.println("f1:" + f1);
		System.out.println("f2:" + f2);
		System.out.println("backs:" + backs);
		System.out.println("exits:" + exits);
		System.out.println("isHasIntersection:" + isHasIntersection);
		System.out.println("##################################");
	}

	@Override
	public PetriNet clone() throws CloneNotSupportedException {
		PetriNet g = new PetriNet();
		for (Node node : getNodes())
			g.addNode(node.clone());
		for (Arc arc : getArcs())
			g.addArc(arc.clone());
		return g;
	}

	/**
	 * 判断所有循环之间是否有交点
	 * 
	 * @param circuits
	 * @return
	 */
	public boolean hasIntersection(List<List<Node>> circuits) {
		if (circuits.size() < 2)
			return false;
		for (int a = 0; a < circuits.size(); a++) {
			for (int b = a + 1; b < circuits.size(); b++) {
				List<Node> ca = circuits.get(a);
				List<Node> cb = circuits.get(b);
				for (Node node : ca)
					if (node.getType().equalsIgnoreCase("place"))
						if (cb.contains(node))
							return true;
			}

		}
		return false;
	}

	/**
	 * 获得当前Petri网中的所有循环子Petri
	 * 
	 * @return
	 */
	public List<PetriNet> getAllCycle() {
		List<PetriNet> graphs = new ArrayList<PetriNet>();
		String[] nodes = new String[getNodes().size()];
		for (int i = 0; i < getNodes().size(); i++) {
			nodes[i] = getNodes().get(i).getID();
		}
		ElementaryCyclesSearch ecs = new ElementaryCyclesSearch(
				getAdjacentMatrix(), nodes);
		List<?> cycles = ecs.getElementaryCycles();
		for (int i = 0; i < cycles.size(); i++) {
			PetriNet graph = new PetriNet();
			@SuppressWarnings("unchecked")
			List<String> cycle = (List<String>) cycles.get(i);
			int index;
			if ((index = ((List<String>) cycle).indexOf("T-ADD")) != -1) {
				int length = cycle.size();
				int j = index;
				do {
					if (j == cycle.size())
						j = 0;
					Node source = getNodeById((String) cycle.get(j % length));
					Node target = getNodeById((String) cycle.get((j + 1)
							% length));
					graph.addNode(source);
					graph.addArc(source, target);
					j++;
				} while (j % cycle.size() != index);
			} else {
				for (int j = 0; j < cycle.size() - 1; j++) {
					Node source = getNodeById((String) cycle.get(j));
					Node target = getNodeById((String) cycle.get(j + 1));
					graph.addNode(source);
					graph.addArc(source, target);
				}

				graph.addNode(getNodeById((String) cycle.get(cycle.size() - 1)));
				graph.addArc(getNodeById((String) cycle.get(cycle.size() - 1)),
						getNodeById((String) cycle.get(0)));
			}
			graphs.add(graph);
		}
		return graphs;
	}

	@Override
	public int hashCode() {
		int hashCode = 0;
		for (Node n : getNodes()) {
			hashCode += n.hashCode();
		}
		for (Arc arc : getArcs()) {
			hashCode += arc.hashCode();
		}
		return hashCode & 0x7FFFFFFF;
	}

	// 11.22
	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof PetriNet))
			return false;

		PetriNet pn = (PetriNet) obj;
		if (getNodes().size() != pn.getNodes().size()
				|| getArcs().size() != pn.getArcs().size())
			return false;

		for (Node node : getNodes()) {
			if (pn.getNodeById(node.getID()) == null)
				return false;
		}
		for (Arc arc : getArcs()) {
			if (pn.getArcByST(arc.getSource().getID(), arc.getTarget().getID()) == null)
				return false;
		}
		return true;
	}
}
