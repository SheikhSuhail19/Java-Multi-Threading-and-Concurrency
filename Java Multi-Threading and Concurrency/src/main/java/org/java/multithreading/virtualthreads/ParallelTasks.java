package org.java.multithreading.virtualthreads;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class ParallelTasks {
	public static void main(String[] args) throws Exception {
		try (ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor()) {
			Future<Integer> f1 = executor.submit(() -> heavyComputation(1));
			Future<Integer> f2 = executor.submit(() -> heavyComputation(2));

			System.out.println("Results: " + f1.get() + " + " + f2.get());
		}
	}

	private static int heavyComputation(int id) throws InterruptedException {
		System.out.println("Starting computation " + id + " on " + Thread.currentThread());
		Thread.sleep(1000);
		return id * 10;
	}
}
