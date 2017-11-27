package org.nust.wsong.util;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 集合工具类
 * @author xxx
 *
 */
public class MyCollectionsUtils<T> {
	/**
	 * 交集
	 * @param l1
	 * @param l2
	 * @return
	 */
	public static<T> Set<T> intersection(Collection<T> l1,Collection<T> l2){
		Set<T> set1 = new HashSet<>();
		for(T t:l1){
			set1.add(t);
		}
		set1.retainAll(l2);
		return set1;
	}
	
	/**
	 * 差集
	 * @param l1
	 * @param l2
	 * @return
	 */
	public static<T> Set<T> difference(Collection<T> l1,Collection<T> l2){
		Set<T> set1 = new HashSet<>();
		for(T t:l1){
			set1.add(t);
		}
		set1.removeAll(l2);
		return set1;
	}
	
	/**
	 * 并集
	 * @param l1
	 * @param l2
	 * @return
	 */
	public static<T> Set<T> union(Collection<T> l1,Collection<T> l2){
		Set<T> set1 = new HashSet<>();
		for(T t:l1){
			set1.add(t);
		}
		set1.addAll(l2);
		return set1;
	}
	
	/**
	 * 两个集合的元素是否一样（不考虑元素的势）
	 * @param l1
	 * @param l2
	 * @return
	 */
	public static<T> boolean isEqualCollection(Collection<T> l1,Collection<T> l2){
		Set<T> set1 = new HashSet<>();
		Set<T> set2 = new HashSet<>();
		for(T t:l1){
			set1.add(t);
		}
		for(T t:l2){
			set2.add(t);
		}
		if(set1.size()!=set2.size())
			return false;
		return set1.containsAll(set2);
	}
}
