package org.java.multithreading.basics;


public class DaemonThread extends Thread {
	@Override
	public void run() {
		while (true) {
			System.out.println("Hello world! ");
		}
	}

	public static void main(String[] args) {
		DaemonThread daemonThread = new DaemonThread();
		daemonThread.setDaemon(true); // daemonThread is now a daemon thread (like Garbage collector)
		DaemonThread t1 = new DaemonThread();
//		t1.start(); // t1 is user thread
		daemonThread.start();
		daemonThread.setDaemon(false);
		System.out.println("Main Done");
	}
}