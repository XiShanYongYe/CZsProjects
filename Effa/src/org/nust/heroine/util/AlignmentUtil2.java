package org.nust.heroine.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.nust.heroine.basicstruct.Trace;

public class AlignmentUtil2 {

	public static void getAccuracy(File originalLog, File errorLog, File repairedFile) {

		// double accuracy = 0.0; // 日志修复的正确率
		// int right = 0; // 统计原trace和错误trace以及错误trace与修复trace的编辑距离一样的个数
		List<Trace> originalTrace = LogUtil.xesParse(originalLog);
		List<Trace> errorTrace = LogUtil.xesParse(errorLog);
		List<List<Trace>> repairedTrace = parseRepaired(repairedFile);

		System.out.println("---------------原始日志");
		for (int i = 0; i < originalTrace.size(); i++) {
			System.out.println(originalTrace.get(i));
		}
		System.out.println("----------------------");
		double Accuracy = 0.0;
		for (int i = 0; i < repairedTrace.size(); i++) {
			Trace original = originalTrace.get(i);
			Trace error = errorTrace.get(i);
			List<Trace> repairedList = repairedTrace.get(i);
			int editDistance1 = StringDistance.editDistance(original.getEvents(), error.getEvents());
			int danbu = 0;
			for (Trace repaired : repairedList) {
				int editDistance2 = StringDistance.editDistance(repaired.getEvents(), error.getEvents());
				if (editDistance1 == editDistance2) {
					danbu++;
				}
			}
			// for (int j = 0; j < repairedTrace.get(i).size(); j++) {
			// int editDistance2 =
			// StringDistance.editDistance(errorTrace.get(i).getEvents(),
			// repairedTrace.get(i).get(j).getEvents());
			// /*
			// * System.out.println("editDistance1 = "+editDistance1 +
			// * " editDistance2 = "+editDistance2); System.out.println(
			// * "OriginalTrace = " + originalTrace.get(i).getEvents());
			// * System.out.println("ErrorTrace = " +
			// * errorTrace.get(i).getEvents()); System.out.println(
			// * "RepairedTrace = " +
			// * repairedTrace.get(i).get(j).getEvents());
			// */
			// if (editDistance1 == editDistance2) {
			// danbu++;
			// }
			// }
			System.out.println();
			double singleAcccuracy = danbu * 1.0 / repairedList.size();
			System.out.println("第" + i + "条准确率：" + singleAcccuracy + " 编辑距离：" + editDistance1);
			Accuracy += singleAcccuracy;
		}
		double Accuracy2 = Accuracy / repairedTrace.size();
		System.out.println(errorLog.getName() + "----------------准确率：" + myDoubleFormat(Accuracy2));
	}

	private static List<List<Trace>> parseRepaired(File repairedFile) {
		List<List<Trace>> repaired = new ArrayList<List<Trace>>();
		Map<Integer, List<Trace>> repairedmap = new HashMap<Integer, List<Trace>>();
		try {
			BufferedReader reader = new BufferedReader(new FileReader(repairedFile));
			String line = null;
			// boolean isNewSet = false;
			List<Trace> repairedTrace = null;
			while ((line = reader.readLine()) != null) {
				if (line.contains("traceIDs")) {
					repairedTrace = new ArrayList<Trace>();
					String[] nums = line.substring(line.lastIndexOf("[") + 1, line.lastIndexOf("]")).split(", ");
					for (int i = 0; i < nums.length; i++)
						repairedmap.put(Integer.parseInt(nums[i]), repairedTrace);
					continue;
				} else {
					String[] events = line.split(",");
					ArrayList<String> eventsList = new ArrayList<String>();
					for (String s : events) {
						eventsList.add(s);
					}
					Trace trace = new Trace(eventsList);
					repairedTrace.add(trace);
				}
			}
			for (int i = 0; i < repairedmap.size(); i++) {
				List<Trace> traces = repairedmap.get(i);
				repaired.add(traces);
			}

			// for(List<Trace> traces:repaired){
			// System.out.println();
			// for(Trace trace:traces)
			// System.out.println(trace);
			// }
			reader.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		/*
		 * for(int i= 0;i<repaired.size();i++){
		 * System.out.println(repaired.get(i)); }
		 */

		return repaired;
	}

	public static void main(String[] args) {

		String filePath = "E:\\常震\\zn\\shuju\\alignment_repaired\\CO.001_mix_ed1_a_repaired.txt";
		parseRepaired(new File(filePath));
	}

	/**
	 * 使d小数点后保留四位数字
	 * 
	 * @param d
	 */
	private static String myDoubleFormat(double d) {
		DecimalFormat format = new DecimalFormat("####0.0000");
		return format.format(d);
	}
}
