package com.engine.util;

import java.awt.Dimension;
import java.awt.Toolkit;

public final class VideoSettings {
	private static final VideoSettings SINGLETON = new VideoSettings();
	private static final Dimension DIMENSIONS = new Dimension();
	private static final int MAX_OUTPUT_WIDTH = Toolkit.getDefaultToolkit().getScreenSize().width + 200;
	private static final int MAX_OUTPUT_HEIGHT = Toolkit.getDefaultToolkit().getScreenSize().height + 200;

	private VideoSettings() {
		// Prevent instantiation.
	}

	public static VideoSettings getSingleton() {
		return SINGLETON;
	}

	public static int getOutputWidth() {
		return (int) DIMENSIONS.getWidth();
	}

	public static int getOutputHeight() {
		return (int) DIMENSIONS.getHeight();
	}

	public static Dimension getOutputDimensions() {
		return DIMENSIONS;
	}

	public static int getMaxOutputHeight() {
		return MAX_OUTPUT_HEIGHT;
	}

	public static int getMaxOutputWidth() {
		return MAX_OUTPUT_WIDTH;
	}

	public static void setOutputWidth(int newOutputWidth) {
		if (newOutputWidth < 100 || newOutputWidth > MAX_OUTPUT_WIDTH) {
			throw new IllegalArgumentException("New output width, " + newOutputWidth
					+ ", is not within the range of 100 to " + MAX_OUTPUT_WIDTH + ".");
		}
		DIMENSIONS.setSize(newOutputWidth, DIMENSIONS.getHeight());
	}

	public static void setOutputHeight(int newOutputHeight) {
		if (newOutputHeight < 100 || newOutputHeight > MAX_OUTPUT_HEIGHT) {
			throw new IllegalArgumentException("New output height, " + newOutputHeight
					+ ", is not within the range of 100 to " + MAX_OUTPUT_HEIGHT + ".");
		}
		DIMENSIONS.setSize(DIMENSIONS.getWidth(), newOutputHeight);
	}
}
