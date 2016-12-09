package view;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

import model.Card;
import model.Player;
import model.PlayerCharacter;
import model.Room;
import model.Weapon;
import model.board.Board;
import model.board.Position;
import controller.Controller;
import controller.InvalidGameException;
import model.board.Tile;
import model.board.Tiles;

/**
 * Command line interface for the cluedo game,
 * displays information to the user and then supplies
 * input to the game controller
 *
 * @author braithdani
 *
 */
public class CLI {

	private static final Scanner IN = new Scanner(System.in);
	private static final PrintStream OUT = System.out;
	private static final Random RAND = new Random();

	/**
	 * List of menu items to be displayed to the user
	 */
	private static final String DISPLAY_BOARD_MENU_ELEMENT = "Display Board";
	private static final String MOVE_MENU_ELEMENT = "Move";
	private static final String VIEW_CARDS_MENU_ELEMENT = "View Cards";
	private static final String SUGGESTION_MENU_ELEMENT = "Suggestion";
	private static final String ACCUSATION_MENU_ELEMENT = "Accusation";
	private static final String END_TURN_MENU_ELEMENT = "End Turn";

	/**
	 * List of menu items to be displayed to the user
	 */
	private static final List<String> menu = Collections
			.unmodifiableList(Arrays.asList(DISPLAY_BOARD_MENU_ELEMENT,
					MOVE_MENU_ELEMENT, VIEW_CARDS_MENU_ELEMENT,
					SUGGESTION_MENU_ELEMENT, ACCUSATION_MENU_ELEMENT,
					END_TURN_MENU_ELEMENT));

	/**
	 * The list of letters for the x axis of the board
	 */
	private static final List<String> letters = Collections.unmodifiableList(Arrays.asList("A", "B", "C", "D", "E", "F", "G", "H", "I",
			"J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "V", "W", "X", "Y"));

	/**
	 * The controller that handles the game
	 * logic
	 */
	private Controller controller;

	/**
	 * Dice roll for the current player
	 */
	private int diceRoll;

	/**
	 * Main game loop, will handle getting input from the user and then calling
	 * actions on the controller. Function is kind of long sorry about that, splitting it
	 * up would of been tricky
	 */
	public void run() {
		setup();

		while (!controller.gameOver()) {
			List<Player> players = controller.getPlayers();

			for (int i = 0; i < players.size(); i++) {

				boolean turnOver = false;
				Player p = players.get(i);

				// Calculate how many spaces the player can move
				diceRoll = 1 + RAND.nextInt(6);

				// Make sure the player can play
				if (!p.isPlaying()) {
					continue;
				}

				displayBoard();

				OUT.println("Player: " + p.getPlayerCharacter().getName() + " (" + (i + 1) + ")");

				String selection = "";

				while (!selection.equals(END_TURN_MENU_ELEMENT) && !controller.gameOver()) {

					// Make sure the player can play
					if (!p.isPlaying() || turnOver) {
						break;
					}

					Room room = controller.getRoom(p);

					OUT.println();
					OUT.println();

					// Let user choose from a menu
					selection = selectFromList(menu,
							"What would you like to do? ");

					switch (selection) {

					case DISPLAY_BOARD_MENU_ELEMENT:
						displayBoard();
						break;

					case MOVE_MENU_ELEMENT:
						// Make sure that the player hasn't moved before
						if (diceRoll == -1) {
							OUT.println("You have already moved this turn!");
							continue;
						}

						movePlayer(p);
						break;

					case VIEW_CARDS_MENU_ELEMENT:
						// Display the cards the user has
						displayPlayerCards(p);
						break;

					case SUGGESTION_MENU_ELEMENT:
						// If player enters a room then they can make a
						// suggestion concerning that room
						// a weapon and a character.
						if (!controller.validateSuggestionRoom(p)) {
							OUT.println("You are currently not in a room so you cant make a sugestion");
							OUT.println("(or you are in the cellar and you cant make a suggestion there ether)");
							continue;
						}

						OUT.println("You are in the " + room.toString());
						makeSuggestion(p);

						turnOver = true;
						break;

					case ACCUSATION_MENU_ELEMENT:
						// Otherwise a player can make an accusation
						// Player either wins if they are right or they are
						// removed from the game
						if (!controller.validateAccusationRoom(p)) {
							OUT.println("You are not in cellar");
							continue;
						}
						makeAccusation(p);
						break;

					default:
						OUT.println("I dont understand that");
						break;
					}
				}
			}
		}

		OUT.println();
		OUT.println("GAME OVER!");
		if (controller.getWinner() != null) {
			OUT.println(controller.getWinner().getPlayerCharacter().getName() + " is the winner");
		} else {
			OUT.println("No one is the winner");
		}
	}

	private void movePlayer(Player p) {
		displayBoard();

		OUT.println("The dice was rolled and you can move "
				+ diceRoll + " spaces!");

		Position newPos = getPosition("Enter coord of where you would like to move");

		// TODO: Possible way to deal with invalid room
		try {
			controller.movePlayer(p, newPos, diceRoll);
			OUT.println("Valid move, Moving Player!");
			diceRoll = -1;
		} catch (InvalidGameException e) {
			OUT.println("That move isn't valid");
		}
	}

	/**
	 * Handles the making accusation action of the
	 * menu
	 *
	 * @param p - Player making the accusation
	 */
	private void makeAccusation(Player p) {
		// Get information from the user about the accusation
		Weapon w = selectFromList(controller.getWeapons(),
				"What do you think the murder weapon was? ");
		PlayerCharacter c = selectFromList(
				controller.getCharacters(),
				"Which character do you think did it? ");

		// Make sure the cellar isn't in the list as
		// the murder cant happen there
		List<Room> allowedRooms = new ArrayList<>();
		for (Room r : Room.values()) {
			if (r != Room.CELLAR) {
				allowedRooms.add(r);
			}
		}


		Room r = selectFromList(allowedRooms, "Which room was it in? ");

		boolean correct = controller.checkAccusation(p, w, c, r);

		// If the accusation is correct the print you have won
		// otherwise print that the player has been removed from the
		// game
		if (correct) {
			OUT.println("You have won!");
		} else {
			OUT.println("You have been removed");
		}
	}

	/**
	 * Handles the making suggestion menu action
	 *
	 * @param p - Player making the suggestion
	 */
	private void makeSuggestion(Player p) {
		Room room = controller.getRoom(p);

		Weapon weapon = selectFromList(controller.getWeapons(),
				"What do you think the murder weapon was? ");
		PlayerCharacter character = selectFromList(
				controller.getCharacters(),
				"Which character do you think did it? ");

		OUT.println("Suggestion: " + character.getName() + " in the " + room.getName() + " with the " + weapon.getName());

		List<List<Card>> ref = controller.checkSuggestion(weapon, character, room);

		for (int j = 0; j < ref.size(); j++) {
			List<Card> cards = ref.get(j);

			if (cards == null) {
				OUT.println("Player " + (j+1) + " has lost\n");
			} else if (cards.size() == 0) {
				OUT.println("Player " + (j+1) + " cant refute sugestion\n");
			} else {
				OUT.println("Player " + (j + 1)
						+ " refutes with: ");
				for (Card c : cards) {
					OUT.println("  " + c.toString());
				}
				OUT.println();
			}
		}
	}

	/**
	 * Handles the display users cards action of the
	 * menu
	 *
	 * @param p - Player that wants to display there cards
	 */
	private void displayPlayerCards(Player p) {
		OUT.println("Your cards are: ");
		for (Card c : p.getCards()) {
			OUT.println(c.toString());
		}
	}

	/**
	 * Displays the board object to the user
	 */
	private void displayBoard() {
		OUT.println();

		// Display a key of charactor name to initial
		OUT.println("Key:");
		for (PlayerCharacter c : PlayerCharacter.values()) {
			OUT.println(String.format("%s = %s", c.getName(), c.getInitial()));
		}
		OUT.println();

		String[][] board = renderBoard();

		for (int i = 0; i < board.length; i++) {
			OUT.print(String.format("%-2s :", i));
			for (int j = 0; j < board[i].length; j++) {
				if (board[i][j] != null) {
					OUT.print(board[i][j] + " ");
				} else {
					OUT.print("R ");
				}


			}

			OUT.println();
		}

		OUT.print("    ");
		for (String s : letters) {
			OUT.print(s + " ");
		}
		OUT.println();
	}

	/**
	 * Sets up the game, asks the user for the total
	 * number of players and then gets the character
	 * that each of the players wants to use
	 */
	private void setup() {
		controller = new Controller();

		OUT.println("SETTING UP GAME!");
		OUT.println("----------------");
		OUT.println();

		// Set the number of players
		setNumPlayers();

		OUT.println();
		OUT.println("PLAYER SELECTION!");
		OUT.println("-----------------");

		// For each of the players they need to be set
		for (int i = 0; i < controller.getNumberOfPlayers(); i++) {
			OUT.println();

			PlayerCharacter character = selectFromList(
					controller.getAvailableCharacters(),
					String.format("%d - Choose your character: ", i + 1));
			controller.addPlayerCharacter(character);
		}

		controller.init();
	}

	/**
	 * Asks the user for the total number of players and tells the controller
	 * how many where selected
	 */
	private void setNumPlayers() {
		while (true) {
			try {
				int numPlayers = getInt("How many players? (between 3 and 6): ");
				controller.setNumberOfPlayers(numPlayers);

				return;
			} catch (IllegalArgumentException e) {
				OUT.println(e.getMessage());
			}
		}
	}

	/**
	 * Create 2d array of strings that represents the board
	 * so it can be printed
	 *
	 * " " Empty tile
	 * "#" Can't move here
	 * number Position of that player
	 *
	 * @return 2D array of strings representing the board
	 */
	public String[][] renderBoard() {
		Board board = controller.getBoard();
		String[][] boardRender = new String[board.getHeight()][board.getWidth()];

		//
		for (int i = 0; i < board.getHeight(); i++) {
			for (int j = 0; j < board.getWidth(); j++) {
				Tile t = board.getTileAtPosition(new Position(j, i));

				if (board.isDoorway(new Position(j, i))) {
					boardRender[i][j] = "D";
				} else if (t.equals(Tiles.EMPTY_TILE)) {
					boardRender[i][j] = " ";
				} else if (t.equals(Tiles.INVALID_TILE)) {
					boardRender[i][j] = "#";
				} else if (t instanceof Room) {
					boardRender[i][j] = "R";
				}
			}
		}

		// Add the players to the board
		for (int i = 0; i < board.getPlayers().size(); i++) {
			Player p = board.getPlayers().get(i);
			Position pos = board.getPlayerPosition(p);

			boardRender[pos.y][pos.x] = p.getPlayerCharacter().getInitial();
		}

		return boardRender;
	}

	// //////////////////
	// Helper Methods //
	// //////////////////

	/**
	 * Takes a list of options and get the user to select one from it
	 *
	 * @param options
	 * @return
	 */
	private static <T> T selectFromList(List<T> options, String message) {
		int option = -1;

		while (option < 0 || option >= options.size()) {
			for (int i = 0; i < options.size(); i++) {
				OUT.println(String.format("%d) %s", i + 1, options.get(i)
						.toString()));
			}

			option = getInt(message) - 1;
		}

		return options.get(option);
	}

	private static int getInt(String message) {
		while (true) {
			OUT.print(message);

			try {
				return IN.nextInt();
			} catch (InputMismatchException e) {
				OUT.println("Input MUST be an number!");
				IN.nextLine();
			}
		}
	}

	private static Position getPosition(String message) {
		while (true) {
			OUT.println(message);
			try {
				OUT.print("Y Pos (0 - 24): ");
				int y = IN.nextInt();
				OUT.print("X Pos (A - Y): ");
				String xChar = IN.next();

				int x = letters.indexOf(xChar.toUpperCase());

				if (x == -1) {
					continue;
				}

				return new Position(x, y);

			} catch (InputMismatchException e) {
				OUT.println("Input was incorrect");
				IN.nextLine();
			}
		}
	}

	public static void main(String[] args) {
		new CLI().run();
	}
}
