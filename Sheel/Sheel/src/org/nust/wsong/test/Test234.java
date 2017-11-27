package org.nust.wsong.test;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.apache.commons.collections4.CollectionUtils;

public class Test234 {
	public static void main(String[] args) {
		Set<Set<Integer>> list = new HashSet<>();
		Set<Integer> set1 = new HashSet<>();
		set1.add(1);
		set1.add(2);
		set1.add(3);
		set1.add(4);
		
		Set<Integer> set2 = new HashSet<>();
		set2.add(2);
		set2.add(3);
		set2.add(4);
		
		Set<Integer> set3 = new HashSet<>();
		set3.add(2);
		set3.add(3);
		set3.add(1);
		
		list.add(set1);
		list.add(set2);
		list.add(set3);
		
		Iterator<Set<Integer>> iter = list.iterator();
		Iterator<Set<Integer>> iter1 = list.iterator();
		System.out.println(iter.next());
		System.out.println(iter1.next());
/*		while(iter.hasNext()){
			
			
			
			
		}*/
		System.out.println(CollectionUtils.containsAll(set1, set3));
		//System.out.println(list);
		
		
		
		
		
		
		
		
		
		
	}
}
