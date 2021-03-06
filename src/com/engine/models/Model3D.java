package com.engine.models;

import com.engine.io.FileIOFormatted;
import com.engine.io.FileType;
import com.engine.math.Face;
import com.engine.util.BinaryOperations;

public final class Model3D implements FileIOFormatted<Model3D> {
	private static final Model3D MODEL3D_BUILDER = new Model3D();

	public static Model3D getBuilder() {
		return MODEL3D_BUILDER;
	}

	private final Face[] faces;

	private final int modelID;

	private Model3D() {
		// To prevent instantiation;
		faces = null;
		modelID = -1;
	}

	public Model3D(final Face[] faces, final int modelID) {
		this.faces = faces;
		this.modelID = modelID;
	}

	@Override
	public Model3D fromBytes(byte[] bytes) {
		int size = BinaryOperations.bytesToInteger(bytes, 0);
		int m = BinaryOperations.bytesToInteger(bytes, 4);
		byte[] faceBytes = new byte[Face.getBuilder().sizeOf()];
		Face[] faces = new Face[(size - 8) / Face.getBuilder().sizeOf()];
		int idx = 8;
		// TODO make so Face.fromBytes works with raw byte array and start idx
		// (like BinaryOperations)
		for (int i = 0; i < faces.length; i++) {
			for (int j = 0; j < faceBytes.length; j++) {
				faceBytes[j] = bytes[idx++];
			}
			faces[i] = Face.getBuilder().fromBytes(faceBytes);
		}
		return new Model3D(faces, m);
	}

	public Face[] getFaces() {
		return faces;
	}

	@Override
	public String getFileName() {
		return "Model_" + modelID;
	}

	@Override
	public FileType getFileType() {
		return FileType.MODEL;
	}

	public int getModelID() {
		return modelID;
	}

	@Override
	public int sizeOf() {
		return (faces.length * Face.getBuilder().sizeOf()) + 8;
	}

	@Override
	public byte[] toBytes() {
		byte[] bytes = new byte[sizeOf()];
		byte[] mBytes = BinaryOperations.toBytes(modelID);
		byte[] sBytes = BinaryOperations.toBytes(sizeOf());
		int idx = 0;
		bytes[idx++] = sBytes[0];
		bytes[idx++] = sBytes[1];
		bytes[idx++] = sBytes[2];
		bytes[idx++] = sBytes[3];
		bytes[idx++] = mBytes[0];
		bytes[idx++] = mBytes[1];
		bytes[idx++] = mBytes[2];
		bytes[idx++] = mBytes[3];
		for (int i = 0; i < faces.length; i++) {
			byte[] faceBytes = faces[i].toBytes();
			for (int j = 0; j < faceBytes.length; j++) {
				bytes[idx++] = faceBytes[j];
			}
		}
		return bytes;
	}

	@Override
	public String toString() {
		return getFileName() + ", FaceCount: " + faces.length;
	}
}
