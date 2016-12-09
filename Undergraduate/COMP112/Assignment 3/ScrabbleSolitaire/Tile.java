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
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;


/**
   Tile
   Represents a single tile.
   Needs at least the name of the image file for drawing itself
   and the value of the tile.  It could store the letter on the tile also,
   though this is not used in this version of the game.

   Needs a
   - constructor
   - draw method, to draw the tile at a position x,y
   - method to return the value of the tile.
*/

public class Tile
{
    public static int TileHeight = 40;
    public static int TileWidth = 40;
    
    String letter;
    int value;
    boolean initilised;
    
    BufferedImage image;
    
    public Tile( String l, int v )
    {
        letter = l;
        value = v;
        
        initilised = false;
    }
    
    /**
     * Function will open the buffered image that will be drawn when the
     * draw function is called, this is to save memory when we have all the tiles in the
     * bag not being drawn.
     * 
     * Used the java website to figure out how to do this
     * http://docs.oracle.com/javase/tutorial/2d/images/loadimage.html
     */
    private void initilise()
    {  
        try
        { 
            image = ImageIO.read(new File("./tiles/" + letter + ".jpg")); 
        }
        catch (IOException e) 
        { 
            UI.println("[E] An error occored when an reading the file"); 
        }
            
    }
    
    /**
     * Draws the tile to the provided x and y
     */
    public boolean draw(int x, int y)
    {
        // Check to see if the class is initilised
        if( !initilised ) initilise();
        
        // Draw the image if possible
        if( image != null ) 
        {
            UI.drawImage(image, x, y);
            return false;
        }
        else return true;
    }
    
    /**
     * Returns the value of the tile
     */
    public int getValue() { return value; }
}
