package org.nust.wsong.test;
class MyThread extends Thread{
	private int i;
	public MyThread(int i) {
		this.i = i;
	}
	@Override
	public void run() {
//		super.run();
		System.out.println(i);
	}
}
public class ThreadTest {
	public static void main(String[] args) {
		for(int i=0;i<10;i++){
			new MyThread(i).start();
		}
//		new MyThread(i)
	}
}
