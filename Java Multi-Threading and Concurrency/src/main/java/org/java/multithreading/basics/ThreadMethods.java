package org.java.multithreading.basics;

public class ThreadMethods {
	static Thread thread = new Thread(() -> {
		try {
			Thread.sleep(1000);
			System.out.println("Thread: " + Thread.currentThread().getName());
		}
		catch (InterruptedException e) {
			e.printStackTrace();
		}
	});

	public static void main(String[] args) throws InterruptedException {
		System.out.println("Thread: " + Thread.currentThread().getName());

		thread.start();
		// thread.start(); -> throws IllegalStateException

		// main thread runs the run() method of the thread object like a normal method
		thread.run();
		thread.run();

		thread.setPriority(Thread.MIN_PRIORITY);
		thread.setPriority(Thread.NORM_PRIORITY);
		thread.setPriority(3);
		thread.setPriority(7);
		// thread.setPriority(13); -> throws IllegalArgumentException
		thread.setPriority(Thread.MAX_PRIORITY);

		thread.join(); // main thread waits indefinitely for the worker thread to die
		thread.join(1000); // main thread waits for the worker thread to die for 1 second
		thread.join(1000, 100); // main thread waits for the worker thread to die for 1 second and 100 milliseconds

		System.out.println(thread.getState());
		System.out.println(Thread.currentThread().getName() + " will always run after the worker thread has " +
				"terminated: ");
	}
}

