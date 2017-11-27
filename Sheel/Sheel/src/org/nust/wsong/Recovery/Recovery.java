package org.nust.wsong.Recovery;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.swing.JFrame;

import org.apache.commons.collections4.CollectionUtils;
import org.nust.wsong.constant.Constant;
import org.nust.wsong.model.Edge;
import org.nust.wsong.model.Graph;
import org.nust.wsong.model.Loop;
import org.nust.wsong.model.Node;
import org.nust.wsong.model.Trace;
import org.nust.wsong.util.GraphViz;
import org.nust.wsong.util.GraphVizUtil;
import org.nust.wsong.util.MyCollectionsUtils;
import org.nust.wsong.util.TraceUtil;
import org.nust.wsong.view.GraphVizViewer;

import oracle.jrockit.jfr.jdkevents.ThrowableTracer;


/**
 * 
 * @author xxx
 * @since 2016-09-28
 *
 */
public class Recovery {
	
	/**Algorithm 2 sub-process discovery
	 * 根据traces获得DAG
	 * @param traces
	 */
	public static Graph mining (Collection<Trace> traces){
		long start = System.nanoTime();
	    Set<String> nodes = new HashSet<>();
		Map<String, Set<String>> indirectRelation = new HashMap<>();//间接关系 
		for(Trace trace:traces){
			for(int i=0;i<trace.size();i++){
				String node1 = trace.get(i);
				nodes.add(node1);
				if(indirectRelation.get(node1)==null){
					indirectRelation.put(node1, new HashSet<String>());
				}
				for(int j=i+1;j<trace.size();j++){
					String node2 = trace.get(j);
					nodes.add(node2);
					indirectRelation.get(node1).add(node2);
				}
			}
		}
		long end1 = System.nanoTime();
		System.out.println("mining1时间:"+(end1-start)/1000000.0 );
		
		Graph g = new Graph();
		for(String s:nodes)
			g.getNodes().add(new Node(s));
		
		for(Entry<String,Set<String>> entry:indirectRelation.entrySet()){
			String key = entry.getKey();
			for(String value:entry.getValue()){
				if(!indirectRelation.get(value).contains(key)){
					Node source ;
					Node target;
					source = new Node(key);
					target = new Node(value);
					/*if(g.getNodes().contains(new Node(key))){
						source = g.getNodeById(key);
					}else{
						source = new Node(key);
					}
					
					if(g.getNodes().contains(new Node(value))){
						target = g.getNodeById(value);
					}else{
						target = new Node(value);
					}
					g.addNode(source);
					g.addNode(target);*/
					g.addEdge(source,target);
				}
			}
		}
		long end2 = System.nanoTime();
		System.out.println("mining2时间:"+(end2-end1)/1000000.0 );
		
		//图显
//		GraphVizUtil.show(g);
//		System.out.println(g);
		return g;
	}

	/**
	 * 不带循环的修复
	 * @param trace
	 * @param g
	 * @return
	 */
	public static Trace repair(Trace trace,Graph g){
		//入度出度
		Map<String,Integer> indegrees = new HashMap<>();
		Map<String, List<Edge>> outEdges = new HashMap<>();
		for(Node node:g.getNodes()){
			indegrees.put(node.getName(), 0);
		}
		
		for(Edge e:g.getEdges()){
			Node source = e.getSource();
			Node target = e.getTarget();
			if(outEdges.get(source.getName())==null){
				List<Edge> edges = new ArrayList<>();
				outEdges.put(source.getName(),edges);
			}
			outEdges.get(source.getName()).add(e);
			indegrees.put(target.getName(), indegrees.get(target.getName())+1);
		}

		Trace repairedTrace = new Trace();
		
		//入度为0的顶点
		List<String> zeroList = new ArrayList<>();
		for(Entry<String, Integer> entry:indegrees.entrySet()){
			if(entry.getValue()==0){
				//indegrees.remove(entry.getKey());
				zeroList.add(entry.getKey());
			}
		}
		
		
		int i =0;
		while(true){
			if(indegrees.size()==0)
				break;
			
			//如果trace 已经遍历完 但图中还有其他节点
			if(i==(trace.getEvents().size())){		
				while(zeroList.size()>0){
					repairedTrace.getEvents().addAll(zeroList);
					List<String> added = new ArrayList<>();
					for(String s:zeroList){
						if(outEdges.get(s)==null)
							break;
						for(Edge e:outEdges.get(s)){
							String target = e.getTarget().getName();
							indegrees.put(target, indegrees.get(target)-1);
							if(indegrees.get(target)==0){
								indegrees.remove(target);
								added.add(target);
							}
						}
						outEdges.remove(s);
					}
					
					zeroList.clear();
					zeroList.addAll(added);
				}
				return repairedTrace;
			}
			
			//冗余元素 删除
			if(!indegrees.containsKey(trace.get(i))){
				i++;
				continue;
			}
			
			//当前元素入度为0
			if(zeroList.contains(trace.get(i))){
				repairedTrace.getEvents().add(trace.get(i));
				indegrees.remove(trace.get(i));
				if(outEdges.get(trace.get(i))==null)
					return repairedTrace;
				for(Edge e:outEdges.get(trace.get(i))){
					String target = e.getTarget().getName();
					indegrees.put(target, indegrees.get(target)-1);
					if(indegrees.get(target)==0){
						//indegrees.remove(target);
						zeroList.add(target);
					}
				}
				outEdges.remove(trace.get(i));
				zeroList.remove(trace.get(i));
				i++;
				continue;
			}
			
			boolean flag = true;
			
			//找一个不属于当前trace 入度为0的
			for(String s:zeroList){
				if(!trace.getEvents().subList(i, trace.getEvents().size()).contains(s)){
					repairedTrace.getEvents().add(s);
					indegrees.remove(s);
					for(Edge e:outEdges.get(s)){
						String target = e.getTarget().getName();
						indegrees.put(target, indegrees.get(target)-1);
						if(indegrees.get(target)==0){
							//indegrees.remove(target);
							zeroList.add(target);
						}
					}
					outEdges.remove(s);
					zeroList.remove(s);
					flag = false;
					break;
				}
			}
			//往后找
			if(flag){
				for(int j=i+1;j<trace.size();j++){
					if(zeroList.contains(trace.get(j))){
						repairedTrace.getEvents().add(trace.get(j));
						indegrees.remove(trace.get(j));
						for(Edge e:outEdges.get(trace.get(j))){
							String target = e.getTarget().getName();
							indegrees.put(target, indegrees.get(target)-1);
							if(indegrees.get(target)==0){
								//indegrees.remove(target);
								zeroList.add(target);
							}
						}
						outEdges.remove(trace.get(j));
						zeroList.remove(trace.get(j));
						flag = false;
						break;
					}
				}
			}
			
		
		}
		return repairedTrace;
	}
	
	/**
	 * 根据g 拓扑排序得到修复后的日志
	 * @param trace
	 * @param g
	 * @return
	 */
	public static Trace repair1(Trace trace,Graph g){
		//入度出度
		Map<String,Integer> indegrees = new HashMap<>();
		Map<String, List<Edge>> outEdges = new HashMap<>();
		for(Node node:g.getNodes()){
			indegrees.put(node.getName(), 0);
		}
		
		for(Edge e:g.getEdges()){
			Node source = e.getSource();
			Node target = e.getTarget();
			if(outEdges.get(source.getName())==null){
				List<Edge> edges = new ArrayList<>();
				outEdges.put(source.getName(),edges);
			}
			outEdges.get(source.getName()).add(e);
			indegrees.put(target.getName(), indegrees.get(target.getName())+1);
		}

		Trace repairedTrace = new Trace();
		
		//入度为0的顶点
		List<String> zeroList = new ArrayList<>();
		for(Entry<String, Integer> entry:indegrees.entrySet()){
			if(entry.getValue()==0){
				//indegrees.remove(entry.getKey());
				zeroList.add(entry.getKey());
			}
		}
		
		
		
		int i =0;
		while(true){
			if(indegrees.size()==0)
				break;
			
			//如果trace 已经遍历完 但图中还有其他节点
			if(i==(trace.getEvents().size())){		
				while(zeroList.size()>0){
					for(String s:zeroList){
						String str =s;
						if(s.lastIndexOf("N")>0)
							str = s.substring(0, str.lastIndexOf("N"));
						repairedTrace.getEvents().add(str);
					}
					List<String> added = new ArrayList<>();
					for(String s:zeroList){
						if(outEdges.get(s)==null)
							break;
						for(Edge e:outEdges.get(s)){
							String target = e.getTarget().getName();
							indegrees.put(target, indegrees.get(target)-1);
							if(indegrees.get(target)==0){
								indegrees.remove(target);
								added.add(target);
							}
						}
						outEdges.remove(s);
					}
					
					zeroList.clear();
					zeroList.addAll(added);
				}
				return repairedTrace;
			}
			
			List<String> list = new ArrayList<>(); //以trace[i]开头的元素
			for(String key:indegrees.keySet()){
				if(key.startsWith(trace.get(i))){
					list.add(key);
				}
			}
			//冗余元素 删除
			if(list.size()==0){
				i++;
				continue;
			}
			
			//当前元素入度为0
			if(CollectionUtils.intersection(zeroList, list).size()>0){
				String str = ((List<String>)CollectionUtils.intersection(zeroList, list)).get(0);
				repairedTrace.getEvents().add(trace.get(i));
				indegrees.remove(str);
				if(outEdges.get(str)==null)
					return repairedTrace;
				for(Edge e:outEdges.get(str)){
					String target = e.getTarget().getName();
					indegrees.put(target, indegrees.get(target)-1);
					if(indegrees.get(target)==0){
						//indegrees.remove(target);
						zeroList.add(target);
					}
				}
				outEdges.remove(str);
				zeroList.remove(str);
				i++;
				continue;
			}
			
			boolean flag = true;
			
			//找一个不属于当前trace 入度为0的
			for(String s:zeroList){
				String str =s;
				if(s.lastIndexOf("N")>0)
					str = s.substring(0, str.lastIndexOf("N"));
				if(!trace.getEvents().subList(i, trace.getEvents().size()).contains(str)){
					repairedTrace.getEvents().add(str);
					indegrees.remove(s);
					for(Edge e:outEdges.get(s)){
						String target = e.getTarget().getName();
						indegrees.put(target, indegrees.get(target)-1);
						if(indegrees.get(target)==0){
							//indegrees.remove(target);
							zeroList.add(target);
						}
					}
					outEdges.remove(s);
					zeroList.remove(s);
					flag = false;
					break;
				}
			}
			//往后找
			if(flag){
				boolean flag1 = true;
				for(int j=i+1;j<trace.size();j++){
					if(!flag1)
						break;
					for(String s:zeroList){
						if(s.startsWith(trace.get(j))){
							repairedTrace.getEvents().add(trace.get(j));
							indegrees.remove(s);
							for(Edge e:outEdges.get(s)){
								String target = e.getTarget().getName();
								indegrees.put(target, indegrees.get(target)-1);
								if(indegrees.get(target)==0){
									//indegrees.remove(target);
									zeroList.add(target);
								}
							}
							outEdges.remove(s);
							zeroList.remove(s);
							flag1 = false;
							break;
						}
					}
				}
			}
			
		
		}
		return repairedTrace;
	}
	/**
	 * 没有嵌套的循环的修复
	 * Algorithm 6
	 * @param t
	 * @param clusters
	 * @param loops
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public static Trace repair2 (Trace t,List<Set<Trace>> clusters,Set<Loop> loops){
		List<Graph> graphs = new ArrayList<>();
		for(Set<Trace> c:clusters){
			graphs.add(mining(c));
		}
		
		//计算N2的发生次数 先假定就一个嵌套循环
		Map<Set<String>, Integer> rN2Map = new HashMap<>();
		Map<Set<String>, Set<String>> nested = (Map<Set<String>, Set<String>>) TraceUtil.traceUtilMap.get(Constant.Sheel_nested);
//		String key = nested
		Map<Set<String>, Integer> position = new HashMap<>();
//		position.put(loops.iterator().next().getN2(), 1);
		
		for(int i=clusters.size()-1;i>=0;i--){
			Trace next = clusters.get(i).iterator().next();
			
			for(Loop loop:loops){
				if(CollectionUtils.containsAll(loop.getN1(),next.getEvents())){
					position.put(loop.getN2(),i);
				}
			}
		}
		System.out.println("position:"+position);
		List<Trace> segments = new LinkedList<>();
		
		
//		boolean withNested = false;
		if(nested.size()>0){
//			withNested = true;
			
			Loop outer = null;
			Loop inner = null;
			for(Loop loop:loops){
				if(nested.containsKey(loop.getN2())){
					outer = loop;
				}else if(nested.containsValue(loop.getN2())){
					inner = loop;
				}
			}
			Map<String, Object> resultMap = TraceUtil.mode(t, outer, inner);
			int mode = (int) resultMap.get(Constant.Sheel_Mode);
			String s = (String) resultMap.get(Constant.Sheel_ModeElement);
			System.out.println(mode);
			
			segments = TraceUtil.splitWithEle(t, s);
			rN2Map.put(outer.getN2(), mode-1);
		}else{
			segments.add(t);
			
		}
		
		int currentIndex = 0;
		Trace currentSegment = segments.get(currentIndex);
		//没有嵌套的循环
		for(Loop loop:loops){
			if(nested.containsKey(loop.getN2()))
				continue;
			int rN1 = TraceUtil.mode(currentSegment, loop);
			int rN2 = rN1 -1;
			rN2Map.put(loop.getN2(), rN2);
		}
		int j =0;
		Graph temp = graphs.get(0);
//		Trace currentSegment = segments.get(0);
		for(int i=1;i<graphs.size();i++){
			Set<String> nodes = new HashSet<>();
			for(Node n:graphs.get(i).getNodes())
				nodes.add(n.getName());
			//不是N2
			if(!rN2Map.containsKey(nodes)){
				temp = DAGcat(temp, graphs.get(i), j++);
				continue;
			}
			if(rN2Map.get(nodes)==0){
				continue;
			}
			if(rN2Map.get(nodes)>0){

				/*if(nested.containsKey(graphs.get(i))){
					//找到
				}*/
				
				//找到相应的位置-1 下次循环就是对应的n1的位置
				temp = DAGcat(temp, graphs.get(i), j++);
				i = position.get(nodes)-1;
				rN2Map.put(nodes,rN2Map.get(nodes)-1);
				
				//嵌套循环跳入下个分段
				if(nested.containsKey(nodes)){
					currentIndex++;
					currentSegment = segments.get(currentIndex);
					//没有嵌套的循环
					for(Loop loop:loops){
						if(nested.containsKey(loop.getN2()))
							continue;
						int rN1 = TraceUtil.mode(currentSegment, loop);
						int rN2 = rN1 -1;
						rN2Map.put(loop.getN2(), rN2);
					}
				}
			
				continue;
			}
		}

		
		//图显
//		GraphVizUtil.show(temp);
//		System.out.println("temp"+temp);
		
		//Thread.sleep(20000);
		Trace repaired = Recovery.repair1(t, temp);
		return repaired;
	}
	
	
	/**
	 * 带嵌套的循环的修复(自己)
	 * Algorithm 6
	 * @param t
	 * @param clusters
	 * @param loops
	 * @throws Exception
	 */
	public static void repair3 (Trace t,List<Set<Trace>> clusters,Set<Loop> loops){
		List<Graph> graphs = new ArrayList<>();
		for(Set<Trace> c:clusters){
			graphs.add(mining(c));
		}
		
		Map<Loop, Integer> rN2Map = new HashMap<>();
		for(Loop loop:loops){
			int rN1 = TraceUtil.mode(t, loop);
			int rN2 = rN1 -1;
			rN2Map.put(loop, rN2);
		}
		
		
//		System.out.println("rN2Map= "+rN2Map);
		Graph temp = graphs.get(0);
		int j =0;
		for(int i=1;i<graphs.size();i++){
			//if(i<graphs.size()-1){
				Graph graphi = graphs.get(i);
				Set<String> graphiSet = new HashSet<>();
				for(Node n:graphi.getNodes()){
					graphiSet.add(n.getName());
				}
				
				Graph graphj = graphs.get(i-1);
				Set<String> graphjSet = new HashSet<>();
				for(Node n:graphj.getNodes()){
					graphjSet.add(n.getName());
				}
				
				
				for(Loop loop:loops){
					if(CollectionUtils.isEqualCollection(loop.getN2(), graphiSet)
							&&CollectionUtils.isEqualCollection(loop.getN1(), graphjSet)){
						//temp = DAGcat(temp, graphs.get(i), j++);
						while((rN2Map.get(loop)>0)){
							temp = DAGcat(temp, graphs.get(i), j++);
							temp = DAGcat(temp, graphs.get(i-1), j++);
							//i++;
							rN2Map.put(loop, rN2Map.get(loop)-1);
						}
						i++;
					}
				}
			//}
			temp = DAGcat(temp, graphs.get(i), j++);
		}
		
		//图显
//		System.out.println("temp"+temp);
		
		//Thread.sleep(20000);
/*		while(true){
			
		}*/
		Trace repaired = Recovery.repair1(t, temp);
		System.out.println("测试修复后"+repaired);
	}
	
	/**
	 * 合并图
	 * @param g1
	 * @param g2
	 * @param i
	 * @return
	 */
	public static Graph DAGcat(Graph g1,Graph g2,int i){
		Graph g = new Graph();
		Node source = null;
		Node target = null;
		for(Node n:g1.getNodes()){
			//if(n.get)
			if(n.getOutDegree()==0)
				source = n;
			
		}
		
		Set<Node> newNodes = new HashSet<>();
		Set<Edge> newEdges = new HashSet<>();
		
		
		for(Node n:g2.getNodes()){
			Node newNode = new Node(n.getName()+"N"+i);
			newNode.setInDegree(n.getInDegree());
			newNode.setOutDegree(n.getOutDegree());
			//n.setName(n.getName()+":N"+i);
			if(newNode.getInDegree()==0){
				//System.out.println(n);
				target = newNode;
			}
			newNodes.add(newNode);
		}
		
		for(Edge e:g2.getEdges()){
			String s = e.getSource().getName();
			String t = e.getTarget().getName();
			
			Edge newEdge = new Edge(new Node(s+"N"+i),new Node(t+"N"+i));
			newEdges.add(newEdge);
		}
		
	
		
		
		//if(g1.getNodes().size()>0)
			g.getNodes().addAll(g1.getNodes());
		//if(g1.getEdges().size()>0)
			g.getEdges().addAll(g1.getEdges());
		
		g.getNodes().addAll(newNodes);
		g.getEdges().addAll(newEdges);
		
		g.addEdge(source, target);
		
		
		
		return g;
	}
	
	/**
	 * 启发式规则1 选出与trace相似性最大的cluster
	 * @param t
	 * @param clusters
	 * @return
	 */
	public static List<Trace> heuristics1 (Trace t,List<List<Trace>> clusters){
		double d = Double.MIN_VALUE;
		List<Trace> maxCluster = null;
		for(List<Trace> cluster:clusters){
			//11.18修改
			Set<String> set = new HashSet<>();
			for(Trace trace:cluster)
				set.addAll(trace.getEvents());
			
			double value =(double) CollectionUtils.intersection(t.getEvents(), set).size()/CollectionUtils.union(t.getEvents(),set).size();
			//11.18修改
			if(d<value){
				d= value;
//				System.out.println("heuristics1 value:"+d);
				maxCluster = cluster;
			}
		}
		return maxCluster;
	}
	
	/**
	 * 启发式规则1 选出与trace相似性最大的cluster
	 * @param t
	 * @param clusters
	 * @return
	 */
	public static List<Trace> heuristics12 (Trace t,List<List<Trace>> clusters){
		double d = Double.MIN_VALUE;
		List<Trace> maxCluster = clusters.get(0);
		for(List<Trace> cluster:clusters){
			List<String> jiaoji = MyCollectionsUtils.jiaoji(t.getEvents(), cluster.get(0).getEvents());
			List<String> bingji = MyCollectionsUtils.bingji(t.getEvents(), cluster.get(0).getEvents());

			double value = (double)jiaoji.size()/bingji.size();
			//11.18修改
			if(d<value){
				d= value;
//				System.out.println("heuristics1 value:"+d);
				maxCluster = cluster;
			}
			
		}
		return maxCluster;
	}
}
