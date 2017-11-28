package log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;

import structure.EventFlow;

/**
 * 将含有trace的txt转换成xes文件
 * 
 * @author zhen
 * 
 */
public class CovertTrace2Log {

	private static List<EventFlow> traceList = new ArrayList<EventFlow>();

	private static String sourceFile = "C:\\Users\\Administrator\\Desktop\\Second\\无选择有循环\\";
	private static String fileName = "1In_bb6y_redundent_ed3";
	private static String dirFile = sourceFile + fileName + ".xes";

	public static void main(String[] args) {

		readFileByLines(sourceFile + fileName + ".txt");

	}

	/**
	 * 以行为单位读取文件，常用于读面向行的格式化文件
	 */
	public static void readFileByLines(String fileName) {

		File file = new File(fileName);
		BufferedReader reader = null;

		try {

			reader = new BufferedReader(new FileReader(file));
			String tempString = null;

			// 一次读入一行，直到读入null为文件结束
			while ((tempString = reader.readLine()) != null) {

				String[] ts = tempString.split("->");

				EventFlow eFlow = new EventFlow();

				for (int i = 0; i < ts.length; i++)
					eFlow.add(ts[i]);

				traceList.add(eFlow);

			}

			reader.close();

		} catch (IOException e) {
			e.printStackTrace();
		} finally {

			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e1) {
				}
			}
		}

		File desFile = new File(dirFile);
		WriteToXes(traceList, desFile);

	}

	public static void WriteToXes(List<EventFlow> traceList, File desFile) {

		Document document = DocumentHelper.createDocument();
		Element root = document.addElement("Log");
		for (EventFlow trace : traceList) {
			Element ele_trace = root.addElement("trace");
			Element ele_string = ele_trace.addElement("String");
			ele_string.addAttribute("key", "concept:name");
			ele_string.addAttribute("value", trace.getTraceId());
			for (String event : trace) {
				Element ele_event = ele_trace.addElement("event");
				Element s1 = ele_event.addElement("String");
				s1.addAttribute("key", "org:resource");
				s1.addAttribute("value", "UNDEFINED");
				Element d1 = ele_event.addElement("date");
				d1.addAttribute("key", "time:timestamp");
				d1.addAttribute("value", "2008-12-09T08:21:01.527+01:00");
				Element s2 = ele_event.addElement("String");
				s2.addAttribute("key", "concept:name");
				s2.addAttribute("value", event);
				Element s3 = ele_event.addElement("String");
				s3.addAttribute("key", "lifecycle:transition");
				s3.addAttribute("value", "receive");
				Element s4 = ele_event.addElement("String");
				s4.addAttribute("key", "input");
				s4.addAttribute("value", "null");
				Element s5 = ele_event.addElement("String");
				s5.addAttribute("key", "output");
				s5.addAttribute("value", "null");
			}
		}
		OutputFormat outputFormat = OutputFormat.createPrettyPrint();
		XMLWriter xmlWriter = null;
		try {
			xmlWriter = new XMLWriter(new FileOutputStream(desFile),
					outputFormat);
			xmlWriter.write(document);
		} catch (Exception e) {
			e.printStackTrace();
		}

		System.out.println("转换成功！");

	}

}
