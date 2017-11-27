package org.nust.wsong.UI.layout;

import java.awt.Dimension;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import edu.uci.ics.jung.algorithms.layout.AbstractLayout;
import edu.uci.ics.jung.graph.Graph;

/**
 * 图 布局
 * @author xxx
 *
 */
public class GraphLayout<V, E> extends AbstractLayout<V, E>{
	private Graph<V, E> g;
	//节点间水平距离
	private static final int H_GAP = 70;
	//节点间垂直距离
	private static final int V_GAP = 70;
	
	public GraphLayout(Graph<V, E> g) {
		super(g);
		this.g = g;
	}

	@Override
	public void initialize() {
		Set<V> set = new HashSet<>();
		Dimension d = getSize();
		if (d == null)
			return;
		double height = d.getHeight();
		double width = d.getWidth();
//		System.out.println("height"+height);
//		System.out.println("width"+width);
		int x = 0;
		int y = V_GAP;
//		V zeroPlaces = null;
		List<V> list = new ArrayList<>();
		//找到入度为0的点
		for(V vertex:g.getVertices()){
			if(g.getPredecessorCount(vertex)==0){
//				zeroPlaces = vertex;
				list.add(vertex);
//				break;
			}
		}
		
		
		while(!list.isEmpty()){
			x+=H_GAP;
			Set<V> newSet = new LinkedHashSet<>();
			for(int i=0;i<list.size();i++){
				y+=V_GAP;
				V v = list.get(i);
				if(set.contains(v))
					continue;
				Point2D coord = transform(v);
				coord.setLocation(x,y);
				set.add(v);
				newSet.addAll(g.getSuccessors(v));
			}
			list.clear();
			y =V_GAP;
			list.addAll(newSet);
		}
	}

	@Override
	public void reset() {
		initialize();
	}

}
