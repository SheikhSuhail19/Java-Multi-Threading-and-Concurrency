package org.java.multithreading.virtualthreads;

import java.util.concurrent.Executors;

public class BlockingIODemo {
	public static void main(String[] args) throws Exception {
		try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
			for (int i = 0; i < 1_000; i++) {
				int id = i;
				executor.submit(() -> {
					try {
						Thread.sleep(100); // looks blocking, but virtual threads yield
					} catch (InterruptedException e) {
						throw new RuntimeException(e);
					}
					System.out.println("Finished task " + id + " on " + Thread.currentThread());
				});
			}
		}
	}
}
