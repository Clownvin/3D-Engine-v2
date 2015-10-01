package com.engine.rendering;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

import com.engine.environment.Environment;
import com.engine.environment.LightSource;
import com.engine.math.Face;
import com.engine.math.MathUtils;
import com.engine.math.Point2D;
import com.engine.math.Point3D;
import com.engine.math.Triangle;
import com.engine.util.ColorPalette;

public final class RenderManager extends Thread {
	private static volatile ArrayList<Triangle> triangleList = new ArrayList<Triangle>();
	private static volatile Face[] faceList = new Face[0];
	private static volatile boolean kill = false;
	private static final RenderManager SINGLETON = new RenderManager();
	
	private RenderManager() {
		//To prevent instantiation.
		this.start();
	}
	
	public static RenderManager getSingleton() {
		return SINGLETON;
	}
	
	public static Triangle[] getTriangles() {
		synchronized (triangleList) {
			return triangleList.toArray(new Triangle[triangleList.size()]);
		}
	}
	
	@Override
	public void run() {
		while (!kill) { // Lazy, replace with end condition.
			faceList = Environment.grabEnvironmentFaces();
			renderTriangles();
		}
	}
	
	public static void kill() {
		kill = true;
	}
	
	private void renderTriangles() {
		Face[] layerFaces = sortByDistance(faceList, Camera.getSingleton().getCoordinates());
		LightSource[] lights = Environment.grabEnvironmentLights();
		Triangle t;
		int sum;
		ArrayList<Triangle> triangles = new ArrayList<Triangle>(faceList.length);
		for (Face face : layerFaces) {
			if (face == null || face.getPoints().length != 3)
				continue;
			t = new Triangle(new Point2D[] { Camera.getSingleton().translatePoint3D(face.getPoints()[0]),
					Camera.getSingleton().translatePoint3D(face.getPoints()[1]),
					Camera.getSingleton().translatePoint3D(face.getPoints()[2]) }, face.getColor());
			sum = 0;
			for (int i = 0; i < 3; i++) {
				sum += ((t.getPoints()[(i + 1) % 3].getX() - t.getPoints()[i].getX())
						* (t.getPoints()[(i + 1) % 3].getY() + t.getPoints()[i].getY()));
			}
			if ((sum < 0 || face.showBackFace()) && (((t.getLX() < VideoSettings.getOutputWidth() && t.getHX() > 0)
					&& (t.getLY() < VideoSettings.getOutputHeight() && t.getHY() > 0)))) {
				t.setColor(face.getColor());
				float percent = 1.0f;
				float calc = 0.0f;
				Color primaryLightColor = Color.WHITE;
				for (LightSource light : lights) {
					float angle = MathUtils.calculateAngleRelativeToNormal(face, light.getCoordinates(),
							MathUtils.calculateSurfaceNormal(face));
					calc = ((angle / 180.0f) * (MathUtils.distance(face.getAveragePoint(), light.getCoordinates())
							/ light.getIntensity()));
					primaryLightColor = calc < percent ? light.getColor() : Color.WHITE;
					percent = calc < percent ? calc : percent;
				}
				t.setColor(ColorPalette.darken(ColorPalette.lightFilter(t.getColor(), primaryLightColor), percent));
				triangles.add(t);
			}
		}
		synchronized (triangleList) {
			triangleList = triangles;
		}
	}

	private Face[] sortByDistance(final Face[] faces, final Point3D point) {
		Point3D newPoint = new Point3D(point.getX(), point.getY(), point.getZ());
		final Comparator<Face> faceComparator = new Comparator<Face>() {
			@Override
			public int compare(Face o1, Face o2) {
				return MathUtils.distance(o2.getAveragePoint(), newPoint) - MathUtils.distance(o1.getAveragePoint(), newPoint);
			}
		};
		Arrays.sort(faces, faceComparator);
		return faces;
	}
}
