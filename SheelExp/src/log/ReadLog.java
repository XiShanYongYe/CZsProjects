package log;
import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import structure.EventFlow;

public class ReadLog {

	/**
	 * 获得给出文件file中的所有事件流
	 * 
	 * @param file
	 * @return 获得的所有事件流
	 */
	@SuppressWarnings("unchecked")
	public static List<EventFlow> getTraces(File file) {

		List<EventFlow> traceList = new ArrayList<>();

		SAXReader reader = new SAXReader();

		try {

			Document doc = reader.read(file);

			Element log = doc.getRootElement();

			Iterator<Element> traceIterator = log.elementIterator("trace");

			// 获得该trace下的事件流
			while (traceIterator.hasNext()) {

				Element trace = traceIterator.next();

				EventFlow eventFlow = new EventFlow();

				for (Element event : (List<Element>) trace.elements("event")) {

					List<Element> traceStrings = event.elements("String");

					if (traceStrings.isEmpty()) {// 区分String与string

						traceStrings = event.elements("string");

					}

					for (Element eventString : traceStrings) {

						// 获得event的concept:name名
						if (eventString.attribute("key").getValue()
								.equals("concept:name")) {

							String value = eventString.attributeValue("value");

							eventFlow.add(value);
							break;
						}

					}
				}

				traceList.add(eventFlow);

			}

		} catch (DocumentException e) {

			e.printStackTrace();

		}

		return traceList;

	}
}
