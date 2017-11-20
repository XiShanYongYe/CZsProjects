package org.nust.heroine.util;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Set;

import org.nust.heroine.basicstruct.Arc;
import org.nust.heroine.basicstruct.Graph;
import org.nust.heroine.basicstruct.PetriNet;
import org.nust.heroine.basicstruct.Node;

/**
 * @author xxx
 * @category Performace Modeling and Analysis of Workflow
 */
public class Decompose {

	static Node t = new Node("T-ADD");

	public static ArrayList<PetriNet> dec(PetriNet pn) {
		t.setType("transition");
		// Set<PetriNet> R = new HashSet<PetriNet>();
		ArrayList<PetriNet> R = new ArrayList<PetriNet>();
		List<Node> Pc = new ArrayList<Node>();

		step1(pn);

		// step2
		for (Node node : pn.getNodes()) {
			if (node.getOutdegree() > 1
					&& node.getType().equalsIgnoreCase("place")) {
				Pc.add(node);
			}
		}
		if (Pc.size() == 0) {
			step5(pn);
			R.clear();
			R.add(pn);
			return R;
		}
		R.add(pn);

		Set<PetriNet> R1 = new HashSet<PetriNet>();
		List<Node> Ps = new ArrayList<Node>();
		Node node = Pc.get(new Random().nextInt(Pc.size()));
		List<PetriNet> graphs = pn.getAllCycle();
		// test
		// R1.clear();
		// R1.addAll(graphs);
		// for(Graph graph:R1){
		// step5(graph);
		// }

		// test
		// System.out.println("graphs"+graphs.size());
		// for(PetriNet test:graphs)
		// System.out.println("graphs test"+test);
		for (int i = 0; i < graphs.size(); i++) {
			PetriNet graph = graphs.get(i);
			if (graph.getNodes().contains(node) && graph.getNodes().contains(t)) {
				R1.add(graph);
				break;
			}
		}
		// System.out.println("R1 size"+R1.size());
		// System.out.println("R1"+R1);

		// step3
		Set<PetriNet> R2 = new HashSet<PetriNet>();
		Set<Node> PL = new HashSet<Node>();

		while (condition(R1, Pc, Ps)) {
			R2.clear();
			R2.addAll(R1);
			PL.clear();

			for (Graph g : R2) {
				for (Node n : g.getNodes()) {
					if (Pc.contains(n) && !Ps.contains(n)) {
						PL.add(n);
						break;
					}
				}
			}
			// TODO 3.3
			Ps.addAll(PL);
			// System.out.println("PL"+PL);

			for (Node n : PL) {
				for (PetriNet g : R2) {
					if (g.getNodes().contains(n)) {
						List<Node> placePath = null;
						Set<PetriNet> tempR1 = new HashSet<PetriNet>();
						ArrayList<List<Node>> mergePath = new ArrayList<List<Node>>();
						tempR1.add(g);
						for (PetriNet p : graphs) {
							placePath = new ArrayList<Node>();
							if (p.equals(g))
								continue;
							int index = p.getNodes().indexOf(n);
							if (index < 0)
								continue;
							// System.out.println("index"+index);
							placePath.add(n);
							while (index < p.getNodes().size()) {
								index++;
								if (index == p.getNodes().size()) // 11.26 20:40
																	// add
									break;
								Node nodetemp = p.getNodes().get(index);
								if (g.getNodes().contains(nodetemp)) {
									placePath.add(nodetemp);
									break;
								} else {
									placePath.add(nodetemp);
								}
							}
							if (placePath.size() <= 2)
								continue;
							// System.out.println("placePath"+placePath);
							Node pl = placePath.get(0);
							Node pie = placePath.get(placePath.size() - 1);
							if (pl.getType().equalsIgnoreCase("place")
									&& g.getNodes().indexOf(pie) > g.getNodes()
											.indexOf(pl)) {
								PetriNet test = replacePlacePath(g, placePath,
										pl, pie);
								tempR1.add(test);
							} else {
								mergePath.add(placePath);
							}
						}
						// System.out.println("tempR1"+tempR1);
						// System.out.println("mergePahth"+mergePath);
						// System.out.println("R1 size"+R1.size());
						if (mergePath.size() > 0) {
							for (List<Node> path : mergePath) {
								for (PetriNet p : tempR1) {
									R1.add(mergePath(p, path, path.get(0),
											path.get(placePath.size() - 1)));
								}
								R1.remove(g);
								R1.add(mergePath(g, path, path.get(0),
										path.get(placePath.size() - 1)));
							}
						} else {
							R1.addAll(tempR1);
						}
					}
				}
			}
		}

		R.clear();
		R.addAll(R1);
		// step4 ���벢��
		// System.out.println("����");
		// System.out.println("R size"+R.size());

		Set<PetriNet> R11 = new HashSet<PetriNet>();
		R11.addAll(R);
		for (PetriNet g : R) {
			List<List<Node>> transitionPaths = new ArrayList<List<Node>>();
			List<Node> xorsplit = new ArrayList<Node>();
			for (Node n : g.getNodes()) {
				if (n.getOutdegree() > 1
						&& n.getType().equalsIgnoreCase("transition")) {
					xorsplit.add(n);
				}
			}
			for (Node n : xorsplit) {
				for (PetriNet p : graphs) {
					List<Node> transitionPath = new ArrayList<Node>();
					transitionPath = new ArrayList<Node>();
					if (p.equals(g))
						continue;
					int index = p.getNodes().indexOf(n);
					if (index < 0)
						continue;
					// System.out.println("index"+index);
					transitionPath.add(n);
					while (index < p.getNodes().size()) {
						index++;
						if (index == p.getNodes().size())// 11.26 20:40 add
							break;
						Node nodetemp = p.getNodes().get(index);
						if (g.getNodes().contains(nodetemp)) {
							transitionPath.add(nodetemp);
							break;
						} else {
							transitionPath.add(nodetemp);
						}
					}
					if (transitionPath.size() <= 2)
						continue;
					// System.out.println("transitionPath"+transitionPath);
					transitionPaths.add(transitionPath);
				}
				R11.remove(g);
				for (List<Node> path : transitionPaths) {
					g = mergePath(g, path, path.get(0),
							path.get(path.size() - 1));
				}
				R11.add(g);
			}
		}

		R.clear();
		R.addAll(R11);

		// 1.14
		ArrayList<PetriNet> RTemp = new ArrayList<PetriNet>();
		// step5
		for (PetriNet pnet : R) {
			step5(pnet);

			// 1.14
			try {
				RTemp.add(pnet.clone());
			} catch (CloneNotSupportedException e) {
				e.printStackTrace();
			}

			// System.out.print("hashcode"+pnet.hashCode());
			// System.out.print("node size"+pnet.getNodes().size());
			// System.out.print("arc size"+pnet.getArcs().size());
			// System.out.println(pnet);
		}

		// for(PetriNet)

		// ArrayList<PetriNet> pnList = new ArrayList<PetriNet>();
		// pnList.addAll(R);
		// return pnList;
		return RTemp;
	}

	private static void step1(PetriNet pn) {
		pn.computePetri();
		Node source = pn.getSourcePlace();
		Node sink = pn.getSinkPlace();
		Arc a = new Arc(sink, t);
		Arc b = new Arc(t, source);
		pn.addNode(t);
		pn.addArc(a);
		pn.addArc(b);

	}

	private static void step5(Graph pn) {
		Iterator<Arc> iter = pn.getArcs().iterator();
		while (iter.hasNext()) {
			Arc arc = iter.next();
			if (arc.getSource().equals(t) || arc.getTarget().equals(t)) {
				iter.remove();
			}
		}
		pn.getNodes().remove(t);
	}

	private static boolean condition(Set<PetriNet> graphs, List<Node> Pc,
			List<Node> Ps) {
		for (PetriNet g : graphs) {
			for (Node n : g.getNodes()) {
				if (Pc.contains(n) && !Ps.contains(n))
					return true;
			}
		}
		return false;
	}

	private static PetriNet replacePlacePath(PetriNet g, List<Node> placePath,
			Node pl, Node pie) {

		PetriNet clone = null;
		try {
			clone = g.clone();
			int position = g.getNodes().indexOf(pl);
			Node temp = pl;
			Node successor = null;
			for (Node node : temp.getSuccessors())
				if (g.getNodes().contains(node))
					successor = node;
			while (successor != pie) {
				Arc arc = new Arc(temp, successor);
				clone.deleteNode(successor);
				clone.deleteArc(arc);
				temp = successor;
				for (Node node : temp.getSuccessors())
					if (g.getNodes().contains(node))
						successor = node;
			}
			clone.deleteArc(new Arc(temp, successor));
			for (int i = 0; i < placePath.size() - 1; i++) {
				Node source = placePath.get(i);
				Node target = placePath.get(i + 1);
				Arc arc = new Arc(source, target);
				clone.addArc(arc);
				// Arc arc = new Arc(source.getID(),target.getID());
				// clone.addArc(source.getID(),target.getID());
				clone.getNodes().add(++position, target);
			}
			clone.getNodes().remove(position);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return clone;
	}

	private static PetriNet mergePath(PetriNet g, List<Node> path, Node pl,
			Node pie) {
		PetriNet clone = null;
		try {
			clone = g.clone();
			for (int i = 0; i < path.size() - 1; i++) {
				Node source = path.get(i);
				Node target = path.get(i + 1);
				// Node source = clone.getNodeById(path.get(i).getID());
				// Node target = clone.getNodeById(path.get(i+1).getID());
				Arc arc = new Arc(source, target);
				if (!clone.getArcs().contains(arc))
					clone.addArc(arc);
				if (!clone.getNodes().contains(target))
					clone.addNode(target);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return clone;
	}

}
