package com.engine.graphics;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;

import com.engine.environment.Environment;
import com.engine.environment.EnvironmentObject;
import com.engine.environment.LightSource;
import com.engine.io.KeyboardHandler;
import com.engine.io.MouseHandler;
import com.engine.math.Point2D;
import com.engine.math.Triangle;
import com.engine.rendering.Camera;
import com.engine.rendering.RenderManager;
import com.engine.rendering.VideoSettings;
import com.engine.util.ColorPalette;

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

	public static Engine getSingleton() {
		return SINGLETON;
	}

	private Engine() {
		System.setProperty("sun.java2d.opengl", "true");
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setTitle("Clown-Engine v3.2");
		MouseHandler m = new MouseHandler();
		this.addMouseMotionListener(m);
		this.addMouseWheelListener(m);
		this.addMouseListener(m);
		this.addKeyListener(new KeyboardHandler());
		// To prevent instantiation.
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
	
	private BufferedImage toCompatibleImage(BufferedImage image)
	{
		// obtain the current system graphical settings
		GraphicsConfiguration gfx_config = GraphicsEnvironment.
			getLocalGraphicsEnvironment().getDefaultScreenDevice().
			getDefaultConfiguration();

		/*
		 * if image is already compatible and optimized for current system 
		 * settings, simply return it
		 */
		if (image.getColorModel().equals(gfx_config.getColorModel()))
			return image;

		// image is not optimized, so create a new image that is
		BufferedImage new_image = gfx_config.createCompatibleImage(
				image.getWidth(), image.getHeight(), image.getTransparency());

		// get the graphics context of the new image to draw the old image on
		Graphics2D g2d = (Graphics2D) new_image.getGraphics();

		// actually draw the image and dispose of context no longer needed
		g2d.drawImage(image, 0, 0, null);
		g2d.dispose();

		// return the new optimized image
		return new_image; 
	}

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
		// TODO Only perform on scale change.
		Camera.getSingleton()
				.setScreenCenter(new Point2D(VideoSettings.getOutputWidth() / 2, VideoSettings.getOutputHeight() / 2));
		//Consider different ways to remove the actual drawing from this area, and only draw the image here.
		Triangle[] triangles = new Triangle[0];
		try {
			triangles = RenderManager.getTriangles();
		}catch (java.lang.NoClassDefFoundError e) {
			System.exit(1);
		}
		graphics.setColor(Color.BLACK);
		graphics.fillRect(0, 0, VideoSettings.getOutputWidth(), VideoSettings.getOutputHeight());
		for (Triangle t : triangles) {
			graphics.setColor(t.getColor());
			graphics.fillPolygon(t.getXValues(), t.getYValues(), t.getPoints().length);
		}
		graphics.setColor(Color.ORANGE);
		try {
			for (EnvironmentObject e : Environment.getObjectsAtCoordinate((int) MouseHandler.getMouseX(),
					(int) MouseHandler.getMouseY())) {
				if (e.getOutline() != null)
					graphics.drawPolygon(e.getOutline().getXValues(), e.getOutline().getYValues(),
							e.getOutline().getPoints().length);
			}
		} catch (java.lang.NoClassDefFoundError e) {
			e.printStackTrace();
			System.exit(1);
		}
		for (LightSource l : Environment.grabEnvironmentLights()) {
			Point2D p = Camera.getSingleton().translatePoint3D(l.getCoordinates());
			graphics.setColor(l.getColor());
			graphics.fillRect(p.getX() - 2, p.getY() - 2, 4, 4);
			graphics.setColor(Color.BLACK);
			graphics.drawRect(p.getX() - 3, p.getY() - 3, 6, 6);
		}
		drawDebugInfo(graphics, "FPS: " + ((int) fps), "CamPos: " + Camera.getSingleton().getCoordinates(),
				"NOF: " + triangles.length);
		g.drawImage(image, 0, 0, null);
		// For fps capping
		long sleepTime = (long) (((1000000000.0 / VideoSettings.getFramesPerSecondCap())
				- (System.nanoTime() - lastTime))) / 1000000;
		if (sleepTime > 0) {
			try {
				Thread.sleep(sleepTime);
			} catch (InterruptedException e) {
			}
		}
		fps = 1000000000.0 / (System.nanoTime() - lastTime);
	}
}
