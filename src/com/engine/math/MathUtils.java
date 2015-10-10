package com.engine.math;

import java.awt.Color;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.Arrays;

import com.engine.rendering.VideoSettings;

public final class MathUtils {
	private static final MathUtils SINGLETON = new MathUtils();

	public static float calculateAngleRelativeToNormal(final Face t, final Point3D sun, final Point3D normal) {
		return (float) ((Math.acos(dotProduct(
				normalizeVector(new Point3D(sun.getX() - t.getAveragePoint().getX(),
						sun.getY() - t.getAveragePoint().getY(), sun.getZ() - t.getAveragePoint().getZ())),
				normalizeVector(normal))) * 180) / Math.PI);
	}

	public static Point2D calculateAveragePoint(final Point2D[] points) {
		int x = 0, y = 0;
		for (int i = 0; i < points.length; i++) {
			x += points[i].getX();
			y += points[i].getY();
		}
		return new Point2D(x / points.length, y / points.length);
	}

	public static Point3D calculateAveragePoint(final Point3D[] points) {
		float x = 0, y = 0, z = 0;
		for (int i = 0; i < points.length; i++) {
			x += points[i].getX();
			y += points[i].getY();
			z += points[i].getZ();
		}
		return new Point3D(x / points.length, y / points.length, z / points.length);
	}

	public static Point3D calculateSurfaceNormal(final Face t) {
		if (t.getPoints().length != 3) {
			throw new IllegalArgumentException(
					"Cannot calculate surface normal of a face containing anything other than 3 points.");
		}
		Point3D p1 = t.getPoints()[0], p2 = t.getPoints()[1], p3 = t.getPoints()[2];
		Point3D u = new Point3D(p2.getX() - p1.getX(), p2.getY() - p1.getY(), p2.getZ() - p1.getZ());
		Point3D v = new Point3D(p3.getX() - p1.getX(), p3.getY() - p1.getY(), p3.getZ() - p1.getZ());
		Point3D normal = new Point3D((u.getY() * v.getZ()) - (u.getZ() * v.getY()),
				(u.getZ() * v.getX()) - (u.getX() * v.getZ()), (u.getX() * v.getY()) - (u.getY() * v.getX()));
		return normal;
	}

	public static Polygon convexHull(Point2D[] points) {
		if (points.length > 1) {
			int n = points.length, k = 0;
			Point2D[] h = new Point2D[2 * n];
			Arrays.sort(points);
			for (int i = 0; i < n; ++i) {
				while (k >= 2 && cross(h[k - 2], h[k - 1], points[i]) <= 0)
					k--;
				h[k++] = points[i];
			}

			for (int i = n - 2, t = k + 1; i >= 0; i--) {
				while (k >= t && cross(h[k - 2], h[k - 1], points[i]) <= 0)
					k--;
				h[k++] = points[i];
			}
			if (k > 1) {
				h = Arrays.copyOfRange(h, 0, k - 1);
			}
			return new Polygon(h);
		} else if (points.length <= 1) {
			return new Polygon(points);
		} else {
			return null;
		}
	}

	public static boolean coordinateInArea(final int x, final int y, final Dimension area) {
		return (x >= 0 && y >= 0) && (x <= area.getWidth() && y <= area.getHeight());
	}

	public static Face[] createSphere(int r, int z, float steps) {
		float step = (float) ((2 * Math.PI) / steps);
		Point3D[][] points = new Point3D[(int) steps][(int) steps];
		int idx1 = 0;
		int idx2 = 0;
		for (float theta = 0; theta < (2 * Math.PI) - step + .01; theta += step) {
			idx1 = 0;
			for (float phi = 0; phi < (2 * Math.PI) - step + .01; phi += step) {
				points[idx2][idx1++] = new Point3D((float) (Math.cos(phi) * Math.sin(theta) * r),
						(float) (Math.sin(phi) * Math.sin(theta) * r), ((float) Math.cos(theta) * r) + z);
			}
			idx2++;
		}
		ArrayList<Face> faces = new ArrayList<Face>();
		for (int i = 0; i < steps; i++) {
			for (int j = 0; j < steps; j++) {
				faces.add(new Face(new Point3D[] { points[i][j], points[(i + 1) % (int) steps][j],
						points[i][(j + 1) % (int) steps] }, Color.WHITE, false));
				faces.add(new Face(new Point3D[] { points[(i + 1) % (int) steps][j],
						points[(i + 1) % (int) steps][(j + 1) % (int) steps], points[i][(j + 1) % (int) steps] },
						Color.WHITE, false));
			}
		}
		return faces.toArray(new Face[faces.size()]);
	}

	public static double cross(Point2D p1, Point2D p2, Point2D p3) {
		return (double) (((p2.getX() - p1.getX()) * (p3.getY() - p1.getY()))
				- ((p2.getY() - p1.getY()) * (p3.getX() - p1.getX())));
	}

	public static int distance(Point3D p1, Point3D p2) {
		return (int) Math.sqrt((Math.pow(p2.x - p1.x, 2) + Math.pow(p2.y - p1.y, 2) + Math.pow(p2.z - p1.z, 2)));
	}

	private static boolean doIntersect(Point2D p1, Point2D q1, Point2D p2, Point2D q2) {
		float o1 = orientation(p1, q1, p2);
		float o2 = orientation(p1, q1, q2);
		float o3 = orientation(p2, q2, p1);
		float o4 = orientation(p2, q2, q1);

		if (o1 != o2 && o3 != o4) {
			if (o4 == 0) {
				return false;
			}
			if (o3 == 0) {
				if (o4 != 1) {
					return false;
				}
			}
			return true;
		}

		if (o1 == 0 && onSegment(p1, p2, q1))
			return true;

		if (o2 == 0 && onSegment(p1, q2, q1))
			return true;

		if (o3 == 0 && onSegment(p2, p1, q2))
			return true;

		if (o4 == 0 && onSegment(p2, q1, q2))
			return true;

		return false;
	}

	public static float dotProduct(Point3D p1, Point3D p2) {
		return (p1.getX() * p2.getX()) + (p1.getY() * p2.getY()) + (p1.getZ() * p2.getZ());
	}

	private static float getLargest(float a, float b) {
		return (a < b) ? b : a;
	}

	public static MathUtils getSingleton() {
		return SINGLETON;
	}

	public static boolean isInside(Point2D[] polygon, Point2D p) {
		int vertices = polygon.length;
		if (vertices < 3)
			return false;
		Point2D extreme = new Point2D(VideoSettings.getMaxOutputWidth() + 100,
				VideoSettings.getMaxOutputHeight() + 100);
		int count = 0, i = 0;
		do {
			int next = (i + 1) % vertices;
			if (doIntersect(polygon[i], polygon[next], p, extreme)) {
				if (orientation(polygon[i], p, polygon[next]) == 0)
					return onSegment(polygon[i], p, polygon[next]);
				count++;
			}
			i = next;
		} while (i != 0);
		return count % 2 == 1;
	}

	public static Point3D normalizeVector(Point3D vector) {
		float magnitude = (float) Math
				.sqrt(Math.pow(vector.getX(), 2) + Math.pow(vector.getY(), 2) + Math.pow(vector.getZ(), 2));
		return new Point3D(vector.getX() / magnitude, vector.getY() / magnitude, vector.getZ() / magnitude);
	}

	private static boolean onSegment(Point2D p, Point2D q, Point2D r) {
		if (q.getX() <= getLargest(p.getX(), r.getX()) && q.getX() >= getLargest(p.getX(), r.getX())
				&& q.getY() <= getLargest(p.getY(), r.getY()) && q.getY() >= getLargest(p.getY(), r.getY()))
			return true;
		return false;
	}

	private static float orientation(Point2D p, Point2D q, Point2D r) {
		float val = (q.getY() - p.getY()) * (r.getX() - q.getX()) - (q.getX() - p.getX()) * (r.getY() - q.getY());

		if (val == 0)
			return 0;
		return (val > 0) ? 1 : 2;
	}

	public static int[][] plotCircle(int r, float steps) {
		int[][] points = new int[(int) steps + 1][2];
		float step = (float) ((2 * Math.PI) / steps);
		int idx = 0;
		for (float theta = 0; theta < (2 * Math.PI); theta += step) {
			points[idx][0] = (int) (Math.sin(theta) * r);
			points[idx++][1] = (int) (Math.cos(theta) * r);
		}
		return points;
	}

	private MathUtils() {
		// To prevent instantiation.
	}
}
