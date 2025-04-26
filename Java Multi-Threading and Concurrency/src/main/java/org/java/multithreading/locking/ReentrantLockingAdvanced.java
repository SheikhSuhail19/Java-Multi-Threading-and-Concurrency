package org.java.multithreading.locking;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class ReentrantLockingAdvanced {
	private final ReentrantLock lock = new ReentrantLock(true); // true = fair lock
	private final Condition notEmpty = lock.newCondition();

	private int data = 0;
	private boolean hasData = false;

	// Producer adds data
	public void produce() throws InterruptedException {
		lock.lock(); // acquire lock
		try {
			while (hasData) {
				notEmpty.await(); // wait until data is consumed
			}
			data++;
			System.out.println(Thread.currentThread().getName() + " produced: " + data);
			hasData = true;
			notEmpty.signal(); // notify one waiting thread (the consumer)
		} finally {
			lock.unlock(); // always release lock
		}
	}

	// Consumer consumes data
	public void consume() throws InterruptedException {
		lock.lock(); // acquire lock
		try {
			while (!hasData) {
				notEmpty.await(); // wait until data is produced
			}
			System.out.println(Thread.currentThread().getName() + " consumed: " + data);
			hasData = false;
			notEmpty.signal(); // notify producer
		} finally {
			lock.unlock(); // always release lock
		}
	}

	public static void main(String[] args) {
		ReentrantLockingAdvanced demo = new ReentrantLockingAdvanced();

		Runnable producerTask = () -> {
			try {
				for (int i = 0; i < 5; i++) {
					demo.produce();
					Thread.sleep(100); // simulate time to produce
				}
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
			}
		};

		Runnable consumerTask = () -> {
			try {
				for (int i = 0; i < 5; i++) {
					demo.consume();
					Thread.sleep(150); // simulate time to consume
				}
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
			}
		};

		Thread producer = new Thread(producerTask, "Producer");
		Thread consumer = new Thread(consumerTask, "Consumer");

		producer.start();
		consumer.start();
	}
}

