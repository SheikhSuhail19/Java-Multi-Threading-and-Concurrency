package org.java.multithreading.basics;

public class ThreadYieldExample extends Thread {
	@Override
	public void run() {
		for (int i = 0; i < 5; i++) {
			System.out.println(Thread.currentThread().getName() + " is running...");
			Thread.yield();
		}
	}

	public static void main(String[] args) {
		ThreadYieldExample t1 = new ThreadYieldExample();
		ThreadYieldExample t2 = new ThreadYieldExample();
		t1.start();
		t2.start();
	}
}