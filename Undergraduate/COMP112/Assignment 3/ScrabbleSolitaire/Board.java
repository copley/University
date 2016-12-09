// This program is copyright VUW.
// You are granted permission to use it to construct your answer to a COMP112 assignment.
// You may not distribute it in any other way without permission.

/* Code for COMP112 Assignment
 * Name: Daniel Thomas Braithwaite
 * Usercode: braithdani
 * ID: 300313770
 */

import ecs100.*;
import java.awt.Color;
import java.util.*;
import java.io.*;

/**
Scrabble Board
methods:
- constructor
- boolean on(double x, double y) : is the point x,y on the board
- int[] rowCol(double x, double y) : returns the row/col at the point x,y on the board
- Tile pickup(int row, int col) : pick up the tile in the cell at row/col (null if no tile)
- boolean place(Tile tile, int row, int col) : place tile at the cell at row/col on the board
(return true if successful, and false if unsuccessful (no space at row/col)
- boolean validPlay() :  the working tiles constitute a valid play.
- void commit() :  commit all the working tiles, if it is a valid play.
- void draw() :  draw the board
- void reset() : reset the board to initial empty state.

NOTE: you MUST use these methods for this class so that we can run an automated test
procedure to check your code (copy of it is given at the end of the class).
 */

public class Board
{
    // Static Variables
    public static int BoardOffsetX = 100;
    public static int BoardOffsetY = 100;

    private Cell[][] board;
    private ArrayList<int[]> workingTiles;
    private boolean firstTime;

    /** Construct a new Board object */
    public Board()
    {
        board = new Cell[15][15];
        workingTiles = new ArrayList<int[]>();
        firstTime = true;
    }

    /** Is the position (x,y) on the board */
    public boolean on(double x, double y)
    {
        return (  ( x > Board.BoardOffsetX && x < Board.BoardOffsetX + ( 15 * Tile.TileWidth ) ) && ( y > Board.BoardOffsetY && y < Board.BoardOffsetY + ( 15 * Tile.TileHeight ) ) );
    }

    /**
     * Return the row/col corresponding to the point x,y.
     */
    public int[] rowCol(double x, double y)
    {
        int[] rowCol = null;

        if( this.on(x, y) )
        {
            rowCol = new int[2];

            rowCol[0] = (int) (( y - Board.BoardOffsetY ) / Tile.TileHeight);
            rowCol[1] = (int) (( x - Board.BoardOffsetX ) / Tile.TileWidth);
        }

        return rowCol;
    }

    /**
     * Pickup tile from the board or rack
     */
    public Tile pickup(int row, int col)
    {
        // Remove from working tiles
        int index = -1;
        for( int i = 0; i < workingTiles.size() && index == -1; i++ )
        {
            int[] cords = workingTiles.get(i);

            if( cords[0] == row && cords[1] == col ) index = i;
        }

        Tile t = board[row][col].getTile();

        if( t != null )
        {
            workingTiles.remove(index);
        }
        return t;
    }

    /**
     * Place the tile on the board, if the board position is empty
     */
    public boolean place(Tile tile, int row, int col)
    {
        // Add to working tiles
        workingTiles.add(new int[] {row, col});

        boolean res = board[row][col].setTile(tile);

        return res;
    }
    
    /**
     * Returns wether the cell at row/col is empty or not
     */
    public boolean isEmpty(int row, int col) { return !board[row][col].containsTile(); }

    /**
     * Commit all the workingTiles to the board
     */
    public void commit()
    {
        for( int i = 0; i < board.length; i++ )
        {
            for( int j = 0; j < board[i].length; j++ )
            {
                board[i][j].commit();
            }
        }

        firstTime = false;
        workingTiles = new ArrayList<int[]>();
    }

    /**
     * Will return the socre for the current round, i got the information on scoring from
     * http://en.wikipedia.org/wiki/Scrabble#Scoring
     * 
     * This function assumes that the calling program has checked to see if the current play is valid
     * 
     * The Scoring Rules:
     * - The score of a word is the sum of all the tiles values mulitplied by any bonus square
     * - If you add a letter to another exsisting word you get all the letter values from the exsiting
     *   word but any multiplier under a commited tile cant be used again.
     * - If 7 tiles are played then you get a bonus of 50 points
     */
    public int getRoundScore()
    {

        int count = 0;
        boolean isRow = false;
        
        // Check to see if there in a row //
        ////////////////////////////////////
        if( workingTiles.size() == 1 && leftOrRightTouching(workingTiles.get(0)) )
        {
            isRow = true;
        }
        else if( workingTiles.size() != 1 )
        {
            for( int i = 1; i < workingTiles.size(); i++ )
            {
                if( workingTiles.get(0)[0] == workingTiles.get(i)[0] ) count++;
            }
            
            if( count == workingTiles.size()-1 ) isRow = true;
        }

        // All the places tiles where in the same row
        if( isRow )
        {
            int wordMultiplier = 0;
            int score = 0;

            // Sum up all letters in that row
            //int row = workingTiles.get(0)[0];
            ArrayList<int[]> letterCords = new ArrayList<int[]>();
            letterCords.add(workingTiles.get(0));

            // Itterate through all the cells in that row and sum up scores
            while( letterCords.size() != 0 )
            {
                int[] cord = letterCords.get(0);
                letterCords.remove(0);

                board[cord[0]][cord[1]].scored();

                score += board[cord[0]][cord[1]].getValue();
                wordMultiplier += board[cord[0]][cord[1]].getWordMultiplyer();

                if( ( cord[1] + 1 ) < 15 && board[cord[0]][cord[1] + 1].containsTile() && !board[cord[0]][cord[1] + 1].hasBeenScored() ) 
                {
                    letterCords.add(new int[] {cord[0], cord[1]+1});
                }
                if( ( cord[1] - 1 ) >= 0 && board[cord[0]][cord[1] - 1].containsTile() && !board[cord[0]][cord[1] - 1].hasBeenScored() ) 
                {
                    letterCords.add(new int[] {cord[0], cord[1]-1});
                }
            }

            if( wordMultiplier != 0 ) score = score * wordMultiplier;

            // Then you want to find the words these letters hook into, these words can only be column words
            // For each of the working tiles
            for( int i = 0; i < workingTiles.size(); i++ )
            {
                // Setup variables
                letterCords = new ArrayList<int[]>();
                wordMultiplier = 0;
                int[] cord = workingTiles.get(i);

                // If there is a tile abouve or below then we know its in a column word
                if( ( ( cord[0] + 1 ) < 15 && board[cord[0] + 1][cord[1]].containsTile() ) || ( ( cord[0] - 1 ) >= 0 && board[cord[0] - 1][cord[1]].containsTile() ) )
                {
                    // Add the letter to the letter cords array for further processing
                    letterCords.add(cord);
                }

                // Loop through all the letters in the column if there is a word there
                while( letterCords.size() != 0 )
                {
                    cord = letterCords.get(0);
                    letterCords.remove(0);

                    board[cord[0]][cord[1]].scored();

                    score += board[cord[0]][cord[1]].getValue();
                    wordMultiplier += board[cord[0]][cord[1]].getWordMultiplyer();

                    if( ( cord[0] + 1 ) < 15 && board[cord[0] + 1][cord[1]].containsTile() && !board[cord[0] + 1][cord[1]].hasBeenScored() ) 
                    {
                        letterCords.add(new int[] {cord[0]+1, cord[1]});
                    }
                    if( ( cord[0] - 1 ) >= 0 && board[cord[0] - 1][cord[1]].containsTile() && !board[cord[0] - 1][cord[1]].hasBeenScored() ) 
                    {
                        letterCords.add(new int[] {cord[0]-1, cord[1]});
                    }
                }
                
                if( wordMultiplier != 0 ) score = score * wordMultiplier;

            }

            if( workingTiles.size() == 7 ) score += 50;
            
            return score;
        }
        // Else they must be in a column
        else 
        {
            int wordMultiplier = 0;
            int score = 0;

            //int row = workingTiles.get(0)[0];
            ArrayList<int[]> letterCords = new ArrayList<int[]>();
            letterCords.add(workingTiles.get(0));

            // Itterate through all the cells in that column and sum up scores
            while( letterCords.size() != 0 )
            {
                int[] cord = letterCords.get(0);
                letterCords.remove(0);

                board[cord[0]][cord[1]].scored();

                score += board[cord[0]][cord[1]].getValue();
                wordMultiplier += board[cord[0]][cord[1]].getWordMultiplyer();

                if( ( cord[0] + 1 ) < 15 && board[cord[0] + 1][cord[1]].containsTile() && !board[cord[0] + 1][cord[1]].hasBeenScored() ) 
                {
                    letterCords.add(new int[] {cord[0]+1, cord[1]});
                }
                if( ( cord[0] - 1 ) >= 0 && board[cord[0] - 1][cord[1]].containsTile() && !board[cord[0] - 1][cord[1]].hasBeenScored() ) 
                {
                    letterCords.add(new int[] {cord[0]-1, cord[1]});
                }
            }

            if( wordMultiplier != 0 ) score = score * wordMultiplier;

            // Then you want to find the words these letters hook into, these words can only be row words
            // For each of the working tiles
            for( int i = 0; i < workingTiles.size(); i++ )
            {
                // Setup variables
                letterCords = new ArrayList<int[]>();
                wordMultiplier = 0;
                int[] cord = workingTiles.get(i);

                // If there is a tile left or right we know its hooked into a row word
                if( ( ( cord[1] + 1 ) < 15 && board[cord[0]][cord[1] + 1].containsTile() ) || ( ( cord[1] - 1 ) >= 0 && board[cord[0]][cord[1] - 1].containsTile() ) )
                {
                    // Add the letter to the letter cords array for further processing
                    letterCords.add(cord);
                }

                // Loop through all the letters in the row if there is a word there
                while( letterCords.size() != 0 )
                {
                    cord = letterCords.get(0);
                    letterCords.remove(0);

                    board[cord[0]][cord[1]].scored();

                    score += board[cord[0]][cord[1]].getValue();
                    wordMultiplier += board[cord[0]][cord[1]].getWordMultiplyer();

                    if( ( cord[1] + 1 ) < 15 && board[cord[0]][cord[1] + 1].containsTile() && !board[cord[0]][cord[1] + 1].hasBeenScored() ) 
                    {
                        letterCords.add(new int[] {cord[0], cord[1]+1});
                    }
                    if( ( cord[1] - 1 ) >= 0 && board[cord[0]][cord[1] - 1].containsTile() && !board[cord[0]][cord[1] - 1].hasBeenScored() ) 
                    {
                        letterCords.add(new int[] {cord[0], cord[1]-1});
                    }
                }
                
                if( wordMultiplier != 0 ) score = score * wordMultiplier;

            }
            
            if( workingTiles.size() == 7 ) score += 50;
            
            return score;
        }

    }

    /**
     * Returns true if the working tiles consitute a valid play:
     * The tiles must all be on a single line (row or column) and with no gaps.
     *  "No gaps" means that there must not be any empty cell between any pair
     *  of moveable tiles, though the movable tiles are not necessarily adjacent
     *  if there are any fixed tiles between the movable tiles.
     * At least one of the moveable tiles must be adjacent to a fixed tile,
     * unless it is the very first turn, in which case there are no fixed tiles.
     */
    public boolean validPlay()
    {
        // If not working tiles return false
        if( workingTiles.size() == 0 ) return false;

        // If only one tile placed and its the first time
        if( workingTiles.size() == 1 && firstTime ) return true;

        int[] start = workingTiles.get(0);

        // Check to see if there in a row //
        ////////////////////////////////////
        
        int count = 0;
        
        for( int i = 1; i < workingTiles.size(); i++ )
        {
            if( start[0] == workingTiles.get(i)[0] ) count++;
        }

        // All the places tiles where in the same row
        if( count == workingTiles.size()-1 )
        {
            boolean isTouchingCommited = false;
            int row = start[0];
            
            int min = 15;
            int max = -1;
            
            // Find min and max x positions
            for( int i = 0; i < workingTiles.size(); i++ )
            {
                if( workingTiles.get(i)[1] < min ) min = workingTiles.get(i)[1];
                if( workingTiles.get(i)[1] > max ) max = workingTiles.get(i)[1];
            }
            
            // Itterate through all the cells between min and max to see if there are gaps
            for( int i = min; i <= max; i++ )
            {
                // If there is a break in the tiles just return false
                if( !board[row][i].containsTile() ) 
                {
                    Trace.println("[VP] There was a break in the tiles");
                    return false;
                }
                else if( isTouchingCommited(new int[] {row, i} ) ) isTouchingCommited = true;
            }
            
            if( isTouchingCommited || firstTime ) return true;
        }

        // Check to see if there in a column //
        ///////////////////////////////////////

        // Reset variables
        count = 0;

        for( int i = 1; i < workingTiles.size(); i++ )
        {
            if( start[1] == workingTiles.get(i)[1] ) count++;
        }

        // All the places tiles where in the same row
        if( count == workingTiles.size()-1 )
        {
            boolean isTouchingCommited = false;
            int col = start[1];
            
            int min = 15;
            int max = -1;
            
            // Find min and max x positions
            for( int i = 0; i < workingTiles.size(); i++ )
            {
                if( workingTiles.get(i)[0] < min ) min = workingTiles.get(i)[0];
                if( workingTiles.get(i)[0] > max ) max = workingTiles.get(i)[0];
            }
            
            // Itterate through all the cells between min and max to see if there are gaps
            for( int i = min; i <= max; i++ )
            {
                // If there is a break in the tiles just return false
                if( !board[i][col].containsTile() ) 
                {
                    Trace.println("[VP] There was a break in the tiles");
                    return false;
                }
                else if( isTouchingCommited(new int[] {i, col} ) ) isTouchingCommited = true;
            }
            
            if( isTouchingCommited || firstTime ) return true;
        }
        
        // Is nether a row or column so must be invalid
        return false;      
    }

    /**
     * Determins if the tile at cord is touching a commited tile
     * returns true if the tile is touching a commited one
     */
    private boolean isTouchingCommited(int[] cord)
    {
        // Check Up
        if( ( cord[0] - 1 ) >= 0 && board[cord[0] - 1][cord[1]].isCommited() ) return true;
        // Check Down
        if( ( cord[0] + 1 ) < 15 &&  board[cord[0] + 1][cord[1]].isCommited() ) return true;
        // Check Right
        if( ( cord[1] - 1 ) >= 0 && board[cord[0]][cord[1] - 1].isCommited() ) return true;
        // Check Left
        if( ( cord[1] + 1 ) < 15 && board[cord[0]][cord[1] + 1].isCommited() ) return true;

        return false;
    }
    
    /**
     * 
     */
    private boolean leftOrRightTouching(int[] cord)
    {
        // Check Right
        if( ( cord[1] - 1 ) >= 0 && board[cord[0]][cord[1] - 1].containsTile() ) return true;
        // Check Left
        if( ( cord[1] + 1 ) < 15 && board[cord[0]][cord[1] + 1].containsTile() ) return true;

        return false;
    }

    /**
     * Draw the board.
     * Assumes that the graphics pane has been cleared
     */
    public boolean draw()
    {
        // Draw all the board cells
        for( int i = 0; i < board.length; i++ )
        {
            for( int j = 0; j < board[i].length; j++ )
            {
                boolean cellError = board[i][j].draw();

                if( cellError ) return true;
            }
        }

        return false;
    }

    /**
     * Resets the board class
     * returns true if an error occored
     */
    public boolean reset()
    {
        firstTime = true;
        workingTiles = new ArrayList<int[]>();

        // Rest the board cells
        try
        {
            int x = Board.BoardOffsetX;
            int y = Board.BoardOffsetY;

            // Opens a reader to the file
            BufferedReader in = new BufferedReader( new FileReader("board.txt"));

            // Reads lines and turns them into tiles
            int i = 0;
            String line = in.readLine();
            while( line != null )
            {
                String[] cells = line.split(" ");

                for( int j = 0; j < cells.length; j++ )
                {
                    // Defaultly set the cell type to regular
                    CellType type = CellType.Regular;
                    switch(Integer.parseInt(cells[j]))
                    {
                        // Double Letter Score
                        case 1: type = CellType.DoubleLetter;
                                break;
                        // Tripple Letter Score
                        case 2: type = CellType.TrippleLetter;
                                break;
                        // Double Word Score
                        case 3: type = CellType.DoubleWord;
                                break;
                        // Tripple Word Score
                        case 4: type = CellType.TrippleWord;
                                break;
                        // Starting Square
                        case 5: type = CellType.Start;
                                break;
                    }

                    // Create new tile
                    board[i][j] = new Cell(type, x, y);

                    x += Tile.TileWidth;
                }

                i++;

                x = Board.BoardOffsetX;
                y += Tile.TileHeight;

                line = in.readLine();

            }

            Trace.println("[>] Board initilised");

            return false;
        }
        catch( IOException e )
        {
            UI.println("Failed to open board file");
            UI.println("Press the reset the button if you wish to try again");
            return true;
        }
    }

    //====================================================================
    /**
     * Tests the reset, place, pick, commit, and validPlay methods
     * by putting tiles on the board.
     * Doesn't draw anything.
     */

    public static void testValid(){
        Board b = new Board();

        // Needed to rest the board
        b.reset();

        Tile t = new Tile("A", 1);
        System.out.println("Testing tiles in a row");
        //place tiles in a row 
        b.place(t, 2, 2);
        b.place(t, 2, 3);
        b.place(t, 2, 4);
        if (!b.validPlay()) {System.out.println("2/2, 2/3, 2/4 should be valid");}
        b.place(t, 2, 6);
        if (b.validPlay()) {System.out.println("2/2, 2/3, 2/4, 2/6 should NOT be valid");}
        b.place(t, 2, 5);
        if (!b.validPlay()) {System.out.println("2/2, .. 2/6 should be valid");}
        b.commit();

        System.out.println("Testing tiles in a disconnected row");
        b.place(t, 5, 4);
        b.place(t, 5, 5);
        b.place(t, 5, 6);
        if (b.validPlay()) {System.out.println("disconnected 5/4, 5/5/, 5/6 should NOT be valid");}
        System.out.println("Testing tiles in an L shape");
        b.place(t, 4, 6);
        b.place(t, 3, 6);
        if (b.validPlay()) {System.out.println("5/4, 5/5, 5/6, 4/6, 3/6 should NOT be valid");}
        b.pickup(5, 4);
        b.pickup(5, 5);
        if (!b.validPlay()) {System.out.println("5/6, 4/6, 3/6 should be valid");}

        System.out.println("Testing tiles in a column");
        b.reset();
        b.place(t, 2, 2);
        b.place(t, 3, 2);
        b.place(t, 4, 2);
        if (!b.validPlay()) {System.out.println("2/2, 3/2, 4/2 should be valid");}
        b.place(t, 6, 2);
        if (b.validPlay()) {System.out.println("2/2, 3/2, 4/2, 6/2 should NOT be valid");}
        b.place(t, 5, 2);
        if (!b.validPlay()) {System.out.println("2/2, .. 6/2 should be valid");}
        b.commit();

        System.out.println("Testing tiles in a disconnected column");
        b.place(t, 4, 5);
        b.place(t, 5, 5);
        b.place(t, 6, 5);
        if (b.validPlay()) {System.out.println("disconnected 4/5, 5/5/, 6/5 should NOT be valid");}
        b.place(t, 6, 4);
        b.place(t, 6, 3);
        if (b.validPlay()) {System.out.println(" 4/5, 5/5, 6/5, 6/4, 6/3 should NOT be valid");}
        b.pickup(4, 5);
        b.pickup(5, 5);
        if (!b.validPlay()) {System.out.println("6/5, 6/4, 6/3 should be valid");}

        System.out.println("Testing column connected at ends and side");
        b.reset();
        b.place(t, 10, 5);
        b.commit();
        b.place(t, 7, 5);
        b.place(t, 8, 5);
        b.place(t, 9, 5);
        if (!b.validPlay()) {System.out.println("7,8,9/5 should be valid, given 10/5");}
        b.reset();
        b.place(t, 10, 5);
        b.commit();
        b.place(t, 11, 5);
        b.place(t, 12, 5);
        b.place(t, 13, 5);
        if (!b.validPlay()) {System.out.println("11,12,13/5 should be valid, given 10/5");}
        b.reset();
        b.place(t, 10, 5);
        b.commit();
        b.place(t, 9, 6);
        b.place(t, 10, 6);
        b.place(t, 11, 6);
        if (!b.validPlay()) {System.out.println("9,10,11/6 should be valid, given 10/5");}

        System.out.println("Testing row connected at ends and side");
        b.reset();
        b.place(t, 5, 10);
        b.commit();
        b.place(t, 5, 7);
        b.place(t, 5, 8);
        b.place(t, 5, 9);
        if (!b.validPlay()) {System.out.println("5/7,8,9 should be valid, given 5/10");}
        b.reset();
        b.place(t, 5, 10);
        b.commit();
        b.place(t, 5, 11);
        b.place(t, 5, 12);
        b.place(t, 5, 13);
        if (!b.validPlay()) {System.out.println("5/11,12,13 should be valid, given 5/10");}
        b.reset();
        b.place(t, 5, 10);
        b.commit();
        b.place(t, 6, 9);
        b.place(t, 6, 10);
        b.place(t, 6, 11);
        if (!b.validPlay()) {System.out.println("6/9,10,11 should be valid, given 5/10");}
        
        System.out.println("Testing column spanning fixed tiles");
        b.reset();
        b.place(t, 6, 5);
        b.place(t, 9, 5);
        b.commit();
        if (b.validPlay()) {System.out.println("no working tiles should NOT be valid");}

        b.place(t, 4, 5);
        b.place(t, 5, 5);
        b.place(t, 7, 5);
        if (!b.validPlay()) {System.out.println("4,5,7/5 should be valid, given 6/5");}
        b.place(t, 10, 5);
        if (b.validPlay()) {System.out.println("4,5,7,10/5, should NOT be valid, given 6,9/5");}
        b.place(t, 8, 5);
        if (!b.validPlay()) {System.out.println("4,5,7,8,10/5, should be valid, given 6,9/5");}
        b.reset();
        b.place(t, 6, 5);
        b.commit();
        b.place(t, 3, 5);
        b.place(t, 4, 5);
        b.place(t, 7, 5);
        b.place(t, 8, 5);
        if (b.validPlay()) {System.out.println("3,4,7,8/5, should NOT be valid, given 6/5");}

        System.out.println("Testing row spanning fixed tiles");
        b.reset();
        b.place(t, 5, 6);
        b.place(t, 5, 9);
        b.commit();
        if (b.validPlay()) {System.out.println("no working tiles should NOT be valid");}

        b.place(t, 5, 4);
        b.place(t, 5, 5);
        b.place(t, 5, 7);
        if (!b.validPlay()) {System.out.println("5/4,5,7 should be valid, given 5/6");}
        b.place(t, 5, 10);
        if (b.validPlay()) {System.out.println("5/4,5,7,10, should NOT be valid, given 5/6,9");}
        b.place(t, 5, 8);
        if (!b.validPlay()) {System.out.println("5/4,5,7,8,10, should be valid, given 5/6,9");}
        b.reset();
        b.place(t, 5, 6);
        b.commit();
        b.place(t, 5, 3);
        b.place(t, 5, 4);
        b.place(t, 5, 7);
        b.place(t, 5, 8);
        if (b.validPlay()) {System.out.println("5/3,4,7,8, should NOT be valid, given 5/6");}

        System.out.println("Tests all done");
    }

}
