package org.nust.wsong.test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections4.CollectionUtils;

public class TestCollectionUtil {
	public static void main(String[] args) {
		Set<Integer> set1 = new HashSet<>();
		set1.add(1);
		set1.add(2);
		set1.add(3);
		
		Set<Integer> set2 = new HashSet<>();
		set2.add(1);
		set2.add(3);
		set2.add(2);
		set2.add(2);
		
		System.out.println(CollectionUtils.isEqualCollection(set1,set2));
		
		List<Integer> list1 = new ArrayList<>();
		list1.add(1);
		list1.add(2);
		list1.add(3);
		
		List<Integer> list2 = new ArrayList<>();
		list2.add(1);
		list2.add(3);
		list2.add(2);
		list2.add(2);
		
		System.out.println(CollectionUtils.isEqualCollection(list1,list2));
		//CollectionUtils.
	}
}
