package org.nust.wsong.test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections4.CollectionUtils;
import org.nust.wsong.util.MyCollectionsUtils;


public class MyCollectionUtil {
	public static void main(String[] args) {
		List<String> list1 = new ArrayList<>();
		List<String> list2 = new ArrayList<>();
		Collections.addAll(list1, "1","2","3","4");
		Collections.addAll(list2, "6","2","3","4");
		
		System.out.println(MyCollectionsUtils.jiaoji(list1, list2));
		System.out.println(MyCollectionsUtils.bingji(list1, list2));
		
		
	}
	
	public List<Integer> jiaoji(List<Integer> l1,List<Integer> l2){
		Set<Integer> set1 = new HashSet<>();
		Set<Integer> set2 = new HashSet<>();
		CollectionUtils.addAll(set1, l1);
		CollectionUtils.addAll(set2, l2);
		
		List<Integer> jiaoji = new ArrayList<>();
		for(Integer i:set1){
			if(set2.contains(i))
				jiaoji.add(i);
			
		}
		return jiaoji;
	}
	
}
