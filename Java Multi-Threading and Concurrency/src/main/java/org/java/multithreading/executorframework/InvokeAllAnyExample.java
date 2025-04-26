package org.java.multithreading.executorframework;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;


public class InvokeAllAnyExample {
	public static void main(String[] args) throws Exception {
		ExecutorService executor = Executors.newFixedThreadPool(2);
		List<Callable<String>> tasks = Arrays.asList(
				() -> "Task 1",
				() -> "Task 2",
				() -> "Task 3"
		);

		// invokeAll
		List<Future<String>> results = executor.invokeAll(tasks);
		for (Future<String> result : results) {
			System.out.println(result.get());
		}

		// invokeAny
		String fastestResult = executor.invokeAny(tasks);
		System.out.println("Fastest result: " + fastestResult);

		executor.shutdown();
	}
}