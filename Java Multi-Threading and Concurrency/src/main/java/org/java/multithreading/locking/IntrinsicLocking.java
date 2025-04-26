package org.java.multithreading.locking;

import java.util.ArrayList;
import java.util.List;

// In the world of synchronized instance methods, it's one thread to rule them all... at a time. Even if the method is innocent like getCount(), it still must wait for the king thread to abdicate the lock throne.
public class IntrinsicLocking {
	private int count = 0;

	public synchronized void increment() {
		System.out.println(Thread.currentThread().getName() + " is trying to enter increment()");
		count++;
		System.out.println(Thread.currentThread().getName() + " exited increment()");
	}

	public synchronized int getCount() {
		System.out.println(Thread.currentThread().getName() + " is trying to enter getCount()");
		int value = count;
		System.out.println(Thread.currentThread().getName() + " exited getCount()");
		return value;
	}

	public synchronized void print() {
		System.out.println(Thread.currentThread().getName() + " is trying to enter print()");
		System.out.println(Thread.currentThread().getName() + " is printing...");
		System.out.println("Current count is: " + getCount());
		System.out.println(Thread.currentThread().getName() + " exited print()");
	}

	public static void main(String[] args) throws InterruptedException {
		IntrinsicLocking intrinsicLocking = new IntrinsicLocking();
		List<Thread> threads = new ArrayList<>();

		for (int i = 0; i < 3; i++) { // Fewer threads for clarity in output
			Thread t = new Thread(() -> {
				for (int j = 0; j < 3; j++) {
					intrinsicLocking.increment();
					intrinsicLocking.print();
				}
			}, "Thread-" + i);
			threads.add(t);
			t.start();
		}

		for (Thread t : threads) {
			t.join();
		}

		System.out.println("All threads done. Final count = " + intrinsicLocking.getCount());
	}
}
