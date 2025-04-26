package org.java.multithreading.locking;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

public class ReentrantLocking {
	private final ReentrantLock lock = new ReentrantLock();
	private int count = 0;

	// Basic lock/unlock
	public void increment() {
		lock.lock();
		try {
			System.out.println(Thread.currentThread().getName() + " acquired the lock for increment()");
			count++;
			System.out.println("Count is now: " + count);

			// Demonstrating reentrancy: same thread calls another locked method
			nested();
		} finally {
			lock.unlock();
			System.out.println(Thread.currentThread().getName() + " released the lock for increment()");
		}
	}

	// Nested call to demonstrate reentrancy
	private void nested() {
		lock.lock();
		try {
			System.out.println(Thread.currentThread().getName() + " re-acquired lock in nested()");
		} finally {
			lock.unlock();
		}
	}

	// TryLock without waiting
	public void tryLockExample() {
		if (lock.tryLock()) {
			try {
				System.out.println(Thread.currentThread().getName() + " got lock using tryLock()");
			} finally {
				lock.unlock();
			}
		} else {
			System.out.println(Thread.currentThread().getName() + " couldn't acquire lock using tryLock()");
		}
	}

	// TryLock with timeout
	public void tryLockWithTimeoutExample() {
		try {
			if (lock.tryLock(1, TimeUnit.SECONDS)) {
				try {
					System.out.println(Thread.currentThread().getName() + " got lock with timeout");
				} finally {
					lock.unlock();
				}
			} else {
				System.out.println(Thread.currentThread().getName() + " timed out waiting for lock");
			}
		} catch (InterruptedException e) {
			System.out.println(Thread.currentThread().getName() + " was interrupted while waiting for lock");
		}
	}

	// LockInterruptibly example
	public void lockInterruptiblyExample() {
		try {
			System.out.println(Thread.currentThread().getName() + " trying lockInterruptibly...");
			lock.lockInterruptibly();
			try {
				System.out.println(Thread.currentThread().getName() + " acquired lock using lockInterruptibly()");
				Thread.sleep(5000); // Simulate long task
			} finally {
				lock.unlock();
				System.out.println(Thread.currentThread().getName() + " released lock");
			}
		} catch (InterruptedException e) {
			System.out.println(Thread.currentThread().getName() + " was interrupted before acquiring the lock");
		}
	}

	public static void main(String[] args) throws InterruptedException {
		ReentrantLocking demo = new ReentrantLocking();

		// Thread 1: Basic increment with reentrancy
		Thread t1 = new Thread(demo::increment, "Thread-1");

		// Thread 2: tryLock (non-blocking)
		Thread t2 = new Thread(demo::tryLockExample, "Thread-2");

		// Thread 3: tryLock with timeout
		Thread t3 = new Thread(demo::tryLockWithTimeoutExample, "Thread-3");

		// Thread 4 and 5: Interruptible locking
		Thread t4 = new Thread(demo::lockInterruptiblyExample, "Thread-4");
		Thread t5 = new Thread(demo::lockInterruptiblyExample, "Thread-5");

		t1.start();
		Thread.sleep(100); // ensure t1 grabs the lock
		t2.start();
		t3.start();

		t4.start();
		Thread.sleep(200); // ensure t4 blocks on lock
		t5.start();

		Thread.sleep(1000);
		t5.interrupt(); // interrupt thread waiting on lock
	}
}
