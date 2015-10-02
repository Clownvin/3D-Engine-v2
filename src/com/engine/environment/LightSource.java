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

	public Color getColor() {
		return color;
	}

	public Point3D getCoordinates() {
		return coordinates;
	}

	public float getIntensity() {
		return intensity;
	}

	public void setColor(Color color) {
		this.color = color;
	}

	public void setCoordinates(Point3D coordinates) {
		this.coordinates = coordinates;
	}

	public void setIntensity(float intensity) {
		this.intensity = intensity;
	}
}
