package org.java.multithreading.synchronization;

public class ThreadSynchronization {
	public static void main(String[] args) {
		CounterSyncMethod counterSyncMethod = new CounterSyncMethod();

		Thread thread1 = new Thread(() -> {
			for (int i = 0; i < 1000; i++) {
				counterSyncMethod.increment();
			}
		});

		Thread thread2 = new Thread(() -> {
			for (int i = 0; i < 1000; i++) {
				counterSyncMethod.increment();
			}
		});

		thread1.start();
		thread2.start();

		try {
			thread1.join();
			thread2.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		System.out.println("Final count: " + counterSyncMethod.getCount());

		CounterSyncBlock counterSyncBlock = new CounterSyncBlock();

		Thread thread3 = new Thread(() -> {
			for (int i = 0; i < 1000; i++) {
				counterSyncBlock.increment();
			}
		});

		Thread thread4 = new Thread(() -> {
			for (int i = 0; i < 1000; i++) {
				counterSyncBlock.increment();
			}
		});

		thread3.start();
		thread4.start();

		try {
			thread3.join();
			thread4.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		System.out.println("Final count: " + counterSyncBlock.getCount());

		Printer printer = new Printer();
		Thread thread5 = new Thread(() -> printer.printDocument("Document 1"));
		Thread thread6 = new Thread(() -> printer.printDocument("Document 2"));

		thread5.start();
		thread6.start();

		try {
			thread5.join();
			thread6.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		if (thread5.isAlive() || thread6.isAlive()) {
			System.out.println("Threads are still running");
		} else {
			System.out.println("Threads have finished");
		}

		Runnable logTask = () -> {
			for (int i = 0; i < 5; i++) {
				Logger.log(Thread.currentThread().getName() + " - message " + i);
			}
		};

		Thread t1 = new Thread(logTask, "Thread-1");
		Thread t2 = new Thread(logTask, "Thread-2");
		Thread t3 = new Thread(logTask, "Thread-3");

		t1.start();
		t2.start();
		t3.start();
	}
}

class CounterSyncMethod {
	private int count = 0;

	public synchronized void increment() {
		count++;
	}

	public synchronized int getCount() {
		return count;
	}
}

class CounterSyncBlock {
	private int count = 0;
	private final Object lock = new Object();

	public void increment() {
		synchronized (lock) {
			count++;
		}
	}

	public int getCount() {
		synchronized (lock) {
			return count;
		}
	}
}

class Printer {
	public void printDocument(String doc) {
		synchronized (this) {
			System.out.println("Printing: " + doc);
		}
	}
}

class Logger {
	public static void log(String message) {
		synchronized (Logger.class) {
			System.out.println("Log: " + message);
			System.out.println("Continue");
		}
	}
}