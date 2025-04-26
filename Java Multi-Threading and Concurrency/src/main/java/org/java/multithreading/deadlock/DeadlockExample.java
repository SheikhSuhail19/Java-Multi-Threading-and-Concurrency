package org.java.multithreading.deadlock;

import java.util.concurrent.CountDownLatch;

class Pen {
	public synchronized void write(Paper paper) {
		System.out.println(Thread.currentThread().getName() + " locked Pen and trying to lock Paper...");
		DeadlockExample.sleep(100); // give other thread time to grab Paper
		paper.write();
	}

	public synchronized void write() {
		System.out.println(Thread.currentThread().getName() + " finished writing with Pen.");
	}
}

class Paper {
	public synchronized void write(Pen pen) {
		System.out.println(Thread.currentThread().getName() + " locked Paper and trying to lock Pen...");
		DeadlockExample.sleep(100); // give other thread time to grab Pen
		pen.write();
	}

	public synchronized void write() {
		System.out.println(Thread.currentThread().getName() + " finished writing on Paper.");
	}
}

public class DeadlockExample {

	public static void main(String[] args) {
		Pen pen = new Pen();
		Paper paper = new Paper();
		CountDownLatch latch = new CountDownLatch(1);

		Thread thread1 = new Thread(() -> {
			try {
				latch.await();
				pen.write(paper);
			} catch (InterruptedException ignored) {
			}
		}, "Thread-1");

		Thread thread2 = new Thread(() -> {
			try {
				latch.await();
				paper.write(pen);
			} catch (InterruptedException ignored) {
			}
		}, "Thread-2");

		thread1.start();
		thread2.start();

		System.out.println("Both threads started. Releasing latch...");
		latch.countDown();
	}

	static void sleep(long ms) {
		try {
			Thread.sleep(ms);
		} catch (InterruptedException ignored) {
		}
	}
}
