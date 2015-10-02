package com.engine.math;

import java.awt.Color;

import com.engine.util.BinaryOperations;
import com.engine.util.ByteFormatted;

public final class Face implements ByteFormatted<Face> {
	private static final Face FACE_BUILDER = new Face();

	public static Face getBuilder() {
		return FACE_BUILDER;
	}

	private final Point3D[] points = new Point3D[3];
	private final Color color;
	private final boolean showBackFace;

	private final Point3D averagePoint;
	private int sourceID = -1;

	public Face setSourceID(int id) {
		sourceID = id;
		return this;
	}

	public int getSourceID() {
		return sourceID;
	}

	private Face() {
		color = null;
		averagePoint = null;
		showBackFace = false;
	}

	public Face(final Point3D[] points) {
		if (points.length != 3)
			throw new IllegalArgumentException("points must be of length 3");
		for (int i = 0; i < 3; i++) {
			this.points[i] = points[i];
		}
		color = Color.WHITE;
		averagePoint = MathUtils.calculateAveragePoint(points);
		showBackFace = false;
	}

	public Face(final Point3D[] points, final Color color) {
		if (points.length != 3)
			throw new IllegalArgumentException("points must be of length 3");
		for (int i = 0; i < 3; i++) {
			this.points[i] = points[i];
		}
		averagePoint = MathUtils.calculateAveragePoint(points);
		this.color = color;
		showBackFace = false;
	}

	public Face(final Point3D[] points, final Color color, final boolean showBackFace) {
		if (points.length != 3)
			throw new IllegalArgumentException("points must be of length 3");
		for (int i = 0; i < 3; i++) {
			this.points[i] = points[i];
		}
		averagePoint = MathUtils.calculateAveragePoint(points);
		this.color = color;
		this.showBackFace = showBackFace;
	}

	public Face applyOffest(Point3D offsetPoint) {
		return new Face(new Point3D[] { points[0].applyOffest(offsetPoint), points[1].applyOffest(offsetPoint),
				points[2].applyOffest(offsetPoint) }, color, showBackFace);
	}

	@Override
	public Face fromBytes(byte[] bytes) {
		boolean backface = bytes[4] == 1 ? true : false;
		Color c = new Color(BinaryOperations.bytesToInteger(bytes, 0));
		Point3D[] p = new Point3D[3];
		byte[] byteQueue = new byte[12];
		for (int i = 5, l = 0; i < 41; i += 12, l++) {
			for (int j = i, k = 0; j < i + 12; j++, k++) {
				byteQueue[k] = bytes[j];
			}
			p[l] = Point3D.getBuilder().fromBytes(byteQueue);
		}
		return new Face(p, c, backface);
	}

	public Point3D getAveragePoint() {
		return averagePoint;
	}

	public Color getColor() {
		return color;
	}

	public Point3D[] getPoints() {
		return points;
	}

	public boolean showBackFace() {
		return showBackFace;
	}

	@Override
	public int sizeOf() {
		return 41;
	}

	@Override
	public byte[] toBytes() {
		byte[] bytes = new byte[41];
		byte[] pBytes = BinaryOperations.toBytes(color.getRGB());
		int idx = 0;
		bytes[idx++] = pBytes[0];
		bytes[idx++] = pBytes[1];
		bytes[idx++] = pBytes[2];
		bytes[idx++] = pBytes[3];
		bytes[idx++] = (byte) (showBackFace ? 1 : 0);
		for (int i = 0; i < 3; i++) {
			pBytes = points[i].toBytes();
			for (int j = 0; j < pBytes.length; j++) {
				bytes[idx++] = pBytes[j];
			}
		}
		return bytes;
	}
}
