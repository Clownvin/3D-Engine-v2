package com.engine.environment;

import com.engine.math.Outline;
import com.engine.math.Point3D;
import com.engine.util.IDTagSystem.IDTag;

public final class EnvironmentObject {
	private final IDTag idTag;
	private Point3D coordinates;
	private float rotation = 0;
	private int modelID;
	private Outline outline;

	public EnvironmentObject(Point3D coordinates, int modelID, IDTag idTag) {
		this.coordinates = coordinates;
		this.modelID = modelID;
		this.idTag = idTag;
	}

	public Outline getOutline() {
		return outline;
	}

	public EnvironmentObject setOutline(Outline outline) {
		this.outline = outline;
		return this;
	}

	public Point3D getCoordinates() {
		return coordinates;
	}

	public int getModelID() {
		return modelID;
	}

	public float getRotation() {
		return rotation;
	}

	public void setCoordinates(Point3D coordinates) {
		this.coordinates = coordinates;
	}

	public void setModel(int modelID) {
		this.modelID = modelID;
	}

	public void setRotation(float rotation) {
		this.rotation = rotation;
	}

	public int getID() {
		return idTag.getID();
	}
}
