import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class MainTest {
	private static ReadLog readLog;
	private static List<String> eventList;
	private static List<Set<String>> traceeventList;
	private static List<EventFlow> flows;
	private static List<DirectPrecedences> directPrecedences;
	private static List<Causal> cList;
	private static List<Independence> indeList;

	private static String dir = "F:\\常震\\CZ\\Second\\无选择有循环\\";
	private static String filename = "1In_bb6yPer90";
	private static String sourceFile = dir + filename + ".xes";
	private static String dirFile = dir + filename + ".pnml";

	private static MineDirectPrecedenceTest md;
	private static MineThreeRelations mt;

	public static void main(String args[]) {

		cList = new ArrayList<>();
		indeList = new ArrayList<>();

		// 读取日志
		File file = new File(sourceFile);
		readLog = new ReadLog();
		flows = ReadLog.getTraces(file);
		System.out.println("trace nums:" + flows.size());
		eventList = ReadLog.getEvent();// 当前日志所有event
		traceeventList = readLog.getTraceEvent();// 当前日志的每条trace及其包含的event

		long log = System.currentTimeMillis();

		// 算法1，挖掘>关系
		md = new MineDirectPrecedenceTest(flows);
		directPrecedences = md.doMineDirectPrecedence();

		mt = new MineThreeRelations(directPrecedences, eventList);
		cList = mt.getcList();

		indeList = mt.getIndeList();

		new AlphaMiner(eventList, cList, indeList, traceeventList, dirFile);

		long end = System.currentTimeMillis();

		System.out.println("挖掘时间:" + (end - log));
	}

}
