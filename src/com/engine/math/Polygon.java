package com.engine.math;

public class Polygon {
	protected Point2D[] points;

	public Polygon(Point2D[] points) {
		this.points = points;
	}

	public Point2D[] getPoints() {
		return points;
	}
}
