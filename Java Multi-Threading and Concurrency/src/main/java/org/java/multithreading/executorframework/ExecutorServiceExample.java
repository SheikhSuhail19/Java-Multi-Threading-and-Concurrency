package org.java.multithreading.executorframework;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class ExecutorServiceExample {
	public static void main(String[] args) {
		ExecutorService executorService = Executors.newFixedThreadPool(2);
		executorService.submit(() -> System.out.println("Task executed by ExecutorService"));
		executorService.shutdown();
	}
}
