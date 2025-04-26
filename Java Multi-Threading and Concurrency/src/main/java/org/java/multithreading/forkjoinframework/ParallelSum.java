package org.java.multithreading.forkjoinframework;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

public class ParallelSum extends RecursiveTask<Long> {
	private static final int THRESHOLD = 1000;
	private final long[] array;
	private final int start, end;

	public ParallelSum(long[] array, int start, int end) {
		this.array = array;
		this.start = start;
		this.end = end;
	}

	@Override
	protected Long compute() {
		int length = end - start;
		if (length <= THRESHOLD) {
			long sum = 0;
			for (int i = start; i < end; i++) {
				sum += array[i];
			}
			return sum;
		}
		int mid = start + length / 2;
		ParallelSum leftTask = new ParallelSum(array, start, mid);
		ParallelSum rightTask = new ParallelSum(array, mid, end);
		leftTask.fork();
		long rightResult = rightTask.compute();
		long leftResult = leftTask.join();
		return leftResult + rightResult;
	}

	public static void main(String[] args) {
		long[] array = new long[10000];
		for (int i = 0; i < array.length; i++) {
			array[i] = i;
		}
		ForkJoinPool pool = new ForkJoinPool();
		ParallelSum task = new ParallelSum(array, 0, array.length);
		long result = pool.invoke(task);
		System.out.println("Sum: " + result);
	}
}