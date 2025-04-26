package org.java.multithreading.executorframework;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ScheduledExecutorServiceExample {
	public static void main(String[] args) {
		ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
		scheduler.schedule(() -> System.out.println("Delayed Task"), 3, TimeUnit.SECONDS);
		scheduler.scheduleAtFixedRate(() -> System.out.println("Periodic Task"), 0, 3, TimeUnit.SECONDS);
		scheduler.scheduleWithFixedDelay(() -> System.out.println("Periodic Task"), 0, 3, TimeUnit.SECONDS);
	}
}

