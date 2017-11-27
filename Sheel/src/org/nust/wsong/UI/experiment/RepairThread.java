package org.nust.wsong.UI.experiment;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.nust.wsong.algorithm.Algorithm1;
import org.nust.wsong.model.Trace;
import org.nust.wsong.util.LogUtils;

/**
 * 修复线程
 * @author xxx
 *
 */
public class RepairThread extends Thread{
	
	private static List<Thread> runningThreads = new ArrayList<>();
	
	/**
	 * compliant trace file
	 */
	private File file1;
	
	/**
	 * deviated trace file
	 */
	private File file2;
	
	/**
	 * compliant traces
	 */
	private List<Trace> cTraces;
	
	/**
	 * deviated trace
	 */
	private List<Trace> dTraces;
	
	/**
	 * 用于存储实验时的数据
	 */
	private List<String> dataList;
	/**
	 * 
	 * @param compliant traces file1
	 * @param deviated trace file2
	 * @param dataList
	 */
	public RepairThread(File file1,File file2,List<String> dataList) {
		this.file1  = file1;
		this.file2 = file2;
		
		cTraces = LogUtils.xesParse(file1);
		dTraces = LogUtils.xesParse(file2);
		this.dataList = dataList;
	}
	
	
	@Override
	public void run() {
		register(this);
		StringBuilder sb = new StringBuilder();
		Algorithm1 algorithm1 = new Algorithm1();
		algorithm1.traceCluster(cTraces);
		
		try {
			sleep(1000);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		
		System.out.println(Thread.currentThread().getName() + "开始修复...");
		long start = System.nanoTime();
		List<Trace> repairedTraces = algorithm1.recovery(dTraces);
		System.out.println(Thread.currentThread().getName() + "结束修复");
		long end = System.nanoTime();
		long timeConsumed = end - start;
		sb.append(file1.getName()+",");
		sb.append(file2.getName()+",");
		sb.append(timeConsumed/1000000.0);
		dataList.add(sb.toString());
		


		unRegister(this);
		
		}  
	
	public void register(Thread t){  
	    synchronized(runningThreads){   
	        runningThreads.add(t);  
	    }  
	}  
	public void unRegister(Thread t){  
	    synchronized(runningThreads){   
	        runningThreads.remove(t);  
	    }  
	}  
	public static boolean hasThreadRunning() {  
		return (runningThreads.size() > 0);  
	}  
}
