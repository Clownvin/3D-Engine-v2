package com.engine3d.environment;

import java.awt.Color;
import java.util.ArrayList;

import com.engine.util.ColorPalette;
import com.engine3d.math.Face;
import com.engine3d.math.Point3D;
import com.engine3d.models.ModelManager;

//TODO add Scenes, and create a map
public final class Environment {
	private static final ArrayList<EnvironmentObject> OBJECTS = new ArrayList<EnvironmentObject>();
	private static final ArrayList<LightSource> LIGHTS = new ArrayList<LightSource>();
	private static final Environment SINGLETON = new Environment();
	
	private Environment() {
		LightSource light = new LightSource(new Point3D(0, 0, 30), 25000 / 2);
		light.setColor(Color.WHITE);
		LIGHTS.add(light);
		for (int x = -15; x < 15; x++) {
			for (int y = -15; y < 15; y++) {
				OBJECTS.add(new EnvironmentObject(new Point3D(x * 10, y * 10, 0), 1));
			}
		}
		OBJECTS.add(new EnvironmentObject(new Point3D(50, 50, 50), 2));
		System.out.println(OBJECTS.size());
		//To prevent instantiation
	}
	
	public static Environment getSingleton() {
		return SINGLETON;
	}
	
	public static Face[] grabEnvironmentFaces() {
		ArrayList<Face> faceList = new ArrayList<Face>(OBJECTS.size() * 10);
		for (int i = 0; i < OBJECTS.size(); i++) {
			Face[] faces = ModelManager.getModel(OBJECTS.get(i).getModelID()).getFaces();
			for (int j = 0; j < faces.length; j++) {
				faceList.add(faces[j].applyOffest(OBJECTS.get(i).getCoordinates()));
			}
		}
		return faceList.toArray(new Face[faceList.size()]);
	}
	
	public static LightSource[] grabEnvironmentLights() {
		return LIGHTS.toArray(new LightSource[LIGHTS.size()]);
	}
}
