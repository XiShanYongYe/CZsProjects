package log;

import java.io.File;
import java.util.List;

import structure.EventFlow;

/**
 * ��ȡĿ��xes�ļ��е�trace
 * 
 * @author Administrator
 * 
 */
public class LogProcess {

	private static String dir = "C:\\Users\\Administrator\\Desktop\\Second\\��ѡ����ѭ��\\";
	private static String firstFile = "1In_bb6y_redundent_ed6.xes";

	public static void main(String[] args) {

		// ��ȡ�ļ�
		File file = new File(dir + "\\" + firstFile);

		List<EventFlow> allTrace = ReadLog.getTraces(file);

		System.out.println("size:" + allTrace.size());
		for (EventFlow eFlow : allTrace)
			System.out.println(eFlow);

	}

}
