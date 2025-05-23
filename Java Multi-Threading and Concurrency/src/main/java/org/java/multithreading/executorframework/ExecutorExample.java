package org.java.multithreading.executorframework;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class ExecutorExample {
	public static void main(String[] args) {
		Executor executor = Executors.newSingleThreadExecutor();
		executor.execute(() -> System.out.println("Task executed by Executor"));
	}
}
