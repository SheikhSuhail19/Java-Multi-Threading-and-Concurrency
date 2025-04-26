package org.java.multithreading.synchronizationaid;

import java.util.concurrent.CountDownLatch;

public class CountDownLatchExample {
	public static void main(String[] args) throws InterruptedException {
		int numberOfWorkers = 3;
		CountDownLatch latch = new CountDownLatch(numberOfWorkers);

		for (int i = 1; i <= numberOfWorkers; i++) {
			new Thread(new Worker(latch, i)).start();
		}

		// Main thread waits until all workers have finished
		latch.await();
		System.out.println("All workers have completed their tasks. Proceeding...");
	}
}

class Worker implements Runnable {
	private final CountDownLatch latch;
	private final int workerNumber;

	Worker(CountDownLatch latch, int workerNumber) {
		this.latch = latch;
		this.workerNumber = workerNumber;
	}

	@Override
	public void run() {
		try {
			System.out.println("Worker " + workerNumber + " is working.");
			Thread.sleep((long) (Math.random() * 1000)); // Simulate work
			System.out.println("Worker " + workerNumber + " has finished.");
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		} finally {
			latch.countDown(); // Decrement the count
		}
	}
}