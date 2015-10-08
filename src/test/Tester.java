package test;

import java.awt.Color;

import com.engine.graphics.Engine;
import com.engine.io.FileIO;
import com.engine.math.Face;
import com.engine.math.MathUtils;
import com.engine.math.Point3D;
import com.engine.models.Model3D;
import com.engine.util.IDTagSystem;
import com.engine.util.Util;
import com.engine.util.IDTagSystem.IDTag;

public class Tester {
	public static void main(String[] args) throws InterruptedException {
		// These are basic models I made manually for testing the engine

		Point3D p1 = new Point3D(0, 0, 0);
		Point3D p2 = new Point3D(10, 0, 0);
		Point3D p3 = new Point3D(10, 10, 0);
		Point3D p4 = new Point3D(0, 10, 0);
		Face f1 = new Face(new Point3D[] { p1, p2, p4 }, Color.WHITE, false);
		Face f2 = new Face(new Point3D[] { p2, p3, p4 }, Color.WHITE, false);
		Model3D m1 = new Model3D(new Face[] { f1, f2 }, 1);
		Model3D m2 = new Model3D(MathUtils.createSphere(50, 25, 36.0f), 2);
		FileIO.writeFile(m1);
		FileIO.writeFile(m2);

		Engine.getSingleton().setVisible(true);
		Engine.getSingleton().setSize(800, 600);
		while (true) {
			Engine.getSingleton().repaint();
		}
	}
}
