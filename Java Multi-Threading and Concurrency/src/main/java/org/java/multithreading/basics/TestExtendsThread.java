package org.java.multithreading.basics;

public class TestExtendsThread {
	public static void main(String[] args) {
		World world = new World();
		world.start();
		for (; ; ) {
			System.out.println("Hello");
		}
	}
}

class World extends Thread {
	@Override
	public void run() {
		for (; ; ) {
			System.out.println("World");
		}
	}
}