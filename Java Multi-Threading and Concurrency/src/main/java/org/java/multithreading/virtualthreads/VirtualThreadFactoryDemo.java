package org.java.multithreading.virtualthreads;

import java.util.concurrent.ThreadFactory;

public class VirtualThreadFactoryDemo {
	public static void main(String[] args) throws InterruptedException {
		ThreadFactory factory = Thread.ofVirtual().factory();

		Thread t1 = factory.newThread(() -> System.out.println("Task 1: " + Thread.currentThread()));
		Thread t2 = factory.newThread(() -> System.out.println("Task 2: " + Thread.currentThread()));

		t1.start();
		t2.start();
		t1.join();
		t2.join();
	}
}