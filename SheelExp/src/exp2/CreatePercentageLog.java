package exp2;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import log.ReadLog;
import structure.EventFlow;

public class CreatePercentageLog {

	private static String dir = "F:/常震/CZ/Second/无选择有循环";
	private static String firstFile = "1In_bb6y.xes";
	private static double percentage = 0.9f;

	public static void main(String[] args) {
		Random random = new Random();
		File file = new File(dir + "/" + firstFile);
		List<EventFlow> allTrace = ReadLog.getTraces(file);
		int num = (int) (allTrace.size() * percentage);
		List<Integer> indexs = new ArrayList<>();
		int i = 0;
		while (i < num) {
			int index = random.nextInt(allTrace.size());
			if(indexs.contains(index)){
				continue;
			}else{
				indexs.add(index);
				i++;
			}
		}
		for(Integer index:indexs){
			System.out.println(allTrace.get(index));
		}
	}

}
