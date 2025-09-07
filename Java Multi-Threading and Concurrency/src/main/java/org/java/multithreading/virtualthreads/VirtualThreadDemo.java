package org.java.multithreading.virtualthreads;

public class VirtualThreadDemo {
	public static void main(String[] args) throws InterruptedException {
		Thread vThread = Thread.ofVirtual().start(() -> {
			System.out.println("Hello from virtual thread: " + Thread.currentThread());
		});

		vThread.join(); // wait for it to finish
	}
}
