/* Code for COMP112 Assignment
 * Name: Daniel Thomas Braithwaite
 * Usercode: braithdani
 * ID: 300313770
 */

import ecs100.*;
import java.awt.Color;

/**
 * Stores the data for each of the cells in the board
 */
public class Cell
{
    // Static variables
    public static int PickedUpX = 10;
    public static int PickedUpY = 10;
    
    private Tile tile;
    private CellType type;
    private Color color;
    private boolean commited;
    private boolean isTouchingCommited;
    private boolean hasBeenScored;
    
    int x;
    int y;
    

    /**
     * Constructor for objects of class Cell
     */
    public Cell(CellType t, int x, int y)
    {
        this.x = x;
        this.y = y;
        this.type = t;
        
        commited = false;
        hasBeenScored = false;
        tile = null;
        
        // Choose the paint
        if( t == CellType.Regular ) color = Color.WHITE;
        else if( t == CellType.DoubleLetter )color =  Color.CYAN;
        else if( t == CellType.TrippleLetter ) color = Color.BLUE;
        else if( t == CellType.DoubleWord || t == CellType.Start ) color = Color.PINK;
        else if( t == CellType.TrippleWord ) color = Color.RED;
        
    }
    
    public void reset()
    {
        commited = false;
        hasBeenScored = false;
        tile = null;
    }

    /**
     * Draws the cell with the tile inside it if the cell contains a tile
     */
    public boolean draw()
    {
        UI.setColor(color);
        UI.fillRect(x, y, Tile.TileWidth, Tile.TileHeight);
        
        if( tile != null ) 
        {
            boolean tileError = tile.draw(x, y);
            if( tileError ) return true;
        }
        
        UI.setColor(Color.BLACK);
        UI.drawRect(x, y, Tile.TileWidth, Tile.TileHeight);
        
        return false;
    }
    
    /**
     * Returns the hasBeenScored flag
     */
    public boolean hasBeenScored()
    {
        return hasBeenScored;
    }
    
    /**
     * Sets the hasBeenScored flag to true
     */
    public void scored()
    {
        hasBeenScored = true;
    }
    
    public String getLetter()
    {
        return tile.getLetter();
    }
    
    /**
     * Retruns the value of the cell e.g. the value of the tile * cell mulitplier
     */
    public int getValue()
    {
        if( !commited )
        {
            if( type == CellType.DoubleLetter ) return tile.getValue() * 2;
            else if( type == CellType.TrippleLetter ) return tile.getValue() * 3;
        }
        
        return tile.getValue();
    }
    
    /**
     * Returns the word mulitpler of the cell
     */
    public int getWordMultiplyer()
    {
        if( !commited )
        {
            if( type == CellType.DoubleWord || type == CellType.Start ) return 2;
            else if( type == CellType.TrippleWord ) return 3;
        }
        
        return 0;
    }
    
    /**
     * Sets the cells tile object
     */
    public boolean setTile(Tile t)
    {
        if( !commited )
        {
            tile = t;
            return true;
        }
        
        return false;
    }
    
    /**
     * Returns the tile contained in the cell and then removes it
     * from this object
     */
    public Tile getTile()
    {
        Tile t = null;
        
        if( !commited )
        {
            t = tile;
            tile = null;
            isTouchingCommited = false;
        }
        
        return t;
    }
    
    /**
     * If the cell contains a tile then set the commit flag to true
     */
    public void commit()
    {
        hasBeenScored = false;
        if( tile != null ) commited = true;
    }
    
    /**
     * Returns true if the cell contains a tile, otherwise false is returned
     */
    public boolean containsTile()
    {
        return ( tile != null );
    }
    
    /**
     * Returns the commited flag
     */
    public boolean isCommited()
    {
        return commited;
    }
    
    @Override
    public String toString()
    {
        if( this.containsTile() ) return tile.toString();
        else return String.format("NULL");
    }
}
