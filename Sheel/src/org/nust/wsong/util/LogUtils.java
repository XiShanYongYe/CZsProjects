package org.nust.wsong.util;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import org.nust.wsong.model.Trace;

/**
 * 读取日志的工具类
 * @author xxx
 *
 */
public class LogUtils {

	public static List<Trace> xmlParse(File file) {
		List<Trace> traces = null;
	try {
		traces = new ArrayList<Trace>();
		SAXReader reader = new SAXReader();
		Document doc = reader.read(file);
		Element root = doc.getRootElement();//processlog �ڵ�
		Iterator<?> iter = root.elementIterator();//case �ڵ�
			while(iter.hasNext()){
				Trace trace = new Trace();
				Element tr = (Element)iter.next();//activity�ڵ�
//				trace.setID(Integer.parseInt(tr.attribute("id").getValue()));
				Iterator<?> iter1 = tr.elementIterator();
				while(iter1.hasNext()){
					Element activity = (Element)iter1.next();
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
	
	
	public static void xmlWrite (ArrayList<Trace> traces,File file) {
		Document document = DocumentHelper.createDocument();
		Element root = document.addElement("Log");
		int index = 0;
		for(Trace trace:traces){
			Element tr = root.addElement("trace");
			tr.addAttribute("id",index+"");//
			for(String s:trace.getEvents()){
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
	
	public static List<Trace> xesParse(File file) {
		List<Trace> traces = null;
		try {
			traces = new ArrayList<Trace>();
			SAXReader reader = new SAXReader();
			Document doc = reader.read(file);
			Element root = doc.getRootElement();
			Iterator<?> iter = root.elementIterator();
			while(iter.hasNext()){
				Trace trace = new Trace();
				Element ca = (Element)iter.next();
//				trace.setID(Integer.parseInt(ca.attribute("id").getValue()));
				Iterator<?> iter1 = ca.elementIterator();
				while(iter1.hasNext()){
					Element event = (Element)iter1.next();
					Iterator<?> iter2 =event.elementIterator("String");
					while(iter2.hasNext()){
						Element string =(Element) iter2.next();
						if(string.attribute("key").getValue().equalsIgnoreCase("concept:name"))
							trace.append(string.attribute("value").getValue());
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
	
	public static void xesWrite (List<Trace> traces,File file) {
		Document document = DocumentHelper.createDocument();
		Element root = document.addElement("Log");
		int index = 0;
		for(Trace trace:traces){
			Element tr = root.addElement("trace");
			tr.addAttribute("id",index+"");//
			for(String s:trace.getEvents()){
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
	
	public static void writeBack(Document doc,File file){
		OutputFormat format = OutputFormat.createPrettyPrint();
		try {
			XMLWriter writer = new XMLWriter(new FileOutputStream(file),format);
			writer.write(doc);
			writer.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
