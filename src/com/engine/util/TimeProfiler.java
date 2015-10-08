package com.engine.util;

public final class TimeProfiler {
	public static final int TYPE_MILLI = 0;
	public static final int TYPE_NANO = 1;
	private final int type;
	private final String name;
	private final int DEFAULT_OUTPUT_FREQUENCY = 100;
	private int outputFrequency = DEFAULT_OUTPUT_FREQUENCY;
	private int count = 0;
	private long cumulative = 0L;
	private long start = 0L;

	public TimeProfiler(String name, int type) {
		this.name = name;
		this.type = type;
	}

	public TimeProfiler(String name, int type, int outputFrequency) {
		this.name = name;
		this.type = type;
		this.outputFrequency = outputFrequency;
	}

	public void start() {
		switch (type) {
		case 0:
			start = System.currentTimeMillis();
			return;
		case 1:
			start = System.nanoTime();
			return;
		default:
			System.err.println("Unknown type detected in TimerProfile.start: " + type);
		}
	}

	public void stop() {
		switch (type) {
		case 0:
			cumulative += System.currentTimeMillis() - start;
			if (++count == outputFrequency) {
				System.out.println(name + ", average time over " + outputFrequency + " ticks: "
						+ (cumulative / outputFrequency) + "ms");
				count = 0;
				cumulative = 0;
			}
			return;
		case 1:
			cumulative += System.nanoTime() - start;
			if (++count == outputFrequency) {
				System.out.println(name + ", average time over " + outputFrequency + " ticks: "
						+ (cumulative / outputFrequency) + "ns");
				count = 0;
				cumulative = 0;
			}
			return;
		default:
			System.err.println("Unknown type detected in TimerProfile.stop: " + type);
			return;
		}
	}
}
