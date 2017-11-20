package org.nust.heroine.recovery;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.nust.heroine.basicstruct.Node;
import org.nust.heroine.basicstruct.PetriNet;
import org.nust.heroine.basicstruct.Trace;
import org.nust.heroine.util.StringDistance;

public class Recovery {
	private PetriNet petriNet;
	private List<PetriNet> petriNets;
	private Map<Double, List<PetriNet>> mlistPetriNet;

	ArrayList<Trace> subResulTraces;

	// public static PetriNet petriNet = new PetriNet();
	// public static ArrayList<PetriNet> petriNets = new
	// ArrayList<PetriNet>();
	public Recovery(PetriNet petriNet, List<PetriNet> petriNets) {
		this.petriNets = petriNets;
		this.petriNet = petriNet;
	}

	public Node enabled = null;
	public HashMap<String, Integer> quot;// 事件发生的实际次数
	public String boundary = null;// 分段事件

	public ArrayList<Trace> recovery(List<Trace> traceSet) {

		ArrayList<Trace> results = new ArrayList<Trace>();
		for (int i = 0; i < traceSet.size(); i++) {
			Trace trace = traceSet.get(i);
			trace = removeAllActivityNotInPN(trace);// 存放修复之后的trace
			Trace result = new Trace();

			// 根据启发式规则1求参考子路径
			mlistPetriNet = new HashMap<Double, List<PetriNet>>();
			double value = 0;
			for (PetriNet pn : petriNets) {
				double temp = criterion1(trace, pn);
				if (mlistPetriNet.containsKey(temp)) {
					mlistPetriNet.get(temp).add(pn);
				} else {
					List<PetriNet> pNets = new ArrayList<PetriNet>();
					pNets.add(pn);
					mlistPetriNet.put(temp, pNets);
				}
				if (temp > value) {
					value = temp;
				}
			}

			subResulTraces = new ArrayList<Trace>();
			for (PetriNet pNet : mlistPetriNet.get(value)) {
				if (pNet.hasCycle) {
					int dominator = domi(trace.getEventFrequency(), pNet, 0);
					if (dominator == 0)
						dominator = 1;
					// System.out.println("dominator："+dominator);
					quot = new HashMap<String, Integer>();
					initQuot(dominator, pNet);
					if (dominator > 1) {
						List<Node> nodes = pNet.f1.get(0);
						for (Node node : nodes) {
							if (quot.get(node.getID()) == dominator)
								boundary = node.getID();
						}
					} else {
						boundary = null;
					}
					pNet.initToken();
					result = sr_plus(trace, pNet);
				} else {
					pNet.initToken();
					result = sr(trace, pNet);
				}
				subResulTraces.add(result);
			}

			// 获取修复trace中距离错误日志编辑距离最短的修复trace
			if (subResulTraces.size() == 1) {
				results.add(subResulTraces.get(0));
			} else {
				int minEditDistance = StringDistance.editDistance(
						subResulTraces.get(0).getEvents(), trace.getEvents());
				Trace minTrace = subResulTraces.get(0);
				for (int j = 1; j < subResulTraces.size(); j++) {
					int compareEditDistance = StringDistance.editDistance(
							subResulTraces.get(j).getEvents(),
							trace.getEvents());
					if (compareEditDistance < minEditDistance) {
						minEditDistance = compareEditDistance;
						minTrace = subResulTraces.get(j);
					}
				}
				results.add(minTrace);
			}
		}
		return results;

	}

	/**
	 * 基本因果网情形下的修复与带选择的情形下的修复，综合轨迹重放方法获得参考轨迹
	 * 
	 * @param trace
	 *            错误日志
	 * @param pn
	 *            参考的Petri网模型
	 * @return
	 */
	public Trace sr(Trace trace, PetriNet pn) {
		Trace rTrace = new Trace();
		int length = trace.getEvents().size();
		int i = 0;
		// 重发参数设置及初始化
		HashMap<String, Boolean> replayed = new HashMap<String, Boolean>();
		for (i = 0; i < pn.getTransitons().size(); i++) {
			replayed.put(pn.getTransitons().get(i).getID(), false);
		}
		i = 0;

		while (pn.getSinkPlace().gettoken() <= 0) {
			if (i < trace.getEvents().size()) {
				String s = trace.getEvents().get(i);
				Node node = pn.getTransMap().get(s);
				if (pn.getTransMap().get(s) == null) {// 冗余，该事件在模型中不存在
					i++;
					continue;
				}
				if (replayed.get(s)) {// 冗余，该事件已经重放
					i++;
					continue;
				}
				if (pn.enabled(node)) {// 正常情况
					enabled = node;
					rTrace.getEvents().add(enabled.getID());
					i++;
				} else if (enableTransition(trace, pn, i, length) < 0) {// 缺失
					rTrace.getEvents().add(enabled.getID());
				} else if (enableTransition(trace, pn, i, length) > 0) {// 乱序
					rTrace.getEvents().add(enabled.getID());
				}
				replayed.put(enabled.getID(), true);
				pn.fire(enabled);
			} else {// 将最后不在trace中的事件添加到参考路径中
				for (Node temp : pn.getTransitons()) {
					if (pn.getSinkPlace().gettoken() >= 1)
						break;
					if (pn.enabled(temp)) {
						pn.fire(temp);
						rTrace.getEvents().add(temp.getID());
					}
				}
			}
		}
		return rTrace;
	}

	/**
	 * 带循环的情形下的修复，综合轨迹重放方法获得参考轨迹
	 * 
	 * @param trace
	 *            错误日志
	 * @param pn
	 *            参考的Petri网模型
	 * @return
	 */
	public Trace sr_plus(Trace trace, PetriNet pn) {
		HashMap<String, Integer> ft = new HashMap<String, Integer>();// 记录已修复部分的trace中事件出现的次数
		Trace rTrace = new Trace();// 修复trace
		int i = 0;
		int k = trace.getEvents().size();
		for (i = 0; i < pn.getTransitons().size(); i++) {
			ft.put(pn.getTransitons().get(i).getID(), 0);
		}
		/**
		 * 修改： 修改人：常震 修改时间：2017.11.14
		 */
		// ///////////////////////////////////
		HashMap<String, Integer> exists = new HashMap<String, Integer>();// 记录trace中各个事件出现的次数
		List<String> traceEvents = trace.getEvents();
		for (String event : traceEvents) {
			if (!exists.containsKey(event))
				exists.put(event, 1);
			else
				exists.put(event, exists.get(event) + 1);
		}
		List<Integer> segments = new ArrayList<Integer>();// 记录trace分段信息
		for (i = 0; i < traceEvents.size(); i++) {
			if (traceEvents.get(i).equals(boundary))
				segments.add(i);
		}
		segments.add(traceEvents.size() - 1);
		// ///////////////////////////////////
		i = 0;
		String pre = boundary;
		while (pn.getSinkPlace().gettoken() <= 0) {
			if (i < trace.getEvents().size()) {
				String s = trace.getEvents().get(i);
				if (pn.isHasIntersection) {
					if (pre == boundary) {
						HashMap<String, Integer> temp = new HashMap<String, Integer>();
						for (int j = i; j < trace.getEvents().size(); j++) {
							String s1 = trace.getEvents().get(j);
							Integer integer = temp.get(s1);
							if (integer == null)
								temp.put(s1, 1);
							else {
								temp.put(s1, integer + 1);
							}
							if (s1.equalsIgnoreCase(boundary)) {
								k = j;
								// System.out.println("next boundary index:"+k);
								break;
							}

						}
						int inner = domi(temp, pn, 1);
						// System.out.println("inner:"+inner);

						if (inner > 1) {
							for (Node node : pn.f1.get(1))
								quot.put(node.getID(), inner);
							for (Node node : pn.f2.get(1))
								quot.put(node.getID(), inner - 1);
						}
						// if(inner ==0){
						// for(Node node:petriNet.f1.get(0))
						// quot.put(node.getID(), 1);
						// for(Node node:petriNet.f2.get(0))
						// quot.put(node.getID(), 0);
						// System.out.println("quot"+quot);
						// }
					}
				}

				Node node = pn.getTransMap().get(s);

				/**
				 * 修改： 修改人：常震 修改时间：2017.11.14
				 */
				// ///////////////////////////////////
				if (exists.get(s) > quot.get(s)) {
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
						sublist = traceEvents.subList(
								segments.get(current - 1), i);
					}
					int before = sublist.lastIndexOf(s);
					if (before != -1) {
						i++;
						exists.put(s, exists.get(s) - 1);
						continue;
					}
				}
				// ///////////////////////////////////
				// if (pre != null && pre.equals(s)) {
				// i++;
				// continue;
				// }
				// ///////////////////////////////////
				if (pn.getTransMap().get(s) == null) {
					i++;
					continue;
				}
				if (ft.get(s) >= quot.get(s)) {
					i++;
					continue;
				}
				int boe;
				// int position;
				if (pn.enabled(node) && ft.get(s) < quot.get(s)) {
					if ((boe = backorexit(pn)) >= 0) {
						Node tb = pn.backs.get(boe);
						if (ft.get(tb.getID()) < quot.get(tb.getID())) {
							enabled = tb;
							if (node.getID().equalsIgnoreCase(tb.getID()))
								i++;
						} else {
							enabled = node;
							i = i + 1;
						}
					} else {
						enabled = node;
						i++;
					}
				} else if (enableTransition(trace, pn, i, k) < 0) {
					if ((boe = backorexit(pn)) >= 0) {
						Node tb = pn.backs.get(boe);
						if (ft.get(tb.getID()) < quot.get(tb.getID())) {
							enabled = tb;
						} else {
							enabled = pn.getTransMap().get(
									pn.exits.get(boe).getID());
						}
					}
				} else if (enableTransition(trace, pn, i, k) > 0) {
					if ((boe = backorexit(pn)) >= 0) {
						Node tb = pn.backs.get(boe);
						if (ft.get(tb.getID()) < quot.get(tb.getID())) {
							enabled = tb;
						} else {
							enabled = pn.getTransMap().get(
									pn.exits.get(boe).getID());
						}
					}
				}
				ft.put(enabled.getID(), ft.get(enabled.getID()) + 1);
				rTrace.getEvents().add(enabled.getID());
				pn.fire(enabled);
				pre = enabled.getID();
			} else {
				for (Node transition : pn.getTransitons()) {
					if (pn.getSinkPlace().gettoken() >= 1)
						break;
					if (pn.enabled(transition)
							&& !pn.backs.contains(transition)) {
						pn.fire(transition);
						rTrace.getEvents().add(transition.getID());
					}
				}
			}

		}
		return rTrace;
	}

	/**
	 * 去掉不在Petri网中的变迁
	 * 
	 * @param trace
	 *            要处理的trace
	 * @return 处理之后的trace
	 */
	public Trace removeAllActivityNotInPN(Trace trace) {
		Iterator<String> itr = trace.getEvents().iterator();
		while (itr.hasNext()) {
			String temp = itr.next();
			if (petriNet.getTransMap().get(temp) == null) {
				itr.remove();
			}
		}
		return trace;
	}

	/**
	 * 获得当前可以发生的便签
	 * 
	 * @param trace
	 * @param pn
	 * @param from
	 * @param to
	 * @return -1表示缺失，大于0表示乱序
	 */
	public int enableTransition(Trace trace, PetriNet pn, int from, int to) {
		ArrayList<Node> enabledTransitions = new ArrayList<Node>();
		// if(pn.getSinkPlace().gettoken()>=1)
		// return -1;
		List<String> subTrace = trace.getEvents().subList(from, to);
		for (Node temp : pn.getTransitons()) {
			if (pn.enabled(temp)) {
				if (!subTrace.contains(temp.getID())) {
					enabled = temp;
					return -1;
				} else {
					enabledTransitions.add(temp);
				}
			}
		}
		int index = Integer.MAX_VALUE;
		int num;
		for (Node temp : enabledTransitions) {
			if ((num = subTrace.indexOf(temp.getID())) < index) {
				index = num;
				enabled = temp;
			}
		}
		return index;
	}

	/**
	 * 启发式规则1求标准
	 * 
	 * @param trace
	 * @param pn
	 * @return
	 */
	public double criterion1(Trace trace, PetriNet pn) {
		int numerator = 0;// 分子
		int denominator = 0;// 分母
		HashMap<String, Boolean> visited = new HashMap<String, Boolean>();
		for (String s : trace.getEvents())
			visited.put(s, false);
		for (String s : trace.getEvents()) {
			if (visited.get(s) == false) {
				if (pn.getTransMap().get(s) != null) {
					numerator++;
				}
				denominator++;
				visited.put(s, true);
			}
		}
		for (String s : pn.getTransMap().keySet()) {
			if (visited.get(s) == null)
				denominator++;
			else if (visited.get(s) == false)
				denominator++;
		}
		// System.out.println("result:     "+(numerator*1.0/denominator));
		return numerator * 1.0 / denominator;
	}

	/**
	 * 求循环次数
	 * 
	 * @param ef
	 *            待修复trace中事件及其对应的次数
	 * @param pn
	 *            子过程对应的Petri网
	 * @param level
	 * @return
	 */
	public int domi(HashMap<String, Integer> ef, PetriNet pn, int level) {
		// System.out.println("test");
		// System.out.println("ef"+ef);
		// System.out.println("---------");
		int numCounter[] = new int[40];
		Integer num = null;
		int times = 0;
		int dominate = 0;
		if (level == 0) {
			if (pn.isHasIntersection) {
				for (Node node : pn.f1.get(0)) {
					if ((num = ef.get(node.getID())) != null
							&& pn.f1.get(1).contains(
									pn.getTransMap().get(node.getID())) == false) {
						numCounter[num]++;
					}
				}
			} else {
				for (Node node : pn.f1.get(0)) {
					if ((num = ef.get(node.getID())) != null) {
						numCounter[num]++;
						// }else{
						// numCounter[0]++;
					}
				}
			}
			for (Node node : pn.f2.get(0)) {
				if ((num = ef.get(node.getID())) != null) {
					numCounter[num + 1]++;
					// }else{
					// numCounter[1]++;
				}
			}

		} else if (level == 1) {
			for (Node node : pn.f1.get(1)) {
				if ((num = ef.get(node.getID())) != null) {
					numCounter[num]++;
				}
			}
			for (Node node : pn.f2.get(1)) {
				if ((num = ef.get(node.getID())) != null) {
					numCounter[num + 1]++;
				}
			}
		}

		for (int i = 0; i < 30; i++) {
			if (times < numCounter[i]) {
				times = numCounter[i];
				dominate = i;
			}
		}
		return dominate;
	}

	/**
	 * 初始化每个节点应该正确发生的次数
	 * 
	 * @param domi
	 * @param pn
	 */
	public void initQuot(int domi, PetriNet pn) {
		for (Node node : pn.getTransitons())
			quot.put(node.getID(), 1);
		for (Node node : pn.f1.get(0))
			// if(!pn.f1.get(1).contains(node))
			quot.put(node.getID(), domi);
		for (Node node : pn.f2.get(0))
			quot.put(node.getID(), domi - 1);
		if (pn.isHasIntersection) {
			for (Node node : pn.f2.get(1))
				quot.put(node.getID(), 0);
		}
	}

	public static int backorexit(PetriNet pn) {
		for (int i = 0; i < pn.backs.size(); i++)
			if (pn.enabled(pn.backs.get(i)) && pn.enabled(pn.exits.get(i)))
				return i;
		return -1;

	}
}
