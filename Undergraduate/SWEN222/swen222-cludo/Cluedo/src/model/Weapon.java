package model;

/**
 * The {@code Card} enum class's instances represents the weapons in Cluedo.
 * This class implements the Card interface so it can be treated as a card in accusations.
 *
 * @author Liam O'Neill
 */
public enum Weapon implements Card {

	// weapon instances
	ROPE("Rope"), SPANNER("Spanner"), REVOLVER("Revolver"), LEAD_PIPE("Lead Pipe"),
		DAGGER("Dagger"), CANDLESTICK("Candlestick");

	private final String name;

	/**
	 * Create new {@code Weapon} instance with the passed name.
	 *
	 * @param name weapon name
	 */
	private Weapon(String name){
		this.name = name;
	}

	/**
	 * Get name.
	 *
	 * @return name
	 */
	public String getName(){
		return name;
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
