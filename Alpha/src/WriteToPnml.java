

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;

public class WriteToPnml {
	
	public static void write(ArrayList<Dot> dots,String dirFile) {
		File file = new File(dirFile);
		Document doc = DocumentHelper.createDocument();
		Element root = doc.addElement("pnml");
		Element net = root.addElement("net");
		net.addAttribute("id", "net1");
		net.addAttribute("type",
				"http://www.pnml.org/version-2009/grammar/pnmlcoremodel");
		net.addElement("name").addElement("text").addText(file.getName());
		Element page = net.addElement("page");
		page.addAttribute("id", "n0");
		for (Dot dot : dots) {
			switch (dot.getType()) {
			case Dot.Dot_Place:
				Element place = page.addElement("place");
				place.addAttribute("id", dot.getId());
				place.addElement("name").addElement("text")
						.addText(dot.getId());
//				Element g1 = place.addElement("graphics");
//				g1.addElement("position").addAttribute("x", dot.getX())
//						.addAttribute("y", dot.getY());
//				g1.addElement("dimension").addAttribute("x", "25")
//						.addAttribute("y", "25");
//				if(dot.getMarkingValue().equalsIgnoreCase("Default,1")){
//					place.addElement("initialMarking").addElement("text")
//					.addText("1");
//				}
				break;
			case Dot.Dot_Transition:
				Element transition = page.addElement("transition");
				transition.addAttribute("id", dot.getId());
//				transition.addElement("name").addElement("text")
//						.addText(dot.getName());
//				Element g2 = transition.addElement("graphics");
//				g2.addElement("position").addAttribute("x", dot.getX())
//						.addAttribute("y", dot.getY());
//				g2.addElement("dimension").addAttribute("x", "25")
//						.addAttribute("y", "25");
//				transition.addElement("fill").addAttribute("color", "#FFFFFF");
				break;
			case Dot.Dot_Arc:
				Element arc = page.addElement("arc");
				arc.addAttribute("id", dot.getId())
						.addAttribute("source", dot.getSource())
						.addAttribute("target", dot.getTarget());
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
}
