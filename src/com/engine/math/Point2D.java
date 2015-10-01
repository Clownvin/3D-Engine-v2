package com.engine.math;

public final class Point2D {
	private float x = 0f, y = 0f;

	public Point2D(final float x, final float y) {
		this.x = x;
		this.y = y;
	}

	public Point2D setX(final float x) {
		this.x = x;
		return this;
	}

	public Point2D setY(final float y) {
		this.y = y;
		return this;
	}

	public float getX() {
		return x;
	}

	public float getY() {
		return y;
	}

	@Override
	public String toString() {
		return "(" + x + "," + y + ")";
	}
}
