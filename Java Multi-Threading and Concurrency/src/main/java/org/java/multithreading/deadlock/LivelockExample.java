package org.java.multithreading.deadlock;

public class LivelockExample {
	public static void main(String[] args) {
		final Token token = new Token();
		final Thread t1 = new Thread(() -> {
			while (true) {
				synchronized (token) {
					if (!token.isHeldByThread1()) {
						token.setHeldByThread1(true);
						System.out.println("Thread-1: Acquired token.");
						break;
					}
				}
			}
		}, "Thread-1");

		final Thread t2 = new Thread(() -> {
			while (true) {
				synchronized (token) {
					if (!token.isHeldByThread2()) {
						token.setHeldByThread2(true);
						System.out.println("Thread-2: Acquired token.");
						break;
					}
				}
			}
		}, "Thread-2");

		t1.start();
		t2.start();
	}
}

class Token {
	private boolean heldByThread1 = false;
	private boolean heldByThread2 = false;

	public boolean isHeldByThread1() {
		return heldByThread1;
	}

	public void setHeldByThread1(boolean heldByThread1) {
		this.heldByThread1 = heldByThread1;
		this.heldByThread2 = !heldByThread1;
	}

	public boolean isHeldByThread2() {
		return heldByThread2;
	}

	public void setHeldByThread2(boolean heldByThread2) {
		this.heldByThread2 = heldByThread2;
		this.heldByThread1 = !heldByThread2;
	}
}
