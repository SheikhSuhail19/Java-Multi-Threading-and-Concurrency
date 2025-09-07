package org.java.multithreading.virtualthreads;

import java.util.concurrent.Executors;

public class SynchronizationProblem {
	private static int counter = 0;

	public static void main(String[] args) throws Exception {
		try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
			for (int i = 0; i < 1000; i++) {
				executor.submit(() -> {
					synchronized (SynchronizationProblem.class) {
						counter++; // can block thousands of virtual threads
					}
				});
			}
		}
		System.out.println("Counter = " + counter);
	}
}
