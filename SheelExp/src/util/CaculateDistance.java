package util;

import java.io.File;
import java.util.List;

import log.ReadLog;
import structure.EventFlow;

public class CaculateDistance {

	private static String dir = "C:\\Users\\Administrator\\Desktop\\日志修复实验\\3\\";
	private static String sourceFile = "1Ku_a1xs.xes";
	private static String targetFile = "1Ku_a1xs_mix_ed6.xes";

	public static void main(String[] args) {

		File file = new File(dir + "\\" + sourceFile);
		List<EventFlow> sourceTraces = ReadLog.getTraces(file);
		file = new File(dir + "\\" + targetFile);
		List<EventFlow> targetTraces = ReadLog.getTraces(file);

		int distance;
		for (int i = 0; i < targetTraces.size(); i++) {
			EventFlow source = sourceTraces.get(i);
			EventFlow target = targetTraces.get(i);
			distance = StringDistance.editDistance(source, target);
			System.out.println(i + ": " + distance);
		}

	}

}
