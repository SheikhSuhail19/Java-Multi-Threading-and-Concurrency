package org.java.multithreading.synchronizationaid;

import java.util.concurrent.Semaphore;

public class SemaphoreExample {
	private static final int MAX_PERMITS = 3;
	private static final Semaphore semaphore = new Semaphore(MAX_PERMITS);
	// private static Semaphore semaphore = new Semaphore(3, true); -> Fairness true

	public static void main(String[] args) {
		for (int i = 1; i <= 6; i++) {
			new Thread(new Worker(i)).start();
		}
	}

	static class Worker implements Runnable {
		private final int workerId;

		Worker(int workerId) {
			this.workerId = workerId;
		}

		@Override
		public void run() {
			try {
				System.out.println("Worker " + workerId + " is waiting for a permit.");
				semaphore.acquire();
				System.out.println("Worker " + workerId + " acquired a permit.");
				Thread.sleep(2000); // Simulate work
				System.out.println("Worker " + workerId + " releasing the permit.");
				semaphore.release();
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
			}
		}
	}
}