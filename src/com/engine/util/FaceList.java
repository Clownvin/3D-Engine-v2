package com.engine.util;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

import com.engine3d.environment.Environment;
import com.engine3d.environment.LightSource;
import com.engine3d.graphics.Camera;
import com.engine3d.math.Face;
import com.engine3d.math.MathUtils;
import com.engine3d.math.Point2D;
import com.engine3d.math.Point3D;
import com.engine3d.math.Triangle;

/**
 * A class comprised of methods and containers to simulate layers on which
 * models reside before being rendered.
 * 
 * The lower the layer, the sooner it get's drawn.
 */
public final class FaceList {
	private static final ArrayList<Face> FACE_LIST = new ArrayList<Face>();
	private static final FaceList SINGLETON = new FaceList();

	private FaceList() {
		// To prevent instantiation
	}

	public static FaceList getSingleton() {
		return SINGLETON;
	}

	public void queueFacesToList(Face[] faces) {
		for (Face face : faces) {
			FACE_LIST.add(face);
		}
	}

	public void queueFaceToList(Face face) {
		FACE_LIST.add(face);
	}

	// Sorts faces by distance, converts to triangles, and keeps the ones that
	// are visible
	public ArrayList<Triangle> grabTriangles(Point3D p) {
		Face[] layerFaces = sortByDistance(FACE_LIST.toArray(new Face[FACE_LIST.size()]), p);
		LightSource[] lights = Environment.grabEnvironmentLights();
		Triangle t;
		int sum;
		ArrayList<Triangle> triangles = new ArrayList<Triangle>(FACE_LIST.size());
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
		return triangles;
	}

	private Face[] sortByDistance(Face[] faces, final Point3D p) {
		final Comparator<Face> faceComparator = new Comparator<Face>() {
			@Override
			public int compare(Face o1, Face o2) {
				return MathUtils.distance(o2.getAveragePoint(), p) - MathUtils.distance(o1.getAveragePoint(), p);
			}
		};
		Arrays.sort(faces, faceComparator);
		return faces;
	}

	public void clear() {
		FACE_LIST.clear();
	}
}
