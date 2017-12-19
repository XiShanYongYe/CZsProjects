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

	private static String dir = "F:/����/CZ/";
	private static String firstFile = "test.xes";

	public static void main(String[] args) {

		// ��ȡ�ļ�
		File file = new File(dir + "\\" + firstFile);

		List<EventFlow> allTrace = ReadLog.getTraces(file);

		int count = 0;
		for (EventFlow eFlow : allTrace)
			// System.out.println("��" + (count++) + "����" + eFlow);
			System.out.println(eFlow);

	}

}
