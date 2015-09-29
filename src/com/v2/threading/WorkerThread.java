package com.v2.threading;

public final class WorkerThread extends Thread implements Runnable {
	private final ThreadPool threadPool;
	private boolean destroy = false;
	private ThreadTask task = null;
	private boolean isWorking = false;

	public WorkerThread(ThreadPool threadPool) {
		this.threadPool = threadPool;
		this.start();
	}

	@Override
	public void run() {
		while (!destroy) {
			synchronized (threadPool) {
				threadPool.notifyAll();
			}
			while (!ThreadPool.hasTask()) {
				synchronized (threadPool) {
					try {
						threadPool.wait();
					} catch (InterruptedException e) {
					}
				}
			}
			task = ThreadPool.getNextTask();
			if (task != null) {
				isWorking = true;
				do {
					task.doTask();
				} while (!task.reachedEnd());
				task.end();
			}
			isWorking = false;
		}
	}

	public void destroy() {
		destroy = true;
	}

	public boolean isWorking() {
		return isWorking;
	}
}
