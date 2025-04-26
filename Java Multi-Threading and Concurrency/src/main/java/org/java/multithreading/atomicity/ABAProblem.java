package org.java.multithreading.atomicity;

import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.atomic.AtomicStampedReference;

public class ABAProblem {

	private static final AtomicReference<String> atomicRef = new AtomicReference<>("A");

	private static final AtomicStampedReference<String> stampedRef =
			new AtomicStampedReference<>("A", 0);

	public static void main(String[] args) throws InterruptedException {
		System.out.println("=== ❌ ABA Problem with AtomicReference ===");

		Thread t1 = new Thread(() -> {
			String value = atomicRef.get();
			System.out.println("Thread-1 read: " + value);
			sleep(100); // let Thread-2 mess it up
			boolean success = atomicRef.compareAndSet(value, "C");
			System.out.println("Thread-1 CAS success (AtomicReference)? " + success);
		});

		Thread t2 = new Thread(() -> {
			sleep(50);
			atomicRef.compareAndSet("A", "B");
			atomicRef.compareAndSet("B", "A"); // back to original
			System.out.println("Thread-2 changed A → B → A");
		});

		t1.start();
		t2.start();
		t1.join();
		t2.join();

		System.out.println("Final value (AtomicReference): " + atomicRef.get());

		System.out.println("\n=== ✅ Fix with AtomicStampedReference ===");

		Thread t3 = new Thread(() -> {
			int[] stampHolder = new int[1];
			String value = stampedRef.get(stampHolder);
			int stamp = stampHolder[0];
			System.out.println("Thread-3 read: value = " + value + ", stamp = " + stamp);

			sleep(150); // Give Thread-4 time to do its ABA changes

			boolean success = stampedRef.compareAndSet(value, "C", stamp, stamp + 1);
			System.out.println("Thread-3 CAS success (Stamped)? " + success);
		});

		Thread t4 = new Thread(() -> {
			sleep(50); // let t3 read first
			int[] stampHolder = new int[1];
			String v1 = stampedRef.get(stampHolder);
			int stamp1 = stampHolder[0];

			stampedRef.compareAndSet(v1, "B", stamp1, stamp1 + 1);
			System.out.println("Thread-4 changed A → B");

			sleep(50);

			String v2 = stampedRef.get(stampHolder);
			int stamp2 = stampHolder[0];
			stampedRef.compareAndSet(v2, "A", stamp2, stamp2 + 1); // back to A
			System.out.println("Thread-4 changed B → A (Stamped), stamp now = " + (stamp2 + 1));
		});

		t3.start();
		t4.start();
		t3.join();
		t4.join();

		int[] finalStamp = new int[1];
		String finalValue = stampedRef.get(finalStamp);
		System.out.println("Final value (Stamped): " + finalValue + " with stamp: " + finalStamp[0]);
	}

	private static void sleep(long ms) {
		try {
			Thread.sleep(ms);
		} catch (InterruptedException ignored) {
		}
	}
}
