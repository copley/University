package model;

/**
 *
 */
public enum PlayerCharacter  implements Card {

	// PlayerCharacter instances
	MISS_SCARLETT("Miss Scarlett", "S"), COLONEL_MUSTARD("Colonel Mustard", "M"),
	MRS_WHITE("Mrs. White", "W"), THE_REVEREND_GREEN("The Reverend Green", "G"),
	MRS_PEACOCK("Mrs. Peacock", "P"), PROFESSOR_PLUM("Professor Plum", "L");

	/**
	 * Character's name.
	 */
	private final String name;

	/**
	 * The initial to be displayed at
	 * this characters location
	 */
	private final String initial;

	/**
	 * Create a new {@code PlayerCharacter} instance with the passed name.
	 *
	 * @param name name of PlayerCharacter
	 */
	private PlayerCharacter(String name, String initial) {
		this.name = name;
		this.initial = initial;
	}

	/**
	 * Get name.
	 *
	 * @return name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Returns the initial to be displayed
	 * on the board
	 * @return
	 */
	public String getInitial() {
		return initial;
	}

	/**
	 * Returns a {@code String} representation of this object.
	 *
	 * @return {@code String} representation of this object
	 */
	@Override
	public String toString() {
		return getName();
	}
}
