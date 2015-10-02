package com.engine.models;

import java.util.TreeMap;

import com.engine.io.FileIO;

public final class ModelManager {
	private static final ModelManager SINGLETON = new ModelManager();
	private static final TreeMap<Integer, Model3D> MODEL_LIST = new TreeMap<Integer, Model3D>();

	public static Model3D getModel(Integer modelID) {
		Model3D mod = MODEL_LIST.get(modelID);
		if (mod == null) {
			mod = loadModel(modelID);
		}
		return mod;
	}

	public static ModelManager getSingleton() {
		return SINGLETON;
	}

	public static Model3D loadModel(Integer modelID) {
		if (MODEL_LIST.get(modelID) != null) {
			System.err.println("ModelManager.loadModel called for existing model!");
			return MODEL_LIST.get(modelID);
		}
		Model3D mod = FileIO.readFile(Model3D.getBuilder(), "Model_" + modelID);
		MODEL_LIST.put(modelID, mod);
		return mod;
	}

	private ModelManager() {
		// To prevent instantiation
	}
}
