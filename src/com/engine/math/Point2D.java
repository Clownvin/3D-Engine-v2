package com.engine.math;

public final class Point2D implements Comparable<Point2D> {
	private final int x, y;

	public Point2D(final int x, final int y) {
		this.x = x;
		this.y = y;
	}

	@Override
	public int compareTo(Point2D p) {
		int xvals = (p.getX() - x);
		if (xvals == 0) {
			return (p.getY() - y);
		}
		return xvals;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public Point2D setX(final int newX) {
		return new Point2D(newX, y);
	}

	public Point2D setY(final int newY) {
		return new Point2D(x, newY);
	}

	@Override
	public String toString() {
		return "(" + x + "," + y + ")";
	}
}
