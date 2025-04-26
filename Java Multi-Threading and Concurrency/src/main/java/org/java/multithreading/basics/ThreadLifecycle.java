package org.java.multithreading.basics;

public class ThreadLifecycle {

	public static void main(String[] args) throws InterruptedException {
		Object lock = new Object();

		Thread t = new Thread(() -> {
			try {
				Thread.sleep(500); // TIMED_WAITING

				synchronized (lock) { // Acquire lock
					lock.wait();  // WAITING, release lock
					// When lock is released, `t` will try to acquire it, which will result in BLOCKED if lock is not available
				}

			}
			catch (InterruptedException e) {
				e.printStackTrace();
			}
		});

		// Thread to notify `t` to simulate BLOCKED
		Thread notifier = new Thread(() -> {
			synchronized (lock) { // Acquire lock
				lock.notify(); // Notify `t` to wake up
				try {
					Thread.sleep(500); // Keep lock so `t` gets BLOCKED
				}
				catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		});

		System.out.println("State after creation: " + t.getState()); // NEW

		t.start();
		System.out.println("State after start: " + t.getState()); // RUNNABLE

		Thread.sleep(200);
		System.out.println("State during sleep: " + t.getState()); // TIMED_WAITING

		Thread.sleep(400);
		System.out.println("State during wait(): " + t.getState()); // WAITING

		notifier.start();
		Thread.sleep(100);
		System.out.println("State after notify(): " + t.getState()); // BLOCKED

		notifier.join();
		t.join();
		System.out.println("State after termination: " + t.getState()); // TERMINATED
	}
}
