package com.engine.threading;

public interface ThreadTask {
	public boolean reachedEnd();

	public void doTask();

	public void end();
}
