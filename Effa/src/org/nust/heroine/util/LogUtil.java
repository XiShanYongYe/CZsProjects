package org.nust.heroine.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Random;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import org.nust.heroine.basicstruct.PetriNet;
import org.nust.heroine.basicstruct.Trace;

/**
 * 日志工具
 * 
 * @author Administrator
 * 
 */
public class LogUtil {

	public static ArrayList<Trace> xmlParse(File file) {
		ArrayList<Trace> traces = null;
		try {
			traces = new ArrayList<Trace>();
			SAXReader reader = new SAXReader();
			Document doc = reader.read(file);
			Element root = doc.getRootElement();
			Iterator<?> iter = root.elementIterator();
			while (iter.hasNext()) {
				Trace trace = new Trace();
				Element tr = (Element) iter.next();
				// trace.setID(Integer.parseInt(tr.attribute("id").getValue()));
				Iterator<?> iter1 = tr.elementIterator();
				while (iter1.hasNext()) {
					Element activity = (Element) iter1.next();
					trace.getEvents().add(activity.elementText("ID"));
				}
				System.out.print(" ");
				traces.add(trace);
			}
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		return traces;
	}

	public static void xmlWrite(ArrayList<Trace> traces, File file) {
		Document document = DocumentHelper.createDocument();
		Element root = document.addElement("Log");
		int index = 0;
		for (Trace trace : traces) {
			Element tr = root.addElement("trace");
			tr.addAttribute("id", index + "");
			for (String s : trace.getEvents()) {
				Element event = tr.addElement("event");
				Element s1 = event.addElement("String");
				s1.addAttribute("key", "org:resource");
				s1.addAttribute("value", "UNDEFINED");
				Element d1 = event.addElement("date");
				d1.addAttribute("key", "time:timestamp");
				d1.addAttribute("value", "2008-12-09T08:21:01.527+01:00");
				Element s2 = event.addElement("String");
				s2.addAttribute("key", "concept:name");
				s2.addAttribute("value", s);
				Element s3 = event.addElement("String");
				s3.addAttribute("key", "lifecycle:transition");
				s3.addAttribute("value", "receive");
				Element s4 = event.addElement("String");
				s4.addAttribute("key", "input");
				s4.addAttribute("value", "null");
				Element s5 = event.addElement("String");
				s5.addAttribute("key", "output");
				s5.addAttribute("value", "null");
			}
			index++;
		}
		writeBack(document, file);
	}

	/**
	 * 事件日志trace获取
	 * 
	 * @param file
	 * @return
	 */
	public static ArrayList<Trace> xesParse(File file) {
		ArrayList<Trace> traces = null;
		try {
			traces = new ArrayList<Trace>();
			SAXReader reader = new SAXReader();
			Document doc = reader.read(file);
			Element root = doc.getRootElement();
			Iterator<?> iter = root.elementIterator();
			while (iter.hasNext()) {
				Trace trace = new Trace();
				Element ca = (Element) iter.next();
				// trace.setID(Integer.parseInt(ca.attribute("id").getValue()));
				Iterator<?> iter1 = ca.elementIterator();
				while (iter1.hasNext()) {
					Element event = (Element) iter1.next();
					Iterator<?> iter2 = event.elementIterator("String");
					while (iter2.hasNext()) {
						Element string = (Element) iter2.next();
						if (string.attribute("key").getValue()
								.equalsIgnoreCase("concept:name"))
							trace.getEvents().add(
									string.attribute("value").getValue());
					}
				}
				traces.add(trace);
			}
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		return traces;
	}

	public static void xesWrite(ArrayList<Trace> traces, File file) {
		Document document = DocumentHelper.createDocument();
		Element root = document.addElement("Log");
		int index = 0;
		for (Trace trace : traces) {
			Element tr = root.addElement("trace");
			tr.addAttribute("id", index + "");
			for (String s : trace.getEvents()) {
				Element event = tr.addElement("event");
				Element s1 = event.addElement("String");
				s1.addAttribute("key", "org:resource");
				s1.addAttribute("value", "UNDEFINED");
				Element d1 = event.addElement("date");
				d1.addAttribute("key", "time:timestamp");
				d1.addAttribute("value", "2008-12-09T08:21:01.527+01:00");
				Element s2 = event.addElement("String");
				s2.addAttribute("key", "concept:name");
				s2.addAttribute("value", s);
				Element s3 = event.addElement("String");
				s3.addAttribute("key", "lifecycle:transition");
				s3.addAttribute("value", "receive");
				Element s4 = event.addElement("String");
				s4.addAttribute("key", "input");
				s4.addAttribute("value", "null");
				Element s5 = event.addElement("String");
				s5.addAttribute("key", "output");
				s5.addAttribute("value", "null");
			}
			index++;
		}
		writeBack(document, file);
	}

	public static void writeBack(Document doc, File file) {
		OutputFormat format = OutputFormat.createPrettyPrint();
		try {
			XMLWriter writer = new XMLWriter(new FileOutputStream(file), format);
			writer.write(doc);
			writer.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void xml2xes(File src, File des) {
		try {
			des.createNewFile();
			ArrayList<Trace> traces = xmlParse(src);
			xesWrite(traces, des);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static Trace logMiss(Trace trace, double missingRate) {
		Trace newTrace = null;
		try {
			newTrace = trace.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		HashSet<Integer> set = new HashSet<Integer>();
		int missingNum = (int) (trace.getEvents().size() * missingRate);
		int temp;
		Random rand = new Random();
		while (set.size() < missingNum) {
			temp = rand.nextInt(trace.getEvents().size());
			set.add(temp);
		}
		Iterator<String> iter = newTrace.getEvents().iterator();
		int i = 0;
		while (iter.hasNext()) {
			iter.next();
			if (set.contains(i)) {
				iter.remove();
			}
			i++;
		}
		return newTrace;
	}

	public static Trace logRedundant(Trace trace, double rate) {
		Trace newTrace = null;
		try {
			newTrace = trace.clone();
			System.out.println("newTrace before" + newTrace);
			int redundantNum = (int) (trace.getEvents().size() * rate);
			int num1;
			int num2;
			int count = 0;
			Random rand = new Random();
			while (count < redundantNum) {
				num1 = rand.nextInt(trace.getEvents().size());
				num2 = rand.nextInt(newTrace.getEvents().size());
				String temp = newTrace.getEvents().get(num1);
				newTrace.getEvents().add(num2, temp);
				count++;
			}
			System.out.println("newTrace after" + newTrace);
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		return newTrace;
	}

	public static Trace logRedundant1(PetriNet petriNet, Trace trace,
			double rate) {
		Trace newTrace = null;
		try {
			// newTrace = trace.clone();
			// System.out.println("newTrace before"+newTrace);
			// int length = trace.getEvents().size();
			// int num = (int) ((rate*10*length)/(10-rate*10));
			// int count = 0;
			// Random rand = new Random();
			// while(count< num){
			// int num1 = rand.nextInt(length);//���ȡһ��Ԫ��
			// int num2 = rand.nextInt(newTrace.getEvents().size());//���ѡһ��λ��
			// String temp = newTrace.getEvents().get(num1);
			// newTrace.getEvents().add(num2, temp);
			// count++;
			// }
			// System.out.println("newTrace after"+newTrace);
			newTrace = trace.clone();
			System.out.println("newTrace before" + newTrace);
			int length = trace.getEvents().size();
			int num = (int) ((rate * 10 * length) / (10 - rate * 10));
			int count = 0;
			Random rand = new Random();
			while (count < num) {
				int num1 = rand.nextInt(petriNet.getTransitons().size());// ���ȡһ��Ԫ��
				// String temp = trace.getEvents().get(num1);
				String temp = petriNet.getTransitons().get(num1).getID();
				int num2 = rand.nextInt(newTrace.getEvents().size());// ���ѡһ��λ��
				// int num3 = rand.nextInt(10);
				// if(num3>=8)
				// newTrace.getEvents().add(num2,"Ttest");
				// else {
				newTrace.getEvents().add(num2, temp);
				// }
				count++;
			}
			System.out.println("newTrace after" + newTrace);
			System.out
					.println("-------------------------------------------------------------");
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		return newTrace;
	}

	public static Trace logRedundant2(Trace trace, double rate) {
		Trace newTrace = null;
		try {
			newTrace = trace.clone();
			System.out.println("newTrace before" + newTrace);
			System.out.println(newTrace.getEvents().size());
			int length = trace.getEvents().size();
			int num = (int) ((rate * 10 * length) / (10 - rate * 10));
			// int num1;
			int num2;
			int count = 0;
			Random rand = new Random();
			while (count < num) {
				// num1 = rand.nextInt(trace.getEvents().size());//���ȡһ��Ԫ��
				num2 = rand.nextInt(length);// ���ѡһ��λ��
				// String temp = newTrace.getEvents().get(num1);
				String temp = "Ttest";
				newTrace.getEvents().add(num2, temp);
				count++;
			}
			System.out.println("newTrace after" + newTrace);
			System.out.println(newTrace.getEvents().size());
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		return newTrace;
	}

	public static Trace logSwap(Trace trace, int swapRate) {
		Trace newTrace = null;

		int rate = 0;
		do {
			Random r = new Random();
			int c = r.nextInt();
			if (c % 10 < 5) {
				newTrace = logRandom(trace);
			} else if (c % 10 < 9) {
				newTrace = logReverse(trace);
				newTrace = logRandom(newTrace);
			} else if (c % 10 == 9) {
				newTrace = logReverse(trace);
			}
			rate = caculateSwapRate(trace, newTrace);
		} while (Math.abs(rate - swapRate) != 0);// Math.abs(rate-swapRate)>=3
													// //rate != swapRate
		System.out.println("rate" + rate);
		return newTrace;
	}

	public static Trace logRandom(Trace trace) {
		Trace newTrace = null;
		try {
			newTrace = trace.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		int size = trace.getEvents().size();
		Random random = new Random();
		int num = random.nextInt(size / 2);
		int count = 0;
		while (count < num) {
			int num1 = random.nextInt(size);
			int num2 = random.nextInt(size);
			String temp = newTrace.getEvents().get(num1);
			newTrace.getEvents().set(num1, newTrace.getEvents().get(num2));
			newTrace.getEvents().set(num2, temp);
			count++;
		}
		return newTrace;
	}

	public static int caculateSwapRate(Trace trace, Trace newTrace) {
		int count = 0;
		int size = trace.getEvents().size();
		for (int i = 0; i < size - 1; i++) {
			String s1 = newTrace.getEvents().get(i);
			for (int j = i + 1; j < size; j++) {
				String s2 = newTrace.getEvents().get(j);
				int i1 = trace.getEvents().indexOf(s1);
				int i2 = trace.getEvents().indexOf(s2);
				if (i1 > i2) {
					count++;
				}
			}
		}
		// System.out.println("count"+count);
		// System.out.println("size"+size);
		double result = 1.0 * count / ((size * (size - 1)) / 2);
		NumberFormat nf = NumberFormat.getInstance();
		nf.setMaximumFractionDigits(1);
		result = Double.parseDouble(nf.format(result));
		int temp = (int) (result * 10);
		System.out.println("temp" + temp);
		return temp;
	}

	public static Trace logReverse(Trace trace) {
		Trace newTrace = new Trace();
		ArrayList<String> temp = new ArrayList<String>();
		int size = trace.getEvents().size();
		for (int i = size - 1; i >= 0; i--) {
			temp.add(trace.getEvents().get(i));
		}
		newTrace.setEvents(temp);
		return newTrace;
	}
}
