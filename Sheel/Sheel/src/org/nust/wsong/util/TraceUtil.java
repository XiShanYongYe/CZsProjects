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

import org.apache.commons.collections4.Bag;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.collections4.bag.HashBag;
import org.nust.wsong.algorithm.Algorithm1;
import org.nust.wsong.constant.Constant;
import org.nust.wsong.model.Loop;
import org.nust.wsong.model.Trace;

/**
 * 
 * @author xxx
 * @see Paper Wsong et al: Sheel:Self-healing Event logs
 * @since 2016-10-1
 */
public class TraceUtil {
	
	public static  Map<String, Object> traceUtilMap = new HashMap<>();
	
	/**
	 * Algorithm1
	 *  Compliant trace clustering
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
			if(!cluster.contains(trace1))
				cluster.add(trace1);
			//traces.remove(i);
			delState[i] = true;
			for (int j = 0; j < traces.size(); j++) {
				if (delState[j])
					continue;
				Trace trace2 = traces.get(j);
				if(CollectionUtils.containsAll(trace1.getEvents(), trace2.getEvents())
						||CollectionUtils.containsAll(trace2.getEvents(), trace1.getEvents())){
					if(!cluster.contains(trace2))
						cluster.add(trace2);
					//traces.remove(j);
					delState[j] = true;
				}
			}
			
			//将cluster中的trace按从长到到短排序
			Collections.sort(cluster, new Comparator<Trace>() {
				@Override
				public int compare(Trace o1, Trace o2) {
					if(o1.getEvents().size()==o2.getEvents().size())
						return 0;
					if(o1.getEvents().size()==o2.getEvents().size())
						return 1;
					return -1;
				}
			});
			
			clusters.add(cluster);
		}
		return clusters;
	}

	/**
	 * Algorithm 4
	 * 分段 老师的算法 自己实现
	 * @param traces
	 */
	public static void traceSegmentation(List<Trace> traces) {
		List<Trace> Ss = new ArrayList<>();
//		Ss.addAll(traces);
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
			//System.out.println("s:"+s);
			

			
//			System.out.println("测试得到循环的时间-----------");
//			long start = System.currentTimeMillis();
			for(int i=1;i<s.size()-1;i++){
				Trace ts = s.get(i);
				Trace ts1 = s.get(i-1);
				Trace ts11 = s.get(i+1);
				if(CollectionUtils.isEqualCollection(ts.getEvents(), CollectionUtils.intersection(ts.getEvents(), ts1.getEvents()))//CollectionUtils.isEqualCollection(ts.getEvents(), CollectionUtils.intersection(ts.getEvents(), ts1.getEvents()))
						&&CollectionUtils.intersection(ts.getEvents(), ts11.getEvents()).size()>0
						&&CollectionUtils.containsAll(ts.getEvents(), CollectionUtils.intersection(ts.getEvents(), ts11.getEvents()))){
					Set<String> tn1 = new HashSet<>();
					tn1.addAll( CollectionUtils.intersection(ts.getEvents(), ts11.getEvents()));
					Set<String> tn2 = new HashSet<>();
					tn2.addAll(CollectionUtils.subtract(ts.getEvents(), ts11.getEvents()));
					System.out.println("tn1"+tn1);
					System.out.println("tn2"+tn2);
					
					if(tn1.isEmpty()||tn2.isEmpty())
						continue;
					Loop loop = new Loop();
					
					loop.setN1(tn1);
					loop.setN2(tn2);
					loops.add(loop);
				}
				
				
			}
//			System.out.println("TraceUtil: "+loops);
			
/*			for (int i = 0; i < s.size(); i++) {
				for (int j = 0; j < s.size(); j++) {
					if (j == i)
						continue;
					for (int k = 0; k < s.size(); k++) {
						if (k == i || k == j)
							continue;
						Trace ts = s.get(i);
						Trace ts1 = s.get(j);
						Trace ts11 = s.get(k);
//						ts.getEvents().size()>ts1.getEvents().size()&& 测试
						if(CollectionUtils.isEqualCollection(ts.getEvents(), CollectionUtils.intersection(ts.getEvents(), ts1.getEvents()))//CollectionUtils.isEqualCollection(ts.getEvents(), CollectionUtils.intersection(ts.getEvents(), ts1.getEvents()))
								&&CollectionUtils.intersection(ts.getEvents(), ts11.getEvents()).size()>0
								&&CollectionUtils.containsAll(ts.getEvents(), CollectionUtils.intersection(ts.getEvents(), ts11.getEvents()))){
							Set<String> tn1 = new HashSet<>();
							tn1.addAll( CollectionUtils.intersection(ts.getEvents(), ts11.getEvents()));
							Set<String> tn2 = new HashSet<>();
							tn2.addAll(CollectionUtils.subtract(ts.getEvents(), ts11.getEvents()));
							System.out.println("tn1"+tn1);
							System.out.println("tn2"+tn2);
							Loop loop = new Loop();
							
							loop.setN1(tn1);
							loop.setN2(tn2);
							loops.add(loop);
						}
					}
				}
			}*/
//			long end = System.currentTimeMillis();
//			System.out.println("测试得到循环所用的时间："+(end-start));
			for(Trace temp:s)
				if(!Ss.contains(temp))//去重
					Ss.add(temp);//
		}
		
		traceUtilMap.put(Constant.Sheel_Ss, Ss);
		traceUtilMap.put(Constant.Sheel_St1, loops);
/*		System.out.println("Ss"+Ss);
		System.out.println("loops"+loops);*/
		Ss = null;
		loops = null;
	}
	
	
	/**
	 * 算法5  自己的想法
	 * @param map
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static List<Set<Trace>> segmentCluster() {
		List<Trace> traces = (List<Trace>) traceUtilMap.get(Constant.Sheel_Ss);
		List<Loop> loops = new ArrayList<>();
		CollectionUtils.addAll(loops, (Set<Loop>) traceUtilMap.get(Constant.Sheel_St1));
		final Trace maxEleTrace = (Trace) traceUtilMap.get(Constant.Sheel_maxEleTrace);
		List<Set<Trace>> clusters = new ArrayList<>();
		
		//测试
		//保存嵌套结构的N2对应关系
		Map<Set<String>,Set<String>> nested = new HashMap<>();
		
		
		//测试
		List<Set<String>> preCluster = new LinkedList<>();
//		Set<Trace> temp = new HashSet<>();
//		Trace temp1 = new Trace();
		if(loops.size()==1){
		/*	temp = new HashSet<>();
			temp1 = new Trace();
			CollectionUtils.addAll(temp1.getEvents(), loops.get(0).getN1());
			temp.add(temp1);
			clusters.add(temp);*/
			preCluster.add(loops.get(0).getN1());
			preCluster.add(loops.get(0).getN2());
			
	/*		temp = new HashSet<>();
			temp1 = new Trace();
			CollectionUtils.addAll(temp1.getEvents(), loops.get(0).getN2());
			temp.add(temp1);
			clusters.add(temp);*/
		}else{
			//解决嵌套循环
			boolean[] delState = new boolean[loops.size()];
			for(int i=0;i<loops.size();i++){
				if(delState[i])
					continue;
				Loop loopi = loops.get(i);
				delState[i]=true;
				for(int j=i+1;j<loops.size();j++){
					if(delState[j])
						continue;
					Loop loopj = loops.get(j);
					delState[j]=true;
					if(loopi.getN1().size()<loopj.getN1().size()){
						Loop tempLoop = loopi;
						loopi = loopj;
						loopj = tempLoop;
					}
					if(CollectionUtils.containsAll(loopi.getN1(), loopj.getN1())){
						
						//测试
						//保存嵌套结构的N2对应关系
						nested.put(loopi.getN2(), loopj.getN2());
						
						//测试
						Iterator<String> iter = loopi.getN1().iterator();
						boolean flag = true;
						Set<String> part1 = new HashSet<>();
						Set<String> part2 = new HashSet<>();
						while(iter.hasNext()){
							String s = iter.next();
							if(loopj.getN1().contains(s)){
								flag = false;
								continue;
							}
							if(flag){
								part1.add(s);
								
							}else{
								part2.add(s);
							}
						}
						if(!part1.isEmpty())
							preCluster.add(part1);
						if(!part2.isEmpty())
							preCluster.add(part2);

					}else{
						preCluster.add(loopi.getN1());
					}
					
					preCluster.add(loopi.getN2());
					preCluster.add(loopj.getN1());
					preCluster.add(loopj.getN2());
				}
			}
		}
		
		
//		System.out.println("TraceUtil--"+nested);
		for(Set<String> set:preCluster){
			Set<Trace> projection = projection(maxEleTrace, set);
			Set<Trace> cluster = new HashSet<>();
//			if(preCluster.size()!=0)
//				cluster.add(projection);
			clusters.add(projection);
			
		}
		
		
		
		for(Trace t:traces){
			for(Set<Trace> c:clusters){
				if(CollectionUtils.containsAll(t.getEvents(), c.iterator().next().getEvents())){
					Set<Trace> projection = projection(t, c.iterator().next().getEvents());
					c.addAll(projection);
				}
			}
			
			
			
			List<Trace> projection = projectionExcept(t, clusters);
			for(Trace trace:projection){
				if(trace.size()!=0){
					Set<Trace> c = new HashSet<>();
					c.add(trace);
					clusters.add(c);
				}
			}
		}
		
		System.out.println("clusters size"+clusters.size());
		System.out.println("clusters before sort: "+clusters);
		System.out.println("测试"+traceUtilMap);
		System.out.println("测试 nested"+nested);
		traceUtilMap.put(Constant.Sheel_nested, nested);
		
		
		//对clusters进行排序
		segmentClusterSort(clusters, maxEleTrace);
		
		
		System.out.println("clusters after sort: "+clusters);
		return clusters;
	}
	
	
	
	/**
	 * 分段 自己的算法实现
	 * 
	 * @param traces
	 */
	public static void  traceSegmentation2(List<Trace> traces) {
		Trace maxEleTrace = (Trace) traceUtilMap.get(Constant.Sheel_maxEleTrace);
		List<Trace> Ss = new ArrayList<>();
		Ss.addAll(traces);
		Set<Loop> loops = new HashSet<>();
		
		int max = Integer.MIN_VALUE;
		//根据活动数最多的trace求出N0,N1,N2,N3
		boolean[] visited = new boolean[maxEleTrace.getEvents().size()];
		max = Integer.MIN_VALUE;
		Map<String, Integer> frequency = new HashMap<>(); // 计算每个event的频数
		for (String s : maxEleTrace.getEvents()) {
			if (frequency.containsKey(s)) {
				frequency.put(s, frequency.get(s) + 1);
				if (frequency.get(s) > max)
					max = frequency.get(s);
			} else
				frequency.put(s, 1);
		}

		/*	List<String> N0 = new ArrayList<>();
			List<String> N3 = new ArrayList<>();*/
//			List<Loop> loops = new ArrayList<>();
			
			/*List<Integer> indexs = new ArrayList<>();
			boolean flag = true;
			for (int i = 0; i < maxEleTrace.getEvents().size(); i++) { // 获得N0和N3
				String str = maxEleTrace.getEvents().get(i);
				if (flag) {
					if (frequency.get(str) == 1) {
						N0.add(str);
						visited[i] = true;
					} else
						flag = false;
				} else {
					if (frequency.get(str) == 1) {
						indexs.add(i);
						//N3.add(str);
						//visited[i] = true;
					} else
						indexs.clear();
				}

			}
			
			for(Integer i:indexs){
				visited[i]=true;
				N3.add(maxEleTrace.getEvents().get(i));
			}*/
			
			while (max > 1) {
				Loop loop = new Loop();
				Set<String> N1 = loop.getN1();
				Set<String> N2 = loop.getN2();
				int pre = max;
				boolean flag1 = true;
				boolean flag2 = true; // 获得N1和N2
				for (int i = 0; i < maxEleTrace.getEvents().size(); i++) {
					String str = maxEleTrace.getEvents().get(i);
					if (!visited[i]) {
						if (flag1) {
							if (flag2 && frequency.get(str) < pre) {
								continue;
							}
							if (frequency.get(str) == pre) {
								N1.add(str);
								visited[i] = true;
								flag2 = false;
							} else {
								pre = frequency.get(str);
								N2.add(str);
								visited[i] = true;
								flag1 = false;
							}
						} else {
							if (frequency.get(str) == pre) {
								N2.add(str);
								visited[i] = true;
							} else {
								//把N1,N2加入集合
								for(String s:N1)
									frequency.put(s, frequency.get(s)-1);
								for(String s:N2)
									frequency.put(s, frequency.get(s)-1);
								break;
							}
						}

					}

				}
				/*System.out.println("N1 "+N1);
				System.out.println("N2 "+N2);*/
				if(!loops.contains(loop))
					loops.add(loop);
				
				max = 0;
				for(Integer i:frequency.values()){
					if(i>max)
						max = i;
				}

			}
			
//			System.out.println("TraceUtil--"+"N0 "+N0);
//			System.out.println("TraceUtil--"+"N3 "+N3);
//			System.out.println("TraceUtil--"+"loops"+loops);
			
			//N2 reduction 交叉时会产生问题  做下redunction  合并N2相同 N1是子集的
			/*List<Set<Trace>> clusters = new ArrayList<>();
			boolean[] delState = new boolean[loops.size()];
			for(int i=0;i<loops.size();i++){
				if(delState[i])
					continue;
				Loop loopi = loops.get(i);
				delState[i]=true;
				for(int j=i+1;j<loops.size();j++){
					if(delState[j])
						continue;
					Loop loopj = loops.get(j);
					delState[j]=true;
					
					Set<Trace> temp3 = new HashSet<>();
					Trace temp1 = new Trace();
					
					
					if(loopi.getN1().size()<loopj.getN1().size()){
						Loop tempLoop = loopi;
						loopi = loopj;
						loopj = tempLoop;
					}
					if(CollectionUtils.containsAll(loopi.getN1(), loopj.getN1())){
						loopsMap.put(loopi, loopj);
						
						Iterator<String> iter = loopi.getN1().iterator();
						boolean flag3 = true;
						Trace part1 = new Trace();
						Trace part2 = new Trace();
						while(iter.hasNext()){
							String s = iter.next();
							if(loopj.getN1().contains(s)){
								flag3 = false;
								continue;
							}
							if(flag3){
								part1.append(s);
								
							}else{
								part2.append(s);
							}
						}
					
						temp3 = new HashSet<>();
						temp3.add(part1);
						clusters.add(temp3);
						
						temp3 = new HashSet<>();
						temp3.add(part2);
						clusters.add(temp3);

					}else{
						loopsMap.put(loopi, null);
						temp = new HashSet<>();
						temp1 = new Trace();
						CollectionUtils.addAll(temp1.getEvents(), loopi.getN1());
						temp3.add(temp1);
						clusters.add(temp3);
						
					}
					temp3 = new HashSet<>();
					temp1 = new Trace();
					CollectionUtils.addAll(temp1.getEvents(), loopj.getN1());
					temp3.add(temp1);
					clusters.add(temp3);
						
					temp3 = new HashSet<>();
					temp1 = new Trace();
					CollectionUtils.addAll(temp1.getEvents(), loopi.getN2());
					temp3.add(temp1);
					clusters.add(temp3);
					
					temp3 = new HashSet<>();
					temp1 = new Trace();
					CollectionUtils.addAll(temp1.getEvents(), loopj.getN2());
					temp3.add(temp1);
					clusters.add(temp3);
				}
			}
			
			
			
			
			
			Set<Trace> cluster = new HashSet<>();
			cluster.add(new Trace(N0));
			clusters.add(cluster);
			
			cluster = new HashSet<>();
			cluster.add(new Trace(N3));
			clusters.add(cluster);
			
			
			for(Trace t:traces){
				for(Set<Trace> c:clusters){
					if(CollectionUtils.containsAll(t.getEvents(), c.iterator().next().getEvents())){
						Set<Trace> projection = projection(t, c);
						c.addAll(projection);
					}
				}
			}
			
			System.out.println("TraceUtil-- clusters size"+clusters.size());
			System.out.println("TraceUtil-- clusters before sort: "+clusters);
			
			
			//对clusters进行排序
			Collections.sort(clusters, new Comparator<Set<Trace>>() {

				@Override
				public int compare(Set<Trace> c1, Set<Trace> c2) {
					String ele1 = c1.iterator().next().getEvents().get(0);
					String ele2 = c2.iterator().next().getEvents().get(0);
					int index1 = maxEleTrace.indexOf(ele1);
					int index2 = maxEleTrace.indexOf(ele2);
					if(index1 == index2)
						return 0;
					if(index1>index2)
						return 1;
					else
						return -1;
				}
			});
			
			
//			System.out.println("clusters after sort: "+clusters);
			
			
			resultMap.put(Constant.Sheel_loopsMap, loopsMap);
			resultMap.put(Constant.Sheel_clusters, clusters);*/
			
//			return resultMap;
			
			//按顺序将segment排列

			
			
		traceUtilMap.put(Constant.Sheel_Ss, Ss);
		traceUtilMap.put(Constant.Sheel_St1, loops);
//		return resultMap;

	}
	

	/**
	 * 计算事件序列s1和s2的距离
	 * 
	 * @param compliant trace t1
	 * @param deviated trace t2
	 * @return
	 */
	public static int editDistance(Trace t1, Trace t2) {
		int distance = 0;
		List<String> tempList = new ArrayList<>();
		tempList.addAll(t2.getEvents());
		
		for(int i=0;i<t1.getEvents().size();i++){

			if(tempList.size()==0){
				distance+=t1.getEvents().size()-i;
				return distance;
			}
			
			String s = t1.getEvents().get(i);
			//相同
			if(tempList.get(0).equals(s)){
				tempList.remove(0);
				continue;
			}
			//冗余
			if(!t1.getEvents().subList(i, t1.getEvents().size()).contains(tempList.get(0))){
				i--;  //下次迭代还是在位置i上
				distance++;
				tempList.remove(0);
				continue;
			}
			
			//交换
			int temp = tempList.indexOf(s);
			if(temp!=-1){
				distance+=temp;
//				String tempStr = s;
				tempList.set(temp, tempList.get(0));
//				tempList.set(0, s);
				tempList.remove(0);
			}else{//缺失
				distance++;
			}
			if(i==t1.getEvents().size()-1){
				distance+= tempList.size();
			}
			
		}
		
		distance+= tempList.size();
		return distance;
	}
	
	/**
	 * project c1 on c2
	 * 求集合1在集合2上的投影
	 * @param c1
	 * @param c2
	 * @return
	 */
	public static Collection<String> projection(Collection<String> c1,Collection<String> c2){
		List<String> result = new ArrayList<>();
		for(String s:c1){
			if(c2.contains(s)){
				result.add(s);
			}
		}
		return result;
	}
	
	
	/**
	 * project c1 on c2
	 * 求Trace t在集合2上的投影
	 * @param t
	 * @param c2
	 * @return
	 */
/*	public static Set<Trace> projection(Trace t,Collection<String> c2){
		Set<Trace> cluster = new HashSet<>();
		List<String> projection = (List<String>) projection(t.getEvents(), c2);
		int start = 0;
		int length = t.getEvents().size();
		while(start+length<projection.size()){
			Trace newTrace = new Trace();
			newTrace.setEvents(projection.subList(start, length));
			start = start+length;
			cluster.add(newTrace);
			
		}
		return cluster;
	}*/
	
	
	/**
	 * project c1 on c2
	 * 求Trace t在集合2上的投影
	 * @param t
	 * @param c2
	 * @return
	 */
	
	public static Set<Trace> projection(Trace t,Collection<String> c2){
		Set<Trace> newCluster = new HashSet<>();
		Trace newTrace = new Trace();
		List<String> projection = (List<String>) projection(t.getEvents(), c2);
		int length = c2.size();
		int count=0;
		int i = 0;
		while(i<=projection.size()){
			if(count==length){
				newCluster.add(newTrace);
				newTrace = new Trace();
				count=0;
			}
			if(i==projection.size())
				break;
			count++;
			newTrace.append(projection.get(i));
			i++;
		}
		
		return newCluster;
	}
	
	
	/**
	 * project c1 on c2
	 * 求trace t在clusters上的投影
	 * @param c1
	 * @param c2
	 * @return
	 */
	public static Set<Trace> projection(Trace t,Set<Trace> cluster){
		
		Set<Trace> newCluster = new HashSet<>();
		List<String> projection = (List<String>) projection(t.getEvents(), cluster.iterator().next().getEvents());
		int length = cluster.iterator().next().getEvents().size();
		int count=0;
		int i = 0;
		Trace newTrace = new Trace();
		while(i<=projection.size()){
			if(count==length){
				newCluster.add(newTrace);
				newTrace = new Trace();
				count=0;
			}
			if(i==projection.size())
				break;
			count++;
			newTrace.append(projection.get(i));
			i++;
		}
		
		
/*		while(start<projection.size()){
			
			System.out.println("test:"+projection.subList(start, length) );
			newTrace.setEvents(projection.subList(start, length));
			newCluster.add(newTrace);
			start+=length;
			
		}*/
		return newCluster;
	}
	
	/**
	 * project c1 on c2
	 * 求trace t在clusters上的投影
	 * @param c1
	 * @param c2
	 * @return
	 */
	public static Set<Trace> projection(Trace t,List<Set<Trace>> clusters){
		List<String> list = new ArrayList<>();
		for(Set<Trace> traces:clusters){
			Iterator<Trace> iter = traces.iterator();
			CollectionUtils.addAll(list, iter.next().getEvents());
		}
		
		Set<Trace> cluster = new HashSet<>();
		List<String> projection = (List<String>) projection(t.getEvents(), list);
		int start = 0;
		int length = t.getEvents().size();
		while(start+length<projection.size()){
			Trace newTrace = new Trace();
			newTrace.setEvents(projection.subList(start, length));
			start = start+length;
			cluster.add(newTrace);
			
		}
		return cluster;
	}
	
	
	/**
	 * project c1 on c2
	 * 求集合1在集合2上的投影
	 * @param c1
	 * @param c2
	 * @return
	 */
	public static List<List<String>> projectionExcept(Collection<String> c1,Collection<String> c2){
		List<List<String>> result = new LinkedList<>();
		List<String> temp = new LinkedList<>();
		for(String s:c1){
			if(!c2.contains(s)){
				temp.add(s);
			}else{
				if(temp.size()!=0)
				result.add(temp);
				temp = new LinkedList<>();
			}
				
		}
		if(temp.size()!=0)
			result.add(temp);
		return result;
	}

	
	/**
	 * project c1 on c2
	 * 求Trace t在集合2上的投影
	 * @param t
	 * @param c2
	 * @return
	 */
	public static List<Trace> projectionExcept(Trace t,Collection<String> c2){
		List<Trace> result = new LinkedList<>();
		List<List<String>> temp = projectionExcept(t.getEvents(), c2);
		for(List<String> list:temp){
			Trace newTrace = new Trace();
			newTrace.setEvents(list);
			result.add(newTrace);
		}
		return result;
	}
	
	/**
	 * project c1 on c2
	 * 求trace t在clusters上的投影
	 * @param c1
	 * @param c2
	 * @return
	 */
	public static List<Trace> projectionExcept(Trace t,List<Set<Trace>> clusters){
		List<Trace> result = new LinkedList<>();
		List<String> list = new ArrayList<>();
		for(Set<Trace> traces:clusters){
			Iterator<Trace> iter = traces.iterator();
			CollectionUtils.addAll(list, iter.next().getEvents());
		}
		result = projectionExcept(t,list);
		return result;
	}
	
	/**
	 * 根据trace中的event的频率 & N1比N2多发生一次的特点  以少数服从多数 推断出循环发生的频率
	 * @return int max
	 */
	public static int mode(Trace t,Loop loop){
		Bag<String> bag1 = new HashBag<>();
		Bag<Integer> bag2 = new HashBag<>();
	
		CollectionUtils.addAll(bag1, t.getEvents());
		
		for(String key:bag1.uniqueSet()){
			int value = bag1.getCount(key);
			Set<String> tn1 = loop.getN1();
			Set<String> tn2 = loop.getN2();
			if(tn1.contains(key)){
				bag2.add(value);
			}
			else if(tn2.contains(key)){
				bag2.add(value+1);
			}
		}

		int max = 0;
		for(Integer i:bag2.uniqueSet()){
			if(bag2.getCount(i)>max)
				max = i;
		}
		
		bag1 = null;
		bag2 = null;
		System.out.println(max);
		return max;
	}
	
	/**
	 * 根据trace中的event的频率 & N1比N2多发生一次的特点  以少数服从多数 推断出循环发生的频率
	 * @param t 待修复的序列
	 * @param loop1 外层循环
	 * @param loop2 内层循环 
	 * @return int max
	 */
	public static Map<String, Object> mode(Trace t,Loop loop1,Loop loop2){
		Set<String> tn11 = loop1.getN1();
		Set<String> tn12 = loop1.getN2();
		Set<String> tn21 = loop2.getN1();
		Set<String> tn22 = loop2.getN2();
		
		Bag<String> bag1 = new HashBag<>();
		Bag<Integer> bag2 = new HashBag<>();

		CollectionUtils.addAll(bag1, t.getEvents());
		
		for(String key:bag1.uniqueSet()){
			int value = bag1.getCount(key);
			if(tn11.contains(key)&&!tn21.contains(key)){
				bag2.add(value);
			}
			else if(tn12.contains(key)&&!tn22.contains(key)){
				bag2.add(value+1);
			}
		}

		int max = 0;
		for(Integer i:bag2.uniqueSet()){
			if(bag2.getCount(i)>max)
				max = i;
		}
		
		Map<String, Object> resultMap = new HashMap<>(); 
		resultMap.put(Constant.Sheel_Mode, max);
		
		for(String s:t.getEvents()){
			if((tn11.contains(s)&&!tn21.contains(s)&&bag1.getCount(s)==max)
					||(tn12.contains(s)&&!tn22.contains(s)&&bag1.getCount(s)==max-1)){
				resultMap.put(Constant.Sheel_ModeElement, s);
				break;
			}
		}
		
		bag1 = null;
		bag2 = null;
		//System.out.println("mode"+max);
		return resultMap;
	}
	public static void segmentClusterSort(List<Set<Trace>> clusters,final Trace maxEleTrace){
		Collections.sort(clusters, new Comparator<Set<Trace>>() {

			@Override
			public int compare(Set<Trace> c1, Set<Trace> c2) {
				String ele1 = c1.iterator().next().getEvents().get(0);
				String ele2 = c2.iterator().next().getEvents().get(0);
				int index1 = maxEleTrace.getEvents().indexOf(ele1);
				int index2 = maxEleTrace.getEvents().indexOf(ele2);
				if(index1 == index2)
					return 0;
				if(index1>index2)
					return 1;
				else
					return -1;
			}
		});
		
		
		
	}
	
	
	/**
	 * 测试是否有活动发生次数大于一次
	 * @param t
	 * @return
	 */
	public static boolean detectOccurrences(Trace t) {
		Set<String> set = new HashSet<>();
		for(String s:t.getEvents()){
			if(set.contains(s))
				return true;
			else
				set.add(s);
		}
		return false;
	}
	
	
	/**
	 * 按指定元素分割路径
	 * @param t
	 * @param ele
	 * @return
	 */
	public static List<Trace> splitWithEle(Trace t,String ele){
		List<Trace> result = new ArrayList<>();
		if(ele==null){
			result.add(t);
			return result;
		}
		List<String> temp = new LinkedList<>();
		for(String s:t.getEvents()){
			if(s.equals(ele)){
				if(temp.size()!=0)
					result.add(new Trace(temp));
				temp = new LinkedList<>();
				temp.add(s);
			}else{
				temp.add(s);
			}
		}
		
		if(temp.size()!=0)
			result.add(new Trace(temp));
		
		List<String> s1 = result.get(0).getEvents();
		List<String> s2 = result.get(1).getEvents();
		CollectionUtils.addAll(s1, s2);
		result.set(0, new Trace(s1));
		result.remove(1);
		return result;
	}
	
	
}
