package controller;

import model.*;
import model.board.Board;
import model.board.Position;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Handles the game logic for the Cluedo game
 * access and updates the models for the view
 *
 * @author braithdani
 *
 */
public class Controller {

	private Board board;

	private List<Card> solution;

	private List<PlayerCharacter> characters;
	private int numPlayers;

	private Player winner;
	private boolean gameRunning;

	public Controller() {
		characters = new ArrayList<>();
		gameRunning = false;
	}

	/**
	 * Function should be called after the players have been set
	 *
	 * The function creates a randomly generated solution and
	 * distributes the rest of the cards evenly to the players
	 */
	public void init() {
		if (characters.size() < numPlayers) {
			throw new IllegalStateException("The size of the player list and the number of players doesn't match.");
		}

		// Get shuffled lists of PlayerCharacters, Weapons, Rooms
		List<Card> characterCards = new ArrayList<>(Arrays.asList((Card[]) PlayerCharacter.values()));
		List<Card> weaponCards = new ArrayList<>(Arrays.asList((Card[]) Weapon.values()));
		List<Card> roomCards = new ArrayList<>(Arrays.asList((Card[]) Room.values()));

		Collections.shuffle(characterCards);
		Collections.shuffle(weaponCards);
		Collections.shuffle(roomCards);

		// Generate a random solution
 		solution = new ArrayList<>();
 		solution.add(characterCards.remove(0));
 		solution.add(weaponCards.remove(0));
 		solution.add(roomCards.remove(0));

		// Create a list of all the cards and shuffle it
 		List<Card> cards = new ArrayList<>(characterCards);
 		cards.addAll(weaponCards);
 		cards.addAll(roomCards);

		Collections.shuffle(cards);

		// Randomly distribute the remaining cards to the players, Do we need to also distribute weapon cards
		// to the rooms?
		int cardsPerPlayer = cards.size() / characters.size();
		List<Player> players = new ArrayList<>();

		// For each of the characters construct there player
		for (PlayerCharacter pc : characters) {

			// Get the cards for the player
			List<Card> playerCards = new ArrayList<>();
			for (int i = 0; i < cardsPerPlayer; i++) {
				playerCards.add(cards.remove(0));
			}

			players.add(new Player(pc, playerCards));
		}

		// Create a new board with the players
		board = new Board(players);

		gameRunning = true;
	}

	/**
	 * @return A list of all the weapons in the game
	 */
	public List<Weapon> getWeapons() {
		return Arrays.asList(Weapon.values());
	}

	/**
	 * @return A list of all the player characters in the game
	 */
	public List<PlayerCharacter> getCharacters() {
		return Arrays.asList(PlayerCharacter.values());
	}

	/**
	 * @return A list of all the rooms in the game
	 */
	public List<Room> getRooms() {
		return Arrays.asList(Room.values());
	}

	/**
	 * Checks an accusation against the solution to the game, if its
	 * correct then it marks the player as the winner otherwise it
	 * removes the player from the game
	 *
	 * @param weapon
	 * @param character
	 * @param room
	 * @return weather the solution is correct
	 */
	public boolean checkAccusation(Player p, Weapon weapon, PlayerCharacter character, Room room) {
		if (!solution.contains(weapon) || !solution.contains(character) || !solution.contains(room)) {
			removePlayer(p);

			// Check to see if there are any players left
			for (Player player : board.getPlayers()) {
				if (player.isPlaying()) {
					return false;
				}
			}

			// No players left so game over
			gameRunning = false;
			return false;
		}

		setWinner(p);
		return true;
	}

	/**
	 * Makes sure the player is in the correct room
	 * for a accusation
	 *
	 * @param p - Player to be checked
	 * @return true if the player was in the right room otherwise false
	 */
	public boolean validateAccusationRoom(Player p) {
		return board.getPlayerRoom(p) == Room.CELLAR;
	}

	/**
	 * Checks the suggestion and returns a list
	 * of cards for all the players, if they cant refute
	 * it then there list is empty, if they have lost then
	 * it is null
	 *
	 * @param weapon Weapon in the suggestion
	 * @param character Character in the suggestion
	 * @param room Room in the suggestion
	 * @return List<List<Card>> of cards, one List<Card> for each player
	 */
	public List<List<Card>> checkSuggestion(Weapon weapon, PlayerCharacter character, Room room) {
		List<List<Card>> ref = new ArrayList<>();
		List<Player> players = getPlayers();

		for (Player player : players) {
			if (player.isPlaying()) {
				if(player.getPlayerCharacter() == character){
					board.movePlayerIntoRoom(player, room);
				}
				List<Card> cards = player.refuteSuggestion(character, weapon, room);
				ref.add(cards);
			} else {
				ref.add(null);
			}
		}

		return ref;
	}

	/**
	 * Makes sure the player was in the correct room for a suggestion
	 *
	 * @param p - Player to be checked
	 * @return true if the player was in the right room otherwise false
	 */
	public boolean validateSuggestionRoom(Player p) {
		return board.getPlayerRoom(p) != null && board.getPlayerRoom(p) != Room.CELLAR;
	}

	/**
	 * Set a player as a winner, and set the game
	 * as over
	 *
	 * @param p - player to set as winner
	 */
	private void setWinner(Player p) {
		gameRunning = false;
		winner = p;
	}

	public Player getWinner() {
		return winner;
	}

	/**
	 * Remove a player, mark them as they player has lost
	 *
	 * @param p - player to marked as being lost
	 */
	private void removePlayer(Player p) {
		p.lost();
	}

	/**
	 * Gets the room the a player is in
	 *
	 * @param p - The player we want the room of
	 * @return the room the player is in
	 */
	public Room getRoom(Player p) {
		return board.getPlayerRoom(p);
	}

	/**
	 * Move a player around the board, will generate a path
	 * from the current position to the new position and make
	 * sure that the number of steps in that path is less than
	 * the dice roll provided (will throw an InvalidMoveException otherwise)
	 *
	 * @param p - the player being moved
	 * @param newPos - new position for the player
	 * @param diceRoll - the number of squares the player can move
	 * @throws InvalidGameException
	 */
	public void movePlayer(Player p, Position newPos, int diceRoll) throws InvalidGameException {
		// Determine path to new point.
		List<Position> path = board.findPath(board.getPlayerPosition(p), newPos);

		// If the path is null, no such path exists.
		if(path == null){
			throw new InvalidGameException();
		}

		// Make sure that the dice roll is enough to cover the move.
		if (path.size() > diceRoll) {
			throw new InvalidGameException();
		}

		// If it is then move the player.
		board.setPlayerPosition(p, newPos);
	}

	/**
	 * @return weather the game is running
	 */
	public boolean gameOver() {
		return !gameRunning;
	}

	/**
	 * @return a list of the players
	 */
	public List<Player> getPlayers() {
		return board.getPlayers();
	}

	/**
	 * @return the number of players
	 */
	public int getNumberOfPlayers() {
		return numPlayers;
	}

	/**
	 * Calculates the characters that can still be picked by a player
	 *
	 * @return
	 */
	public List<PlayerCharacter> getAvailableCharacters() {
		// TODO: Remove all the ones that are being used
		List<PlayerCharacter> remainingCharacters = new ArrayList<>();
		for (PlayerCharacter pc : PlayerCharacter.values()) {
			if (!characters.contains(pc)) {
				remainingCharacters.add(pc);
			}
		}

		return remainingCharacters;
	}

	/**
	 * Add a character to the list of characters
	 *
	 * @param character to be added
	 */
	public void addPlayerCharacter(PlayerCharacter character) {
		// Need to create a new player and then insert it into the list of characters
		// also set the position of the player to that characters starting position
		characters.add(character);
	}

	/**
	 * Sets the number of players and will throw an exception
	 * if the number of players entered is between 3 and 6
	 *
	 * @param numPlayers
	 * @throws IllegalArgumentException - If the input is invalid
	 */
	public void setNumberOfPlayers(int numPlayers) {
		if (numPlayers < 3 || numPlayers > 6) {
			throw new IllegalArgumentException("Number of players must be between 3 and 6");
		}

		this.numPlayers = numPlayers;
	}

	public Board getBoard() {
		return board;
	}
}
