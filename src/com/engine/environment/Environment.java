package com.engine.environment;

import java.awt.Color;
import java.util.ArrayList;

import com.engine.environment.map.MapGenerator;
import com.engine.math.Face;
import com.engine.math.MathUtils;
import com.engine.math.Point2D;
import com.engine.math.Point3D;
import com.engine.models.ModelManager;
import com.engine.util.IDTagSystem;

//TODO add Scenes, and create a map
public final class Environment {
	private static final int MAX_ENVIRONMENT_OBJECTS = 2000000;
	private static final IDTagSystem TAGGER = new IDTagSystem(MAX_ENVIRONMENT_OBJECTS);
	private static final ArrayList<EnvironmentObject> OBJECTS = new ArrayList<EnvironmentObject>();
	private static final ArrayList<LightSource> LIGHTS = new ArrayList<LightSource>();
	private static final Environment SINGLETON = new Environment();
	private static final Face[] mapFaces = MapGenerator.generateBaseMap(200, 200, 10.0f);

	public static Environment getSingleton() {
		return SINGLETON;
	}

	public static Face[] grabEnvironmentFaces() {
		
		ArrayList<Face> faceList = new ArrayList<Face>(OBJECTS.size() * 10);
		for (int i = 0; i < mapFaces.length; i++) {
			faceList.add(mapFaces[i]);
		}
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
			throw new ArrayIndexOutOfBoundsException("index " + index + " is out of bounds");
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
		 * This contents of this block shouldn't exist when compiled to library;
		 */
		 LightSource light1 = new LightSource(new Point3D(0, 0, 100), 300);
		 light1.setColor(new Color(((int) (0xFF * 1.0f) << 16) | ((int) (0xFF * 0.9f) << 8) | ((int) (0xFF * 0.9f))));
		 LIGHTS.add(light1);
		 LightSource light2 = new LightSource(new Point3D(500, 500, 400), 300);
		 light2.setColor(Color.GREEN);
		 LIGHTS.add(light2);
		 LightSource light3 = new LightSource(new Point3D(500, -500, 400),
		 300);
		 light3.setColor(Color.RED);
		 LIGHTS.add(light3);
		 LightSource light4 = new LightSource(new Point3D(-500, 500, 400),
		 300);
		 light4.setColor(Color.BLUE);
		 LIGHTS.add(light4);
		 LightSource light5 = new LightSource(new Point3D(-500, -500, 400),
		 300);
		 light5.setColor(Color.MAGENTA);
		 LIGHTS.add(light5);
//		 OBJECTS.add(new EnvironmentObject(new Point3D(0, 0, 100), 2, TAGGER.getTag()));
	}
}
