package assignment2;

import java.awt.Point;
import java.util.*;

import static maze.Direction.*;
import maze.*;

/**
 * An any implementation of the left walker, which you need to complete
 * following the notes.
 * 
 */
public class LeftWalker extends Walker {

	private Map<String, ArrayList<Direction>> exits;
	private Map<String, Direction> directions;
	private String[] directionOrder = new String[] { "foward", "right", "reverse", "left" };

	private int x;
	private int y;
	
	private boolean wallFound = false;

	public LeftWalker() {
		super("Left Walker");

		directions = new HashMap<String, Direction>();
		exits = new HashMap<String, ArrayList<Direction>>();

		// Sets up the initital directions
		directions.put("foward", Direction.NORTH);
		directions.put("reverse", Direction.SOUTH);
		directions.put("left", Direction.WEST);
		directions.put("right", Direction.EAST);
	}
	
	/**
	 * Decides what direction should be taken and returns it
	 */
	protected Direction move(View v) {

		// When the left walker is initially started we want to move foward
		// until we find a wall and then align with the wall
		if (!wallFound) {
			if (v.mayMove(EAST) && v.mayMove(WEST) && v.mayMove(NORTH) && v.mayMove(SOUTH)) {
				return NORTH;
			} else {
				while (v.mayMove(directions.get("left"))) {
					rotateRight();
				}
				
				wallFound = true;
			}
		}
		
		// Based on which directions are touching the wall
		// we can choose a prefered direction
		if (v.mayMove(directions.get("left"))) {
			Direction currentFoward = directions.get("foward");
			
			if (currentFoward == WEST || currentFoward == EAST || currentFoward == NORTH) {
				rotateLeft();
			} else if ( currentFoward == SOUTH) {
				rotateRight();
			}
			
			return getDirection("foward", v);
		} else if (v.mayMove(directions.get("foward"))) {
			return getDirection("foward", v);
		} else if (v.mayMove(directions.get("right"))) {
			rotateRight();
			return getDirection("foward", v);
		} else {
			rotateRight();
			rotateRight();
			return getDirection("foward", v);
		}
	}

	/**
	 * Updates the coordernates based on the direction
	 * its moving in
	 * 
	 * @param direction
	 */
	private void updateCoords(String direction) {
		String currentPointKey = x + ":" + y;
		if (!exits.containsKey(currentPointKey)) {
			exits.put(currentPointKey, new ArrayList<Direction>());
		}

		// Based on the direction we are taking update the coords
		if (directions.get(direction).ordinal() == Direction.NORTH.ordinal()) {
			y++;

			exits.get(currentPointKey).add(Direction.NORTH);
		} else if (directions.get(direction).ordinal() == Direction.SOUTH
				.ordinal()) {
			y--;

			exits.get(currentPointKey).add(Direction.SOUTH);
		} else if (directions.get(direction).ordinal() == Direction.EAST
				.ordinal()) {
			x++;

			exits.get(currentPointKey).add(Direction.EAST);
		} else if (directions.get(direction).ordinal() == Direction.WEST
				.ordinal()) {
			x--;

			exits.get(currentPointKey).add(Direction.WEST);
		}

	}

	/**
	 * Returns a direction to take, if the prefered direction is 
	 * allowed then that will be returned otherwise it will return
	 * and exit from the current location that hasnt been taken before
	 * 
	 * @param prefered
	 * @param v
	 * @return
	 */
	private Direction getDirection(String prefered, View v) {
		String currentPointKey = x + ":" + y;

		if (!exits.containsKey(currentPointKey)) {
			updateCoords(prefered);
			return directions.get(prefered);
		}

		ArrayList<Direction> exitedDirections = exits.get(currentPointKey);

		if (!directionAttempted(directions.get(prefered), exitedDirections)) {
			updateCoords(prefered);
			return directions.get(prefered);
		}

		// Find the position of the prefered direction in the direction
		// ordering
		int index = -1;
		for (int i = 0; i < directionOrder.length; i++) {
			if (directionOrder[i].equals(prefered)) {
				index = i;
				break;
			}
		}

		// Move clockwise through the array to find an allowed
		// direction to take
		String newDirection;
		for (int i = index + 1, j = 0; j < 4; i++, j++) {

			// Ensure that i is within range
			if (i == directionOrder.length) {
				i = 0;
			}

			// Get the new direction
			newDirection = directionOrder[i];

			if (!directionAttempted(directions.get(newDirection), exitedDirections) && v.mayMove(directions.get(newDirection))) {
				// Rotate j times
				for (int k = 0; k <= j; k++) {
					rotateRight();
				}

				updateCoords("foward");
				return directions.get("foward");
			}
		}

		return null;
	}

	/**
	 * Checks to see if a direction is in an array list
	 * 
	 * @param d
	 * @param attmepts
	 * @return wether the direction is contained in the list
	 */
	private boolean directionAttempted(Direction d, ArrayList<Direction> attmepts) {
		for (Direction a : attmepts) {
			if (a.ordinal() == d.ordinal()) {
				return true;
			}
		}

		return false;
	}

	/**
	 * Updates the directions in a left rotataion
	 */
	private void rotateLeft() {
		Direction f = directions.get("left");
		Direction r = directions.get("foward");
		Direction b = directions.get("right");
		Direction l = directions.get("reverse");

		directions.put("foward", f);
		directions.put("right", r);
		directions.put("reverse", b);
		directions.put("left", l);
	}

	/**
	 * Updates the directions in a right rotation
	 */
	private void rotateRight() {
		Direction f = directions.get("right");
		Direction r = directions.get("reverse");
		Direction b = directions.get("left");
		Direction l = directions.get("foward");

		directions.put("foward", f);
		directions.put("right", r);
		directions.put("reverse", b);
		directions.put("left", l);
	}
}