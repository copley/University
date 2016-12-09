package model;

/**
 *
 */
public enum PlayerCharacter  implements Card {

	// PlayerCharacter instances
	MISS_SCARLETT("Miss Scarlett", "S", "MissScarlet.png"), COLONEL_MUSTARD("Colonel Mustard", "M", "ColonelMustard.png"),
	MRS_WHITE("Mrs. White", "W", "MrsWhite.png"), THE_REVEREND_GREEN("The Reverend Green", "G", "ReverendGreen.png"),
	MRS_PEACOCK("Mrs. Peacock", "P", "MrsPeacock.png"), PROFESSOR_PLUM("Professor Plum", "L", "ProfessorPlum.png");

	/**
	 * Character's name.
	 */
	private final String name;

	/**
	 * The initial to be displayed at
	 * this characters location
	 */
	private final String initial;
	
	private final String fileName;

	/**
	 * Create a new {@code PlayerCharacter} instance with the passed name.
	 *
	 * @param name name of PlayerCharacter
	 */
	private PlayerCharacter(String name, String initial, String fileName) {
		this.name = name;
		this.initial = initial;
		this.fileName = fileName;
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
	
	public String getFileName() {
		return fileName;
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
