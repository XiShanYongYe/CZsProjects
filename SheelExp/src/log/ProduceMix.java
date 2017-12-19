package log;

import java.io.File;
import java.util.List;
import java.util.Random;

import structure.EventFlow;

/**
 * 对完备日志中的每条trace进行mix(miss,redundent,switch)，产生对应编辑距离的偏差日志
 * 
 * @author Administrator
 * 
 */
public class ProduceMix {

	private static String dir = "F:/常震/CZ/Second/无选择有循环/";
	private static String firstFile = "1In_bb6y.xes";

	public static void main(String[] args) {

		// 读取文件
		File file = new File(dir + "\\" + firstFile);
		List<EventFlow> allTrace = ReadLog.getTraces(file);

		int distance = 4;// 设置编辑距离

		produce(allTrace, distance);

		for (EventFlow eFlow : allTrace)
			System.out.println(eFlow);

	}

	private static void produce(List<EventFlow> allTrace, int distance) {

		for (EventFlow eventFlow : allTrace)
			mixEvent(eventFlow, distance);

	}

	private static void mixEvent(EventFlow eventFlow, int distance) {

		Random random = new Random();
		int num_miss = random.nextInt(distance + 1);
		int num_switch = random.nextInt(distance + 1 - num_miss);
		int num_redundent = distance - num_miss - num_switch;
		ProduceMiss.misshEvent(eventFlow, num_miss);
		// ProduceSwitch.swichEvent(eventFlow, num_switch);
		ProduceRedundent.redundentEvent(eventFlow, num_redundent);

	}

}
