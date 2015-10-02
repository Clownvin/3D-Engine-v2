package com.engine.threading;

public interface ThreadTask {
	public void doTask();

	public void end();

	public boolean reachedEnd();
}
