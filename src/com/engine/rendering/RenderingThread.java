package com.engine.rendering;

import java.awt.Color;
import java.util.ArrayList;

import com.engine.environment.LightSource;
import com.engine.math.Face;
import com.engine.math.MathUtils;
import com.engine.math.Point2D;
import com.engine.math.Triangle;
import com.engine.util.ColorPalette;

public class RenderingThread extends Thread {
	private static volatile int counter = 0;
	private volatile Face[] faceArray;
	private volatile ArrayList<Triangle> triangles;
	private volatile LightSource[] lights;
	private volatile boolean finished = false;
	private volatile boolean kill = false;
	private final RenderManager renderingManager;

	public RenderingThread(RenderManager renderingManager) {
		this.renderingManager = renderingManager;
		this.start();
	}

	public synchronized ArrayList<Triangle> getTriangles() {
		return triangles;
	}

	public synchronized boolean isFinished() {
		return finished;
	}

	public synchronized void kill() {
		kill = true;
	}

	@Override
	public void run() {
		this.setPriority(MAX_PRIORITY);
		while (!kill) {
			try {
				synchronized (this) {
					this.wait();
				}
			} catch (InterruptedException e) {
			}
			finished = false;
			int thisCount = counter++;
			for (Face face : faceArray) {
				if (face == null || face.getPoints().length != 3)
					continue;
				Triangle t = new Triangle(new Point2D[] { Camera.getSingleton().translatePoint3D(face.getPoints()[0]),
						Camera.getSingleton().translatePoint3D(face.getPoints()[1]),
						Camera.getSingleton().translatePoint3D(face.getPoints()[2]) }, face.getColor());
				if (!(((t.getLX() < VideoSettings.getOutputWidth() && t.getHX() > 0)
						&& (t.getLY() < VideoSettings.getOutputHeight() && t.getHY() > 0)))) {
					continue;
				}
				int sum = 0;
				for (int i = 0; i < 3; i++) {
					sum += ((t.getPoints()[(i + 1) % 3].getX() - t.getPoints()[i].getX())
							* (t.getPoints()[(i + 1) % 3].getY() + t.getPoints()[i].getY()));
				}
				if ((sum < 0 || face.showBackFace())) {
					t.setColor(face.getColor());
					if (lights.length > 0) {
						double percent = 1.0f;
						double calc = 0.0f;
						Color primaryLightColor = Color.WHITE;
						for (LightSource light : lights) {
							double angle = MathUtils.calculateAngleRelativeToNormal(face, light.getCoordinates(),
									MathUtils.calculateSurfaceNormal(face));
							calc = ((angle / 180.0f)
									* (MathUtils.distance(face.getAveragePoint(), light.getCoordinates())
											/ light.getIntensity()));
							primaryLightColor = calc < percent ? light.getColor() : primaryLightColor;
							percent = calc < percent ? calc : percent;
						}
						t.setColor(ColorPalette.darken(ColorPalette.lightFilter(t.getColor(), primaryLightColor),
								percent));
						t.setSourceID(face.getSourceID());
					}
					/*
					 * if (MathUtils.isInside(t.getPoints(), mousePoint)) {
					 * boolean exists = false; for (int i = 0; i <
					 * outlineBuffers.size(); i++) { if
					 * (outlineBuffers.get(i).getSourceID() ==
					 * face.getSourceID()) exists = true; } if (!exists)
					 * outlineBuffers.add(new
					 * OutlineBuffer(face.getSourceID())); }
					 */
					triangles.add(t);
				}
			}
			finished = true;
			synchronized (renderingManager) {
				renderingManager.notifyAll();
			}
		}
	}

	public synchronized void startRender(Face[] faceArray, LightSource[] lights) {
		this.faceArray = faceArray;
		this.lights = lights;
		triangles = new ArrayList<Triangle>(faceArray.length);
		synchronized (this) {
			this.notifyAll();
		}
	}
}
