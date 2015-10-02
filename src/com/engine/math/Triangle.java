package com.engine.math;

import java.awt.Color;

public final class Triangle extends Polygon {
	private Color color;
	private int lx = Integer.MAX_VALUE, ly = Integer.MAX_VALUE, hx = Integer.MIN_VALUE, hy = Integer.MIN_VALUE;
	private int sourceID = -1;

	public Triangle(Point2D[] points, Color color) {
		super(points);
		this.color = color;
		for (Point2D point : points) {
			if (point.getX() < lx) {
				this.lx = point.getX();
			}
			if (point.getX() > hx) {
				this.hx = point.getX();
			}
			if (point.getY() < ly) {
				this.ly = point.getY();
			}
			if (point.getY() > hy) {
				this.hy = point.getY();
			}
		}
	}
	
	public Triangle setSourceID(int sourceID) {
		this.sourceID = sourceID;
		return this;
	}
	
	public int getSourceID() {
		return sourceID;
	}

	public Color getColor() {
		return color;
	}

	public float getHX() {
		return hx;
	}

	public float getHY() {
		return hy;
	}

	public float getLX() {
		return lx;
	}

	public float getLY() {
		return ly;
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

	public void setColor(Color color) {
		this.color = color;
	}
}
