package org.nust.heroine.util;

import java.io.File;
import java.util.Iterator;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.nust.heroine.basicstruct.Arc;
import org.nust.heroine.basicstruct.Node;
import org.nust.heroine.basicstruct.PetriNet;

/**
 * 模型文件pnml解析
 * 
 * @author Administrator
 * 
 */
public class PnmlParser {

	public static PetriNet parse(File file) throws Exception {
		PetriNet petriNet = new PetriNet();
		Node PetriNet_Node;// 节点
		Arc PetriNet_Arc;// 边
		// Data PetriNet_Data;

		SAXReader reader = new SAXReader();
		Document doc = reader.read(file);
		Element root = doc.getRootElement();
		Element net = root.element("net");
		Element page = net.element("page");
		for (Iterator<?> rootIt = page.elementIterator("place"); rootIt
				.hasNext();) {
			/*
			 * for (Iterator<?> rootIt = net.elementIterator("place"); rootIt
			 * .hasNext();) {
			 */
			PetriNet_Node = new Node();
			PetriNet_Node.setType("place");
			// xxx-add
			// petriNet.getPlaces().add(PetriNet_Node);

			Element element = (Element) rootIt.next();
			PetriNet_Node.setID(element.attributeValue("id"));
			for (Iterator<?> it = element.elementIterator("graphics"); it
					.hasNext();) {
				Element e = (Element) it.next();
				for (Iterator<?> lastIt = e.elementIterator("position"); lastIt
						.hasNext();) {
					Element lastElement = (Element) lastIt.next();
					PetriNet_Node.setPX((int) Float.valueOf(
							lastElement.attributeValue("x")).floatValue());
					PetriNet_Node.setPY((int) Float.valueOf(
							lastElement.attributeValue("y")).floatValue());
				}
				for (Iterator<?> lastIt = e.elementIterator("dimension"); lastIt
						.hasNext();) {
					Element lastElement = (Element) lastIt.next();
					PetriNet_Node.setDX((int) Float.valueOf(
							lastElement.attributeValue("x")).floatValue());
					PetriNet_Node.setDY((int) Float.valueOf(
							lastElement.attributeValue("y")).floatValue());
				}
			}
			for (Iterator<?> it = element.elementIterator("name"); it.hasNext();) {
				Element e = (Element) it.next();
				for (Iterator<?> lastIt = e.elementIterator(); lastIt.hasNext();) {
					Element e0 = (Element) lastIt.next();
					if (e0.getName() == "text") {
						PetriNet_Node.setName(e.getStringValue());
					}
				}
			}
			petriNet.addNode(PetriNet_Node);
		}
		for (Iterator<?> rootIt = page.elementIterator("transition"); rootIt
				.hasNext();) {
			/*
			 * for (Iterator<?> rootIt = net.elementIterator("transition");
			 * rootIt .hasNext();) {
			 */
			PetriNet_Node = new Node();
			PetriNet_Node.setType("transition");
			// xxx-add
			// petriNet.getTransitons().add(PetriNet_Node);
			Element element = (Element) rootIt.next();
			PetriNet_Node.setID(element.attributeValue("id"));
			for (Iterator<?> it = element.elementIterator("graphics"); it
					.hasNext();) {
				Element e = (Element) it.next();
				for (Iterator<?> lastIt = e.elementIterator("position"); lastIt
						.hasNext();) {
					Element lastElement = (Element) lastIt.next();
					PetriNet_Node.setPX((int) Float.valueOf(
							lastElement.attributeValue("x")).floatValue());
					PetriNet_Node.setPY((int) Float.valueOf(
							lastElement.attributeValue("y")).floatValue());
				}
				for (Iterator<?> lastIt = e.elementIterator("dimension"); lastIt
						.hasNext();) {
					Element lastElement = (Element) lastIt.next();
					PetriNet_Node.setDX((int) Float.valueOf(
							lastElement.attributeValue("x")).floatValue());
					PetriNet_Node.setDY((int) Float.valueOf(
							lastElement.attributeValue("y")).floatValue());
				}
			}
			for (Iterator<?> it = element.elementIterator("name"); it.hasNext();) {
				Element e = (Element) it.next();
				for (Iterator<?> lastIt = e.elementIterator(); lastIt.hasNext();) {
					Element e0 = (Element) lastIt.next();
					if (e0.getName() == "text") {
						PetriNet_Node.setName(e.getStringValue());
					}
				}
			}
			petriNet.addNode(PetriNet_Node);
		}
		for (Iterator<?> rootIt = page.elementIterator("arc"); rootIt.hasNext();) {
			/*
			 * for (Iterator<?> rootIt = net.elementIterator("arc"); rootIt
			 * .hasNext();) {
			 */
			PetriNet_Arc = new Arc();
			Element element = (Element) rootIt.next();
			PetriNet_Arc.setID(element.attributeValue("id"));
			for (int i = 0; i < petriNet.getNodes().size(); i++) {
				if (petriNet.getNodes().get(i).getID()
						.equalsIgnoreCase(element.attributeValue("source"))) {
					Node n = petriNet.getNodes().get(i);
					PetriNet_Arc.setSource(n);
					// n.setOutdegree(n.getOutdegree()+1);

				}
				if (petriNet.getNodes().get(i).getID()
						.equalsIgnoreCase(element.attributeValue("target"))) {
					Node n = petriNet.getNodes().get(i);
					PetriNet_Arc.setTarget(n);
					// n.setIndegree(n.getIndegree()+1);
				}
			}
			for (Iterator<?> it = element.elementIterator("toolspecific"); it
					.hasNext();) {
				Element e = (Element) it.next();
				for (Iterator<?> lastIt = e.elementIterator("spline"); lastIt
						.hasNext();) {
					Element lastElement = (Element) lastIt.next();
					int i = 0;
					for (Iterator<?> last = lastElement
							.elementIterator("point"); last.hasNext();) {
						Element el = (Element) last.next();
						for (int j = i; j < 100; j++) {
							PetriNet_Arc.setSplineX(j,
									(int) Float.valueOf(el.attributeValue("x"))
											.floatValue());
							PetriNet_Arc.setSplineY(j,
									(int) Float.valueOf(el.attributeValue("y"))
											.floatValue());
						}
						i++;
					}
					for (Iterator<?> last = lastElement.elementIterator("end"); last
							.hasNext();) {
						Element el = (Element) last.next();
						PetriNet_Arc.setEndX((int) Float.valueOf(
								el.attributeValue("x")).floatValue());
						PetriNet_Arc.setEndY((int) Float.valueOf(
								el.attributeValue("y")).floatValue());
					}
				}
			}
			petriNet.addArc(PetriNet_Arc);
		}
		/*
		 * for (Iterator<?> rootIt = page.elementIterator("data"); rootIt
		 * .hasNext();) { PetriNet_Data = new Data(); Element element =
		 * (Element) rootIt.next();
		 * PetriNet_Data.setID(element.attributeValue("id")); for (Iterator<?>
		 * it = element.elementIterator(); it.hasNext();) { Element e =
		 * (Element) it.next(); if (e.getName() == "name") {
		 * PetriNet_Data.setName(e.getStringValue()); } else if (e.getName() ==
		 * "readby") { for (int i = 0; i < petriNet.getNodes().size(); i++) {
		 * for (int k = 0; k < e.getStringValue().split(",").length; k++) { if
		 * (petriNet .getNodes() .get(i) .getID() .equalsIgnoreCase(
		 * e.getStringValue().split(",")[k])) {
		 * PetriNet_Data.addReadby(petriNet.getNodes() .get(i)); } } } } else if
		 * (e.getName() == "writtenby") { for (int i = 0; i <
		 * petriNet.getNodes().size(); i++) { for (int k = 0; k <
		 * e.getStringValue().split(",").length; k++) { if (petriNet .getNodes()
		 * .get(i) .getID() .equalsIgnoreCase(
		 * e.getStringValue().split(",")[k])) {
		 * PetriNet_Data.addWrittenby(petriNet .getNodes().get(i)); } } } } } //
		 * petriNet.addData(PetriNet_Data); }
		 */

		return petriNet;
	}

}
