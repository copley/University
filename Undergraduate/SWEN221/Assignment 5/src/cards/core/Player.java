package cards.core;

/**
 * Represents a player on the board, which can be either a computer player or a
 * human. Every player has a direction (North, East, South, West) and a hand of
 * cards which they are currently playing.
 * 
 * @author David J. Pearce
 * 
 */
public class Player implements Cloneable {
	
	/**
	 * Represents one of the four position on the table (North, East, South and
	 * West).
	 * 
	 * @author David J. Pearce
	 * 
	 */
	public enum Direction {
		NORTH,
		EAST,
		SOUTH,
		WEST;
		
		/**
		 * Returns the next direction to play after this one (i.e. following a
		 * clockwise rotation).
		 * 
		 * @return
		 */
		public Direction next() {
			if (this.equals(NORTH)) return EAST;
			if (this.equals(EAST)) return SOUTH;
			if (this.equals(SOUTH)) return WEST;
			return NORTH;
		}
	}

	public final Direction direction;
	public final Hand hand;
	
	public Player(Direction direction) {
		this.direction = direction;
		this.hand = new Hand();
	}
	
	private Player(Direction direction, Hand hand) {
		this.direction = direction;
		this.hand = hand;
	}
	
	/**
	 * Get the position in which this player is sitting.
	 * 
	 * @return
	 */
	public Direction getDirection() {
		return direction;
	}
	
	/**
	 * Get the current hand of this player.
	 * 
	 * @return
	 */
	public Hand getHand() {
		return hand;
	}
	
	public Player clone() {
		return new Player(this.direction, this.hand.clone());
	}
}
