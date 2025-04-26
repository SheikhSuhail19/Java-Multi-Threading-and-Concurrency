package org.java.multithreading.atomicity;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicMarkableReference;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.atomic.AtomicStampedReference;

public class AtomicRaceSimulation {

	private static final AtomicBoolean seatReserved = new AtomicBoolean(false);
	private static final AtomicReference<String> currentUser = new AtomicReference<>(null);
	private static final AtomicStampedReference<String> sessionToken = new AtomicStampedReference<>("SESSION_ABC", 1);
	private static final AtomicMarkableReference<String> paymentStatus = new AtomicMarkableReference<>("PENDING", false);

	public static void main(String[] args) throws InterruptedException {
		ExecutorService executor = Executors.newFixedThreadPool(4);

		// Thread 1: Tries to reserve seat
		executor.submit(() -> {
			if (seatReserved.compareAndSet(false, true)) {
				System.out.println("🎟️ Seat reserved by Thread-1");
			} else {
				System.out.println("🚫 Seat already reserved (Thread-1)");
			}
		});

		// Thread 2: Tries to reserve seat too
		executor.submit(() -> {
			if (seatReserved.compareAndSet(false, true)) {
				System.out.println("🎟️ Seat reserved by Thread-2");
			} else {
				System.out.println("🚫 Seat already reserved (Thread-2)");
			}
		});

		// Thread 3: Simulate user login
		executor.submit(() -> {
			String previousUser = currentUser.get();
			boolean success = currentUser.compareAndSet(previousUser, "User_Alice");
			System.out.println(success
					? "👩 User_Alice logged in."
					: "🔁 Failed to log in User_Alice. Someone else beat her.");
		});

		// Thread 4: Simulate session update with stamp
		executor.submit(() -> {
			int[] stampHolder = new int[1];
			String currentSession = sessionToken.get(stampHolder);
			boolean updated = sessionToken.compareAndSet(currentSession, "SESSION_XYZ", stampHolder[0], stampHolder[0] + 1);
			System.out.println(updated
					? "🔄 Session token updated to SESSION_XYZ"
					: "❌ Session update failed due to version mismatch.");
		});

		// Simulate payment vs cancel conflict
		Runnable payTask = () -> {
			boolean[] mark = new boolean[1];
			String currentState = paymentStatus.get(mark);
			try {
				Thread.sleep(50); // Just enough time for other thread to sneak in
			} catch (InterruptedException ignored) {
			}
			boolean success = paymentStatus.compareAndSet(currentState, "PAID", mark[0], true);
			System.out.println(success
					? "💳 Payment completed successfully."
					: "❌ Payment failed. Possibly canceled.");
		};

		Runnable cancelTask = () -> {
			boolean[] mark = new boolean[1];
			String currentState = paymentStatus.get(mark);
			boolean canceled = paymentStatus.compareAndSet(currentState, "CANCELED", mark[0], true);
			System.out.println(canceled
					? "❌ Payment canceled successfully."
					: "⚠️ Cancel failed. Payment might've already been processed.");
		};

		executor.submit(payTask);
		executor.submit(cancelTask);

		executor.shutdown();
		executor.awaitTermination(5, TimeUnit.SECONDS);

		// Final system state
		int[] finalStamp = new int[1];
		boolean[] finalMark = new boolean[1];

		System.out.println("\n=== 🧾 Final State ===");
		System.out.println("Seat Reserved: " + seatReserved.get());
		System.out.println("Current User: " + currentUser.get());
		System.out.println("Session Token: " + sessionToken.get(finalStamp) + " (version: " + finalStamp[0] + ")");
		System.out.println("Payment Status: " + paymentStatus.get(finalMark) + " (confirmed: " + finalMark[0] + ")");
	}
}
