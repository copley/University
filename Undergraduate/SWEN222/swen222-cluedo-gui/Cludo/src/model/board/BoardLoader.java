package model.board;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Scanner;
import java.util.Set;

import model.PlayerCharacter;
import model.Room;

/**
 * Handles loading of plaintext board data.
 *
 * @author Liam O'Neill
 */
public class BoardLoader {

	/**
	 * Filename for default data file.
	 */
	private static final String FILE_NAME = "data/board.txt";

	/**
	 * Width of board.
	 */
	private final int width;

	/**
	 * Height of board.
	 */
	private final int height;

	/**
	 * A map of PlayerCharacters to their starting positions.
	 * Note this collection is read only.
	 */
	private final Map<PlayerCharacter, Position> startingPositions;

	/**
	 * A map of all room instances to a set of their doorway positions.
	 * Note this collection is read only.
	 */
	private final Map<Room, Set<Position>> entrances;

	/**
	 * 2D array of tile instances at each position.
	 */
	private final Tile[][] tiles;


	/**
	 * Constructs a new BoardLoader, using the default filePath.
	 * If this file doesn't exist a {@code RuntimeException} will be thrown.
	 */
	public BoardLoader(){
		this(FILE_NAME);
	}

	/**
	 * Constructs a new BoardLoader, using the provided filePath.
	 * If this file doesn't exist a {@code RuntimeException} will be thrown.
	 */
	public BoardLoader(String fName){
		try (Scanner scan = new Scanner(new File(FILE_NAME))){
			Map<Integer, Tile> tileIds = parseIdToTile(scan);
			Map<Room, Set<Position>> doorways = parseDoorways(tileIds, scan);
			Map<Integer, PlayerCharacter> charIds = parseIdToChar(scan);
			Map<PlayerCharacter, Position> charStarts = parseCharStarts(charIds, scan);
			Tile[][] tiles = parseBoard(tileIds, scan);
			this.width = tiles.length;
			this.height = tiles[0].length;
			this.entrances = doorways;
			this.startingPositions = charStarts;
			this.tiles = tiles;
		} catch (IOException e) {
			throw new RuntimeException("Can't find board data: " + FILE_NAME, e);
		}
	}

	/**
	 * Parses the id to tile data.
	 * This method will consume tokens on the scanner.
	 *
	 * @param s - scanner to read tokens from
	 * @return map of ids to the corresponding tile instance.
	 */
	private static Map<Integer, Tile> parseIdToTile(Scanner s){
		Map<String, Tile> roomNames = new HashMap<>();
		for(Room r : Room.values()){
			roomNames.put(r.getName(), r);
		}
		for(Tiles t : Tiles.values()){
			roomNames.put(t.getName(), t);
		}
		Map<Integer, Tile> out = new HashMap<>();
		int lines = s.nextInt();
		s.nextLine();
		for(int i = 0; i < lines; i++){
			String[] parts = s.nextLine().split(" ");
			String name = join(" ", Arrays.copyOf(parts, parts.length - 1)).trim();
			int id = Integer.parseInt(parts[parts.length - 1]);
			out.put(id, Objects.requireNonNull(roomNames.get(name)));
		}
		return Collections.unmodifiableMap(out);
	}

	/**
	 * Parses the doorway data.
	 * This method will consume tokens on the scanner.
	 *
	 * @param tileIds - map of ids to the corresponding tile instance
	 * @param s - scanner to read tokens from
	 * @return map of room instances to the corresponding set of doorway positions.
	 */
	private static Map<Room, Set<Position>> parseDoorways(Map<Integer, Tile> tileIds, Scanner s){
		Map<Room, Set<Position>> out = new HashMap<>();
		int lines = s.nextInt();
		s.nextLine();
		for (int i = 0; i < lines; i++){
			Set<Position> doorways = new HashSet<>();
			String[] parts = s.nextLine().split(" ");
			int id = Integer.parseInt(parts[0]);
			for(int j = 1; j < parts.length; j += 2){
				int x = Integer.parseInt(parts[j]);
				int y = Integer.parseInt(parts[j + 1]);
				doorways.add(new Position(x, y));
			}
			out.put((Room)Objects.requireNonNull(tileIds.get(id)), Collections.unmodifiableSet(doorways));
		}
		return Collections.unmodifiableMap(out);
	}

	/**
	 * Parses the id to playerCharacters data.
	 * This method will consume tokens on the scanner.
	 *
	 * @param s - scanner to read tokens from
	 * @return map of ids to the corresponding playerCharacters instances.
	 */
	private static Map<Integer, PlayerCharacter> parseIdToChar(Scanner s){
		Map<String, PlayerCharacter> characterNames = new HashMap<>();
		for(PlayerCharacter c : PlayerCharacter.values()){
			characterNames.put(c.getName(), c);
		}
		Map<Integer, PlayerCharacter> out = new HashMap<>();
		int lines = s.nextInt();
		s.nextLine();
		for(int i = 0; i < lines; i++){
			String[] parts = s.nextLine().split(" ");
			String name = join(" ", Arrays.copyOf(parts, parts.length - 1)).trim();
			int id = Integer.parseInt(parts[parts.length - 1]);
			out.put(id, Objects.requireNonNull(characterNames.get(name)));
		}
		return Collections.unmodifiableMap(out);
	}

	/**
	 * Parse playerCharacter starting positions.
	 * This method will consume tokens on the scanner.
	 *
	 * @param charIds - map of ids to the corresponding playerCharacters instances
	 * @param s - scanner to read tokens from
	 * @return map of playerCharacters to the corresponding starting positions
	 */
	private static Map<PlayerCharacter, Position> parseCharStarts(Map<Integer, PlayerCharacter> charIds, Scanner s){
		Map<PlayerCharacter, Position> out = new HashMap<>();
		int lines = s.nextInt();
		s.nextLine();
		for (int i = 0; i < lines; i++){
			int id = s.nextInt();
			int x = s.nextInt();
			int y = s.nextInt();
			out.put(Objects.requireNonNull(charIds.get(id)), new Position(x, y));
			s.nextLine();
		}
		return Collections.unmodifiableMap(out);
	}

	/**
	 * Parse board tiles.
	 * This method will consume tokens on the scanner.
	 *
	 * @param tileIds - map of ids to the corresponding tile instances
	 * @param s - scanner of read tokens from
	 * @return 2D array of tiles
	 */
	private static Tile[][] parseBoard(Map<Integer, Tile> tileIds, Scanner s){
		int width = s.nextInt();
		int height = s.nextInt();
		s.nextLine();
		Tile[][] out = new Tile[width][height];
		for(int y = 0; y < height; y++){
			for(int x = 0; x < width; x++){
				int id = s.nextInt();
				out[x][y] = Objects.requireNonNull(tileIds.get(id));
			}
			s.nextLine();
		}
		return out;
	}

	/**
	 * Joins elements by placing a delimiter in between them.
	 * Note this method is in Java8's JDK however as Java7 was the target this has been implemented here.
	 *
	 * @param delimiter - delimiter to place in between each element
	 * @param elements - varargs of elements
	 * @return resulting string
	 */
	private static String join(CharSequence delimiter, CharSequence... elements) {
		StringBuilder sb = new StringBuilder();
		for(int i = 0; i < elements.length; i++){
			sb.append(elements[i]);
			if(i != elements.length - 1)
				sb.append(delimiter);
		}
		return sb.toString();
	}

	// Getters and Setters below this point

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public Map<PlayerCharacter, Position> getStartingPositions() {
		return startingPositions;
	}

	public Map<Room, Set<Position>> getEntrances() {
		return entrances;
	}

	public Tile[][] getTiles() {
		return tiles;
	}
}
