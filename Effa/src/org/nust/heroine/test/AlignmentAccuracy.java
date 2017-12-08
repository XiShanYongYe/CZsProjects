package org.nust.heroine.test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import org.junit.Test;
import org.nust.heroine.util.AlignmentUtil2;

public class AlignmentAccuracy {

	@Test
	public void test() {
		try {

			// 读取pnml文件名
			BufferedReader pnmlReader = new BufferedReader(
					new FileReader(new File("F:\\常震\\CZ\\Second\\pnmlName.txt")));
			String pnmlName = pnmlReader.readLine();
			pnmlReader.close();

			// 读取xes文件名
			BufferedReader nameReader = new BufferedReader(new FileReader(new File("F:\\常震\\CZ\\Second\\logname.txt")));
			String line = nameReader.readLine();
			line = line.replace(".xes", "");
			nameReader.close();

			// 找到对应的修复日志文件
			File repairedFile = null;
			File dir = new File("F:\\常震\\CZ\\Second\\alignment_repaired");
			File[] list = dir.listFiles();
			for (File file : list) {
				if (file.getName().contains(line)) {
					repairedFile = file;
					break;
				}
			}

			// 找到原日志文件和错误日志文件
			File originalLog = null;
			File errorLog = null;
			pnmlName = pnmlName.replace(".pnml", "");
			File dirFile = new File("F:\\常震\\CZ\\Second\\无选择有循环");
			// File dirFile = new File("F:\\陈芳菲\\3_日志修复实验\\2");
			File[] logs = dirFile.listFiles();
			for (File file : logs) {
				if (file.getName().equals(pnmlName + ".xes")) {
					originalLog = file;
				}
				if (file.getName().equals(line + ".xes"))
					errorLog = file;
			}
			AlignmentUtil2.getAccuracy(originalLog, errorLog, repairedFile);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// private List<Trace> getLogTrace(File file) {
	// return LogUtil.xesParse(file);
	// }
}
