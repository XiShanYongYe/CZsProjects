package org.nust.heroine.test;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;



public class TestCallable {
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static void main(String args[]){
		System.out.println("xxx");
		Callable<Data> c1 = new MyCall("task1");
		Callable<Data> c2 = new MyCall("task2");
		
		ExecutorService pool = Executors.newFixedThreadPool(2);
		
		List<Future<Data>> lists = new ArrayList<Future<Data>>();
		
		lists.add(pool.submit(c1));
		lists.add(pool.submit(c2));
		
		for(Future<Data> f:lists){
			try {
				System.out.println("-------------------");
				System.out.println(f.get().getName()+"  "+f.get().getTime());
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ExecutionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
}
class MyCall implements  Callable<Data>{
	private String taskId;
	public MyCall(String taskId) {
		this.taskId = taskId;
	}
	
	@Override
	public Data call() throws Exception {
		
		long startTime = System.nanoTime();
		System.out.println(taskId+"��������");
		Thread.sleep(1000);
		long endTime = System.nanoTime();
		Data data = new Data(taskId,(endTime-startTime)/1000000.0);
		return data;
	}
}
class Data{
	private String name;
	private double time;
	public Data(String name, double time) {
		super();
		this.name = name;
		this.time = time;
	}
	public String getName() {
		return name;
	}
	public double getTime() {
		return time;
	}
	public void setName(String name) {
		this.name = name;
	}
	public void setTime(double time) {
		this.time = time;
	}
	
}