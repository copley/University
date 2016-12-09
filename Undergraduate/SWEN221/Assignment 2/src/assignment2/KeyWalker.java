package assignment2;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.*;

import static maze.Direction.*;
import maze.*;

/**
 * An any implementation of the key walker, which you need to complete
 * following the notes.
 * 
 */
public class KeyWalker extends Walker implements KeyListener{
	
	public boolean[] keyPressed;
	
	public KeyWalker() {
		super("Key Walker");
		
		keyPressed = new boolean[4];
	}

	protected Direction move(View v) {
		
		if (keyPressed[0]) {
			keyPressed[0] = false;
			return Direction.NORTH;
		} else if (keyPressed[1]) {
			keyPressed[1] = false;
			return Direction.SOUTH;
		} else if (keyPressed[2]) {
			keyPressed[2] = false;
			return Direction.WEST;
		} else if (keyPressed[3]) {
			keyPressed[3] = false;
			return Direction.EAST;
		}
		
		return null;
	}

	/**
	 * When a key is pressed down will update the key pressed
	 * array so that the move function knows what directions
	 * are pressed
	 */
	@Override
	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_UP) {
			keyPressed[0] = true;
		} else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
			keyPressed[1] = true;
		} else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
			keyPressed[2] = true;
		} else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
			keyPressed[3] = true;
		}
		
	}

	/**
	 * When a key is released it updates the key pressed array so
	 * that the move function knows what directions are pressed
	 */
	@Override
	public void keyReleased(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_UP) {
			keyPressed[0] = false;
		} else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
			keyPressed[1] = false;
		} else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
			keyPressed[2] = false;
		} else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
			keyPressed[3] = false;
		}
		
	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}
}