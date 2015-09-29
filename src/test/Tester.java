package test;

import java.awt.Color;

import javax.swing.JFrame;

import com.engine3d.graphics.Engine;
import com.engine3d.io.FileIO;
import com.engine3d.math.Face;
import com.engine3d.math.MathUtils;
import com.engine3d.math.Point3D;
import com.engine3d.models.Model3D;
import com.engine3d.models.ModelManager;

public class Tester {
	public static void main(String[] args) throws InterruptedException {
//		 Model3D m2 = FileIO.readFile(Model3D.getBuilder(), "Model_"+1);
//		 System.out.println(""+m2);
		
//		FaceList.getSingleton().queueFacesToList(ModelManager.loadModel(1).getFaces());
//		long start = System.currentTimeMillis();
//		for (int i = 0; i < 60000; i++) {
//			CaptureMesh.getImage();
//		}
//		System.out.println("Total time per frame: "+(((float)(System.currentTimeMillis() - start)) / 60000.0f));
//		
		
		Point3D p1 = new Point3D(0, 0, 0);
		 Point3D p2 = new Point3D(10, 0, 0);
		 Point3D p3 = new Point3D(10, 10, 0);
		 Point3D p4 = new Point3D(0, 10, 0);
		 
		 Point3D p5 = new Point3D(0, 0, 10);
		 Point3D p6 = new Point3D(10, 0, 10);
		 Point3D p7 = new Point3D(10, 10, 10);
		 Point3D p8 = new Point3D(0, 10, 10);
		 Face f1 = new Face(new Point3D[] {p1, p2, p4}, Color.WHITE, false);
		 Face f2 = new Face(new Point3D[] {p2, p3, p4}, Color.WHITE, false);
		 Model3D m1 = new Model3D(new Face[] {f1, f2}, 1);
		 Model3D m2 = new Model3D(MathUtils.createSphere(50, 25, 36.0f), 2);
		FileIO.writeFile(m1);
		FileIO.writeFile(m2);
		Engine.getSingleton().setVisible(true);
		Engine.getSingleton().setSize(800, 600);
		System.out.println("Model1: "+ModelManager.getModel(1).getFaces()[0].getPoints()[0]);
		
		
		while (true) {
			Engine.getSingleton().repaint();
			Thread.sleep(2);
		}
		
		
		 
	}
}
