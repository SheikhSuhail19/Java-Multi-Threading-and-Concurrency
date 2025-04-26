package org.java.multithreading.atomicity;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicMarkableReference;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.atomic.AtomicStampedReference;

public class AtomicBookingSystem {

	// 1. AtomicBoolean — track if the seat is reserved
	private static final AtomicBoolean seatReserved = new AtomicBoolean(false);

	// 2. AtomicReference — currently logged-in user
	private static final AtomicReference<String> currentUser = new AtomicReference<>(null);

	// 3. AtomicStampedReference — versioned session state
	private static final AtomicStampedReference<String> sessionToken = new AtomicStampedReference<>("SESSION123", 1);

	// 4. AtomicMarkableReference — payment status with "mark" for verification
	private static final AtomicMarkableReference<String> paymentState = new AtomicMarkableReference<>("NOT_PAID", false);

	public static void main(String[] args) {
		System.out.println("📢 Initial system state:");
		printState();

		// 1. Reserve seat if not already reserved
		if (seatReserved.compareAndSet(false, true)) {
			System.out.println("\n🎟️ Seat successfully reserved.");
		} else {
			System.out.println("\n🚫 Seat already reserved.");
		}

		// 2. Set user login atomically
		currentUser.set("user42");
		System.out.println("👤 Logged in user: " + currentUser.get());

		// 3. Update session if the stamp/version matches
		int[] stampHolder = new int[1];
		String currentSession = sessionToken.get(stampHolder);
		boolean sessionUpdated = sessionToken.compareAndSet(currentSession, "SESSION456", stampHolder[0], stampHolder[0] + 1);
		System.out.println(sessionUpdated ? "🔐 Session updated." : "❌ Session update failed.");

		// 4. Mark payment as completed
		boolean[] markHolder = new boolean[1];
		String currentPayment = paymentState.get(markHolder);
		if (paymentState.compareAndSet(currentPayment, "PAID", markHolder[0], true)) {
			System.out.println("💳 Payment status updated to PAID.");
		} else {
			System.out.println("💥 Payment update failed.");
		}

		System.out.println("\n✅ Final system state:");
		printState();
	}

	private static void printState() {
		System.out.println("Seat reserved? " + seatReserved.get());
		System.out.println("Current user: " + currentUser.get());
		int[] stamp = new int[1];
		System.out.println("Session token: " + sessionToken.get(stamp) + " (version: " + stamp[0] + ")");
		boolean[] mark = new boolean[1];
		System.out.println("Payment state: " + paymentState.get(mark) + " (verified: " + mark[0] + ")");
	}
}
