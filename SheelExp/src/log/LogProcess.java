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

	private static String dir = "C:\\Users\\Administrator\\Desktop\\Second\\无选择有循环\\";
	private static String firstFile = "1In_bb6y_redundent_ed6.xes";

	public static void main(String[] args) {

		// 读取文件
		File file = new File(dir + "\\" + firstFile);

		List<EventFlow> allTrace = ReadLog.getTraces(file);

		System.out.println("size:" + allTrace.size());
		for (EventFlow eFlow : allTrace)
			System.out.println(eFlow);

	}

}
