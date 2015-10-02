package com.engine.threading;

import com.engine.util.CycleStack;

public final class ThreadPool extends Thread implements Runnable {
	private static final int DEFAULT_WORKERTHREADS_LENGTH = 100;
	private static WorkerThread[] workerThreads = new WorkerThread[DEFAULT_WORKERTHREADS_LENGTH];
	private static final CycleStack<ThreadTask> queuedTasks = new CycleStack<ThreadTask>(10000);
	private static final ThreadPool SINGLETON = new ThreadPool();

	static {
		for (int i = 0; i < workerThreads.length; i++) {
			workerThreads[i] = new WorkerThread(SINGLETON);
		}
		SINGLETON.start();
	}

	public static void addTask(ThreadTask task) {
		synchronized (queuedTasks) {
			queuedTasks.add(task);
		}
		synchronized (SINGLETON) {
			SINGLETON.notify();
		}
	}

	public static ThreadTask getNextTask() {
		synchronized (queuedTasks) {
			if (!hasTask()) 
				return null;
			return queuedTasks.removeNext();
		}
	}

	public static ThreadPool getSingleton() {
		return SINGLETON;
	}

	public static boolean hasTask() {
		synchronized (queuedTasks) {
			return queuedTasks.size() > 0;
		}
	}

	public static int threadsSleeping() {
		int count = 0;
		synchronized (workerThreads) {
			for (int i = 0; i < workerThreads.length; i++) {
				if (!workerThreads[i].isWorking()) {
					count++;
				}
			}
		}
		return count;
	}

	private ThreadPool() {
		// To prevent instantiation.
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
