package org.java.multithreading.deadlock;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadInfo;
import java.lang.management.ThreadMXBean;
import java.util.concurrent.CountDownLatch;

class DeadlockDetector extends Thread {

	private final ThreadMXBean threadMXBean = ManagementFactory.getThreadMXBean();

	@Override
	public void run() {
		while (true) {
			long[] deadlockedThreadIds = threadMXBean.findDeadlockedThreads(); // null if none

			if (deadlockedThreadIds != null) {
				System.out.println("🔴 Deadlock detected!");
				ThreadInfo[] threadInfos = threadMXBean.getThreadInfo(deadlockedThreadIds);

				for (ThreadInfo threadInfo : threadInfos) {
					System.out.println(threadInfo);
				}
				System.exit(1); // kill the app after detecting deadlock (optional)
			}

			try {
				Thread.sleep(1000); // check every 1 second
			} catch (InterruptedException ignored) {
			}
		}
	}
}

public class DeadlockDetectorExample {
	public static void main(String[] args) {
		new DeadlockDetector().start(); // 🔥 start deadlock monitoring

		Pen pen = new Pen();
		Paper paper = new Paper();
		CountDownLatch latch = new CountDownLatch(1);

		Thread thread1 = new Thread(() -> {
			try {
				latch.await();
				pen.write(paper);
			} catch (InterruptedException ignored) {
			}
		}, "Thread-1");

		Thread thread2 = new Thread(() -> {
			try {
				latch.await();
				paper.write(pen);
			} catch (InterruptedException ignored) {
			}
		}, "Thread-2");

		thread1.start();
		thread2.start();

		System.out.println("Both threads started. Releasing latch...");
		latch.countDown();
	}
}

