package org.nust.heroine.tempTest;

import java.io.File;
import java.text.DecimalFormat;
import java.util.List;

import javax.swing.JFileChooser;

import org.nust.heroine.basicstruct.PetriNet;
import org.nust.heroine.basicstruct.Trace;
import org.nust.heroine.recovery.Recovery;
import org.nust.heroine.util.Decompose;
import org.nust.heroine.util.LogUtil;
import org.nust.heroine.util.PnmlParser;
import org.nust.heroine.util.StringDistance;
import org.nust.heroine.view.MyJFileChooser;

public class test {

	/**
	 * @param args
	 */
	
	
	/*static List<Trace> cTraces;
	static List<Trace> dTraces;
	static List<Trace> repairedTraces;
	
	static PetriNet petriNet;
	static List<PetriNet> petriNets;
	
	public static void main(String[] args) {


		String deviateFilepath = "C:\\Users\\lenovo-p\\Desktop\\problem\\偏差日志2.xes";
		String pnmlFilePath = "C:\\Users\\lenovo-p\\Desktop\\problem\\PP.109_1.pnml";
		String compliantFilePath = "C:\\Users\\lenovo-p\\Desktop\\problem\\原始日志2.xes";

		File comliantFile = new File(compliantFilePath);
		cTraces= logParse(comliantFile);
		System.out.println("compliant traces:");
		for (int i = 0; i < cTraces.size(); i++) {
			Trace trace = cTraces.get(i);
			System.out.println( trace.getEvents());
		}
		
		File deviateFile = new File(deviateFilepath);
		dTraces = logParse(deviateFile);
		System.out.println("deviated traces:");
		for (int i = 0; i < dTraces.size(); i++) {
			Trace trace = dTraces.get(i);
			System.out.println(trace.getEvents());
		}
	
		try {
			File pnmlFile = new File(pnmlFilePath);
			petriNet = PnmlParser.parse(pnmlFile);
			petriNet.computePetri();
			petriNet.initTransMap();

			// PetriNetFrame frame = new PetriNetFrame("�ֽ�ǰ",
			// Recovery.petriNet);
			// frame.launchFrame();//��ʾ

			System.out.println("pnml文件");
			petriNets = Decompose.dec(PnmlParser.parse(pnmlFile));
			for (int i = 0; i < petriNets.size(); i++) {
				petriNets.get(i).computePetri();
			}	
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
		System.out.println("开始修复");
		recovery();
		System.out.println("修复完成");
	}
	
	public static List<Trace> logParse(File file) {
		List<Trace> traceSet = LogUtil.xesParse(file);
		return traceSet;
	}
	
	public static void recovery() {
		// try {
		// Thread.sleep(1000);
		// } catch (InterruptedException e) {
		// e.printStackTrace();
		// }

		Recovery recovery = new Recovery(petriNet, petriNets);
		long start = System.nanoTime();
		repairedTraces = recovery.recovery(dTraces);
		long end = System.nanoTime();
		long time = end - start;
		// for(int i = 0;i<traceSet.size();i++){
		//// Trace pre = traceSet.get(i);
		// System.out.print(traceSet.get(i).getEvents().size()+" ");
		// System.out.println("�޸�ǰ��"+traceSet.get(i));
		// System.out.print(result.get(i).getEvents().size()+" ");
		// System.out.println("�޸���"+result.get(i));
		//// System.out.println(LogUtil.caculateSwapRate(pre,
		// preTraceSets.get(0).get(i)));
		// System.out.println("--------------------------------------------");
		// }

		// 1.14
//		System.out.println("test-----------------");
		for (int i = 0; i < dTraces.size(); i++) {
			System.out.println(repairedTraces.get(i));
		}
//		System.out.println("test-----------------");

		System.out.println(time / 1000000.0);
		calculateAccuracy();
		// System.out.println("耗时"+time/1000000.0+"ms");
		// caculateRate();
		// LogUtil.xesWrite(result, new File(""));

		// 写回
		// LogUtil.xesWrite(result,
		// new
		// File(logFile.getParent()+File.separator+logFile.getName().substring(0,
		// logFile.getName().lastIndexOf("."))+"_recovery.xes"));
	}

	private static void calculateAccuracy() {
		int right = 0;
		double rate ;
		for(int i=0;i<cTraces.size();i++){
			int editcost1 = StringDistance.editDistance(cTraces.get(i).getEvents(), dTraces.get(i).getEvents());
			System.out.println(editcost1);
			int editcost2 = StringDistance.editDistance(repairedTraces.get(i).getEvents(), dTraces.get(i).getEvents());
//			System.out.println("editcost1:"+editcost1+", editcost2:"+editcost2);
			//等价
			if(editcost1 == editcost2){
				System.out.println("right");
				right++;
			}else {
				System.out.println("wrong");
			}
			
		}
		System.out.println("right = "+right+"     all = "+cTraces.size());
		DecimalFormat format = new DecimalFormat("#0.0000");
		rate = right*1.0/cTraces.size();
//		rate = Double.parseDouble(format.format(rate));
//		rate = rate*100;
//		System.out.println("正确率:"+rate+"%");
		System.out.println("正确率:"+format.format(rate));
	}*/
	

}
