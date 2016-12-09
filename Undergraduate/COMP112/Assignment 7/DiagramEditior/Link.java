import java.awt.*;

/**
 * Write a description of class Link here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Link implements Shape
{
    private Shape s1;
    private Shape s2;
    private boolean selected;
    private boolean deleted;
    
    public Link( Shape s1, Shape s2 )
    {
        this.s1 = s1;
        this.s2 = s2;
        
        selected = false;
        deleted = false;
    }
    
    public String toString()
    {
        return String.format("LINK[%d:%d]", s1.getID(), s2.getID());
    }
    
    public void draw( Graphics g, boolean b )
    {
        if( deleted ) return;
        
        int[] center1 = s1.getCenter();
        int[] center2 = s2.getCenter();
        
        if( selected ) g.setColor(Color.CYAN);
        else g.setColor(Color.BLACK);
        g.drawLine(center1[0], center1[1], center2[0], center2[1]);
    }
    
    public boolean isWithinBounds(int x, int y)
    {
        if( deleted ) return false;
        
        int[] center1 = s1.getCenter();
        int[] center2 = s2.getCenter();
        
        double distance = Math.abs((center2[0] - center1[0])*(center1[1] - y) - (center1[0] - x)*(center2[1] - center1[1]))/Math.sqrt(Math.pow((center2[0] - center1[0]), 2) + Math.pow((center2[1] - center1[1]), 2));
        if( distance < 10 ) return true;
        
        return false;
    }
    
    public void select()
    {
        selected = true;
    }
    
    public void deSelect()
    {
        selected = false;
    }
    
    public void delete()
    {
        deleted = true;
    }
    
    public void undoDelete()
    {
        deleted = false;
    }
    
    public boolean isDeleted()
    {
        return deleted;
    }
    
    public boolean isConnectedTo( int id )
    {
        return ( id == s1.getID() || id == s2.getID() );
    }
    
    // Un needed functions
    public void setColor(Color c) {}
    public void commit() {}
    public void setText( String text ) {}
    public void removeText() {}
    public void move( int x, int y ) {}
    public void startScale(int x, int y) {}
    public void scale( int x, int y ) {}
    public int[] getCenter() { return null; }
    public int getID() { return -1; }
    public void setOrderValue( int o ){}
    public int getOrderValue(){ return -1; }
    public boolean undo() { return true; }
}
