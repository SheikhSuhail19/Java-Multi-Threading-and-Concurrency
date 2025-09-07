package org.java.multithreading.virtualthreads;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class VirtualThreadExecutorDemo {
	public static void main(String[] args) throws Exception {
		try (ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor()) {
			Future<String> result = executor.submit(() -> {
				Thread.sleep(1000);
				return "Done by " + Thread.currentThread();
			});

			System.out.println(result.get());
		}
	}
}

