package org.java.multithreading.executorframework;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ShutdownExample {
	public static void main(String[] args) {
		ExecutorService executor = Executors.newFixedThreadPool(2);
		executor.submit(() -> {
			try {
				Thread.sleep(1000);
				System.out.println("Task completed");
			} catch (InterruptedException e) {
				System.out.println("Task interrupted");
			}
		});

		executor.shutdown(); // Graceful shutdown
		// executor.shutdownNow(); // Forceful shutdown
	}
}

