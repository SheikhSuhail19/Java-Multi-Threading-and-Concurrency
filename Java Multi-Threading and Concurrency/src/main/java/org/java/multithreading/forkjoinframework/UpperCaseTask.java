package org.java.multithreading.forkjoinframework;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;

public class UpperCaseTask extends RecursiveAction {
	private static final int THRESHOLD = 10;
	private final String[] array;
	private final int start;
	private final int end;

	public UpperCaseTask(String[] array, int start, int end) {
		this.array = array;
		this.start = start;
		this.end = end;
	}

	@Override
	protected void compute() {
		int length = end - start;
		if (length <= THRESHOLD) {
			for (int i = start; i < end; i++) {
				array[i] = array[i].toUpperCase();
			}
		} else {
			int mid = start + length / 2;
			UpperCaseTask leftTask = new UpperCaseTask(array, start, mid);
			UpperCaseTask rightTask = new UpperCaseTask(array, mid, end);
			invokeAll(leftTask, rightTask);
		}
	}
}

class ForkJoinUpperCaseExample {
	public static void main(String[] args) {
		String[] words = {"fork", "join", "framework", "java", "parallel", "processing", "task", "compute", "divide", "conquer"};
		ForkJoinPool pool = new ForkJoinPool();
		UpperCaseTask task = new UpperCaseTask(words, 0, words.length);
		pool.invoke(task);
		for (String word : words) {
			System.out.println(word);
		}
	}
}
