package com.engine.math;

import com.engine.util.BinaryOperations;
import com.engine.util.ByteFormatted;

public class Point3D implements ByteFormatted<Point3D> {
	private static final Point3D POINT3D_BUILDER = new Point3D();
	public final float x, y, z;

	private Point3D() {
		// For builder only
		x = 0;
		y = 0;
		z = 0;
	}

	public static Point3D getBuilder() {
		return POINT3D_BUILDER;
	}

	public Point3D(float x, float y, float z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public Point3D applyOffest(Point3D offsetPoint) {
		return new Point3D(x + offsetPoint.getX(), y + offsetPoint.getY(), z + offsetPoint.getZ());
	}

	public float getX() {
		return x;
	}

	public float getY() {
		return y;
	}

	public float getZ() {
		return z;
	}

	public Point3D setX(float newX) {
		return new Point3D(newX, y, z);
	}

	public Point3D setY(float newY) {
		return new Point3D(x, newY, z);
	}

	public Point3D setZ(float newZ) {
		return new Point3D(x, y, newZ);
	}

	// Length = 12bytes
	@Override
	public byte[] toBytes() {
		return BinaryOperations.toBytes(new float[] { x, y, z });
	}

	@Override
	public int sizeOf() {
		return 12;
	}

	@Override
	public Point3D fromBytes(byte[] bytes) {
		float[] floats = BinaryOperations.byteArrayToFloatArray(bytes);
		return new Point3D(floats[0], floats[1], floats[2]);
	}

	@Override
	public String toString() {
		return "(" + x + "," + y + "," + z + ")";
	}
}
