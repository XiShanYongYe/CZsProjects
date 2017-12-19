

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
 * traceList�д��eventFlow
 * eventFlowҲ��һ��list��������event����
 * event�������value,lifecycle,timestamp
 */

public class ReadLog {


	private static List<String> eventList;//�������log������event
	private static List<Event> EventList;//�������log������Event
	private static List<Set<String>> traceEventList;//set�д�ŵ���ÿ��trace�е�event
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
			Element log = doc.getRootElement(); //����Ŀǰ������־���ǻ�ȡ<Log>
			Iterator<Element> traceIterator = log.elementIterator("trace");//���õ����������<trace>
			while (traceIterator.hasNext()) {
				Element trace = traceIterator.next();	//��ȡÿһ��<trace>
				EventFlow eventFlow = new EventFlow(); //һ��trace�д�Ŷ��event
				Set traceEventSet=new LinkedHashSet<String>();
				//��ȡ�ڵ�event
				for (Element event : (List<Element>) trace.elements("event")) {	//trace���ÿ��eventѭ��	
					Event EVENT=new Event();
					List<Element> traceStrings2 = ((Element) event).elements("String");	//ÿ��event��string��ʼ�Ĵ����list��
					if (traceStrings2.isEmpty()) {
						traceStrings2 = ((Element) event).elements("string");
					}
					List<Element> traceDate = ((Element) event).elements("date");
					for (Element eventString : traceStrings2) {	//��ǰevent��ÿһ������
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
		//��ȡEvent�����ڶ�����trace��
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
