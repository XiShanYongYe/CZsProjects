package log;

import java.io.File;
import java.util.List;

import structure.EventFlow;

/**
 * 读取目标xes文件中的trace
 * 
 * @author Administrator
 * 
 */
public class LogProcess {

	private static String dir = "F:/常震/CZ/";
	private static String firstFile = "test.xes";

	public static void main(String[] args) {

		// 读取文件
		File file = new File(dir + "\\" + firstFile);

		List<EventFlow> allTrace = ReadLog.getTraces(file);

		int count = 0;
		for (EventFlow eFlow : allTrace)
			// System.out.println("第" + (count++) + "个：" + eFlow);
			System.out.println(eFlow);

	}

}
