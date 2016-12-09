package model;

/**
 * The {@code Card} enum class's instances represents the weapons in Cluedo.
 * This class implements the Card interface so it can be treated as a card in accusations.
 *
 * @author Liam O'Neill
 */
public enum Weapon implements Card {

	// weapon instances
	ROPE("Rope", "Rope.png"), SPANNER("Spanner", "Wrench.png"), REVOLVER("Revolver", "Revolver.png"), LEAD_PIPE("Lead Pipe", "LeadPipe.png"),
		KNIFE("Knifed", "Knife.png"), CANDLESTICK("Candlestick", "Candlestick.png");

	private final String name;
	
	private final String fileName;

	/**
	 * Create new {@code Weapon} instance with the passed name.
	 *
	 * @param name weapon name
	 */
	private Weapon(String name, String fileName){
		this.name = name;
		this.fileName = fileName;
	}

	/**
	 * Get name.
	 *
	 * @return name
	 */
	public String getName(){
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
	public String toString() {
		return getName();
	}
}
