package model;

import model.board.Tile;

/**
 * The {@code Room} enum class's instances represents the rooms in Cluedo's board.
 *
 * @author Liam O'Neill
 */
public enum Room implements Card, Tile {

	// all rooms
	KITCHEN("Kitchen", "Kitchen.png"), BALL_ROOM("Ballroom", "Ballroom.png"), CONSERVATORY("Conservatory", "Conservatory.png"),
	BILLIARD_ROOM("Billiard Room", "BilliardRoom.png"), LIBRARY("Library", "Library.png"), STUDY("Study", "Study.png"),
	HALL("Hall", "Hall.png"), LOUNGE("Lounge", "Lounge.png"), DINING_ROOM("Dining Room", "DiningRoom.png"), CELLAR("Cellar", "");

	/**
	 * Name of the room.
	 */
	private final String name;

	private final String fileName;
	
	/**
	 * Create a new room with the passed room.
	 *
	 * @param name room name.
	 */
	private Room(String name, String fileName) {
		this.name = name;
		this.fileName = fileName;
	}

	/**
	 * Get the name of the room.
	 *
	 * @return name
	 */
	public String getName() {
		return name;
	}
	
	public String getFileName() {
		return fileName;
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
