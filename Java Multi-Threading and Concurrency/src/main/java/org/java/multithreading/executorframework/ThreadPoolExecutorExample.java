package org.java.multithreading.executorframework;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ThreadPoolExecutorExample {
	public static void main(String[] args) {
		ThreadPoolExecutor executor = new ThreadPoolExecutor(
				2, 4, 60, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>()
		);
		executor.execute(() -> System.out.println("Task executed by ThreadPoolExecutor"));
		executor.shutdown();
	}
}
