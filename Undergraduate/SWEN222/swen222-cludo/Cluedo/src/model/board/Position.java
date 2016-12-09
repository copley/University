package model.board;

/**
 * The {@code Position} class defines a point representing a location
 * in {@code (x,y)} coordinate space.
 *
 * @author Liam O'Neill
 */
public class Position {

	/**
	 * The X coordinate of this {@code Position}.
	 */
	public final int x;

	/**
	 * The Y coordinate of this {@code Position}.
	 */
	public final int y;

	/**
	 * Create new Position with the passed x and y values.
	 *
	 * @param x value for x
	 * @param y value for y
	 */
	public Position(int x, int y) {
		this.x = x;
		this.y = y;
	}

	/**
	 * Returns the hashcode for this {@code Position}.
	 * @return a hash code for this {@code Position}.
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + x;
		result = prime * result + y;
		return result;
	}

	/**
	 * Determines whether or not two points are equal. Two instances of
	 * {@code Position} are equal if the values of their
	 * {@code x} and {@code y} member fields, representing
	 * their position in the coordinate space, are the same.
	 * @param obj an object to be compared with this {@code Position}
	 * @return {@code true} if the object to be compared is
	 *         an instance of {@code Position} and has
	 *         the same values; {@code false} otherwise.
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Position other = (Position) obj;
		if (x != other.x)
			return false;
		if (y != other.y)
			return false;
		return true;
	}

	/**
	 * Create a new {@code Position} which represents {@code (x + 1, y)}.
	 *
	 * @return position to the right.
	 */
	public Position right(){
		return new Position(x + 1, y);
	}

	/**
	 * Create a new {@code Position} which represents {@code (x - 1, y)}.
	 *
	 * @return position to the left.
	 */
	public Position left(){
		return new Position(x - 1, y);
	}

	/**
	 * Create a new {@code Position} which represents {@code (x, y - 1)}.
	 *
	 * @return position upwards.
	 */
	public Position up(){
		return new Position(x, y - 1);
	}

	/**
	 * Create a new {@code Position} which represents {@code (x, y + 1)}.
	 *
	 * @return position downwards.
	 */
	public Position down(){
		return new Position(x, y + 1);
	}

	/**
	 * Returns a {@code Position} that represents the value
	 * of this {@code Position}.
	 * @return a string representation of this {@code Position}.
	 */
	@Override
	public String toString() {
		return "Position [x=" + x + ", y=" + y + "]";
	}
}
