package org.java.multithreading.completablefuture;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CompletableFutureExample {

	public static void main(String[] args) throws Exception {
		// 1. Run a task asynchronously without returning a result
		CompletableFuture<Void> runAsyncExample = CompletableFuture.runAsync(() ->
				System.out.println("Running asynchronously")
		);

		// 2. Run a task asynchronously and return a result
		CompletableFuture<String> supplyAsyncExample = CompletableFuture.supplyAsync(() -> "Hello");

		// 3. Transform the result using thenApply
		CompletableFuture<String> thenApplyExample = supplyAsyncExample.thenApply(result -> result + " World");

		// 4. Consume the result using thenAccept
		thenApplyExample.thenAccept(System.out::println);

		// 5. Chain multiple asynchronous tasks using thenCompose
		CompletableFuture<String> thenComposeExample = CompletableFuture.supplyAsync(() -> "Compose")
				.thenCompose(result -> CompletableFuture.supplyAsync(() -> result + " Example"));
		thenComposeExample.thenAccept(System.out::println);

		// 6. Combine two independent futures using thenCombine
		CompletableFuture<String> future1 = CompletableFuture.supplyAsync(() -> "Future1");
		CompletableFuture<String> future2 = CompletableFuture.supplyAsync(() -> "Future2");
		CompletableFuture<String> combinedFuture = future1.thenCombine(future2, (a, b) -> a + " " + b);
		combinedFuture.thenAccept(System.out::println);

		// 7. Wait for multiple futures to complete using allOf
		CompletableFuture<String> futureA = CompletableFuture.supplyAsync(() -> "A");
		CompletableFuture<String> futureB = CompletableFuture.supplyAsync(() -> "B");
		CompletableFuture<Void> allOf = CompletableFuture.allOf(futureA, futureB);
		allOf.thenRun(() -> {
			try {
				System.out.println("All completed: " + futureA.get() + ", " + futureB.get());
			} catch (Exception e) {
				e.printStackTrace();
			}
		});

		// 8. Handle exceptions using exceptionally
		CompletableFuture<String> exceptionallyExample = CompletableFuture.supplyAsync(() -> {
			if (true) throw new RuntimeException("Error");
			return "Result";
		}).exceptionally(ex -> "Handled error: " + ex.getMessage());
		exceptionallyExample.thenAccept(System.out::println);

		// 9. Handle result or exception using handle
		CompletableFuture<String> handleExample = CompletableFuture.supplyAsync(() -> {
			if (true) throw new RuntimeException("Error");
			return "Result";
		}).handle((result, ex) -> {
			if (ex != null) return "Handled error: " + ex.getMessage();
			return result;
		});
		handleExample.thenAccept(System.out::println);

		// 10. Manually complete a CompletableFuture
		CompletableFuture<String> manualFuture = new CompletableFuture<>();
		manualFuture.complete("Manual completion");
		manualFuture.thenAccept(System.out::println);

		// 11. Cancel a CompletableFuture
		CompletableFuture<String> cancelFuture = new CompletableFuture<>();
		cancelFuture.cancel(true);
		System.out.println("Cancelled: " + cancelFuture.isCancelled());

		// 12. Use a custom executor
		ExecutorService executor = Executors.newFixedThreadPool(2);
		CompletableFuture<String> customExecutorExample = CompletableFuture.supplyAsync(() -> "Custom Executor", executor);
		customExecutorExample.thenAccept(System.out::println);
		executor.shutdown();

		// Wait for all tasks to complete
		Thread.sleep(2000);
	}
}
