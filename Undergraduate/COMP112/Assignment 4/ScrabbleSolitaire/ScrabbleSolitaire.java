// This program is copyright VUW.
// You are granted permission to use it to construct your answer to a COMP112 assignment.
// You may not distribute it in any other way without permission.

/* Code for Assignment 7
 * Name: Daniel Thomas Braithwaite
 * Usercode: braithdani
 * ID: 300313770
 */

import ecs100.*;
import java.util.*;
import java.io.*;
import java.awt.Color;

/** Lets a user play a solitaire game of Scrabble.
Scrabble is a word game in which players take turns at adding words to
a crossword by placing tiles on a board. Each tile has a letter and a score.

There are 102 tiles in the whole game, which start off in a bag.

The player has a rack which holds seven tiles.
The player can rearrange tiles on their rack while they try to construct words;
they can move  tiles from their rack onto the board to make a word, and can move them
around on the board or back to the rack, until they commit to their word.  
The rack is then refilled with random tiles from the bag to replace the ones they put out.
They can't move any tiles that have been committed.

The user can drag tiles around with the mouse - "pressing" on top of a tile will pick it up
(unless the user is already "holding" a tile), and "releasing" on the rack or on an empty
space on the board will put the currently held tile at that place.  Attempting to place
the tile on a full rack will not succeed. Placing the tile on top of a tile on the rack which
has some space will push the tiles to the side to make space for it.

PROGRAM DESIGN
Each tile has a name, and a score.  It also contains the name of the image file for drawing
the tile.  All the images are square, and have a size of 48 pixels.

The program has two arrays and an ArrayList to keep track of the tiles:
bag:   an arrayList of the remaining tiles.
rack:  an array of up to 7 tiles. Nulls represent cells with no tile .
board: a 15x15 array of cells where the tiles can be placed.

The game is displayed with the board at the top, and the rack underneath.
When the player has picked up a tile to move it, but hasn't placed it yet,
the tile is shown in the top left corner.

There is a Restart button, which will restart the game with an empty board, a refilled bag,
and a rack with 7 tiles taken from the bag.

There is a "Commit" button, which will commit all the tiles on the board that haven't yet been
committed, and will refill the rack from the bag (as long as there are tiles in the bag).

 */

public class ScrabbleSolitaire implements UIMouseListener, UIButtonListener, UISliderListener
{
    // Save Game Key
    private static final String SAVE_GAME_KEY = "SCRABBLE_SOLITAIRE";
    private static final String SAVE_GAME_KEY_LOG = "SCRABBLE_SOLITAIRE_LOG";
    public static final int SLIDER_MAX = 101;

    // Static Variables
    private static int HoldingX = 50;
    private static int HoldingY = 45;

    // fields to hold the bag, rack and board, and the tile in the hand
    private Board board;
    private Rack rack;
    private Tile holding;
    private int score;
    private int roundScore;

    // Varable to hold the logger
    private Log log;

    // Board used to play the logs back
    private PlaybackBoard pb;
    private boolean drawingTempBoard;
    private int sliderVal;

    private boolean changesMade = false;

    private boolean isRunning;
    /**
     * Set up the GUI (buttons and mouse listener) and restart the game
     */
    public ScrabbleSolitaire()
    {
        // Set mouse and button listener
        UI.setMouseMotionListener(this);

        UI.setImmediateRepaint(false);

        // Set up the buttons
        UI.addButton("Commit", this);
        UI.addButton("Save", this);
        UI.addButton("Load", this);
        UI.addButton("Log Playback", this);
        UI.addButton("Stop Log Playback", this);
        UI.addButton("Restart", this);

        UI.addSlider("Playback Slider (Speed)", -2, 2, 0, this);
        sliderVal = 0;

        // Create new board object and reset it
        board = new Board();
        rack = new Rack();

        isRunning = true;
        sliderVal = 0;

        restart();
    }

    /**
     * User can drag a tile from the board or the rack to a space on the board or the rack.
     * The user can also drag a tile from the board or rack to a full place on the rack if the
     *    tiles on the rack could be shifted up or down to make space for it.
     *  If the mouse is pressed on a tile in the rack and released and redraw
     */
    public void mousePerformed(String action, double x, double y)
    {
        if( isRunning )
        {
            if( action.equals("pressed") )
            {
                // Make sure the player isnt in log playback mode
                if( pb != null )
                {
                    UI.println("You cant peform any action, you are in log playback mode");
                }
                else if( rack.isPosOnRack(x, y) ) 
                {
                    Tile c = rack.getClickedTile(x, y);
                    holding = c;

                    //c.pickup();
                    Trace.println("Cell On Rack Clicked");

                }
                else if( board.on(x, y) )
                {
                    int[] rowCol = board.rowCol(x, y);
                    Tile c = board.pickup(rowCol[0], rowCol[1]);
                    holding = c;

                    //c.pickup();
                    Trace.println("Cell On Board Clicked");
                }

                if( pb == null ) draw();
            }

            else if( action.equals("released") )
            {
                // Check to see if you are actually holding a tile
                if( holding != null )
                {
                    // Make sure the player isnt in log playback mode
                    if( pb != null )
                    {
                        UI.println("You cant peform any action, you are in log playback mode");
                    }
                    // If the release was on the board object
                    else if( board.on(x, y) )
                    {
                        int[] rowCol = board.rowCol(x, y);
                        Trace.println("[D] Row-Col: Row - " + rowCol[0] + ". Col - " + rowCol[1] + ".");

                        // If that slot on the board is empty the insert it into the board else put it back into
                        // the rack
                        if( board.isEmpty(rowCol[0], rowCol[1]) )
                        {
                            boolean result = board.place(holding, rowCol[0], rowCol[1]);
                            if( result ) holding = null;
                            else
                            {
                                rack.insert(holding);
                                holding = null;
                            }
                        }
                        else
                        {
                            rack.insert(holding);
                            holding = null;
                        }

                    }
                    // If its on the rack insert the tile back into the rack but at the pos
                    // it was dropped
                    else if( rack.isPosOnRack(x, y) )
                    {
                        rack.insert(holding, x);
                        holding = null;
                    }
                    // Else just insert it backinto the rack
                    else
                    {
                        rack.insert(holding);
                        holding = null;
                    }
                }

                if( pb == null ) draw();
            }
        }
    }

    /**
     * Respond to the buttons by calling methods.
     */
    public void buttonPerformed(String button)
    {
        if( button.equals("Commit") && isRunning )
        {
            if( pb == null )
            {
                if( board.validPlay() )
                {
                    // Get the current round score
                    roundScore = board.getRoundScore();
                    Trace.println("Round Score: " + roundScore);

                    // Update score
                    score += roundScore;

                    // Create a new commit in the log
                    log.newCommit(score, roundScore);

                    // Commit the working tiles and refill the rack
                    board.commit(log);
                    rack.refil();

                    // Reset the message line
                    UI.printMessage("");

                    // Alert the player that there are no more tiles left
                    if( !rack.isMoreTiles() )
                    {
                        UI.printMessage("No More Tiles In Bag");
                    }

                    changesMade = true;
                }
                else UI.printMessage("Play cant be commited because it isnt valid");
            }
            else UI.println("You are in log playback mode you cant commit anything");
        }
        else if( button.equals("Save") )
        {
            if( pb == null )
            {
                SaveGame s = new SaveGame();

                // Add score and such
                String data = String.format("%d, %d", score, roundScore);
                s.add(ScrabbleSolitaire.SAVE_GAME_KEY, data);

                // Add the log file
                // Save the log file name
                s.add(ScrabbleSolitaire.SAVE_GAME_KEY_LOG, log.getFilePath());

                // Save the board
                board.save(s);

                // Save the rack
                rack.save(s);

                // Got from UI class documentation
                String fileName = UIFileChooser.save("Scrabble Solitaire Save File Name Chooser");

                s.save(fileName);
                
                changesMade = false;
            }
            else UI.println("You are in log playback mode you cant save the game");
        }
        else if( button.equals("Load") )
        {
            if( pb == null )
            {
                String filePath = UIFileChooser.open("Scrabble Solitaire Save Game Chooser");

                if( filePath != null )
                {
                    if( filePath.indexOf(".ssg") != -1 )
                    {

                        SaveGame s = new SaveGame(filePath);

                        // Should always contain the key but just incase someone tampered with the file

                        // Load board and store wether there was an error
                        boolean b = board.load(s);

                        // Load rack
                        boolean r = false;
                        if( b )  r = rack.load(s);

                        if( s.containsKey(ScrabbleSolitaire.SAVE_GAME_KEY_LOG) && s.containsKey(ScrabbleSolitaire.SAVE_GAME_KEY) && b && r )
                        {
                            changesMade = false;
                            
                            // Load the log from the save game
                            log = new Log(s.get(ScrabbleSolitaire.SAVE_GAME_KEY_LOG));

                            // Retreve the score information
                            String data = s.get(ScrabbleSolitaire.SAVE_GAME_KEY);
                            String[] components = data.split(", ");

                            // Get total score
                            score = Integer.parseInt(components[0]);
                            // Get round score
                            roundScore = Integer.parseInt(components[1]);

                            // Pritnt new information 
                            UI.clearText();
                            UI.println("Log File: " + log.getFilePath());
                            UI.println("\nGame Output:");
                        }
                        else UI.println("The save game is curoupted");
                    }
                    else UI.println("That wasnt a save game file.\nsave game files have the file extenction .ssg");
                }
            }
            else UI.println("You are in log playback mode, you cant load a save game");
        }
        else if( button.equals("Log Playback") )
        {
            if( !isRunning )
            {
                UI.println("Cant play a log file and error has occored");
            }
            else if( pb == null )
            {
                String path = UIFileChooser.open("Choose A Log");

                if( path != null )
                {
                    if( path.indexOf(".sl") == -1 )
                    {
                        UI.println("Not a log file, log files have extection .sl");
                    }
                    else
                    {
                        // Clear the graphics paine
                        UI.clearGraphics(true);

                        pb = new PlaybackBoard(path);
                        //pb.draw(sliderVal);
                        drawingTempBoard = true;

                        while( drawingTempBoard )
                        {
                            pb.draw(sliderVal);

                            if( sliderVal != 0 ) UI.sleep(1000/Math.abs(sliderVal));
                            else UI.sleep(1000);
                        }

                        pb = null;
                    }
                }
            }
            else UI.println("You are allready playing a log back");
        }
        else if( button.equals("Stop Log Playback") )
        {
            if( pb != null ) drawingTempBoard = false;//pb = null;
            else UI.println("No log was being played");
        }
        else if( button.equals("Restart") )
        {
            if( pb == null )
            {
                if( !changesMade )
                {
                    // Reset all the objects
                    UI.clearText();
                    restart();
                }
                else
                {
                    UI.println("WARNING: You have made changes to this game!\nare you sure you want to discard them?");
                    UI.println("NOTE! if this is a loaded game and you discard these changes\nthe log file will become desynched from this game\nand the log playback wont work propperly!");

                    boolean res = UI.askBoolean("Cancle?: ");

                    if( !res )
                    {
                        // Reset all the objects
                        UI.clearText();
                        restart();
                    }
                }
            }
            else UI.println("Cant restart, you are currently in log playback mode");

        }

        // Draw the board and rack aslong as no errors have occored
        if( isRunning && pb == null ) draw();
    }

    /**
     * Is run when the slider is updated, will update the playback of a log if one is
     * being played back
     */
    public void sliderPerformed(String name, double value)
    {
        sliderVal = (int) value;
    }

    /** Restart the game:
     * set the board to be empty,
     * set the rack to have no tiles
     * set the bag to be empty, and then read the file of tile names into the bag
     * (keeping track of bagCount)
     */
    public void restart()
    {
        UI.printMessage("");
        changesMade = false;

        // Print welcome info
        UI.println("Welcome To Scrabble Solitare!\n");
        UI.println("Special Squares: ");
        UI.println("Red - Trippe Word Score");
        UI.println("Pink - Double Word Score");
        UI.println("Blue - Tripple Letter Score");
        UI.println("Cyan - Double Letter Score");
        UI.println("\nTo play click on a tile to pick it up\nand then release the mouse it to drop it");

        // Reset the log
        if( log != null ) log.close();
        log = new Log();
        UI.println("\nLog File: " + log.getFilePath() + "\n");

        UI.println("Game Output:\n");

        // Reset score variables 
        score = 0;
        roundScore = 0;

        // Reset the board
        boolean boardError = board.reset();

        // Reset the rack
        boolean rackError = rack.reset();

        if( boardError || rackError ) 
        {
            UI.println("An error has occored, the game has been stopped.\nplease make sure all relevant files are where they should be");
            isRunning = false;
        }
        else 
        {
            isRunning = true;
            draw();
        }
    }

    /**
     * Draw the board, rack, and hand
     */
    public void draw()
    {
        UI.clearGraphics();

        // If holding a tile draw it to the screen
        if( holding != null ) holding.draw(Board.BoardOffsetX+10, ScrabbleSolitaire.HoldingY);

        // Draw the score
        UI.setFontSize(30);
        UI.drawString("Score: " + score + " ( +" + roundScore + " )", Board.BoardOffsetX + 380, Board.BoardOffsetY-20);

        boolean boardError = board.draw();
        boolean rackError = rack.draw();

        if( boardError || rackError ) 
        {
            UI.println("An error has occored, the game has been stopped.\nplease make sure all relevant files are where they should be");
            isRunning = false;
        }
        else UI.repaintGraphics();
    }

    public static void main(String[] args)
    {
        ScrabbleSolitaire obj = new ScrabbleSolitaire();
    }        

}
