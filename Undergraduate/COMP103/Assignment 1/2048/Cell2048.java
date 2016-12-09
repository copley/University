import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.geom.Rectangle2D;

/**
 */
public class Cell2048
{
    public static final int CELL_WIDTH = 128;
    public static final int CELL_HEIGHT = 128;
    
    private boolean updated;
    private int value = 0;
    private int x;
    private int y;
    
    public Cell2048(int v, int x, int y)
    {
        value = v;
        this.x = x;
        this.y = y;
        updated = false;
    }
    
    public void cellUpdated()
    {
        updated = true;
    }
    
    public void movingFinished()
    {
        updated = false;
    }
    
    public boolean isCellUpdated()
    {
        return updated;
    }
    
    public void draw(Graphics2D g2d)
    {
        if( value != 0 ) 
        {
            g2d.setColor(getColor(value));
            g2d.fillRoundRect(x+10, y+10, CELL_WIDTH-10, CELL_HEIGHT-10, 10, 10);
            
            String toDraw = String.format("%d", value);
            g2d.setFont(new Font("SansSerif", Font.PLAIN, 50));
            g2d.setColor(new Color(0, 0, 0, 80));
            
            FontMetrics fm = g2d.getFontMetrics();
            Rectangle2D r = fm.getStringBounds(toDraw, g2d);
            g2d.drawString(toDraw, x + ( CELL_WIDTH - (int) r.getWidth())/2 + 5, y + ( CELL_HEIGHT - (int) r.getHeight() )/2 + fm.getAscent());
        }
        else 
        {
            g2d.setColor(new Color(207, 207, 196));
            g2d.fillRoundRect(x+10, y+10, CELL_WIDTH-10, CELL_HEIGHT-10, 10, 10);
        }
    }
    
    public int getValue()
    {
        return value;
    }
   
    public void setValue(int v)
    {
        value = v;
    }
    
    private static Color getColor( int value )
    {
        switch (value)
        {
            case 2 : { return new Color(253, 253, 150); }
            case 4 : { return new Color(255, 179, 71); }
            case 8 : { return new Color(255, 209, 220); }
            case 16 : { return new Color(222, 165, 164); }
            case 32 : { return new Color(244, 154, 194); }
            case 64 : { return new Color(255, 105, 97); }
            case 128 : { return new Color(179, 158, 181); }
            case 256 : { return new Color(100, 20, 100); }
            case 512 : { return new Color(203, 153, 201);}
            case 1024 : { return new Color(174, 198, 207); }
            case 2048 : { return new Color(119, 158, 203); }
            default : { return Color.BLACK; }
        }
    }
}
