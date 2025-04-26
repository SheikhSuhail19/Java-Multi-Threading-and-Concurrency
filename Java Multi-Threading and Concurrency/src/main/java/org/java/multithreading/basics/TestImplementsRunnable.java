package org.java.multithreading.basics;

public class TestImplementsRunnable {
	public static void main(String[] args) {
		Worldx world = new Worldx();
		Thread thread = new Thread(world);
		thread.start();
		for (; ; ) {
			System.out.println("Hello");
		}
	}
}

class Worldx implements Runnable {
	@Override
	public void run() {
		for (; ; ) {
			System.out.println("World");
		}
	}
}