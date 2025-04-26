package org.java.multithreading.atomicity;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

public class AtomicLikeCounter {

	// Thread-safe counter
	private static final AtomicInteger likeCount = new AtomicInteger(0);

	// Simulate a user clicking "Like"
	static class UserClick implements Runnable {
		private final int userId;

		public UserClick(int userId) {
			this.userId = userId;
		}

		@Override
		public void run() {
			int updatedCount = likeCount.incrementAndGet(); // atomic operation
			System.out.println("User " + userId + " clicked like. Total likes: " + updatedCount);
		}
	}

	public static void main(String[] args) {
		int numberOfUsers = 100;

		// Simulate 100 users liking the post concurrently
		ExecutorService executor = Executors.newFixedThreadPool(10);

		for (int i = 1; i <= numberOfUsers; i++) {
			executor.submit(new UserClick(i));
		}

		executor.shutdown();

		// Wait for all tasks to finish
		while (!executor.isTerminated()) {
			// chill
		}

		System.out.println("✅ Final like count: " + likeCount.get());
	}
}
