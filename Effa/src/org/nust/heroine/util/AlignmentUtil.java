package org.nust.heroine.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.nust.heroine.basicstruct.Trace;

/**
 * 创建traceid和trace的映射 ，因为存在多个id映射多条trace，所以采取集合映射集合的方法
 * 
 * @author 赵宁
 *
 */
public class AlignmentUtil {

	/**
	 * 得到一个traceID到修复序列的map映射
	 * 
	 * @param file
	 * @return
	 */
	private static Map<Set<Integer>, List<Trace>> map = null;
	private static int allCount; // 所有修复序列的个数
	private static int rightCount; // 所有正确修复序列的个数
	private static double rate;// 修复正确的序列占总数的比率
	private static Map<Integer, Trace> finalMap = new HashMap<Integer, Trace>();// 得到map使traceID和修复trace一一对应

	/**
	 * 得到每个traceID对应的修复trace序列
	 * 
	 * @param file
	 *            修复日志的trace序列所在文件
	 * @return
	 */
	private static void getMap(File file) {

		map = new HashMap<Set<Integer>, List<Trace>>();

		try {
			BufferedReader reader = new BufferedReader(new FileReader(file));
			String line = null;
			Set<Integer> intSet = new HashSet<Integer>();
			List<Trace> traceList = new ArrayList<Trace>();

			while ((line = reader.readLine()) != null) {
				if (line.contains("[")) {
					if (intSet.size() != 0) {
						// 重新创建map的key和value，将intSet 和traceList集合中的内容拷贝进去
						Set<Integer> copyInt = new HashSet<Integer>();
						List<Trace> copyTrace = new ArrayList<Trace>();
						copyInt.addAll(intSet);
						copyTrace.addAll(traceList);
						map.put(copyInt, copyTrace);
						intSet.clear();
						traceList.clear();
					}
					int preIndex = line.indexOf("[");
					int postIndex = line.indexOf("]");
					// System.out.println("preIndex = " + preIndex + " postIndex
					// = " + postIndex);
					String subString = line.substring(preIndex + 1, postIndex);
					// System.out.println("substring = " + subString);
					if (subString.contains(",")) {
						String numArr[] = subString.split(",");
						for (String num : numArr) {
							if (num.contains(" ")) {
								num = num.replace(" ", "");
								// System.out.println("num =" + num);
							}
							intSet.add(Integer.parseInt(num));
						}
					} else {
						intSet.add(Integer.parseInt(subString));
					}

				} else {
					String[] arr = line.split(",");
					ArrayList<String> events = new ArrayList<String>();
					for (String string : arr) {
						events.add(string);
					}
					System.out.println(events);
					traceList.add(new Trace(events));
				}
			}
			// 将最后一组加入map中
			Set<Integer> copyInt = new HashSet<Integer>();
			List<Trace> copyTrace = new ArrayList<Trace>();
			copyInt.addAll(intSet);
			copyTrace.addAll(traceList);
			map.put(copyInt, copyTrace);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// System.out.println(map);
	}

	/**
	 * 得到traceID和修复trance序列的一对一
	 * 
	 * @param xesFile
	 *            xesFile为问题日志
	 */
	private static void dropRepeated(File xesFile) {
		ArrayList<Trace> traceList = LogUtil.xesParse(xesFile);
		Set<Set<Integer>> keySet = map.keySet();
		for (int i = 0; i < traceList.size(); i++) {
			List<Trace> list = null; // 得到traceID对应的list
			for (Set<Integer> set : keySet) {
				if (set.contains(i)) {
					list = map.get(set);
					break;
				}
			}
			allCount += list.size(); // 统计所有的修复trace序列的个数
			// 遍历list得到和tracelist[i]编辑距离最近的trace
			int minDistance = 99999999;
			Trace minTrace = null;// 记录距离最小的修复trace
			for (Trace trace : list) {
				int distance = StringDistance.editDistance(trace.getEvents(), traceList.get(i).getEvents());
				if (distance < minDistance) {
					minDistance = distance;
					minTrace = trace;
				}
			}

			// 统计修复正确的trace的个数 rightCount
			for (Trace trace : list) {
				int distance = StringDistance.editDistance(trace.getEvents(), traceList.get(i).getEvents());
				if (distance == minDistance)
					rightCount++;
			}

			// 得到finalMap
			finalMap.put(i, minTrace);
		}

		System.out.println("rightCount = " + rightCount + "        allCount = " + allCount);
		rate = 1.0 * rightCount / allCount;
		System.out.println("修复序列占比：" + myDoubleFormat(rate));
	}

	/**
	 * 
	 * @param originalLog
	 *            原始的log文件（peitri网能够得到所有trace形成的log）
	 * @param errorLog
	 *            有问题的日志
	 * @param repairedFile
	 *            问题日志修复后得到的文件
	 * @return
	 */
	public static void getAccuracy(File originalLog, File errorLog, File repairedFile) {

		double accuracy = 0.0; // 日志修复的正确率
		int right = 0; // 统计原trace和错误trace以及错误trace与修复trace的编辑距离一样的个数
		List<Trace> originalTrace = LogUtil.xesParse(originalLog);
		List<Trace> errorTrace = LogUtil.xesParse(errorLog);
		getMap(repairedFile);
		dropRepeated(errorLog);
		for (int i = 0; i < finalMap.size(); i++) {
			int editDistance1 = StringDistance.editDistance(originalTrace.get(i).getEvents(),
					errorTrace.get(i).getEvents());
			int editDistance2 = StringDistance.editDistance(errorTrace.get(i).getEvents(), finalMap.get(i).getEvents());
			if (editDistance1 == editDistance2)
				right++;
		}
		System.out.println("accuracy------------right = " + right + "    all=" + finalMap.size());
		accuracy = 1.0 * right / finalMap.size();
		System.out.println(errorLog.getName() + "----------------准确率：" + myDoubleFormat(accuracy));
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
