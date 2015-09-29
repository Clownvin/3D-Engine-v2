package com.engine3d.graphics;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.swing.JFrame;

import com.engine.util.FaceList;
import com.engine.util.MaximumCapacityReachedException;
import com.engine.util.VideoSettings;
import com.engine3d.environment.Environment;
import com.engine3d.io.MouseHandler;
import com.engine3d.math.MathUtils;
import com.engine3d.math.Point2D;
import com.engine3d.math.Point3D;
import com.engine3d.math.Triangle;
import com.engine3d.models.ModelManager;
import com.v2.threading.ThreadPool;
import com.v2.threading.ThreadTask;

public final class Engine extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2127211880599087732L;
	private static final Engine SINGLETON = new Engine();
	private static long lastTime;
	private static double fps;
	private static BufferedImage image = null;
	private static Graphics2D graphics = null;
	
	private static final ThreadTask POST_RENDER_TASK = new ThreadTask() {
		public boolean done = true;
		@Override
		public boolean reachedEnd() {
			return done;
		}

		@Override
		public void doTask() {
			synchronized (this) {
				this.done = false;
				//Post-render tasks here
				synchronized (FaceList.getSingleton()) {
					FaceList.getSingleton().clear();
					FaceList.getSingleton().queueFacesToList(Environment.grabEnvironmentFaces());
				}
				this.done = true;
			}
		}

		@Override
		public void end() {
			synchronized (this) {
				this.notifyAll();
			}
		}
	};

	private Engine() {
		System.setProperty("sun.java2d.opengl", "true");
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		MouseHandler m = new MouseHandler();
		this.addMouseMotionListener(m);
		this.addMouseWheelListener(m);
		this.addMouseListener(m);
		// To prevent instantiation.
	}

	public static Engine getSingleton() {
		return SINGLETON;
	}

	@Override
	public void paint(Graphics g) {
		lastTime = System.nanoTime();
		synchronized (POST_RENDER_TASK) {
			while (!POST_RENDER_TASK.reachedEnd()) {
				try {
					POST_RENDER_TASK.wait(300);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		if (image == null || graphics == null) {
			image = (BufferedImage) createImage(VideoSettings.getMaxOutputWidth(),
					VideoSettings.getMaxOutputHeight());
			image.setAccelerationPriority(1);
			graphics = (Graphics2D) image.getGraphics();
			RenderingHints rh = new RenderingHints(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_SPEED);
			graphics.setRenderingHints(rh);
			return;
		}
		VideoSettings.setOutputWidth(this.getWidth());
		VideoSettings.setOutputHeight(this.getHeight());
		Camera.getSingleton().setScreenCenter(new Point2D(VideoSettings.getOutputWidth() / 2, VideoSettings.getOutputHeight() / 2));
		ArrayList<Triangle> triangles;
		synchronized (FaceList.getSingleton()) {
			triangles = FaceList.getSingleton().grabTriangles(Camera.getSingleton().getCoordinates());
		}
		graphics.setColor(Color.BLACK);
		graphics.fillRect(0, 0, VideoSettings.getOutputWidth(), VideoSettings.getOutputHeight());
		for (Triangle t : triangles) {
			graphics.setColor(t.getColor());
			graphics.fillPolygon(t.getXValues(), t.getYValues(), t.getPoints().length);
//			graphics.setColor(Color.BLACK);
//			graphics.drawPolygon(t.getXValues(), t.getYValues(), t.getPoints().length);
		}
		try {
			ThreadPool.addTask(POST_RENDER_TASK);
		} catch (MaximumCapacityReachedException e) {
			e.printStackTrace();
		}
		Point2D tranLight = Camera.getSingleton().translatePoint3D(Environment.grabEnvironmentLights()[0].getCoordinates());
		graphics.setColor(Color.MAGENTA);
		graphics.fillRect((int) tranLight.getX()+2, (int) tranLight.getY()+2, 4, 4);
		drawDebugInfo(graphics, "FPS: "+((int)fps), "CamPos: "+Camera.getSingleton().getCoordinates());
		g.drawImage(image, 0, 0, null);
		fps = 1000000000.0 / (System.nanoTime() - lastTime);
	}
	
	private void drawDebugInfo(Graphics g, String... strings) {
		g.setColor(Color.BLACK);
		for (int i = 0; i < strings.length; i++) {
			g.drawString(strings[i], 8, (i * 10) + 39);
		}
		g.setColor(Color.GREEN);
		for (int i = 0; i < strings.length; i++) {
			g.drawString(strings[i], 9, (i * 10) + 40);
		}
	}
}
