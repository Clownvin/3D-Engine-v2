package com.engine.environment;

import com.engine.math.Point3D;

public final class EnvironmentObject {
	private Point3D coordinates;
	private float rotation = 0;
	private int modelID;

	public EnvironmentObject(Point3D coordinates, int modelID) {
		this.coordinates = coordinates;
		this.modelID = modelID;
	}

	public int getModelID() {
		return modelID;
	}

	public void setModel(int modelID) {
		this.modelID = modelID;
	}

	public Point3D getCoordinates() {
		return coordinates;
	}

	public float getRotation() {
		return rotation;
	}

	public void setRotation(float rotation) {
		this.rotation = rotation;
	}

	public void setCoordinates(Point3D coordinates) {
		this.coordinates = coordinates;
	}
}
