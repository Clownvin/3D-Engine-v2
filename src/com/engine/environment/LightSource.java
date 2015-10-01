package com.engine.environment;

import java.awt.Color;

import com.engine.math.Point3D;

public final class LightSource {
	private Point3D coordinates;
	private float intensity;
	private Color color = Color.WHITE;

	public LightSource(Point3D coordinates, float intensity) {
		this.coordinates = coordinates;
		this.intensity = intensity;
	}

	public void setIntensity(float intensity) {
		this.intensity = intensity;
	}

	public void setCoordinates(Point3D coordinates) {
		this.coordinates = coordinates;
	}

	public void setColor(Color color) {
		this.color = color;
	}

	public float getIntensity() {
		return intensity;
	}

	public Color getColor() {
		return color;
	}

	public Point3D getCoordinates() {
		return coordinates;
	}
}
