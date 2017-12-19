package log;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import structure.EventFlow;

/**
 * 对完备日志中的每条trace进行switch，产生对应编辑距离的偏差日志
 * 
 * @author Administrator
 * 
 */
public class ProduceSwitch {

	private static String dir = "F:/常震/CZ/Second/无选择有循环/";
	private static String firstFile = "1In_bb6y.xes";

	public static void main(String[] args) {

		// 读取文件
		File file = new File(dir + "\\" + firstFile);
		List<EventFlow> allTrace = ReadLog.getTraces(file);

		int distance = 1;// 设置编辑距离

		produce(allTrace, distance);

		for (EventFlow eFlow : allTrace)
			System.out.println(eFlow);

	}

	private static void produce(List<EventFlow> allTrace, int distance) {

		for (EventFlow eventFlow : allTrace)
			swichEvent(eventFlow, distance);

	}

	public static void swichEvent(EventFlow eventFlow, int distance) {

		List<Integer> sites = new ArrayList<>();
		Random random = new Random();
		int target = 0;
		while (target < distance) {
			int site = random.nextInt(eventFlow.size() - 1);
			if (sites.contains(site)) {
				continue;
			} else {
				sites.add(site);
				target++;
			}
		}

		int locations[] = new int[distance];
		for (int i = 0; i < distance; i++)
			locations[i] = sites.get(i);
		Arrays.sort(locations);

		// for(int i=0;i<distance;i++)
		// System.out.print(locations[i]+" ");
		// System.out.println();

		for (int i = 0; i < distance; i++) {
			String temp = eventFlow.get(locations[i]);
			eventFlow.set(locations[i], eventFlow.get(locations[i] + 1));
			eventFlow.set(locations[i] + 1, temp);
		}

	}

}
