package com.engine.environment;

import java.awt.Color;
import java.util.ArrayList;

import com.engine.math.Face;
import com.engine.math.MathUtils;
import com.engine.math.Point2D;
import com.engine.math.Point3D;
import com.engine.models.ModelManager;
import com.engine.util.IDTagSystem;

//TODO add Scenes, and create a map
public final class Environment {
	private static final int MAX_ENVIRONMENT_OBJECTS = 20000;
	private static final IDTagSystem TAGGER = new IDTagSystem(MAX_ENVIRONMENT_OBJECTS);
	private static final ArrayList<EnvironmentObject> OBJECTS = new ArrayList<EnvironmentObject>();
	private static final ArrayList<LightSource> LIGHTS = new ArrayList<LightSource>();
	private static final Environment SINGLETON = new Environment();

	public static Environment getSingleton() {
		return SINGLETON;
	}

	public static Face[] grabEnvironmentFaces() {
		ArrayList<Face> faceList = new ArrayList<Face>(OBJECTS.size() * 10);
		for (int i = 0; i < OBJECTS.size(); i++) {
			Face[] faces = ModelManager.getModel(OBJECTS.get(i).getModelID()).getFaces();
			for (int j = 0; j < faces.length; j++) {
				faceList.add(faces[j].applyOffest(OBJECTS.get(i).getCoordinates()).setSourceID(OBJECTS.get(i).getID()));
			}
		}
		return faceList.toArray(new Face[faceList.size()]);
	}

	public static LightSource[] grabEnvironmentLights() {
		return LIGHTS.toArray(new LightSource[LIGHTS.size()]);
	}

	public static EnvironmentObject getObjectForID(int id) {
		for (int i = 0; i < OBJECTS.size(); i++) {
			if (OBJECTS.get(i).getID() == id) {
				return OBJECTS.get(i);
			}
		}
		throw new NullPointerException("no object in object list with id " + id);
	}
	
	public static EnvironmentObject getObjectAtIndex(int index) {
		if (index < 0 || index >= OBJECTS.size())
			throw new ArrayIndexOutOfBoundsException("index "+index+" is out of bounds");
		return OBJECTS.get(index);
	}
	
	public static int getObjectListSize() {
		return OBJECTS.size();
	}

	public static EnvironmentObject[] getObjectsAtCoordinate(int x, int y) {
		ArrayList<EnvironmentObject> objects = new ArrayList<EnvironmentObject>();
		for (int i = 0; i < OBJECTS.size(); i++) {
			if (OBJECTS.get(i) != null && OBJECTS.get(i).getOutline() != null)
				if (MathUtils.isInside(OBJECTS.get(i).getOutline().getPoints(), new Point2D(x, y))) {
					objects.add(OBJECTS.get(i));
				}
		}
		return objects.toArray(new EnvironmentObject[objects.size()]);
	}

	private Environment() {
		/*
		 * This section wont exist when compiled into libraries.
		 */
		LightSource light1 = new LightSource(new Point3D(0, 0, 100), 50);
		light1.setColor(Color.WHITE);
		LIGHTS.add(light1);
		LightSource light2 = new LightSource(new Point3D(100, 100, 50), 30);
		light2.setColor(Color.GREEN);
		LIGHTS.add(light2);
		for (int x = -30; x < 30; x++) {
			for (int y = -30; y < 30; y++) {
				OBJECTS.add(new EnvironmentObject(new Point3D(x * 10, y * 10, 0), 1, TAGGER.getTag()));
			}
		}
		OBJECTS.add(new EnvironmentObject(new Point3D(50, 50, 50), 2, TAGGER.getTag()));
		System.out.println(OBJECTS.size());
		// To prevent instantiation
	}
}
