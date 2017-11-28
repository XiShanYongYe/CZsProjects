package log;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import structure.EventFlow;

/**
 * ���걸��־�е�ÿ��trace����redudent��������Ӧ�༭�����ƫ����־
 * 
 * @author Administrator
 * 
 */
public class ProduceRedundent {

	private static String dir = "C:\\Users\\Administrator\\Desktop\\Second\\��ѡ����ѭ��\\";
	private static String firstFile = "1In_bb6y.xes";

	public static void main(String[] args) {

		// ��ȡ�ļ�
		File file = new File(dir + "\\" + firstFile);
		List<EventFlow> allTrace = ReadLog.getTraces(file);

		int distance = 3;// ���ñ༭����

		produce(allTrace, distance);

		for (EventFlow eFlow : allTrace)
			System.out.println(eFlow);

	}

	private static void produce(List<EventFlow> allTrace, int distance) {

		for (EventFlow eventFlow : allTrace)
			redundentEvent(eventFlow, distance);

	}

	public static void redundentEvent(EventFlow eventFlow, int distance) {

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

		// System.out.println("begin:" + eventFlow);

		for (int i = distance - 1; i >= 0; i--) {
			eventFlow.add(locations[i], eventFlow.get(locations[i]));
		}

		// for (int i = 0; i < distance; i++)
		// System.out.print(locations[i] + " ");
		// System.out.println();
		// System.out.println("end:" + eventFlow);

	}

}
