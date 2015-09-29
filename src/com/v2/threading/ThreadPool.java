package com.v2.threading;

import com.engine.util.CyclicArrayList;
import com.engine.util.MaximumCapacityReachedException;

public final class ThreadPool extends Thread implements Runnable {
	private static final int DEFAULT_WORKERTHREADS_LENGTH = 10;
	private static WorkerThread[] workerThreads = new WorkerThread[DEFAULT_WORKERTHREADS_LENGTH];
	private static final CyclicArrayList<ThreadTask> queuedTasks = new CyclicArrayList<ThreadTask>(10000);
	private static final ThreadPool SINGLETON = new ThreadPool();

	private ThreadPool() {
		// To prevent instantiation.
	}

	static {
		for (int i = 0; i < workerThreads.length; i++) {
			workerThreads[i] = new WorkerThread(SINGLETON);
		}
		SINGLETON.start();
	}

	public static ThreadPool getSingleton() {
		return SINGLETON;
	}

	public static void addTask(ThreadTask task) throws MaximumCapacityReachedException {
		try {
			synchronized (queuedTasks) {
				queuedTasks.add(task);
			}
			synchronized (SINGLETON) {
				SINGLETON.notify();
			}
		} catch (MaximumCapacityReachedException e) {
			throw e;
		}
	}

	public static boolean hasTask() {
		return queuedTasks.size() > 0;
	}

	public static ThreadTask getNextTask() {
		synchronized (queuedTasks) {
			return queuedTasks.removeNext();
		}
	}

	@Override
	public void run() {
		while (true) {
			// Regular mode (Doesn't do anything)
			while (queuedTasks.size() == 0) {
				synchronized (SINGLETON) {
					try {
						SINGLETON.wait();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
			// Queue mode (Only use queue tasks)
			while (queuedTasks.size() > 0) {
				synchronized (SINGLETON) {
					SINGLETON.notify();
				}
			}
		}
	}
}
