package model;

import model.board.Tile;

/**
 * The {@code Room} enum class's instances represents the rooms in Cluedo's board.
 *
 * @author Liam O'Neill
 */
public enum Room implements Card, Tile {

	// all rooms
	KITCHEN("Kitchen"), BALL_ROOM("Ballroom"), CONSERVATORY("Conservatory"),
	BILLIARD_ROOM("Billiard Room"), LIBRARY("Library"), STUDY("Study"),
	HALL("Hall"), LOUNGE("Lounge"), DINING_ROOM("Dining Room"), CELLAR("Cellar");

	/**
	 * Name of the room.
	 */
	private final String name;

	/**
	 * Create a new room with the passed room.
	 *
	 * @param name room name.
	 */
	private Room(String name) {
		this.name = name;
	}

	/**
	 * Get the name of the room.
	 *
	 * @return name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Returns a {@code String} representation of this object.
	 *
	 * @return {@code String} representation of this object
	 */
	@Override
	public String toString(){
		return name;
	}
}
