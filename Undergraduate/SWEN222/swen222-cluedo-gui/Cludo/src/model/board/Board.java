package model.board;

import model.Player;
import model.PlayerCharacter;
import model.Room;

import java.util.*;

/**
 * The {@code Board} class defines the Cluedo board.
 *
 * This class is holds the positions of players and finds paths for which the players could take.
 *
 * @author Liam O'Neill
 */
public class Board {

	/**
	 * Width of board.
	 */
	private final int width;

	/**
	 * Height of board.
	 */
	private final int height;

	/**
	 * Map of rooms to the set of all their roomsToDoorways(entrances).
	 * Note under the current implementation this collection is read only.
	 */
	private final Map<Room, Set<Position>> roomsToDoorways;

	/**
	 * 2D array of tiles representing the Room Tile/Empty Tile/Invalid Tile at that position.
	 * The first array represents the x-axis, and the nested arrays the y-axis.
	 * It's guaranteed that the length of the x-axis is this.width and similarly for the y-axis and this.height.
	 */
	private final Tile[][] tiles;

	/**
	 * List of players.
	 */
	private final List<Player> players;

	/**
	 * Map of players to their positions on the board.
	 * Note under the current implementation this collection is read only.
	 */
	private final Map<Player, Position> playerPositions = new HashMap<>();

	/**
	 * Set of all doorways.
	 * This is the union of all sets mapped to in roomsToDoorways.
	 */
	private final Set<Position> doorways;

	/**
	 * Create new Board with the parsed players on the board.
	 *
	 * @param players list of player instances who are playing.
	 */
	public Board(List<Player> players){
		this.players = players;

		BoardLoader loader = new BoardLoader();
		this.width = loader.getWidth();
		this.height = loader.getHeight();
		this.roomsToDoorways = loader.getEntrances();
		this.tiles = loader.getTiles();

		Set<Position> doorways = new HashSet<>();
		for(Set<Position> e : loader.getEntrances().values())
			doorways.addAll(e);

		this.doorways = Collections.unmodifiableSet(doorways);

		Map<PlayerCharacter, Position> startingPositions = loader.getStartingPositions();
		for(Player p : players){
			playerPositions.put(p, Objects.requireNonNull(startingPositions.get(p.getPlayerCharacter())));
		}
	}

	/**
	 * Get the position of a player
	 *
	 * @param p player we want the position of
	 * @return position of player
	 */
	public Position getPlayerPosition(Player p) {
		Position out = playerPositions.get(p);
		if(out == null)
			throw new IllegalArgumentException("No such player: " + p);
		return out;
	}
	
	public Player getPlayerAt(Position p) {
		for (Player player : players) {
			if (playerPositions.get(player).equals(p)) {
				return player;
			}
		}
		
		return null;
	}

	/**
	 * Sets the position of a player
	 *
	 * @param p Player we want the position of
	 * @param pos Position of the player
	 */
	public void setPlayerPosition(Player p, Position pos) {
		ensureInBounds(pos);
		playerPositions.put(p, pos);
	}

	/**
	 * Get valid neighbours of a position.
	 * Starts with the 4 adjoining positions (up, down, left, right),
	 * and removes positions which are out of bounds, invalid tiles,
	 * rooms which are on  the other side of a wall.
	 *
	 * @param p position to get neighbours of
	 * @return set of all valid neighbours.
	 */
	private Set<Position> getNeighbours(Position p){
		Tile tile = tiles[p.x][p.y];

		if(tile instanceof Room) {
			return roomsToDoorways.get(tile);
		} else {
			List<Position> nbhs = new ArrayList<>(Arrays.asList(p.right(), p.left(), p.up(), p.down()));
			for (Iterator<Position> it = nbhs.iterator(); it.hasNext(); ){
				Position nbh = it.next();

				// nbh is out of the board's bounds
				if(nbh.x < 0 || nbh.x >= width || nbh.y < 0 || nbh.y >= height){
					it.remove();
					continue;
				}

				Tile nbhTile = tiles[nbh.x][nbh.y];

				if(nbhTile instanceof Room){

					// if p isn't a doorway, then this room isn't a nbh
					Set<Position> doors = roomsToDoorways.get(nbhTile);
					if (!doors.contains(p)){
						it.remove();
						continue;
					}
				}

				// can't move onto an invalid tile
				if(nbhTile == Tiles.INVALID_TILE){
					it.remove();
					continue;
				}
			}
			return new HashSet<>(nbhs);
		}
	}

	/**
	 * Finds the path between two positions, if it exists, otherwise returns null.
	 *
	 * @param start position the path will start at.
	 * @param goal position the path will end at.
	 *
	 * @return list of positions if there is a path between the two positions, otherwise null.
	 */
	public List<Position> findPath(Position start, Position goal){
		if(!isInsideBounds(start))
			throw new IllegalArgumentException("start position not in bounds: " + start);

		if(!isInsideBounds(goal))
			throw new IllegalArgumentException("goal position not in bounds: " + goal);

		Tile targetTile = tiles[goal.x][goal.y];

		Queue<DijkstraNode> fringe = new PriorityQueue<>();
		Set<Position> closedSet = new HashSet<>();
		Map<Position, DijkstraNode> leastCostSoFar = new HashMap<>();

		DijkstraNode startNode = new DijkstraNode(start, null, 0);
		leastCostSoFar.put(start, startNode);
		fringe.offer(startNode);

		while(!fringe.isEmpty()){
			DijkstraNode current = fringe.poll();

			// if the current position is equal to the goal, the path is complete.
			if(current.position.equals(goal)){
				return constructPath(current);
			}

			// if the current position is inside the target room then the path is complete as the room is one big tile.
			else if(targetTile instanceof Room){
				Tile currentTile = tiles[current.position.x][current.position.y];
				if(currentTile == targetTile){
					return constructPath(current);
				}
			}

			closedSet.add(current.position);

			for(Position nbh : getNeighbours(current.position)){
				if(closedSet.contains(nbh))
					continue;

				// additional cost of 1, as only straight line movement is allowed
				int costSoFar = current.costSoFar + 1;

				DijkstraNode bestNbh = leastCostSoFar.get(nbh);
				if(bestNbh == null || costSoFar < bestNbh.costSoFar){
					DijkstraNode nbhNode = new DijkstraNode(nbh, current, costSoFar);
					leastCostSoFar.put(nbh, nbhNode);
					fringe.offer(nbhNode);
				}
			}
		}

		// null is returned in the case no path exits
		return null;
	}

	/**
	 * Form optimum path from final AStarNode instance.
	 *
	 * @param end final node from findPath method
	 * @return list of positions in ascending order with respect to time.
	 */
	private List<Position> constructPath(DijkstraNode end){
		List<Position> path = new ArrayList<>();
		for(DijkstraNode current = end; current.from != null; current = current.from){
			path.add(current.position);
		}
		Collections.reverse(path);
		return path;
	}

	/**
	 * Checks if a position is in the bounds of the board.
	 *
	 * @param p position to test
	 * @return true iff {@code 0 =< p.x < width && 0 =< p.y < height}
	 */
	private boolean isInsideBounds(Position p){
		return p.x >= 0 && p.x < width && p.y >= 0 && p.y < height;
	}

	/**
	 * Returns a tile at a position.
	 *
	 * @param p position to get tile at
	 * @return tile instance at that position.
	 * @throws java.lang.IllegalArgumentException iff the point is out of bounds see {@link #isInsideBounds(Position)}.
	 */
	public Tile getTileAtPosition(Position p) {
		ensureInBounds(p);
		return tiles[p.x][p.y];
	}

	/**
	 * Get board height.
	 *
	 * @return board height
	 */
	public int getHeight() {
		return height;
	}

	/**
	 * Get board width.
	 *
	 * @return board width
	 */
	public int getWidth() {
		return width;
	}

	/**
	 * Get list of players
	 *
	 * @return list of players
	 */
	public List<Player> getPlayers() {
		return players;
	}

	/**
	 * Get the room passed the player is current in.
	 *
	 * @param p player to find the room of
	 * @return room which the player is in, if the player isn't in a room returns null
	 */
	public Room getPlayerRoom(Player p){
		Position pos = playerPositions.get(p);
		Tile tileAt = tiles[pos.x][pos.y];

		if(tileAt instanceof Room)
			return (Room) tileAt;
		else
			return null;
	}

	/**
	 * Check whether there is a doorway at the passed position.
	 *
	 * @param p position to check at
	 * @return true iff there exists a doorway at the given position
	 */
	public boolean isDoorway(Position p){
		ensureInBounds(p);
		return doorways.contains(p);
	}

	private void ensureInBounds(Position p){
		if(!isInsideBounds(p))
			throw new IllegalArgumentException("Out of bounds position: " + p);
	}

	public void movePlayerIntoRoom(Player player, Room room){
		Position freePosition = null;
		for(int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				Tile t = tiles[x][y];
				Position pos = new Position(x, y);
				if (t == room) {
					if (freePosition == null || !playerPositions.values().contains(pos))
						freePosition = pos;
				}
			}
		}
		setPlayerPosition(player, freePosition);
	}

	/**
	 * Private static inner class which holds the values needed for a A* node.
	 */
	private static class DijkstraNode implements Comparable<DijkstraNode> {

		/**
		 * Position of the node on the board.
		 */
		public final Position position;

		/**
		 * Node which this one came from.
		 */
		public final DijkstraNode from;

		/**
		 * Cost of travelling to this node.
		 */
		public final int costSoFar;

		/**
		 * Construct new AStarNode with the given values.
		 *
		 * @param position - {@code Position} of this node on the board
		 * @param from - {@code AStarNode} which this came from
		 * @param costSoFar - cost to travelling to this node
		 */
		public DijkstraNode(Position position, DijkstraNode from, int costSoFar) {
			this.position = position;
			this.from = from;
			this.costSoFar = costSoFar;
		}

		/**
		 * Compares this instance to another {@code AStarNode}.
		 * Equivalent to calling {@code Integer.compare(this.costSoFar, other.costSoFar)}.
		 *
		 * @param other another {@code AStarNode}
		 * @return comparative int following the conditions above
		 */
		@Override
		public int compareTo(DijkstraNode other) {
			return Integer.compare(costSoFar, other.costSoFar);
		}

		/**
		 * Returns the hashcode for this {@code AStarNode}.
		 *
		 * @return a hash code for this {@code AStarNode}
		 */
		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + costSoFar;
			result = prime * result + ((from == null) ? 0 : from.hashCode());
			result = prime * result
					+ ((position == null) ? 0 : position.hashCode());
			return result;
		}

		/**
		 * Determines whether or not two AStarNode are equal. Two instances of
		 * {@code AStarNode} are equal if the values of their
		 * {@code position}, {@code from}, {@code costSoFar}, {@code estTotal} member fields.
		 *
		 * @param obj an object to be compared with this {@code AStarNode}
		 * @return {@code true} if the object to be compared is
		 *         an instance of {@code AStarNode} and has
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
			DijkstraNode other = (DijkstraNode) obj;
			if (costSoFar != other.costSoFar)
				return false;
			if (from == null) {
				if (other.from != null)
					return false;
			} else if (!from.equals(other.from))
				return false;
			if (position == null) {
				if (other.position != null)
					return false;
			} else if (!position.equals(other.position))
				return false;
			return true;
		}

		/**
		 * Returns a {@code String} that represents the value of this {@code AStarNode}.
		 *
		 * @return a string representation of this {@code AStarNode}.
		 */
		@Override
		public String toString() {
			return "AStarNode [position=" + position + ", from=" + from
					+ ", costSoFar=" + costSoFar + "]";
		}
	}
}
