package org.java.multithreading.virtualthreads;

import java.lang.management.GarbageCollectorMXBean;
import java.lang.management.ManagementFactory;
import java.lang.management.OperatingSystemMXBean;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * ThreadPerfComparisonMetrics
 *
 * <p>This class benchmarks and compares the performance characteristics of
 * traditional Platform Threads versus Virtual Threads (Project Loom) in Java.
 * It demonstrates both I/O-bound and CPU-bound workloads, measuring execution
 * time, thread usage, memory consumption, CPU load, and garbage collection activity.</p>
 *
 * <p><b>Key Scenarios Tested:</b></p>
 * <ul>
 *   <li><b>I/O-Bound Workload:</b> Simulated with a large number of tasks performing Thread.sleep().
 *       Compares how Platform Threads and Virtual Threads handle massive blocking concurrency.</li>
 *   <li><b>CPU-Bound Workload:</b> Simulated with recursive Fibonacci computations.
 *       Demonstrates that Virtual Threads do not inherently speed up CPU-intensive tasks.</li>
 * </ul>
 *
 * <p><b>Metrics Collected:</b></p>
 * <ul>
 *   <li>Active thread count before and after each benchmark.</li>
 *   <li>Memory usage (used and total heap).</li>
 *   <li>CPU system load average.</li>
 *   <li>Garbage collection count and total GC time.</li>
 * </ul>
 *
 * <p><b>Insights Expected:</b></p>
 * <ul>
 *   <li>Virtual Threads excel at scaling massive I/O-bound workloads with minimal threads.</li>
 *   <li>For CPU-bound workloads, Virtual Threads do not provide speedup over a fixed
 *       Platform Thread pool and may have slight overhead.</li>
 *   <li>Memory and GC behavior is predictable and lightweight for Virtual Threads.</li>
 * </ul>
 *
 * <p><b>Usage:</b></p>
 * <pre>{@code
 * java ThreadPerfComparisonMetrics
 * }</pre>
 *
 * <p>This benchmark requires Java 21+ with Project Loom support enabled.</p>
 *
 * @author Sheikh Suhail
 * @version 1.0
 */
public class ThreadPerfComparisonMetrics {

	public static void main(String[] args) throws Exception {
		int tasks = 50_000;
		int sleepMs = 100;

		System.out.println("🔹 Running benchmark with " + tasks + " tasks (I/O-bound sleep + CPU-bound work)");
		System.out.println("=========================================================");

		benchmarkIO("Platform Threads", Executors.newFixedThreadPool(200), tasks, sleepMs);
		benchmarkIO("Virtual Threads", Executors.newVirtualThreadPerTaskExecutor(), tasks, sleepMs);

		int cpuTasks = 5_000;
		System.out.println("\n🔹 Now running CPU-bound benchmark with " + cpuTasks + " tasks");
		System.out.println("=========================================================");
		benchmarkCPU("Platform Threads", Executors.newFixedThreadPool(200), cpuTasks);
		benchmarkCPU("Virtual Threads", Executors.newVirtualThreadPerTaskExecutor(), cpuTasks);
	}

	// I/O Bound Benchmark
	private static void benchmarkIO(String name, ExecutorService executor, int tasks, int sleepMs) throws Exception {
		System.out.println("\n▶️ " + name + " I/O-Bound Benchmark");
		printSystemStats("Before");

		long start = System.currentTimeMillis();
		CountDownLatch latch = new CountDownLatch(tasks);

		for (int i = 0; i < tasks; i++) {
			executor.submit(() -> {
				try {
					Thread.sleep(sleepMs);
				} catch (InterruptedException ignored) {
				}
				latch.countDown();
			});
		}

		latch.await();
		executor.shutdown();

		long duration = System.currentTimeMillis() - start;
		System.out.println("⏱ " + name + " I/O done in: " + duration + " ms");
		printSystemStats("After");
		System.out.println("---------------------------------------------------------");
	}

	// CPU Bound Benchmark
	private static void benchmarkCPU(String name, ExecutorService executor, int tasks) throws Exception {
		System.out.println("\n▶️ " + name + " CPU-Bound Benchmark");
		printSystemStats("Before");

		long start = System.currentTimeMillis();
		CountDownLatch latch = new CountDownLatch(tasks);

		for (int i = 0; i < tasks; i++) {
			executor.submit(() -> {
				fib(35); // heavy computation
				latch.countDown();
			});
		}

		latch.await();
		executor.shutdown();

		long duration = System.currentTimeMillis() - start;
		System.out.println("⏱ " + name + " CPU done in: " + duration + " ms");
		printSystemStats("After");
		System.out.println("---------------------------------------------------------");
	}

	// Recursive Fibonacci for CPU stress
	private static long fib(int n) {
		if (n <= 1) return n;
		return fib(n - 1) + fib(n - 2);
	}

	// Print system + GC stats
	private static void printSystemStats(String label) {
		Runtime runtime = Runtime.getRuntime();
		long usedMem = (runtime.totalMemory() - runtime.freeMemory()) / (1024 * 1024);
		long totalMem = runtime.totalMemory() / (1024 * 1024);

		int activeThreads = Thread.activeCount();

		OperatingSystemMXBean osBean = ManagementFactory.getOperatingSystemMXBean();
		double load = osBean.getSystemLoadAverage();

		long gcCount = 0;
		long gcTime = 0;
		for (GarbageCollectorMXBean gcBean : ManagementFactory.getGarbageCollectorMXBeans()) {
			gcCount += gcBean.getCollectionCount();
			gcTime += gcBean.getCollectionTime();
		}

		System.out.printf("   [%s] Threads: %d | Used Mem: %d MB / %d MB | CPU Load Avg: %.2f | GC Count: %d | GC Time: %d ms%n",
				label, activeThreads, usedMem, totalMem, load, gcCount, gcTime);
	}
}
