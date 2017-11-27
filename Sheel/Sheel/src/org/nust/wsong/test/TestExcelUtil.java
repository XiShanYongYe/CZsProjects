package org.nust.wsong.test;

import static org.junit.Assert.*;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.Test;
import org.nust.wsong.util.ExcelUtil;

public class TestExcelUtil {
	@Test
	public void testName() throws Exception {
		File file = new File("d://123.xls");
		List<String> headers = new ArrayList<>();
		Collections.addAll(headers, "id","name","sex","age","address");
		ExcelUtil.createExcel(file, headers);
		
		List<List<String>> records = new ArrayList<>();
		List<String> record = new ArrayList<>();
		Collections.addAll(record, "1","xxx","male","24","njust");
		records.add(record);
		
		record = new ArrayList<>();
		Collections.addAll(record, "2","yyy","female","12","njust");
		records.add(record);
		
		record = new ArrayList<>();
		Collections.addAll(record, "3","zzz","xtest","12","njust");
		records.add(record);
		
		System.out.println(records);
		for(List<String> r:records)
			ExcelUtil.append2File(file, r);
	}
}
