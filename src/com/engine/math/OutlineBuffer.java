package com.engine.math;

import java.util.ArrayList;

public final class OutlineBuffer {
	private final ArrayList<Point2D> points = new ArrayList<Point2D>();
	private final int sourceID;

	public OutlineBuffer(int sourceID) {
		this.sourceID = sourceID;
	}

	public OutlineBuffer addPoint(Point2D point) {
		points.add(point);
		return this;
	}

	public OutlineBuffer addPoints(Point2D[] points) {
		for (int i = 0; i < points.length; i++) {
			this.points.add(points[i]);
		}
		return this;
	}

	public Outline createOutline() {
		return new Outline(MathUtils.convexHull(points.toArray(new Point2D[points.size()])).getPoints(), sourceID);
	}

	public int getSourceID() {
		return sourceID;
	}
}
