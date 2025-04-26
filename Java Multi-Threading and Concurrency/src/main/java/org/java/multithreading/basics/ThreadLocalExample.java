package org.java.multithreading.basics;

public class ThreadLocalExample {
	private static final ThreadLocal<String> correlationId = ThreadLocal.withInitial(() -> "N/A");

	public static void main(String[] args) {
		Runnable task = () -> {
			String threadName = Thread.currentThread().getName();
			correlationId.set("Req-" + threadName.hashCode()); // simulate unique ID per request
			processRequest(threadName);
			correlationId.remove(); // Clean up to avoid memory leaks
		};

		Thread t1 = new Thread(task, "User-Login");
		Thread t2 = new Thread(task, "User-Register");

		t1.start();
		t2.start();
	}

	private static void processRequest(String threadName) {
		System.out.printf("[%s] Handling request with Correlation ID: %s%n",
				threadName, correlationId.get());
	}
}
