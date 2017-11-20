package org.processmining.plugins.dcfe;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.deckfour.xes.model.XEvent;
import org.deckfour.xes.model.XLog;
import org.deckfour.xes.model.XTrace;

public class ReadLog {

	public static List<EventFlow> getTraces(XLog log) {

		List<EventFlow> traceList = new ArrayList<>();

		Iterator<XTrace> iterator = log.listIterator();
		while (iterator.hasNext()) {
			XTrace trace = iterator.next();
			EventFlow eventFlow = new EventFlow();
			for (XEvent event : trace) {
				eventFlow.add(event.getAttributes().get("concept:name").toString());
			}
			traceList.add(eventFlow);
		}
		return traceList;
	}
}
