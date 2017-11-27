package org.nust.wsong.util;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections4.CollectionUtils;

/**
 * 
 * @author xxx
 * @since 2016-10-1
 */
public class MyCollectionsUtils {
	/**
	 * 两个集合的交集元素
	 * @param l1
	 * @param l2
	 * @return
	 */
	public static List<String> jiaoji(List<String> l1,List<String> l2){
		Set<String> set1 = new HashSet<>();
		Set<String> set2 = new HashSet<>();
		CollectionUtils.addAll(set1, l1);
		CollectionUtils.addAll(set2, l2);
		
		List<String> jiaoji = new ArrayList<>();
		for(String i:set1){
			if(set2.contains(i))
				jiaoji.add(i);
			
		}
		return jiaoji;
	}
	
	/**
	 * 两个集合的并集元素
	 * @param l1
	 * @param l2
	 * @return
	 */
	public static List<String> bingji(List<String> l1,List<String> l2){
		List<String> list = new ArrayList<>();
		for(String s:l1){
			if(!list.contains(s))
				list.add(s);
		}
		
		for(String s:l2){
			if(!list.contains(s))
				list.add(s);
		}
		return list;
	}
	
	/**
	 * 两个集合的元素是否一样（不考虑元素的势）
	 * @param l1
	 * @param l2
	 * @return
	 */
	public static boolean isEqualCollection(List<String> l1,List<String> l2){
		Set<String> set1 = new HashSet<>();
		Set<String> set2 = new HashSet<>();
		CollectionUtils.addAll(set1, l1);
		CollectionUtils.addAll(set2, l2);
		
		return CollectionUtils.isEqualCollection(set1, set2);
		
	}
}
