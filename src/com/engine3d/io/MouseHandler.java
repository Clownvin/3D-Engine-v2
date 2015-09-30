package com.engine3d.io;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;

import com.engine3d.graphics.Camera;

public final class MouseHandler extends MouseAdapter {
	private static float lastMouseX = -200.0f, lastMouseY = -200.0f;

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseWheelMoved(MouseWheelEvent arg0) {
		Camera.getSingleton().changeZoom(arg0.getWheelRotation() * 1000);
		Camera.getSingleton().updateCoordinates();
	}

	@Override
	public void mouseDragged(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseMoved(MouseEvent arg0) {
		if (lastMouseX == -200) {
			lastMouseX = arg0.getX();
			lastMouseY = arg0.getY();
			return;
		}
		Camera.getSingleton().updateTheta(arg0.getX() - lastMouseX);
		Camera.getSingleton().updatePhi(arg0.getY() - lastMouseY);
		lastMouseX = arg0.getX();
		lastMouseY = arg0.getY();
	}
}
