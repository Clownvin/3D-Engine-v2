package com.engine.rendering;

import com.engine.math.Point2D;
import com.engine.math.Point3D;

/*
 * 
 * This is a pretty strange file right now. It's based off of math that I found through
 * Research on the Internet. The math for finding the camera's Z position is in desperate need of repair.
 *
 */
public final class Camera {
	private final double DEG_TO_RAD = 0.017453292;
	private Point3D coordinates = new Point3D(0, 0, 0f);
	private final Point3D screenPosition = new Point3D(0, 0, 40 * 48);
	// View angle z is only one ever used?
	private final Point3D viewAngle = new Point3D(0, 0, 10);
	private static final Camera SINGLETON = new Camera();
	private float zoom = 70000f;
	private float degTheta = 180f;
	private float degPhi = 150f;
	private float radTheta = (float) ((180) * DEG_TO_RAD);
	private float radPhi = (float) ((150) * DEG_TO_RAD);
	private float ctcp = 0f;
	private float ctsp = 0f;
	private float stcp = 0f;
	private float stsp = 0f;
	private float ct = 0f;
	private float st = 0f;
	private float cp = 0f;
	private float sp = 0f;
	private Point2D screenCenter = new Point2D(0, 0);

	public void changeZoom(int i) {
		zoom += i;
		if (zoom < 40) {
			zoom = 40;
		}
		if (zoom > Integer.MAX_VALUE) {
			zoom = Integer.MAX_VALUE;
		}
	}

	public Point3D getViewAngle() {
		return viewAngle;
	}

	public Point3D getScreenPosition() {
		return screenPosition;
	}

	public Point3D getCoordinates() {
		return coordinates;
	}

	public float getDegreePhi() {
		return degPhi;
	}

	public float getDegreePhi(float n) {
		return degPhi + n;
	}

	public float getDegreeTheta() {
		return degTheta - 90;
	}

	public float getDegreeTheta(float n) {
		return degTheta + n + 90;
	}

	public float getRadianPhi() {
		return radPhi;
	}

	public float getRadianPhi(float n) {
		return radPhi + n;
	}

	public float getRadianTheta() {
		return (float) (radTheta - (90 * DEG_TO_RAD));
	}

	public float getRadianTheta(float n) {
		return (float) ((radTheta - (90 * DEG_TO_RAD)) + (n * DEG_TO_RAD));
	}

	public static Camera getSingleton() {
		return SINGLETON;
	}

	public float getZoom() {
		return zoom / 48;
	}

	public void setScreenCenter(Point2D _screenCenter) {
		screenCenter = _screenCenter;
	}

	public Point2D translatePoint3D(Point3D point) {
		double x = screenPosition.x + point.getX() * ct - point.getY() * st;
		double y = screenPosition.y + point.getX() * stsp + point.getY() * ctsp + point.getZ() * cp;
		double temp = viewAngle.z / (screenPosition.z + point.getX() * stcp + point.getY() * ctcp - point.getZ() * sp);
		return new Point2D((float) (screenCenter.getX() + getZoom() * temp * x),
				(float) (screenCenter.getY() + getZoom() * temp * y));
	}

	public void updateCoordinates() {
		float r = ((1 / zoom) * 100000) * 30f;
		float z = (float) (r * Math.sin(getRadianPhi()));
		float rad = (float) (r * Math.cos(getRadianPhi()));
		float y = (float) (rad * Math.sin(getRadianTheta()));
		float x = (float) -(rad * Math.cos(getRadianTheta()));
		ct = (float) Math.cos(radTheta);
		st = (float) Math.sin(radTheta);
		cp = (float) Math.cos(radPhi);
		sp = (float) Math.sin(radPhi);
		ctcp = ct * cp;
		ctsp = ct * sp;
		stcp = st * cp;
		stsp = st * sp;
		coordinates = new Point3D(x, y, z);
	}

	public void updatePhi(float change) { // In degrees
		degPhi += change;
		degPhi %= 360;
		radPhi = (float) (degPhi * DEG_TO_RAD);
		updateCoordinates();
	}

	public void updateTheta(float change) { // In degrees
		degTheta += change;
		degTheta %= 360;
		radTheta = (float) ((degTheta - 90) * DEG_TO_RAD);
		updateCoordinates();
	}

	private Camera() {
		updateCoordinates();
	}
}
