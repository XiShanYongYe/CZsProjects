package org.nust.wsong.util;

import java.awt.print.Paper;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections4.CollectionUtils;
import org.nust.wsong.constant.Constant;
import org.nust.wsong.model.Loop;
import org.nust.wsong.model.Trace;

/**
 * 
 * @author xxx
 * @see Paper Wsong et al: Sheel:Self-healing Event logs
 * 
 */
public class TraceUtils {

	public static Map<String, Object> traceUtilMap = new HashMap<>();

	/**
	 * Algorithm1 Compliant trace clustering
	 * 
	 * @param traces
	 * @return
	 */
	public static List<List<Trace>> traceCluster(List<Trace> traces) {
		boolean[] delState = new boolean[traces.size()];
		List<List<Trace>> clusters = new ArrayList<>();
		for (int i = 0; i < traces.size(); i++) {
			Trace trace1 = traces.get(i);
			if (delState[i])
				continue;
			List<Trace> cluster = new ArrayList<>();
			if (!cluster.contains(trace1))
				cluster.add(trace1);
			delState[i] = true;
			for (int j = 0; j < traces.size(); j++) {
				if (delState[j])
					continue;
				Trace trace2 = traces.get(j);
				if (trace1.set().containsAll(trace2.set())
						|| trace2.set().containsAll(trace1.set())) {
					if (!cluster.contains(trace2))
						cluster.add(trace2);
					delState[j] = true;
				}
			}

			// 将cluster中的trace按从长到到短排序
			Collections.sort(cluster, new Comparator<Trace>() {// 匿名内部类
						@Override
						public int compare(Trace o1, Trace o2) {
							if (o1.set().size() > o2.set().size())
								return -1;
							else
								return 0;
						}
					});

			clusters.add(cluster);
		}
		return clusters;
	}

	/**
	 * Algorithm 4 分段 老师的算法 自己实现
	 * 
	 * @param traces
	 */
	public static void segmentation(List<Trace> traces) {
		List<Trace> Ss = new ArrayList<>();
		Set<Loop> loops = new HashSet<>();

		for (Trace t : traces) {
			List<Trace> s = new ArrayList<>();

			Map<String, Integer> count = new HashMap<>();
			Trace seg = new Trace();
			for (int i = 0; i < t.size(); i++) {
				String s1 = t.get(i);
				if (count.containsKey(s1)) {
					count.clear();
					s.add(seg);
					seg = new Trace();
				}
				count.put(s1, 1);
				seg.append(s1);
			}
			s.add(seg);
			// System.out.println("s:"+s);
			// System.out.println("测试得到循环的时间-----------");
			// long start = System.currentTimeMillis();
			for (int i = 1; i < s.size() - 1; i++) {
				Trace ts = s.get(i);
				Trace ts1 = s.get(i - 1);
				Trace ts11 = s.get(i + 1);
				if (CollectionUtils.isEqualCollection(
						ts.getEvents(),
						CollectionUtils.intersection(ts.getEvents(),
								ts1.getEvents()))// CollectionUtils.isEqualCollection(ts.getEvents(),
													// CollectionUtils.intersection(ts.getEvents(),
													// ts1.getEvents()))
						&& CollectionUtils.intersection(ts.getEvents(),
								ts11.getEvents()).size() > 0
						&& CollectionUtils.containsAll(ts.getEvents(),
								CollectionUtils.intersection(ts.getEvents(),
										ts11.getEvents()))) {
					Set<String> tn1 = new HashSet<>();
					tn1.addAll(CollectionUtils.intersection(ts.getEvents(),
							ts11.getEvents()));
					Set<String> tn2 = new HashSet<>();
					tn2.addAll(CollectionUtils.subtract(ts.getEvents(),
							ts11.getEvents()));
					// System.out.println("tn1" + tn1);
					// System.out.println("tn2" + tn2);

					if (tn1.isEmpty() || tn2.isEmpty())
						continue;
					Loop loop = new Loop();

					loop.setN1(tn1);
					loop.setN2(tn2);
					loops.add(loop);
				}
			}
			// System.out.println("TraceUtil: "+loops);
			// long end = System.currentTimeMillis();
			// System.out.println("测试得到循环所用的时间："+(end-start));
			for (Trace temp : s)
				if (!Ss.contains(temp))// 去重
					Ss.add(temp);//
		}

		traceUtilMap.put(Constant.Sheel_Ss, Ss);
		traceUtilMap.put(Constant.Sheel_St1, loops);
		/*
		 * System.out.println("Ss"+Ss); System.out.println("loops"+loops);
		 */
		Ss = null;
		loops = null;
	}

	/**
	 * 算法5 自己的想法
	 * 
	 * @param map
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static List<Set<Trace>> segmentCluster() {
		List<Trace> traces = (List<Trace>) traceUtilMap.get(Constant.Sheel_Ss);
		List<Loop> loops = new ArrayList<>();
		CollectionUtils.addAll(loops,
				(Set<Loop>) traceUtilMap.get(Constant.Sheel_St1));
		final Trace maxEleTrace = (Trace) traceUtilMap
				.get(Constant.Sheel_maxEleTrace);
		List<Set<Trace>> clusters = new ArrayList<>();

		// 测试
		// 保存嵌套结构的N2对应关系
		Map<Set<String>, Set<String>> nested = new HashMap<>();

		// 测试
		List<Set<String>> preCluster = new LinkedList<>();
		// Set<Trace> temp = new HashSet<>();
		// Trace temp1 = new Trace();
		if (loops.size() == 1) {
			/*
			 * temp = new HashSet<>(); temp1 = new Trace();
			 * CollectionUtils.addAll(temp1.getEvents(), loops.get(0).getN1());
			 * temp.add(temp1); clusters.add(temp);
			 */
			preCluster.add(loops.get(0).getN1());
			preCluster.add(loops.get(0).getN2());

			/*
			 * temp = new HashSet<>(); temp1 = new Trace();
			 * CollectionUtils.addAll(temp1.getEvents(), loops.get(0).getN2());
			 * temp.add(temp1); clusters.add(temp);
			 */
		} else {
			// 解决嵌套循环
			boolean[] delState = new boolean[loops.size()];
			for (int i = 0; i < loops.size(); i++) {
				if (delState[i])
					continue;
				Loop loopi = loops.get(i);
				delState[i] = true;
				for (int j = i + 1; j < loops.size(); j++) {
					if (delState[j])
						continue;
					Loop loopj = loops.get(j);
					delState[j] = true;
					if (loopi.getN1().size() < loopj.getN1().size()) {
						Loop tempLoop = loopi;
						loopi = loopj;
						loopj = tempLoop;
					}
					if (CollectionUtils.containsAll(loopi.getN1(),
							loopj.getN1())) {

						// 测试
						// 保存嵌套结构的N2对应关系
						nested.put(loopi.getN2(), loopj.getN2());

						// 测试
						Iterator<String> iter = loopi.getN1().iterator();
						boolean flag = true;
						Set<String> part1 = new HashSet<>();
						Set<String> part2 = new HashSet<>();
						while (iter.hasNext()) {
							String s = iter.next();
							if (loopj.getN1().contains(s)) {
								flag = false;
								continue;
							}
							if (flag) {
								part1.add(s);

							} else {
								part2.add(s);
							}
						}
						if (!part1.isEmpty())
							preCluster.add(part1);
						if (!part2.isEmpty())
							preCluster.add(part2);

					} else {
						preCluster.add(loopi.getN1());
					}
					preCluster.add(loopi.getN2());
					preCluster.add(loopj.getN1());
					preCluster.add(loopj.getN2());
				}
			}
		}

		// System.out.println("test preCluster" + preCluster);
		// System.out.println("TraceUtil--"+nested);
		for (Set<String> set : preCluster) {
			Set<Trace> projection = projection(maxEleTrace, set);
			// Set<Trace> cluster = new HashSet<>();
			// if(preCluster.size()!=0)
			// cluster.add(projection);

			clusters.add(projection);
		}
		// System.out.println("test clusters" + clusters);

		for (Trace t : traces) {
			for (Set<Trace> c : clusters) {
				if (CollectionUtils.containsAll(t.getEvents(), c.iterator()
						.next().getEvents())) {
					Set<Trace> projection = projection(t, c.iterator().next()
							.getEvents());
					c.addAll(projection);
				}
			}

			List<Trace> projection = projectionExcept(t, clusters);
			for (Trace trace : projection) {
				if (trace.size() != 0) {
					Set<Trace> c = new HashSet<>();
					c.add(trace);
					clusters.add(c);
				}
			}
		}

		// System.out.println("clusters size" + clusters.size());
		// System.out.println("clusters before sort: " + clusters);
		// System.out.println("测试" + traceUtilMap);
		// System.out.println("测试 nested" + nested);
		traceUtilMap.put(Constant.Sheel_nested, nested);

		// 对clusters进行排序
		segmentClusterSort(clusters, maxEleTrace);

		// System.out.println("clusters after sort: " + clusters);
		return clusters;
	}

	public static void segmentClusterSort(List<Set<Trace>> clusters,
			final Trace maxEleTrace) {
		Collections.sort(clusters, new Comparator<Set<Trace>>() {

			@Override
			public int compare(Set<Trace> c1, Set<Trace> c2) {
				String ele1 = c1.iterator().next().getEvents().get(0);
				String ele2 = c2.iterator().next().getEvents().get(0);
				int index1 = maxEleTrace.getEvents().indexOf(ele1);
				int index2 = maxEleTrace.getEvents().indexOf(ele2);
				if (index1 == index2)
					return 0;
				if (index1 > index2)
					return 1;
				else
					return -1;
			}
		});
	}

	/**
	 * 计算事件序列s1和s2的距离
	 * 
	 * @param compliant
	 *            trace t1
	 * @param deviated
	 *            trace t2
	 * @return
	 */
	public static int editDistance(Trace t1, Trace t2) {
		int distance = 0;
		List<String> tempList = new ArrayList<>();
		tempList.addAll(t2.getEvents());

		for (int i = 0; i < t1.getEvents().size(); i++) {

			if (tempList.size() == 0) {
				distance += t1.getEvents().size() - i;
				return distance;
			}

			String s = t1.getEvents().get(i);
			// 相同
			if (tempList.get(0).equals(s)) {
				tempList.remove(0);
				continue;
			}
			// 冗余
			if (!t1.getEvents().subList(i, t1.getEvents().size())
					.contains(tempList.get(0))) {
				i--; // 下次迭代还是在位置i上
				distance++;
				tempList.remove(0);
				continue;
			}

			// 交换
			int temp = tempList.indexOf(s);
			if (temp != -1) {
				distance += temp;
				// String tempStr = s;
				tempList.set(temp, tempList.get(0));
				// tempList.set(0, s);
				tempList.remove(0);
			} else {// 缺失
				distance++;
			}
			if (i == t1.getEvents().size() - 1) {
				distance += tempList.size();
			}
		}

		distance += tempList.size();
		return distance;
	}

	/**
	 * project c1 on c2 求集合1在集合2上的投影
	 * 
	 * @param c1
	 * @param c2
	 * @return
	 */
	public static Collection<String> projection(Collection<String> c1,
			Collection<String> c2) {
		Set<String> set = new HashSet<>();
		for (String s : c2) {
			set.add(s);

		}

		// CollectionUtils.addAll(set, c2);
		List<String> result = new ArrayList<>();
		for (String s : c1) {
			if (set.contains(s)) {
				result.add(s);
			}
		}
		return result;
	}

	/**
	 * project t on c2 求Trace t在集合c2上的投影
	 * 
	 * @param t
	 * @param c2
	 * @return
	 */

	public static Set<Trace> projection(Trace t, Collection<String> c2) {
		Set<Trace> newCluster = new HashSet<>();
		Trace newTrace = new Trace();
		List<String> projection = (List<String>) projection(t.getEvents(), c2);
		int length = c2.size();
		int count = 0;
		int i = 0;
		if (projection.size() < c2.size()) {
			newCluster.add(new Trace(projection));
			return newCluster;
		}

		while (i <= projection.size()) {
			if (count == length) {
				newCluster.add(newTrace);
				newTrace = new Trace();
				count = 0;
			}
			if (i == projection.size())
				break;
			count++;
			newTrace.append(projection.get(i));
			i++;
		}
		return newCluster;
	}

	/**
	 * project c1 on c2 求trace t在clusters上的投影
	 * 
	 * @param c1
	 * @param c2
	 * @return
	 */
	public static Set<Trace> projection(Trace t, Set<Trace> cluster) {

		Set<Trace> newCluster = new HashSet<>();
		List<String> projection = (List<String>) projection(t.getEvents(),
				cluster.iterator().next().getEvents());
		int length = cluster.iterator().next().getEvents().size();
		int count = 0;
		int i = 0;
		Trace newTrace = new Trace();
		while (i <= projection.size()) {
			if (count == length) {
				newCluster.add(newTrace);
				newTrace = new Trace();
				count = 0;
			}
			if (i == projection.size())
				break;
			count++;
			newTrace.append(projection.get(i));
			i++;
		}
		return newCluster;
	}

	/**
	 * project c1 on c2 求集合1在集合2上的投影
	 * 
	 * @param c1
	 * @param c2
	 * @return
	 */
	public static List<List<String>> projectionExcept(Collection<String> c1,
			Collection<String> c2) {
		List<List<String>> result = new LinkedList<>();
		List<String> temp = new LinkedList<>();
		for (String s : c1) {
			if (!c2.contains(s)) {
				temp.add(s);
			} else {
				if (temp.size() != 0)
					result.add(temp);
				temp = new LinkedList<>();
			}
		}
		if (temp.size() != 0)
			result.add(temp);
		return result;
	}

	/**
	 * project c1 on c2 求集合1在集合2上的投影
	 * 
	 * @param c1
	 * @param c2
	 * @return
	 */
	public static Set<Trace> projectionExcept(Trace t1, Trace t2) {
		Set<Trace> result = new HashSet<>();
		List<List<String>> projectionExcept = projectionExcept(t1.getEvents(),
				t2.getEvents());
		for (List<String> strList : projectionExcept) {
			Trace t = new Trace(strList);
			result.add(t);
		}
		return result;
	}

	/**
	 * project c1 on c2 求Trace t在集合2上的投影
	 * 
	 * @param t
	 * @param c2
	 * @return
	 */
	public static List<Trace> projectionExcept(Trace t, Collection<String> c2) {
		List<Trace> result = new LinkedList<>();
		List<List<String>> temp = projectionExcept(t.getEvents(), c2);
		for (List<String> list : temp) {
			Trace newTrace = new Trace();
			newTrace.setEvents(list);
			result.add(newTrace);
		}
		return result;
	}

	/**
	 * project c1 on c2 求trace t在clusters上的投影
	 * 
	 * @param c1
	 * @param c2
	 * @return
	 */
	public static List<Trace> projectionExcept(Trace t,
			List<Set<Trace>> clusters) {
		List<Trace> result = new LinkedList<>();
		List<String> list = new ArrayList<>();
		for (Set<Trace> traces : clusters) {
			Iterator<Trace> iter = traces.iterator();
			for (String s : iter.next().getEvents()) {
				list.add(s);
			}
		}
		result = projectionExcept(t, list);
		return result;
	}

	/**
	 * 根据trace中的event的频率 & N1比N2多发生一次的特点 以少数服从多数 推断出循环发生的频率
	 * 
	 * @return int max
	 */
	public static int mode(Trace t, Loop loop) {
		System.out
				.println("-----------------------------------------------------");
		System.out.println(t);
		// 这里确定了循环的次数---------
		Map<String, Integer> map1 = new HashMap<>();// 计算每个event的次数
		Map<Integer, Integer> map2 = new HashMap<>();// 根据event的次数计算mode

		for (String s : t.getEvents()) {
			if (!map1.containsKey(s))
				map1.put(s, 1);
			else
				map1.put(s, map1.get(s) + 1);
		}

		Set<String> tn1 = loop.getN1();
		Set<String> tn2 = loop.getN2();

		System.out.println("tn1 = " + tn1);
		System.out.println("tn2 = " + tn2);

		// map2.put(0, 0);
		// map2.put(1, 0);

		for (String s : tn1) {
			if (map1.containsKey(s)) {
				int value = map1.get(s);
				if (!map2.containsKey(value))
					map2.put(value, 1);
				else
					map2.put(value, map2.get(value) + 1);
				// }else{
				// if(map2.get(0)==null)
				// map2.put(0, 1);
				// else
				// map2.put(map2.get(0), map2.get(0)+1);
			}

		}

		for (String s : tn2) {
			if (map1.containsKey(s)) {
				int value = map1.get(s);
				if (!map2.containsKey(value + 1))
					map2.put(value + 1, 1);
				else
					map2.put(value + 1, map2.get(value + 1) + 1);
				// }else{
				// if(map2.get(0)==null)
				// map2.put(0, 1);
				// else
				// map2.put(map2.get(0), map2.get(0)+1);
			}
		}

		int keyMax = 0;
		int valueMax = 0;
		for (Integer i : map2.keySet()) {
			if (map2.get(i) > valueMax) {
				valueMax = map2.get(i);
				keyMax = i;
			}
		}

		map1 = null;
		map2 = null;
		// max = 3;
		/*
		 * System.out.println("循环的次数："+max); return max;
		 */
		System.out.println("循环的次数：" + keyMax);
		return keyMax;
	}

	/**
	 * 根据trace中的event的频率 & N1比N2多发生一次的特点 以少数服从多数 推断出循环发生的频率
	 * 
	 * @param t
	 *            待修复的序列
	 * @param loop1
	 *            外层循环
	 * @param loop2
	 *            内层循环
	 * @return int max
	 */
	public static Map<String, Object> mode(Trace t, Loop loop1, Loop loop2) {
		Set<String> tn11 = loop1.getN1();
		Set<String> tn12 = loop1.getN2();
		Set<String> tn21 = loop2.getN1();
		Set<String> tn22 = loop2.getN2();

		Map<String, Integer> map1 = new HashMap<>();// 计算每个event的次数
		Map<Integer, Integer> map2 = new HashMap<>();// 根据event的次数计算mode

		for (String s : t.getEvents()) {
			if (!map1.containsKey(s))
				map1.put(s, 1);
			else
				map1.put(s, map1.get(s) + 1);
		}

		// 有问题 待修改
		for (String key : map1.keySet()) {
			int value = map1.get(key);
			if (tn11.contains(key) && !tn21.contains(key)) {
				if (!map2.containsKey(value))
					map2.put(value, 1);
				else
					map2.put(value, map2.get(value) + 1);
			} else if (tn12.contains(key) && !tn22.contains(key)) {
				if (!map2.containsKey(value + 1))
					map2.put(value + 1, 1);
				else
					map2.put(value + 1, map2.get(value + 1) + 1);
			}
		}

		int max = 0;
		for (Integer i : map2.keySet()) {
			if (map2.get(i) > max)
				max = i;
		}

		Map<String, Object> resultMap = new HashMap<>();
		resultMap.put(Constant.Sheel_Mode, max);

		for (String s : t.getEvents()) {
			if ((tn11.contains(s) && !tn21.contains(s) && map1.get(s) == max)// bag1.getCount(s)
					|| (tn12.contains(s) && !tn22.contains(s) && map1.get(s) == max - 1)) {
				resultMap.put(Constant.Sheel_ModeElement, s);
				break;
			}
		}
		map1 = null;
		map2 = null;
		// System.out.println("mode"+max);
		return resultMap;
	}

	/**
	 * 测试是否有活动发生次数大于一次
	 * 
	 * @param t
	 * @return
	 */
	public static boolean detectOccurrences(Trace t) {
		Set<String> set = new HashSet<>();
		for (String s : t.getEvents()) {
			if (set.contains(s))
				return true;
			else
				set.add(s);
		}
		return false;
	}

	/**
	 * 按指定元素分割路径
	 * 
	 * @param t
	 * @param ele
	 * @return
	 */
	public static List<Trace> splitWithEle(Trace t, String ele) {
		List<Trace> result = new ArrayList<>();
		if (ele == null) {
			result.add(t);
			return result;
		}
		List<String> temp = new LinkedList<>();
		for (String s : t.getEvents()) {
			if (s.equals(ele)) {
				if (temp.size() != 0)
					result.add(new Trace(temp));
				temp = new LinkedList<>();
				temp.add(s);
			} else {
				temp.add(s);
			}
		}

		if (temp.size() != 0)
			result.add(new Trace(temp));

		List<String> s1 = result.get(0).getEvents();
		List<String> s2 = result.get(1).getEvents();

		for (String s : s2)
			s1.add(s);
		// CollectionUtils.addAll(s1, s2);
		result.set(0, new Trace(s1));
		result.remove(1);
		return result;
	}

}
