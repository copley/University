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
    The bag is represented by a list of tiles.
    The bag should be initialised to have the standard distribution of tiles:

    2 blank tiles                                      0 points
    Ex12, Ax9, Ix9, Ox8, Nx6, Rx6, Tx6, Lx4, Sx4, Ux4  1 point 
    Dx4, Gx3                                            2 points
    Bx2, Cx2, Mx2, Px2                                    3 points
    Fx2, Hx2, Vx2, Wx2, Yx2                            4 points
    Kx1                                                    5 points
    Jx1, Xx1                                            8 points
    Qx1, Zx1                                            10 points
 */

public class Bag
{
    // ArrayList that stores all the tiles
    private ArrayList<Tile> tiles;
    
    /**
     * Constructor for the Bag class. Got info for reading files from http://www.mkyong.com/java/how-to-read-file-from-java-bufferedreader-example/
     * 
     * Will initilise the list of titles so when a new tile is needed one can be
     * radomly chosen quickly
     */
    public Bag()
    {
        Trace.println("[D] Bag class created");
        
        tiles = new ArrayList<Tile>();
        
        reset();
    }
    
    /**
     * Function randomly selects a tile from the tiles array and returns it
     */
    public Tile getNewTile()
    {
        // If there are more tiles available then return a random one
        if( this.isMoreTiles() )
        {
            Random r = new Random();
            int index = r.nextInt(tiles.size());
            Tile t = tiles.get(index);
            tiles.remove(index);
        
            return t;
        }
        
        return null;
    }
    
    /**
     * Returns true if there are tiles remaining in the bag
     * other wise false is returned 
     */
    public boolean isMoreTiles() { return ( tiles.size() != 0 ); }
    
    /**
     * Resets the bag of tiles
     */
    public boolean reset()
    {
        // Reset the tiles array to contain nothing
        tiles = new ArrayList<Tile>();
        
        try
        {
            // Opens a reader to the file
            BufferedReader in = new BufferedReader( new FileReader("tile-bag.txt"));
            
            // Reads lines and turns them into tiles
            String line = in.readLine();
            while( line != null )
            {
                String[] tileComponents = line.split(" ");
                tiles.add(new Tile(tileComponents[0], Integer.parseInt(tileComponents[1])));
                
                line = in.readLine();
            }
            
            return false;
        }
        catch( IOException e )
        {
            // Alert user to the error
            UI.println("There was an error when reading from the tiles file");
            UI.println("If you wish to try again please click the reset button");
            
            // Return true to let caller know that error has occored
            return true;
        }
    }

}
