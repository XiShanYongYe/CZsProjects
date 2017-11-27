package org.nust.wsong.test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.nust.wsong.util.MyCollectionsUtils;

public class MyCollectionUtilsTest {
	public static void main(String[] args) {
		List<Integer> collection1 = new ArrayList<>();
		List<Integer> collection2 = new ArrayList<>();
		List<Integer> collection3 = new ArrayList<>();
		Collections.addAll(collection1, 1,2,3,4);
		Collections.addAll(collection2, 2,3,4,5);
		Collections.addAll(collection3, 1,2,3,4,2,3);
		
		System.out.println("交集"+MyCollectionsUtils.intersection(collection1, collection2));
		System.out.println("并集"+MyCollectionsUtils.union(collection1, collection2));
		System.out.println("差集"+MyCollectionsUtils.difference(collection1, collection2));
		
		System.out.println("collection1与collection3里面元素是否相等:"+MyCollectionsUtils.isEqualCollection(collection1,collection3));
		//CollectionUtils.
	}
}
