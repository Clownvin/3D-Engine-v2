package com.engine.math;

public final class Outline extends Polygon {
	private final int sourceID;

	public Outline(Point2D[] points, int sourceID) {
		super(points);
		this.sourceID = sourceID;
	}

	public int getSourceID() {
		return sourceID;
	}

	public int[] getXValues() {
		int[] x = new int[points.length];
		for (int i = 0; i < points.length; i++) {
			x[i] = (int) points[i].getX();
		}
		return x;
	}

	public int[] getYValues() {
		int[] y = new int[points.length];
		for (int i = 0; i < points.length; i++) {
			y[i] = (int) points[i].getY();
		}
		return y;
	}
}
