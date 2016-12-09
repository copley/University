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

/**    
The rack is represented by an array of tiles.
The rack should be displayed with a rectangular border and each tile displayed
by drawing the image of the tile at that place. Empty spaces in the rack 
should be represented by nulls and displayed as empty.
The user can select a position on the rack using the mouse. The selected tile
(or empty space) should be highlighted with a border around it.

Suggested methods:
- constructor
- boolean on(double x, double y) : is the point x,y on the rack
- int index(double x, double y) : returns the index of the cell at the point x,y on the rack
- Tile pickup(int pos) : pick up the tile at the index pos (null if no tile)
- boolean place(Tile tile, int pos) : place tile at the index pos on the rack
pushing tiles to the side if necessary. (return true if successful, and false if rack full)
- void fill(Bag bag) :  fill all the space on the rack from the bag
- void draw() :  draw the rack
- void reset() : reset the rack to initial empty state.

 */

public class Rack
{
    // Save Game Key
    private static final String SAVE_GAME_KEY = "RACK";

    // Static Variables
    private static int RackOffsetX = 270;
    private static int RackOffsetY = 710;
    public static int RackSize = 7;

    private Tile[] rack;
    private Bag bag;

    public Rack()
    {
        rack = new Tile[Rack.RackSize];

        // Initlise bag
        bag = new Bag();

        // Reset everything
        reset();
    }

    /**
     * Draws all the cells to the rack
     */
    public boolean draw()
    {
        for( int i = 0; i < rack.length; i++ ) 
        {
            // If there is a tile at that position in the rack then display it
            if( rack[i] != null ) 
            {
                boolean tileError = rack[i].draw((i * Tile.TileWidth) + Rack.RackOffsetX, Rack.RackOffsetY);
                if( tileError ) return tileError;
            }
            // Draw border around tile
            UI.drawRect((i * Tile.TileWidth) + Rack.RackOffsetX, Rack.RackOffsetY, Tile.TileWidth, Tile.TileHeight);
            
        }
        
        return false;
    }

    /**
     *  Resets the bag and gets 7 new tiles for the rack
     */
    public boolean reset()
    {
        boolean bagError = bag.reset();

        if( bagError ) return true;

        for( int i = 0; i < rack.length; i++ ) 
        {
            rack[i] = bag.getNewTile();
        }

        return false;
    }

    /**
     * Returns true if the x and y passed are inside the rack bounds
     */
    public boolean isPosOnRack( double x, double y )
    {
        return ( ( x > Rack.RackOffsetX && x < ( Rack.RackOffsetX + ( Rack.RackSize * Tile.TileWidth ) ) ) && ( y > Rack.RackOffsetY && y < ( Rack.RackOffsetY + Tile.TileHeight ) ) );
    }

    /**
     * Returns true if there are more tiles available else return false
     */
    public boolean isMoreTiles()
    {
        return bag.isMoreTiles();
    }

    /**
     * Returns the tile that the x and y sit on top of
     * 
     * Note: This function assumes that you have checked that the position is on the rack
     */
    public Tile getClickedTile( double x, double y )
    {
        // Get index of the clicked tile
        int index = (int) ( ( x - Rack.RackOffsetX ) / Tile.TileHeight );

        // Retreve tile, remove from rack and return it 
        Tile t = rack[index];
        rack[index] = null;
        return t;
    }

    /**
     * Inserts the passed tile into the rack at the specified column
     * 
     * Note: Assumes that the x cord is inside the rack bounds
     */
    public void insert(Tile t, double x )
    {

        int col = (int) ( ( x - Rack.RackOffsetX ) / Tile.TileHeight );

        if( rack[col] == null ) rack[col] = t;
        else
        {
            // Find closest cell to the position tile was dropped at
            int freePos = -1;
            int distance = 8;

            for( int i = 0; i < Rack.RackSize; i++ )
            {
                if( rack[i] == null && ( Math.abs(col - i ) < distance ) ) 
                {
                    distance = ( Math.abs(col - i ) );
                    freePos = i;
                }
            }

            if ( freePos > col )
            {
                // Move cells along
                for( int i = freePos; i > col; i-- )
                {
                    rack[i] = rack[i-1];
                }

                // Insert cell into slot
                rack[col] = t;
            }
            else
            {
                // Move cells along
                for( int i = freePos; i < col; i++ )
                {
                    rack[i] = rack[i+1];
                }

                // Insert cell into slot
                rack[col] = t;
            }
        }
    }

    /**
     * Inserts the tile t at the first avaliable spot
     */
    public void insert(Tile t)
    {
        boolean hasFinished = false;
        int i = 0;

        while( !hasFinished )
        {
            if( rack[i] == null )
            {
                hasFinished = true;
                rack[i] = t;
            }

            i++;
        }
    }

    /**
     * Refils the rack with tiles from the bag
     */
    public void refil()
    {
        for( int i = 0; i < rack.length; i++ )
        {
            // If spot dosnt contain a tile get a new one
            if( rack[i] == null ) rack[i] = bag.getNewTile();
        }
    }

    /**
     * Saves all the information related to the rack class
     */
    public void save( SaveGame s )
    {
        // Add rack data into the save game
        String data = "";

        for( int i = 0; i < rack.length; i++ )
        {
            if( rack[i] != null ) data += rack[i].toString() + ";";
            else data += "NULL;";
            data += "\n";
        }

        s.add(Rack.SAVE_GAME_KEY, data);

        // Add bag to the save game
        bag.save(s);
    }

    /**
     * Loads all the information that the rack needs
     */
    public boolean load( SaveGame s )
    {
        boolean b = bag.load(s);

        if( b )
        {
            String data = s.get(Rack.SAVE_GAME_KEY);

            String[] rows = data.split(";");
            for( int i = 0; i < rows.length; i++ )
            {
                if( !rows[i].equals("NULL") )
                {
                    String[] info = rows[i].substring(rows[i].indexOf("[")+1, rows[i].indexOf("]")).split(", ");
                    rack[i] = new Tile(info[0], Integer.parseInt(info[1]));
                }
                else rack[i] = null;
            }
        }
        else return false;

        return true;
    }

}
