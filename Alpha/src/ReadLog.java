

import java.io.File;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
/*
 * traceList中存放eventFlow
 * eventFlow也是一个list，里面存放event对象
 * event对象包含value,lifecycle,timestamp
 */

public class ReadLog {


	private static List<String> eventList;//存放整个log的所有event
	private static List<Event> EventList;//存放整个log的所有Event
	private static List<Set<String>> traceEventList;//set中存放的是每条trace中的event
	@SuppressWarnings("unchecked")
	public static List<EventFlow> getTraces(File file) {
		traceEventList=new ArrayList<Set<String>>();
		eventList=new ArrayList<String>();
		EventList=new ArrayList<Event>();
		Set<Event> eventSet=new HashSet<Event>();
		List<EventFlow> traceList = new ArrayList<EventFlow>();
		SAXReader reader = new SAXReader();
		try {
			Document doc = reader.read(file);
			Element log = doc.getRootElement(); //对于目前所用日志，是获取<Log>
			Iterator<Element> traceIterator = log.elementIterator("trace");//设置迭代器，设别<trace>
			while (traceIterator.hasNext()) {
				Element trace = traceIterator.next();	//获取每一条<trace>
				EventFlow eventFlow = new EventFlow(); //一条trace中存放多个event
				Set traceEventSet=new LinkedHashSet<String>();
				//获取节点event
				for (Element event : (List<Element>) trace.elements("event")) {	//trace里的每个event循环	
					Event EVENT=new Event();
					List<Element> traceStrings2 = ((Element) event).elements("String");	//每个event的string开始的存放在list中
					if (traceStrings2.isEmpty()) {
						traceStrings2 = ((Element) event).elements("string");
					}
					List<Element> traceDate = ((Element) event).elements("date");
					for (Element eventString : traceStrings2) {	//当前event的每一个属性
						if (eventString.attribute("key").getValue().equals("concept:name")) {
							if(!eventList.contains(eventString.attribute("value").getValue()))
								eventList.add(eventString.attribute("value").getValue());
							if(!traceEventSet.contains(eventString.attribute("value").getValue()))
								traceEventSet.add(eventString.attribute("value").getValue());
							EVENT.setValue(eventString.attribute("value").getValue());
						}
						if(eventString.attribute("key").getValue().equals("lifecycle:transition")){
							EVENT.setLifecycle(eventString.attribute("value").getValue());
						}						
					}
					for (Element eventString : traceDate) {
						if(eventString.attribute("key").getValue().equals("time:timestamp")){
							String date = eventString.attribute("value").getValue().substring(0, 23);
							date=date.replace('T', ' ');
							Timestamp ts = Timestamp.valueOf(date);
							EVENT.setTimestamp(ts);
						}
					
					}
					eventFlow.add(EVENT);	
					if(!eventSet.contains(EVENT)){
						eventSet.add(EVENT);
					}
				}
				traceEventList.add(traceEventSet);
				traceList.add(eventFlow);
			}
			EventList.addAll(eventSet);

		} catch (DocumentException e) {
			e.printStackTrace();
		}
		return traceList;
	}
	public List<Set<String>> getTraceEvent(){
		return traceEventList;
	}
	public static List<String> getEvent(){
		return eventList;
	}
	public static List<Event> getEvents(){
		//获取Event出现在多少条trace中
		for (int i = 0; i < EventList.size(); i++) {
			for (int j = 0; j < traceEventList.size(); j++) {
				if (traceEventList.get(j).contains(EventList.get(i).getValue())) {
					EventList.get(i).setCountTrace(EventList.get(i).getCountTrace()+1);
				}
			}
		}
		return EventList;
	}

}
