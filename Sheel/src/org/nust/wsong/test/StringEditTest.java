package org.nust.wsong.test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.junit.Test;
import org.nust.wsong.util.StringDistance;



public class StringEditTest {
	@Test
	public void testName() throws Exception {
		List<String> str1 = new ArrayList<>();
		Collections.addAll(str1, "trans_2", "trans_7", "trans_0", "trans_6","trans_4", "trans_1", "twork_1");
		List<String> str2 = new ArrayList<>();
		Collections.addAll(str2, "trans_2", "trans_7", "trans_3", "twork_2");
		
		
		int editDistance = StringDistance.editDistance(str1, str2);
		System.out.println(editDistance);
		
		
		
		
		
		
		
	}
}
