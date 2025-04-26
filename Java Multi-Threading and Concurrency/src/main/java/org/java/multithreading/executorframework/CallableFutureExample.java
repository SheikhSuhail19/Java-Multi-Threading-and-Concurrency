package org.java.multithreading.executorframework;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;


public class CallableFutureExample {
	public static void main(String[] args) throws Exception {
		ExecutorService executor = Executors.newSingleThreadExecutor();
		Callable<String> task = () -> "Result from Callable";
		Future<String> future = executor.submit(task);
		System.out.println(future.get()); // Outputs: Result from Callable
		executor.shutdown();
	}
}

class CallableFutureExample2 {
	public static void main(String[] args) {
		ExecutorService executor = Executors.newSingleThreadExecutor();

		Callable<String> task = () -> {
			Thread.sleep(1000);
			return "Task's execution result";
		};

		Future<String> future = executor.submit(task);

		try {
			// Blocks until the result is available
			String result = future.get();
			System.out.println("Result: " + result);
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
		} finally {
			executor.shutdown();
		}
	}
}