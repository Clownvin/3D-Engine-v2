package com.engine3d.math;

import java.awt.Color;

public final class Triangle {
	private Point2D[] points;
	private Color color;
	private float lx = Integer.MAX_VALUE, ly = Integer.MAX_VALUE, hx = Integer.MIN_VALUE, hy = Integer.MIN_VALUE;

	public Triangle(Point2D[] points, Color color) {
		this.points = points;
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

	public Point2D[] getPoints() {
		return points;
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

	public Color getColor() {
		return color;
	}

	public float getLX() {
		return lx;
	}

	public float getLY() {
		return ly;
	}

	public float getHX() {
		return hx;
	}

	public float getHY() {
		return hy;
	}
	
	public void setColor(Color color) {
		this.color = color;
	}
}
