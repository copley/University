package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.WeakHashMap;

import javax.imageio.ImageIO;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.SwingConstants;

import model.Card;
import model.Player;
import model.PlayerCharacter;
import model.Room;
import model.Weapon;
import model.board.Board;
import model.board.Position;
import model.board.Tile;
import model.board.Tiles;
import controller.Controller;
import controller.InvalidGameException;

/**
 * GUI view for the cluedo game
 * 
 * @author danielbraithwt
 */
public class CluedoFrame extends JFrame {

	private static final Random random = new Random();

	private static final int PADDING = 20;

	private static final int NEW_TURN_STATE = -1;
	private static final int MOVE_STATE = 0;
	private static final int SUGGESTION_STATE = 1;
	private static final int ACCUSATION_STATE = 2;
	private static final int GAME_OVER_STATE = 3;

	private int INFORMATION_START_X = 20;
	private int INFORMATION_START_Y = 40;
	private Font INFORMATION_FONT = new Font("TimesRoman", Font.BOLD, 20);

	private int BOARD_START_X = 20;
	private int BOARD_START_Y = INFORMATION_START_Y + 30;
	private int BOARD_TILE_SIZE = 20;

	private int DICE_START_X = 20;
	private int DICE_START_Y = BOARD_START_Y + (25 * BOARD_TILE_SIZE) + PADDING;
	private int DICE_SIZE = 100;

	private int CARDS_START_X = DICE_START_X + DICE_SIZE + 20;
	private int CARDS_START_Y = DICE_START_Y;
	private int CARD_HEIGHT = 167;
	private int CARD_WIDTH = 108;
	private int CARD_GAP = 30;

	private JPanel canvas;
	private JMenuBar menuBar;
	private JMenu file;
	private JMenu game;
	private JMenuItem closeGame;
	private JMenuItem resetGame;
	private JToolBar toolBar;
	private JButton endTurn;
	private JButton move;
	private JButton suggestion;
	private JButton accusation;

	private Map<Card, BufferedImage> cardImages;
	private BufferedImage cardBack;

	private Controller controller;
	private int currentPlayer;
	private int diceRoll;
	private int state;

	private boolean turnStarted;

	private int currentSquareX;
	private int currentSquareY;

	private String popupMessage;
	private int popupMessageX;
	private int popupMessageY;

	public CluedoFrame() {
		setLayout(new BorderLayout());

		// Create a key listener to listen for our shortcut
		// keys
		KeyListener macroListener = new KeyListener() {
			@Override
			public void keyReleased(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_Z) {
					nextTurn();
				} else if (e.getKeyCode() == KeyEvent.VK_X) {
					setStateMoving();
				} else if (e.getKeyCode() == KeyEvent.VK_C) {
					setStateSuggestion();
				} else if (e.getKeyCode() == KeyEvent.VK_V) {
					setStateAccusation();
				} else if (e.getKeyCode() == KeyEvent.VK_UP) {
					updateCurrentMoveSquare(0, -1);
				} else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
					updateCurrentMoveSquare(0, 1);
				} else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
					updateCurrentMoveSquare(-1, 0);
				} else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
					updateCurrentMoveSquare(1, 0);
				} else if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					new Thread(new Runnable() {
						@Override
						public void run() {
							attemptMove();
						}
					}).start();
				}
			}

			@Override
			public void keyTyped(KeyEvent e) {}
			
			@Override
			public void keyPressed(KeyEvent arg0) {}
		};
		addKeyListener(macroListener);

		// Create all the menu elements and add them to the window
		menuBar = new JMenuBar();

		file = new JMenu("File");
		menuBar.add(file);

		closeGame = new JMenuItem("Close");
		closeGame.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (verifyQuit()) {
					setVisible(false);
					System.exit(0);
				}

			}

		});
		file.add(closeGame);

		game = new JMenu("Game");
		menuBar.add(game);

		resetGame = new JMenuItem("Reset");
		resetGame.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				int dialogResult = JOptionPane.showConfirmDialog(null,
						"Are you sure you want to reset the game?",
						"Are You Sure?", JOptionPane.YES_NO_OPTION);
				if (dialogResult == JOptionPane.YES_OPTION) {
					startNewGame();
				}
			}

		});
		game.add(resetGame);

		// Create the JPanel that we will use to display the board
		canvas = new JPanel() {
			@Override
			public void paintComponent(Graphics g) {
				super.paintComponent(g);
				draw(g);
			}

			public void draw(Graphics g) {
				Graphics2D g2d = (Graphics2D) g;

				// Compute new sizes for the window
				resize(g2d);

				// Draw the information
				Font origonal = g2d.getFont();
				g2d.setFont(INFORMATION_FONT);
				g2d.setColor(Color.BLACK);
				g2d.drawString("Current Player: "
						+ controller.getPlayers().get(currentPlayer)
								.getPlayerCharacter(), INFORMATION_START_X,
						INFORMATION_START_Y);
				g2d.setFont(origonal);

				int currentX = BOARD_START_X;
				int currentY = BOARD_START_Y;

				// Draw the board
				Board board = controller.getBoard();
				for (int i = 0; i < board.getHeight(); i++) {
					for (int j = 0; j < board.getWidth(); j++) {
						Position pos = new Position(j, i);
						Tile t = board.getTileAtPosition(pos);

						// Set the color of the square depending on the type
						if (board.isDoorway(pos)) {
							g2d.setColor(Color.YELLOW);
						} else if (t.equals(Tiles.EMPTY_TILE)) {
							g2d.setColor(Color.GRAY);
						} else if (t.equals(Tiles.INVALID_TILE)) {
							g2d.setColor(Color.BLACK);
						} else if (t instanceof Room) {
							int index = Arrays.asList(Room.values()).indexOf(t);
							float h = ((float) (100 / Room.values().length) * index) / 100.0f;

							g2d.setColor(Color.getHSBColor(h, 0.68f, 0.66f));
						}

						if (state == MOVE_STATE) {
							if (j == currentSquareX && i == currentSquareY) {
								g2d.setColor(new Color(255, 102, 0));
							}
						}

						g2d.fillRect(currentX, currentY, BOARD_TILE_SIZE,
								BOARD_TILE_SIZE);
						currentX += BOARD_TILE_SIZE;
					}

					currentY += BOARD_TILE_SIZE;
					currentX = BOARD_START_X;
				}

				// Draw the players to the board, with the active player being a
				// diffrent color
				for (int i = 0; i < controller.getPlayers().size(); i++) {
					Position playerPos = board.getPlayerPosition(controller
							.getPlayers().get(i));

					if (i == currentPlayer) {
						g2d.setColor(Color.ORANGE);
					} else {
						g2d.setColor(Color.WHITE);
					}

					g2d.fillOval(BOARD_START_X
							+ (playerPos.x * BOARD_TILE_SIZE), BOARD_START_Y
							+ (playerPos.y * BOARD_TILE_SIZE), BOARD_TILE_SIZE,
							BOARD_TILE_SIZE);
				}

				int currentCardX = CARDS_START_X;
				// Draw the players cards
				for (Card card : controller.getPlayers().get(currentPlayer)
						.getCards()) {
					drawCard(card, currentCardX, CARDS_START_Y, g2d);

					// Increment the card x so we dont overlap the cards and so
					// we have a gap between them
					currentCardX += CARD_WIDTH + PADDING;
				}

				// Draw dice
				if (diceRoll != -1) {
					try {
						BufferedImage dice = ImageIO.read(new File("./dice/dice-"
								+ diceRoll + ".png"));
						g2d.drawImage(dice, DICE_START_X, DICE_START_Y, DICE_SIZE,
								DICE_SIZE, null);
					} catch (IOException e) {
						System.out.println("Failed to read dice image... './dice/dice-" + diceRoll + ".png");
					}
				}

				// Draw popup if necessary
				if (popupMessage != null) {
					FontMetrics fm = g2d.getFontMetrics();
					int width = fm.stringWidth(popupMessage);
					int height = fm.getHeight()
							* (popupMessage.split("\n").length);

					System.out.println(height);

					g2d.setColor(new Color(0.0f, 0.0f, 0.0f, 0.3f));
					g2d.fillRect(popupMessageX, popupMessageY, width + PADDING,
							height + PADDING);
					g2d.setColor(Color.WHITE);
					g2d.drawString(popupMessage, popupMessageX + PADDING / 2,
							popupMessageY + fm.getHeight() + (PADDING / 2) - 3);
				}
			}

			/**
			 * Draws the image associated with a given card to the canvas
			 * @param card
			 * @param x
			 * @param y
			 * @param g2d
			 */
			private void drawCard(Card card, int x, int y, Graphics2D g2d) {
				BufferedImage cardImage = getCardImage(card);
				g2d.drawImage(cardImage, x, y, CARD_WIDTH, CARD_HEIGHT, null);
			}

			/**
			 * Based on the size of the window we want to determine how
			 * big each of the elements should be
			 * 
			 * @param g Graphics object we are drawing to
			 */
			private void resize(Graphics2D g) {
				int width = getWidth();
				int height = getHeight();

				// Remove the padding from the total available height and
				// width
				width -= PADDING;
				height -= PADDING;

				// Account for the size of the dice
				width -= DICE_SIZE;

				// Account for the size of the cards section
				int numCards = controller.getCardsPerPlayer();
				CARD_WIDTH = Math.min(
						(width - (numCards * PADDING)) / numCards, 108);
				CARD_HEIGHT = (int) ((CARD_WIDTH / 108.0f) * 167);

				DICE_SIZE = CARD_WIDTH;

				// Account for the size of the information section
				FontMetrics metrics = g.getFontMetrics(INFORMATION_FONT);
				int fontHeight = metrics.getHeight();
				height -= (fontHeight + INFORMATION_START_X);

				height -= CARD_HEIGHT;

				// Calcuate the tile size of the
				BOARD_TILE_SIZE = Math.min(width, height - PADDING * 3) / 25;

				// Calculate where the board should start
				BOARD_START_X = (width - 24 * BOARD_TILE_SIZE) / 2 + 2
						* PADDING;

				// Set where the information should start
				// INFORMATION_START_X = BOARD_START_X;

				// Calculate where the dice y position is
				DICE_START_Y = BOARD_START_Y + (25 * BOARD_TILE_SIZE) + PADDING;
				DICE_START_X = (width - (DICE_SIZE + PADDING + (CARD_WIDTH + PADDING)
						* numCards))
						/ 2 + 2 * PADDING;

				// Calculate where the cards starts
				CARDS_START_Y = PADDING + BOARD_TILE_SIZE * 25 + PADDING
						+ fontHeight + PADDING;
				CARDS_START_X = DICE_START_X + DICE_SIZE + PADDING;

			}
		};
		canvas.addKeyListener(macroListener);
		canvas.setFocusable(false);
		add(canvas, BorderLayout.CENTER);

		setJMenuBar(menuBar);

		// Set up the toolbar
		toolBar = new JToolBar("Game Controll");

		endTurn = new JButton("");
		endTurn.setFocusable(false);
		endTurn.setIcon(new ImageIcon("./menu/next.png"));
		endTurn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				nextTurn();

			}

		});
		toolBar.add(endTurn);

		move = new JButton("");
		move.setFocusable(false);
		move.setIcon(new ImageIcon("./menu/move.png"));
		move.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				setStateMoving();

			}

		});
		toolBar.add(move);

		suggestion = new JButton("");
		suggestion.setFocusable(false);
		suggestion.setIcon(new ImageIcon("./menu/suggestion.png"));
		suggestion.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				setStateSuggestion();

			}
		});
		toolBar.add(suggestion);

		accusation = new JButton("");
		accusation.setFocusable(false);
		accusation.setIcon(new ImageIcon("./menu/accusation.png"));
		accusation.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				setStateAccusation();

			}
		});
		toolBar.add(accusation);

		add(toolBar, BorderLayout.NORTH);

		// Create the mouse listener for the canvas
		// so when the user clicks they can be moved
		canvas.addMouseListener(new MouseListener() {

			@Override
			public void mouseClicked(MouseEvent e) {
				if (state != MOVE_STATE) {
					return;
				}

				// Find the grid square clicked
				int x = (e.getX() - BOARD_START_X) / BOARD_TILE_SIZE;
				int y = (e.getY() - BOARD_START_Y) / BOARD_TILE_SIZE;

				// Make sure that the click is within bounds
				if (x > controller.getBoard().getWidth() || x < 0
						|| y > controller.getBoard().getHeight() || y < 0) {
					return;
				}

				currentSquareX = x;
				currentSquareY = y;

				new Thread(new Runnable() {
					@Override
					public void run() {
						attemptMove();
					}
				}).start();

				canvas.repaint();
			}
			
			@Override
			public void mouseReleased(MouseEvent arg0) {
			}

			@Override
			public void mousePressed(MouseEvent arg0) {
			}

			@Override
			public void mouseExited(MouseEvent arg0) {
			}

			@Override
			public void mouseEntered(MouseEvent arg0) {
			}
		});

		// Create the motion listener for the canvas so that
		// the popup will be displayed
		canvas.addMouseMotionListener(new MouseMotionListener() {
			@Override
			public void mouseMoved(MouseEvent e) {
				// Find the grid square mouse is over
				int x = (e.getX() - BOARD_START_X) / BOARD_TILE_SIZE;
				int y = (e.getY() - BOARD_START_Y) / BOARD_TILE_SIZE;

				if (x >= controller.getBoard().getWidth() || x < 0
						|| y >= controller.getBoard().getHeight() || y < 0) {
					return;
				}

				// Get the tile
				Tile t = controller.getBoard().getTileAtPosition(
						new Position(x, y));

				// Check to see if its a player
				Player p = controller.getBoard()
						.getPlayerAt(new Position(x, y));

				if (p != null) {
					setPopup(p.getPlayerCharacter().toString(), x, y);
				} else if (t instanceof Room) {
					Room r = (Room) t;
					setPopup(r.toString(), x, y);
				} else {
					setPopup(null, 0, 0);
				}
			}

			@Override
			public void mouseDragged(MouseEvent arg0) {}
		});

		// Create the controller and add a redraw listener to the window
		// so the controller can call a redraw on the GUI
		controller = new Controller();
		controller.addRedrawListener(new RedrawListener() {
			@Override
			public void onRedraw(RedrawEvent e) {
				canvas.repaint();
			}
		});

		// Set up frame so will ask if user is sure before
		// the program closes
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		addWindowListener(new WindowListener() {
			@Override
			public void windowClosing(WindowEvent arg0) {
				if (verifyQuit()) {
					setVisible(false);
					dispose();
					System.exit(0);
				}

			}

			@Override
			public void windowClosed(WindowEvent arg0) {
			}

			@Override
			public void windowActivated(WindowEvent arg0) {
			}

			@Override
			public void windowDeactivated(WindowEvent arg0) {
			}

			@Override
			public void windowDeiconified(WindowEvent arg0) {
			}

			@Override
			public void windowIconified(WindowEvent arg0) {
			}

			@Override
			public void windowOpened(WindowEvent arg0) {
			}

		});

		// Create the map for the cards and load up the card back
		// image, no need to store this in the map as we will always want to be
		// using it
		cardImages = new WeakHashMap<>();
		try {
			cardBack = ImageIO.read(new File("./cards/cardback.png"));
		} catch(IOException e) {
			System.out.println("Error when reading the cardback.png image");
		}

		startNewGame();

		setSize(new Dimension(1000, 1000));
		setVisible(true);
	}

	/**
	 * Sets up the popup message to be shown next time the
	 * draw method is run
	 * 
	 * @param message String to be displayed
	 * @param x x position of the message
	 * @param y y position of the message
	 */
	private void setPopup(String message, int x, int y) {
		popupMessage = message;
		popupMessageX = BOARD_START_X + (x * BOARD_TILE_SIZE)
				+ (BOARD_TILE_SIZE / 2);
		popupMessageY = BOARD_START_Y + (y * BOARD_TILE_SIZE)
				+ (BOARD_TILE_SIZE / 2);

		canvas.repaint();
	}

	/**
	 * Starts the next turn, re rolls the dice and alerts 
	 * the players who needs to come to the screen
	 */
	public void nextTurn() {
		// If the game is over do nothing
		if (state == GAME_OVER_STATE) {
			return;
		}

		nextPlayer();
		state = NEW_TURN_STATE;

		diceRoll = 1 + random.nextInt(6);

		turnStarted = false;

		canvas.repaint();

		// Tell the players whos turn it is
		JOptionPane.showMessageDialog(
				null,
				"Get "
						+ controller.getPlayerName(controller.getPlayers().get(
								currentPlayer)), "Get Player",
				JOptionPane.PLAIN_MESSAGE);

		turnStarted = true;
		canvas.repaint();
	}

	/**
	 * Will update position indicating where the player
	 * wants to move
	 * 
	 * @param dx amount to change X
	 * @param dy amount to change Y
	 */
	public void updateCurrentMoveSquare(int dx, int dy) {
		if (currentSquareX + dx < 0
				|| currentSquareX + dx >= controller.getBoard().getWidth()
				&& currentSquareY + dy < 0
				|| currentSquareY + dy >= controller.getBoard().getHeight()) {
			return;
		}

		currentSquareX += dx;
		currentSquareY += dy;

		canvas.repaint();
	}

	/**
	 * Will take the current position the player wants
	 * to move to and try move there, displaying a message
	 * if the move fails
	 * 
	 * @return true iff the move was successful
	 */
	public boolean attemptMove() {
		// If the state isnt moving state do nothing
		if (state != MOVE_STATE) {
			return false;
		}

		// Attempt the move catching the exception if it fails
		try {
			controller.movePlayer(controller.getPlayers().get(currentPlayer),
					new Position(currentSquareX, currentSquareY), diceRoll);
			diceRoll = -1;

			state = NEW_TURN_STATE;

			return true;
		} catch (InvalidGameException exception) {
			// Show alert telling the user that the move was invalid
			JOptionPane.showMessageDialog(null, "That move was invalid",
					"Move Invalid", JOptionPane.PLAIN_MESSAGE);

			return false;
		} finally {
			canvas.repaint();
		}
	}

	/**
	 * Updates the state so the game knows the player
	 * wants to move
	 */
	public void setStateMoving() {
		// If the game is over the do nothing
		if (state == GAME_OVER_STATE) {
			return;
		}

		Position pos = controller.getBoard().getPlayerPosition(
				controller.getPlayers().get(currentPlayer));
		currentSquareX = pos.x;
		currentSquareY = pos.y;

		// Make sure they havent allready moved this turn
		if (diceRoll == -1) {
			JOptionPane.showMessageDialog(null,
					"You have alleady moved this turn", "Allready Moved",
					JOptionPane.PLAIN_MESSAGE);
			return;
		}

		state = MOVE_STATE;
		canvas.repaint();
	}

	/**
	 * Will get information about the suggestion from the user and then
	 * get the refution information from the controller
	 */
	public void setStateSuggestion() {
		// If game is over dont do anything
		if (state == GAME_OVER_STATE) {
			return;
		}

		state = SUGGESTION_STATE;

		// Make sure the player is in a room that isnt the cellar
		if (!controller.validateSuggestionRoom(controller.getPlayers().get(
				currentPlayer))) {
			JOptionPane.showMessageDialog(this,
					"Must be in a room to make a suggestion",
					"Cant Make Suggestion", JOptionPane.PLAIN_MESSAGE);
			return;
		}

		// Get the weapon
		Weapon weapon = (Weapon) JOptionPane.showInputDialog(null,
				"Select A Weapon", "What weapon was used",
				JOptionPane.PLAIN_MESSAGE, null, controller.getWeapons()
						.toArray(), "3");

		// If they canceled then exit
		if (weapon == null) {
			state = NEW_TURN_STATE;
			return;
		}

		// Get the character
		PlayerCharacter character = (PlayerCharacter) JOptionPane
				.showInputDialog(null, "Select A Character",
						"What character was it?", JOptionPane.PLAIN_MESSAGE,
						null, controller.getCharacters().toArray(), "3");

		// If they canceled then exit
		if (character == null) {
			state = NEW_TURN_STATE;
			return;
		}

		List<List<Card>> refuting = controller.checkSuggestion(weapon,character,
				controller.getBoard().getPlayerRoom(controller.getPlayers().get(currentPlayer)));

		canvas.repaint();

		boolean refuted = false;

		// Go through all the refutions and display the first one
		// to the user
		for (int i = 0; i < refuting.size() && !refuted; i++) {
			if (refuting.get(i) != null && refuting.get(i).size() > 0
					&& i != currentPlayer) {
				new ShowCardsDialog(this, controller.getPlayerName(controller
						.getPlayers().get(i)), refuting.get(i));
				refuted = true;
			}
		}

		if (!refuted) {
			JOptionPane.showMessageDialog(null,
					"No one could refrute suggestion", "No Refutions",
					JOptionPane.PLAIN_MESSAGE);
		}

		nextTurn();
	}

	/**
	 * Verify the user is in the cellar, gets information
	 * about the accusation and then asks the controller if its
	 * correct
	 */
	public void setStateAccusation() {
		if (state == GAME_OVER_STATE) {
			return;
		}

		// Make sure the player is in the cellar
		if (!controller.validateAccusationRoom(controller.getPlayers().get(
				currentPlayer))) {
			JOptionPane.showMessageDialog(this,
					"Must be in the cellar to make a accusation",
					"Cant Make Accusation", JOptionPane.PLAIN_MESSAGE);
			return;
		}

		// Make sure the cellar isn't in the list as
		// the murder cant happen there
		List<Room> allowedRooms = new ArrayList<>();
		for (Room r : Room.values()) {
			if (r != Room.CELLAR) {
				allowedRooms.add(r);
			}
		}

		// Get the room
		Room room = (Room) JOptionPane.showInputDialog(null, "Select A Room",
				"What room was it in", JOptionPane.PLAIN_MESSAGE, null,
				allowedRooms.toArray(), "3");

		// If they canceled then exit
		if (room == null) {
			state = NEW_TURN_STATE;
			return;
		}

		// Get the weapon
		Weapon weapon = (Weapon) JOptionPane.showInputDialog(null,
				"Select A Weapon", "What weapon was used",
				JOptionPane.PLAIN_MESSAGE, null, controller.getWeapons()
						.toArray(), "3");

		// If they canceled then exit
		if (weapon == null) {
			state = NEW_TURN_STATE;
			return;
		}

		// Get the character
		PlayerCharacter character = (PlayerCharacter) JOptionPane
				.showInputDialog(null, "Select A Character",
						"What character was it?", JOptionPane.PLAIN_MESSAGE,
						null, controller.getCharacters().toArray(), "3");

		// If they canceled then exit
		if (character == null) {
			state = NEW_TURN_STATE;
			return;
		}

		if (controller.checkAccusation(controller.getPlayers().get(currentPlayer), weapon, character,room)) {
			JOptionPane.showMessageDialog(null, "You Win",
							"You where correct, congrats :D",
							JOptionPane.PLAIN_MESSAGE);
		} else {
			JOptionPane.showMessageDialog(null, "You Loose",
					"You where incorrect", JOptionPane.PLAIN_MESSAGE);
		}

		// Check to see if the game is over if so set state
		// accordingly, otherwise go to next player
		if (controller.gameOver()) {
			setStateGameOver();
		} else {
			nextPlayer();
			canvas.repaint();
		}
	}

	/**
	 * Set the game as over and see if the user would like to player
	 * another game
	 */
	private void setStateGameOver() {
		state = GAME_OVER_STATE;
		currentPlayer = -1;
		canvas.repaint();

		// See if user wants to play another game
		int dialogResult = JOptionPane.showConfirmDialog(null,
				"Would You Like To Start A New Game", "New Game?",
				JOptionPane.YES_NO_OPTION);
		if (dialogResult == JOptionPane.YES_OPTION) {
			startNewGame();
		}
	}

	/**
	 * Will get rid of all the current game information
	 * and make a fresh one
	 */
	private void startNewGame() {
		controller.reset();

		int numPlayers = getNumPlayers();
		controller.setNumberOfPlayers(numPlayers);

		for (int i = 0; i < numPlayers; i++) {
			selectCharacter();
		}

		controller.init();

		state = NEW_TURN_STATE;

		currentPlayer = -1;
		turnStarted = true;
		nextTurn();
	}

	private void nextPlayer() {
		if (state == GAME_OVER_STATE) {
			return;
		}

		currentPlayer++;

		if (currentPlayer >= controller.getNumberOfPlayers()) {
			currentPlayer = 0;
		}

		if (!controller.getPlayers().get(currentPlayer).isPlaying()) {
			nextPlayer();
		}
	}

	/**
	 * Handles getting the user to create a new
	 * player
	 */
	private void selectCharacter() {
		new PlayerSelectionDialog(this);
	}

	/**
	 * Creates a popup to ask the users how many
	 * players there are
	 * 
	 * @return number of players
	 */
	private int getNumPlayers() {

		while (true) {
			Integer num = (Integer) JOptionPane.showInputDialog(this, "How many players?",
					"Number Of Players", JOptionPane.PLAIN_MESSAGE, null,
					new Object[] { 3, 4, 5, 6 }, "3");
		
			if (num == null) {
				if (verifyQuit()) {
					System.exit(0);
				}
			} else {
				return num;
			}
		}
	}

	/**
	 * Gets the card image associated with a given card, all images
	 * are stored in a WeakHashMap and will be read in only if they
	 * arnt available from the map
	 * 
	 * @param card
	 * @return BufferedImage to be displayed
	 */
	private BufferedImage getCardImage(Card card) {
		
		// If the turn hasnt started then we just want to return
		// the back of the card
		if (!turnStarted) {
			return cardBack;
		} 
		
		// If card not in hash map then read it in and insert it
		if (!cardImages.containsKey(card)) {

			BufferedImage image = null;
			File f;
			try {
				image = ImageIO.read(new File("./cards/" + card.getFileName()));
			} catch (IOException e) {
				System.out.println("Card image not found");
			}

			cardImages.put(card, image);
		}

		return cardImages.get(card);
	}

	/**
	 * Will display a list of cards to the user
	 * 
	 * @author danielbraithwt
	 *
	 */
	private class ShowCardsDialog extends JDialog {
		private JPanel main;
		private JPanel drawing;
		private JButton dismiss;
		private List<Card> cards;
		private String playerName;

		public ShowCardsDialog(Frame owner, String name, List<Card> c) {
			super(owner, true);
			cards = c;
			playerName = name;
			
			setTitle(name + " Refutes With");

			// Compute the size of the window
			int width = cards.size() * (108 + 10) + 10;
			int height = 167 + 50;

			main = new JPanel();
			main.setLayout(new BorderLayout());
			main.setMinimumSize(new Dimension(width, height));

			// Create the panel for displaying the cards
			drawing = new JPanel() {
				@Override
				protected void paintComponent(Graphics g) {
					super.paintComponent(g);
					draw(g);
				}

				private void draw(Graphics g) {
					int currentX = 10;
					int currentY = 10;

					for (Card c : cards) {
						System.out.println(c);
						BufferedImage im = getCardImage(c);
						g.drawImage(im, currentX, currentY, 108, 167, null);

						currentX += 108 + 10;
					}
				}
			};

			drawing.setMinimumSize(new Dimension(width, height));
			main.add(drawing, BorderLayout.CENTER);

			JPanel buttons = new JPanel();
			buttons.setLayout(new FlowLayout());

			// Create the button to close the window
			dismiss = new JButton("Okay");
			dismiss.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent arg0) {
					setVisible(false);
					dispose();
				}

			});
			buttons.add(dismiss);

			main.add(buttons, BorderLayout.SOUTH);

			add(main);

			setMinimumSize(new Dimension(width, height + 40));
			setSize(new Dimension(width, height));

			pack();
			setVisible(true);
		}
	}

	/**
	 * Handles getting a players character selection and name
	 * 
	 * @author danielbraithwt
	 */
	private class PlayerSelectionDialog extends JDialog {
		private JPanel main;
		private JTextField nameInput;
		private ButtonGroup playerSelectionGroup;
		private JButton submit;

		public PlayerSelectionDialog(Frame owner) {
			super(owner, true);
			main = new JPanel();
			main.setLayout(new BorderLayout());

			// Create the panel to hold the name input
			JPanel nameInputPanel = new JPanel();
			nameInputPanel.setLayout(new BoxLayout(nameInputPanel,
					BoxLayout.Y_AXIS));

			// Create the name input box
			JLabel nameLabel = new JLabel("Your Name");
			nameInputPanel.add(nameLabel);
			nameInput = new JTextField();
			nameInputPanel.add(nameInput);
			nameInputPanel.add(new JSeparator(SwingConstants.VERTICAL));

			main.add(nameInputPanel, BorderLayout.NORTH);

			JPanel playerSelectionPanel = new JPanel();
			playerSelectionPanel.setLayout(new BoxLayout(playerSelectionPanel,
					BoxLayout.Y_AXIS));

			// Create the section for selecting the character
			JLabel playerSelectionLabel = new JLabel("Select Your Player");
			playerSelectionPanel.add(playerSelectionLabel);

			playerSelectionGroup = new ButtonGroup();
			for (int i = 0; i < controller.getAvailableCharacters().size(); i++) {
				PlayerCharacter character = controller.getAvailableCharacters()
						.get(i);
				JRadioButton rb = new JRadioButton(character.toString());
				rb.setActionCommand(character.toString());

				if (i == 0) {
					rb.setSelected(true);
				}

				playerSelectionPanel.add(rb);
				playerSelectionGroup.add(rb);
			}
			playerSelectionPanel.add(new JSeparator(SwingConstants.VERTICAL));

			main.add(playerSelectionPanel, BorderLayout.CENTER);

			submit = new JButton("Sumbit!");
			submit.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent arg0) {
					String name = nameInput.getText();
					PlayerCharacter character = null;

					// Find what character is selected
					for (PlayerCharacter c : controller
							.getAvailableCharacters()) {
						if (c.toString().equals(
								playerSelectionGroup.getSelection()
										.getActionCommand())) {
							character = c;
							break;
						}
					}

					// Add the player to the game and close the window
					controller.addPlayerCharacter(character, name);
					setVisible(false);
				}

			});
			main.add(submit, BorderLayout.SOUTH);

			add(main);
			
			setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
			addWindowListener(new WindowListener() {

				@Override
				public void windowClosing(WindowEvent arg0) {
					if (verifyQuit()) {
						System.exit(0);
					}
				}
				
				@Override
				public void windowActivated(WindowEvent arg0) {}

				@Override
				public void windowClosed(WindowEvent arg0) {}

				@Override
				public void windowDeactivated(WindowEvent arg0) {}

				@Override
				public void windowDeiconified(WindowEvent arg0) {}

				@Override
				public void windowIconified(WindowEvent arg0) {}

				@Override
				public void windowOpened(WindowEvent arg0) {}
				
			});

			pack();
			setVisible(true);
		}
	}

	/**
	 * Will ask the user if they want to quit the game
	 * 
	 * @return true iff they say they want to quit
	 */
	public boolean verifyQuit() {
		int closeGame = JOptionPane.showOptionDialog(this,
				"Are you sure you want to quit?", "Are You Sure",
				JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null,
				null, null);

		return closeGame == 0;
	}

	public static void main(String[] args) {
		new CluedoFrame();
	}
}
