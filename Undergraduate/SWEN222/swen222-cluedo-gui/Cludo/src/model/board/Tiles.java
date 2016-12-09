package model.board;

/**
 * The {@code Tiles} enum class has the {@code EMPTY_TILE} and {@code INVALID_TILE} instances which are the only non-room tiles.
 *
 * @author Liam O'Neill
 */
public enum Tiles implements Tile {

	/**
	 * Tile which isn't a room nor invalid.
	 */
	EMPTY_TILE("Empty Tile"),

	/**
	 * Tile inside the square board, however a player can't move onto it.
	 */
	INVALID_TILE("Invalid Tile");

	/**
	 * Name of tile
	 */
	private final String name;


	/**
	 * Create new Tile instance with the given name.
	 *
	 * @param name
	 */
	private Tiles(String name){
		this.name = name;
	}

	/**
	 * Returns a String representation of this object.
	 *
	 * @return a string representation
	 */
	public String getName(){
		return name;
	}
}
