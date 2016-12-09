import java.awt.Graphics;
import java.awt.Color;

/**
 * Write a description of interface Shape here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public interface Shape
{
    public void draw(Graphics g, boolean b);
    public void setColor(Color c);
    public boolean isWithinBounds(int x, int y);
    
    public void select();
    public void deSelect();
    
    public void delete();
    public void undoDelete();
    public boolean isDeleted();
    
    public void commit();
    
    public void setText( String text );
    public void removeText();
    
    public void move( int x, int y );
    public void startScale(int x, int y);
    public void scale( int x, int y );
    
    public boolean undo();
    
    public int[] getCenter();
    public int getID();
    
    public void setOrderValue( int o );
    public int getOrderValue();
    
    public String toString();
}
