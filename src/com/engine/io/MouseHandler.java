package com.engine.io;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;

import com.engine.rendering.Camera;

public final class MouseHandler extends MouseAdapter {
	private static float lastMouseX = -200.0f, lastMouseY = -200.0f;

	public static float getMouseX() {
		return lastMouseX;
	}

	public static float getMouseY() {
		return lastMouseY;
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		if (lastMouseX == -200) {
			lastMouseX = e.getX();
			lastMouseY = e.getY();
			return;
		}
		lastMouseX = e.getX();
		lastMouseY = e.getY();
	}

	@Override
	public void mouseDragged(MouseEvent arg0) {
		if (lastMouseX == -200) {
			lastMouseX = arg0.getX();
			lastMouseY = arg0.getY();
			return;
		}
		Camera.getSingleton().updateTheta(-(arg0.getX() - lastMouseX) / 10.0f);
		Camera.getSingleton().updatePhi(-(arg0.getY() - lastMouseY) / 10.0f);
		lastMouseX = arg0.getX();
		lastMouseY = arg0.getY();
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		if (lastMouseX == -200) {
			lastMouseX = e.getX();
			lastMouseY = e.getY();
			return;
		}
		lastMouseX = e.getX();
		lastMouseY = e.getY();
	}

	@Override
	public void mouseExited(MouseEvent e) {
		if (lastMouseX == -200) {
			lastMouseX = e.getX();
			lastMouseY = e.getY();
			return;
		}
		lastMouseX = e.getX();
		lastMouseY = e.getY();
	}

	@Override
	public void mouseMoved(MouseEvent arg0) {
		if (lastMouseX == -200) {
			lastMouseX = arg0.getX();
			lastMouseY = arg0.getY();
			return;
		}
		lastMouseX = arg0.getX();
		lastMouseY = arg0.getY();
	}

	@Override
	public void mousePressed(MouseEvent e) {
		if (lastMouseX == -200) {
			lastMouseX = e.getX();
			lastMouseY = e.getY();
			return;
		}
		lastMouseX = e.getX();
		lastMouseY = e.getY();
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		if (lastMouseX == -200) {
			lastMouseX = e.getX();
			lastMouseY = e.getY();
			return;
		}
		lastMouseX = e.getX();
		lastMouseY = e.getY();
	}

	@Override
	public void mouseWheelMoved(MouseWheelEvent arg0) {
		Camera.getSingleton().changeZoom(arg0.getWheelRotation() * 1000);
	}
}
