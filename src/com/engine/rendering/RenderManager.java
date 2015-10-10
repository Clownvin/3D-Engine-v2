/*
 * Notes 10/2/2015:
 * 	It appears that the only thing really affecting the time of this method is the amount of faces being rendered.
 * 
 * Ideas:
 * 
 * To solve the issue with outlines, consider only creating outlines for objects roughly within range of mouse
 * 
 * To solve lighting, consider moving it somewhere else, such as directly affecting faces before they even get here.
 * Or even have a separate lighting thread specifically for the purpose of lighting calculations. 
 * 
 * Remember: It's often more efficient to check if you should do something before doing something to everything.
 * 
 * 
 */

package com.engine.rendering;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

import com.engine.environment.Environment;
import com.engine.environment.LightSource;
import com.engine.io.MouseHandler;
import com.engine.math.Face;
import com.engine.math.MathUtils;
import com.engine.math.OutlineBuffer;
import com.engine.math.Point2D;
import com.engine.math.Point3D;
import com.engine.math.Triangle;
import com.engine.util.ColorPalette;
import com.engine.util.TimeProfiler;

public final class RenderManager extends Thread {
	private static volatile ArrayList<Triangle> triangleList = new ArrayList<Triangle>();
	private static int[] lastOutlineIDs = new int[0];
	private static volatile Face[] faceList = new Face[0];
	private static volatile boolean kill = false;
	private static final RenderManager SINGLETON = new RenderManager();

	public static RenderManager getSingleton() {
		return SINGLETON;
	}

	public static Triangle[] getTriangles() {
		synchronized (triangleList) {
			return triangleList.toArray(new Triangle[triangleList.size()]);
		}
	}

	public static void kill() {
		kill = true;
	}
	

	private RenderManager() {
		this.setPriority(MAX_PRIORITY);
		this.start();
	}

	TimeProfiler outlineProfiler = new TimeProfiler("Outline", TimeProfiler.TYPE_NANO, 1000);
	TimeProfiler renderProfiler = new TimeProfiler("Render", TimeProfiler.TYPE_NANO, 1000);
	
	
	private void renderTriangles() {
		Face[] layerFaces = sortByDistance(faceList, Camera.getSingleton().getCoordinates());
		LightSource[] lights = Environment.grabEnvironmentLights();
		ArrayList<Triangle> triangles = new ArrayList<Triangle>(faceList.length);
		ArrayList<OutlineBuffer> outlineBuffers = new ArrayList<OutlineBuffer>();
		Triangle t;
		int sum;
		double percent;
		double angle;
		Point2D mousePoint = new Point2D((int) MouseHandler.getMouseX(), (int) MouseHandler.getMouseY());
		renderProfiler.start();
		for (Face face : layerFaces) {
			if (face == null || face.getPoints().length != 3)
				continue;
			t = new Triangle(new Point2D[] { Camera.getSingleton().translatePoint3D(face.getPoints()[0]),
					Camera.getSingleton().translatePoint3D(face.getPoints()[1]),
					Camera.getSingleton().translatePoint3D(face.getPoints()[2]) }, face.getColor());
			if (!(((t.getLX() < VideoSettings.getOutputWidth() && t.getHX() > 0)
					&& (t.getLY() < VideoSettings.getOutputHeight() && t.getHY() > 0)))) {
				continue;
			}
			sum = 0;
			for (int i = 0; i < 3; i++) {
				sum += ((t.getPoints()[(i + 1) % 3].getX() - t.getPoints()[i].getX())
						* (t.getPoints()[(i + 1) % 3].getY() + t.getPoints()[i].getY()));
			}
			if ((sum < 0 || face.showBackFace())) {
				t.setColor(face.getColor());
				if (lights.length > 0) {
					Color[] c = new Color[lights.length];
					float[] p = new float[lights.length];
					int idx = 0;
					percent = 1.0f;
					for (LightSource light : lights) {
						angle = MathUtils.calculateAngleRelativeToNormal(face, light.getCoordinates(),
								MathUtils.calculateSurfaceNormal(face));
						p[idx] = (float) ((angle / 180.0f) * (MathUtils.distance(face.getAveragePoint(), light.getCoordinates())
								/ light.getIntensity()));
						percent = p[idx] < percent ? p[idx] : percent;
						c[idx++] = light.getColor();
						
					}
					t.setColor(ColorPalette.darken(ColorPalette.lightFilter(t.getColor(), ColorPalette.mixByPercent(c, p)), percent));
					t.setSourceID(face.getSourceID());
				}
				if (MathUtils.isInside(t.getPoints(), mousePoint)) {
					boolean exists = false;
					for (int i = 0; i < outlineBuffers.size(); i++) {
						if (outlineBuffers.get(i).getSourceID() == face.getSourceID())
							exists = true;
					}
					if (!exists)
						outlineBuffers.add(new OutlineBuffer(face.getSourceID()));
				}
				triangles.add(t);
			}
		}
		renderProfiler.stop();
		
		outlineProfiler.start();
		outer: for (int i = 0; i < lastOutlineIDs.length; i++) {
			for (OutlineBuffer buffer : outlineBuffers) {
				if (buffer.getSourceID() == lastOutlineIDs[i])
					continue outer;
			}
			try {
			Environment.getObjectForID(lastOutlineIDs[i]).setOutline(null);
			} catch (NullPointerException e) {};
		}
		lastOutlineIDs = new int[outlineBuffers.size()];
		for (int i = 0; i < outlineBuffers.size(); i++) {
			lastOutlineIDs[i] = outlineBuffers.get(i).getSourceID();
			for (int j = 0; j < triangles.size(); j++) {
				if (triangles.get(j).getSourceID() == outlineBuffers.get(i).getSourceID()) {
					outlineBuffers.get(i).addPoints(triangles.get(j).getPoints());
				}
			}
			try {
			Environment.getObjectForID(outlineBuffers.get(i).getSourceID())
					.setOutline(outlineBuffers.get(i).createOutline());
			} catch (NullPointerException e) {
				
			}
		}
		outlineProfiler.stop();
		synchronized (triangleList) {
			triangleList = triangles;
		}
	}

	@Override
	public void run() {
		TimeProfiler rmMainProfiler = new TimeProfiler("RM Main", TimeProfiler.TYPE_MILLI, 1000);
		while (!kill) {
			rmMainProfiler.start();
			Camera.getSingleton().updateCoordinates();
			faceList = Environment.grabEnvironmentFaces();
			renderTriangles();
			rmMainProfiler.stop();
		}
	}

	private Face[] sortByDistance(final Face[] faces, final Point3D point) {
		Point3D newPoint = new Point3D(point.getX(), point.getY(), point.getZ());
		final Comparator<Face> faceComparator = new Comparator<Face>() {
			@Override
			public int compare(Face o1, Face o2) {
				return MathUtils.distance(o2.getAveragePoint(), newPoint)
						- MathUtils.distance(o1.getAveragePoint(), newPoint);
			}
		};
		Arrays.sort(faces, faceComparator);
		return faces;
	}
}

/*
 * 
		*/
