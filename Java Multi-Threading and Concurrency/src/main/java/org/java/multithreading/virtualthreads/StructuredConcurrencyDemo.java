package org.java.multithreading.virtualthreads;

import java.util.concurrent.StructuredTaskScope;
import java.util.concurrent.StructuredTaskScope.Subtask;

public class StructuredConcurrencyDemo {
	public static void main(String[] args) throws Exception {
		try (var scope = new StructuredTaskScope.ShutdownOnFailure()) {
			Subtask<String> f1 = scope.fork(() -> fetchData("API-1"));
			Subtask<String> f2 = scope.fork(() -> fetchData("API-2"));

			scope.join();           // wait for both
			scope.throwIfFailed();  // rethrow if any failed

			System.out.println("Results: " + f1.get() + ", " + f2.get());
		}
	}

	private static String fetchData(String source) throws InterruptedException {
		Thread.sleep(500); // simulate API call
		return "Response from " + source;
	}
}
