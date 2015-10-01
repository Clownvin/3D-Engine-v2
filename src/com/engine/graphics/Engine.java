package com.engine.graphics;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.swing.JFrame;

import com.engine.io.MouseHandler;
import com.engine.math.Point2D;
import com.engine.math.Triangle;
import com.engine.rendering.Camera;
import com.engine.rendering.FaceList;
import com.engine.rendering.RenderManager;
import com.engine.rendering.VideoSettings;

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

	//TODO TODO TODO
	/*
	 * (non-Javadoc)
	 * @see java.awt.Window#paint(java.awt.Graphics)
	 * 
	 * New plan!
	 * Instead of handling triangle creation at all in this method,
	 * I've devised a new plan where a thread (or more) will constantly
	 * keep handling triangle creation, and pushing the new triangles to an array
	 * which is then grabbed when this paint method is called.
	 * 
	 * That way, at all times, a new array of triangles will be under construction
	 * so that this method never has to wait for anything, and can always focus entirely on
	 * drawing the triangles/other things.
	 */
	@Override
	public void paint(Graphics g) {
		lastTime = System.nanoTime();
		if (image == null || graphics == null) {
			image = (BufferedImage) createImage(VideoSettings.getMaxOutputWidth(), VideoSettings.getMaxOutputHeight());
			image.setAccelerationPriority(1);
			graphics = (Graphics2D) image.getGraphics();
			RenderingHints rh = new RenderingHints(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_SPEED);
			graphics.setRenderingHints(rh);
			return;
		}
		VideoSettings.setOutputWidth(this.getWidth());
		VideoSettings.setOutputHeight(this.getHeight());
		//TODO Only perform on scale change.
		Camera.getSingleton()
				.setScreenCenter(new Point2D(VideoSettings.getOutputWidth() / 2, VideoSettings.getOutputHeight() / 2));
		Triangle[] triangles = RenderManager.getTriangles();
		graphics.setColor(Color.BLACK);
		graphics.fillRect(0, 0, VideoSettings.getOutputWidth(), VideoSettings.getOutputHeight());
		for (Triangle t : triangles) {
			graphics.setColor(t.getColor());
			graphics.fillPolygon(t.getXValues(), t.getYValues(), t.getPoints().length);
		}
		drawDebugInfo(graphics, "FPS: " + ((int) fps), "CamPos: " + Camera.getSingleton().getCoordinates());
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
