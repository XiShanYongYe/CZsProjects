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
	 * ��ø����ļ�file�е������¼���
	 * 
	 * @param file
	 * @return ��õ������¼���
	 */
	@SuppressWarnings("unchecked")
	public static List<EventFlow> getTraces(File file) {

		List<EventFlow> traceList = new ArrayList<>();

		SAXReader reader = new SAXReader();

		try {

			Document doc = reader.read(file);

			Element log = doc.getRootElement();

			Iterator<Element> traceIterator = log.elementIterator("trace");

			// ��ø�trace�µ��¼���
			while (traceIterator.hasNext()) {

				Element trace = traceIterator.next();

				EventFlow eventFlow = new EventFlow();

				for (Element event : (List<Element>) trace.elements("event")) {

					List<Element> traceStrings = event.elements("String");

					if (traceStrings.isEmpty()) {// ����String��string

						traceStrings = event.elements("string");

					}

					for (Element eventString : traceStrings) {

						// ���event��concept:name��
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
