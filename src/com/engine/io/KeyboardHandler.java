package com.engine.io;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyboardHandler implements KeyListener {
	public static volatile float x = 0, y = 0;

	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyReleased(KeyEvent e) {
		switch (e.getKeyCode()) {
		case KeyEvent.VK_W:
			x++;
			return;
		case KeyEvent.VK_S:
			x--;
			return;
		case KeyEvent.VK_A:
			y++;
			return;
		case KeyEvent.VK_D:
			y--;
			return;
		}

	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub

	}

}
