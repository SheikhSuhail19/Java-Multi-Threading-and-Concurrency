package org.java.multithreading.locking;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ReentrantWithConditional {
	private final Lock lock = new ReentrantLock();
	private final Condition bufferNotFull = lock.newCondition();
	private final Condition bufferNotEmpty = lock.newCondition();

	private final int[] buffer = new int[5];
	private int count = 0;

	public void produce() throws InterruptedException {
		lock.lock();
		try {
			while (count == buffer.length) {
				System.out.println("Buffer full, producer waiting...");
				bufferNotFull.await();
			}
			buffer[count++] = 1;
			System.out.println("Produced: " + count);
			bufferNotEmpty.signal(); // notify consumer
		} finally {
			lock.unlock();
		}
	}

	public void consume() throws InterruptedException {
		lock.lock();
		try {
			while (count == 0) {
				System.out.println("Buffer empty, consumer waiting...");
				bufferNotEmpty.await();
			}
			buffer[--count] = 0;
			System.out.println("Consumed: " + count);
			bufferNotFull.signal(); // notify producer
		} finally {
			lock.unlock();
		}
	}

	public static void main(String[] args) {
		ReentrantWithConditional pc = new ReentrantWithConditional();

		Thread producer = new Thread(() -> {
			try {
				for (int i = 0; i < 10; i++) pc.produce();
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
			}
		});

		Thread consumer = new Thread(() -> {
			try {
				for (int i = 0; i < 10; i++) pc.consume();
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
			}
		});

		producer.start();
		consumer.start();
	}
}
