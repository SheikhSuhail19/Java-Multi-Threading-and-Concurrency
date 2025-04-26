package org.java.multithreading.executorframework;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class AwaitTerminationExample {
	public static void main(String[] args) throws InterruptedException {
		ExecutorService executor = Executors.newFixedThreadPool(2);
		executor.submit(() -> System.out.println("Task executed"));
		executor.shutdown();
		if (executor.awaitTermination(5, TimeUnit.SECONDS)) {
			System.out.println("All tasks completed");
		} else {
			System.out.println("Timeout elapsed before termination");
		}
	}
}
