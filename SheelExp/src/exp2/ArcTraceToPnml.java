package exp2;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;

/**
 * heuristics net手动写trace.txt,程序转换为pnml
 * 
 * @author sqm
 *
 */

public class ArcTraceToPnml {

	private static String sourceFile = "F:/常震/CZ/Second/无选择无循环/";
	private static String fileName = "";
	private static String dirFile = "F:/常震/CZ/Second/无选择无循环/" + "1Pr_10omPer90.pnml";

	private static List<String[]> traceList;
	private static ArrayList<Dot> dots;

	public static void main(String args[]) {

		traceList = new ArrayList<String[]>();
		new ArrayList<String>();
		dots = new ArrayList<>();

		readFileByLines(sourceFile + fileName + ".txt");
		writeToArc();
		write(dots, dirFile);
		System.out.println("转化完成");

	}

	/**
	 * 以行为单位读取文件，常用于读面向行的格式化文件
	 */
	public static void readFileByLines(String fileName) {

		File file = new File(fileName);
		BufferedReader reader = null;

		try {
			reader = new BufferedReader(new FileReader(file));
			String tempString = null;
			while ((tempString = reader.readLine()) != null) {
				String[] ts = tempString.split(":");
				traceList.add(ts);
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e1) {
				}
			}
		}

	}

	/**
	 * 写到pnml
	 */
	public static void writeToArc() {
		int count = 0;
		Set<String> set1 = new HashSet<>();// t
		Set<String> set2 = new HashSet<>();// p
		for (String[] trace : traceList) {
			for (int i = 0; i < trace.length; i++) {
				String source = trace[i].substring(0, trace[i].indexOf("-"));
				String target = trace[i].substring(trace[i].indexOf(">") + 1);
				if (source.startsWith("t") && !set1.contains(source)) {
					Dot dot2 = new Dot(2);
					dot2.setId(source);
					dots.add(dot2);
					set1.add(source);
				}
				if (target.startsWith("p") && !set2.contains(target)) {
					Dot dot2 = new Dot(1);
					dot2.setId(target);
					dots.add(dot2);
					set2.add(target);
				}
				if (source.startsWith("p") && !set2.contains(source)) {
					Dot dot2 = new Dot(1);
					dot2.setId(source);
					dots.add(dot2);
					set2.add(source);
				}
				if (target.startsWith("t") && !set1.contains(target)) {
					Dot dot2 = new Dot(2);
					dot2.setId(target);
					dots.add(dot2);
					set1.add(target);
				}
				Dot dot = new Dot(3);
				dot.setId("arc" + count++);
				dot.setSource(source);
				dot.setTarget(target);
				dots.add(dot);
				System.out.println(source + ";" + target);
			}
		}
	}

	public static void write(ArrayList<Dot> dots, String dirFile) {
		File file = new File(dirFile);
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
				place.addElement("name").addElement("text").addText(dot.getId());
				break;
			case Dot.Dot_Transition:
				Element transition = page.addElement("transition");
				transition.addAttribute("id", dot.getId());
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

}
