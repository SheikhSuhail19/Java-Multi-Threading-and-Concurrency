package org.java.multithreading.completablefuture;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

record ProductInfo(String name, double price) {
	public String getName() {
		return name;
	}

	public double getPrice() {
		return price;
	}
}

record Inventory(int stock) {
	public int getStock() {
		return stock;
	}
}

record Review(double rating) {
	public double getRating() {
		return rating;
	}
}

record ProductPage(ProductInfo info, Inventory inventory, Review review) {

	@Override
	public String toString() {
		return "ProductPage{" +
				"name=" + info.getName() +
				", price=" + info.getPrice() +
				", stock=" + inventory.getStock() +
				", rating=" + review.getRating() +
				'}';
	}
}

/*
Key Highlights:
	- supplyAsync: Runs tasks asynchronously.
	- thenCombine: Combines results of two futures.
	- Non-blocking: Product info, inventory, and reviews fetch in parallel.

Final result: All are merged into one ProductPage.

In real life, this approach speeds up your API response time drastically compared to sequential calls.
 */
public class ECommerceExample {
	private static final ExecutorService executor = Executors.newFixedThreadPool(3);

	public static void main(String[] args) throws Exception {
		CompletableFuture<ProductInfo> productInfoFuture =
				CompletableFuture.supplyAsync(() -> fetchProductInfo(101), executor)
						.exceptionally(ex -> {
							System.out.println("❌ Failed to fetch product info: " + ex);
							return new ProductInfo("Unknown", 0.0); // fallback
						});

		CompletableFuture<Inventory> inventoryFuture =
				CompletableFuture.supplyAsync(() -> fetchInventory(101), executor)
						.handle((result, ex) -> {
							if (ex != null) {
								System.out.println("❌ Inventory service failed: " + ex);
								return new Inventory(0); // assume out of stock
							}
							return result;
						});

		CompletableFuture<Review> reviewFuture =
				CompletableFuture.supplyAsync(() -> fetchReviews(101), executor)
						.exceptionally(ex -> {
							System.out.println("❌ Reviews service failed: " + ex);
							return new Review(0.0); // no rating
						});

		// Combine all futures into one
		CompletableFuture<ProductPage> productPageFuture =
				productInfoFuture.thenCombine(inventoryFuture, PartialProduct::new)
						.thenCombine(reviewFuture, (partial, review) ->
								new ProductPage(partial.info, partial.inv, review))
						.whenComplete((page, ex) -> {
							if (ex != null) {
								System.out.println("⚠️ Something went wrong building product page: " + ex);
							} else {
								System.out.println("✅ Successfully built product page.");
							}
						});

		// Block and get the final result
		ProductPage page = productPageFuture.get();
		System.out.println(page);

		executor.shutdown();
	}

	// Mock services
	private static ProductInfo fetchProductInfo(int productId) {
		sleep(500); // simulate delay
		return new ProductInfo("Laptop", 1200.50);
	}

	private static Inventory fetchInventory(int productId) {
		sleep(800);
//		return new Inventory(15);
		throw new RuntimeException("Warehouse API down!"); // simulate failure
	}

	private static Review fetchReviews(int productId) {
		sleep(300);
		return new Review(4.5);
	}

	private static void sleep(long ms) {
		try {
			Thread.sleep(ms);
		} catch (InterruptedException ignored) {
		}
	}

	// Helper to hold partial state before combining
	static class PartialProduct {
		ProductInfo info;
		Inventory inv;

		PartialProduct(ProductInfo info, Inventory inv) {
			this.info = info;
			this.inv = inv;
		}
	}
}
