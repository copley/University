import ecs100.*;
import java.awt.Color;

/**
 * Cut down and slightly changed version of the Cell class, this is for the Playback Board class 
 * 
 */
public class PlaybackCell
{
    private Tile tile;
    private CellType type;
    private Color color;
    
    private int stage;
    private int x;
    private int y;
    
    public PlaybackCell(CellType t, int x, int y)
    {
        this.x = x;
        this.y = y;
        this.type = t;
        
        tile = null;
        
        // Choose the paint
        if( t == CellType.Regular ) color = Color.WHITE;
        else if( t == CellType.DoubleLetter )color =  Color.CYAN;
        else if( t == CellType.TrippleLetter ) color = Color.BLUE;
        else if( t == CellType.DoubleWord || t == CellType.Start ) color = Color.PINK;
        else if( t == CellType.TrippleWord ) color = Color.RED;
    }
    
    /**
     * Sets the stage at which this cell should display its tile ( if it has one )
     */
    public void setStage(int s)
    {
        this.stage = s;
    }
    
    /**
     * Sets the tile for this cell
     */
    public void setTile(Tile t)
    {
        tile = t;
    }
    
    /**
     * Draws the cell with the tile inside it if the cell contains a tile
     */
    public boolean draw(int s)
    {
        UI.setColor(color);
        UI.fillRect(x, y, Tile.TileWidth, Tile.TileHeight);
        
        if( tile != null && s >= stage ) 
        {
            boolean tileError = tile.draw(x, y);
            if( tileError ) return true;
        }
        
        UI.setColor(Color.BLACK);
        UI.drawRect(x, y, Tile.TileWidth, Tile.TileHeight);
        
        return false;
    }
}
