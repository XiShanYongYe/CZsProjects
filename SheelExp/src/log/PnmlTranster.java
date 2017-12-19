package log;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Iterator;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

class Dot {
	public static final int Dot_Place = 1;
	public static final int Dot_Transition = 2;
	public static final int Dot_Arc = 3;

	private int type;
	private String id;
	private String name;
	private String markingValue = null;
	private String x;
	private String y;
	private String source;
	private String target;

	public Dot(int type) {
		setType(type);
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public String getX() {
		return x;
	}

	public String getY() {
		return y;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setX(String x) {
		this.x = x;
	}

	public void setY(String y) {
		this.y = y;
	}

	public String getSource() {
		return source;
	}

	public String getTarget() {
		return target;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public void setTarget(String target) {
		this.target = target;
	}

	public String getMarkingValue() {
		return markingValue;
	}

	public void setMarkingValue(String markingValue) {
		this.markingValue = markingValue;
	}
}

public class PnmlTranster {

	public static void main(String[] args) {

		File file = new File("F:\\常震\\CZ\\Second\\无选择有循环\\1In_bb6y.pnml");
		xml2pnml(file);
		System.out.println("ok");

	}

	public static ArrayList<Dot> readFile(File file) {
		ArrayList<Dot> list = new ArrayList<Dot>();
		Dot dot;
		SAXReader reader = new SAXReader();
		try {
			Document document = reader.read(file);
			Element root = document.getRootElement();
			Element netElement = root.element("net");
			netElement = netElement.element("page");
			Iterator<?> iter1 = netElement.elementIterator("place");
			while (iter1.hasNext()) {
				Element placeElement = (Element) iter1.next();
				dot = new Dot(Dot.Dot_Place);
				dot.setId(placeElement.attributeValue("id"));
				// dot.setX(placeElement.element("graphics").element("position").attributeValue("x"));
				// dot.setY(placeElement.element("graphics").element("position").attributeValue("y"));
				// dot.setName(placeElement.element("name").element("text").getText());
				// dot.setMarkingValue(placeElement.element("initialMarking").element("value").getText());
				// System.out.println(placeElement.element("initialMarking").element("value").getText());
				list.add(dot);
			}

			Iterator<?> iter2 = netElement.elementIterator("transition");
			while (iter2.hasNext()) {
				Element transitionElement = (Element) iter2.next();
				dot = new Dot(Dot.Dot_Transition);
				dot.setId(transitionElement.attributeValue("id"));
				// dot.setX(transitionElement.element("graphics").element("position").attributeValue("x"));
				// dot.setY(transitionElement.element("graphics").element("position").attributeValue("y"));
				// dot.setName(transitionElement.element("name").element("text").getText());
				list.add(dot);
			}

			Iterator<?> iter3 = netElement.elementIterator("arc");
			while (iter3.hasNext()) {
				Element arcElement = (Element) iter3.next();
				dot = new Dot(Dot.Dot_Arc);
				dot.setId(arcElement.attributeValue("id"));
				dot.setSource(arcElement.attributeValue("source"));
				dot.setTarget(arcElement.attributeValue("target"));
				list.add(dot);
			}
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		return list;
	}

	public static void write(ArrayList<Dot> dots, File file) {
		Document doc = DocumentHelper.createDocument();
		Element root = doc.addElement("pnml");
		Element net = root.addElement("net");
		net.addAttribute("id", "net1");
		net.addAttribute("type", "http://www.pnml.org/version-2009/grammar/pnmlcoremodel");
		net.addElement("name").addElement("text").addText(file.getName());
		Element page = net.addElement("page");
		page.addAttribute("id", "n0");
		for (Dot dot : dots) {
			switch (dot.getType()) {
			case Dot.Dot_Place:
				Element place = page.addElement("place");
				place.addAttribute("id", dot.getId());
				// place.addElement("name").addElement("text").addText(dot.getId());
				// Element g1 = place.addElement("graphics");
				// g1.addElement("position").addAttribute("x",
				// dot.getX()).addAttribute("y", dot.getY());
				// g1.addElement("dimension").addAttribute("x",
				// "25").addAttribute("y", "25");
				// if (dot.getMarkingValue().equalsIgnoreCase("Default,1")) {
				// place.addElement("initialMarking").addElement("text").addText("1");
				// }
				break;
			case Dot.Dot_Transition:
				Element transition = page.addElement("transition");
				transition.addAttribute("id", dot.getId());
				// transition.addElement("name").addElement("text").addText(dot.getName());
				// Element g2 = transition.addElement("graphics");
				// g2.addElement("position").addAttribute("x",
				// dot.getX()).addAttribute("y", dot.getY());
				// g2.addElement("dimension").addAttribute("x",
				// "25").addAttribute("y", "25");
				// transition.addElement("fill").addAttribute("color",
				// "#FFFFFF");
				break;
			case Dot.Dot_Arc:
				Element arc = page.addElement("arc");
				arc.addAttribute("id", dot.getId()).addAttribute("source", dot.getSource()).addAttribute("target",
						dot.getTarget());
				arc.addElement("name").addElement("text").addText("1");
				arc.addElement("arctype").addElement("text").addText("normal");
				break;
			}
		}
		OutputFormat format = OutputFormat.createPrettyPrint();
		try {
			XMLWriter writer = new XMLWriter(new FileOutputStream(file), format);
			writer.write(doc);
			writer.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void xml2pnml(File file) {
		ArrayList<Dot> dots = readFile(file);
		write(dots, file);
	}

}
