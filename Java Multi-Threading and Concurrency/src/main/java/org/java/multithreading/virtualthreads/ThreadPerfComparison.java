package org.java.multithreading.virtualthreads;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThreadPerfComparison {

	public static void main(String[] args) throws Exception {
		int tasks = 50_000; // change this to push harder
		int sleepMs = 100;

		System.out.println("🔹 Benchmarking with " + tasks + " tasks, each sleeping " + sleepMs + "ms");

		benchmarkPlatformThreads(tasks, sleepMs);
		benchmarkVirtualThreads(tasks, sleepMs);
	}

	private static void benchmarkPlatformThreads(int tasks, int sleepMs) throws Exception {
		long start = System.currentTimeMillis();

		ExecutorService executor = Executors.newFixedThreadPool(200); // typical limit
		doSomeWork(tasks, sleepMs, executor);
		executor.shutdown();

		long duration = System.currentTimeMillis() - start;
		System.out.println("⏱ Platform threads done in: " + duration + " ms");
	}

	private static void benchmarkVirtualThreads(int tasks, int sleepMs) throws Exception {
		long start = System.currentTimeMillis();

		try (ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor()) {
			doSomeWork(tasks, sleepMs, executor);
		}

		long duration = System.currentTimeMillis() - start;
		System.out.println("🚀 Virtual threads done in: " + duration + " ms");
	}

	private static void doSomeWork(int tasks, int sleepMs, ExecutorService executor) throws InterruptedException {
		CountDownLatch latch = new CountDownLatch(tasks);

		for (int i = 0; i < tasks; i++) {
			executor.submit(() -> {
				try {
					Thread.sleep(sleepMs);
				} catch (InterruptedException ignored) {
				}
				latch.countDown();
			});
		}

		latch.await();
	}
}
