package org.java.multithreading.deadlock;

public class ThreadStarvationExample {

	private static final Object lock = new Object();

	public static void main(String[] args) {
		// High-priority thread hogging the lock
		Thread hogger = new Thread(() -> {
			while (true) {
				synchronized (lock) {
					System.out.println(Thread.currentThread().getName() + " got the lock!");
					try {
						Thread.sleep(50); // hold the lock for a while
					} catch (InterruptedException e) {
						Thread.currentThread().interrupt();
					}
				}
			}
		}, "Hogger-Thread");

		hogger.setPriority(Thread.MAX_PRIORITY);
		hogger.start();

		// Low-priority worker threads
		for (int i = 1; i <= 5; i++) {
			Thread worker = new Thread(() -> {
				while (true) {
					synchronized (lock) {
						System.out.println(Thread.currentThread().getName() + " got the lock!");
					}
					try {
						Thread.sleep(100); // simulate some work
					} catch (InterruptedException e) {
						Thread.currentThread().interrupt();
					}
				}
			}, "Worker-Thread-" + i);

			worker.setPriority(Thread.MIN_PRIORITY);
			worker.start();
		}
	}
}
