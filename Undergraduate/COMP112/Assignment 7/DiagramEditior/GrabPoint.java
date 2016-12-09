import java.awt.Graphics;
import java.awt.Color;

/**
 * Write a description of class GrabPoint here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class GrabPoint
{
    private int x;
    private int y;
    private int height;
    private int width;
    private int posid;
    private boolean activated;

    private int tmpX;
    private int tmpY;

    public GrabPoint(int x, int y, int h, int w, int id)
    {
        this.x = x;
        this.y = y;
        this.height = h;
        this.width = w;
        this.posid = id;

        this.tmpX = x;
        this.tmpY = y;

        activated = false;
    }

    public int getPosID() { return posid; }

    public boolean hasBeenClicked(int x, int y)
    {
        return ( Math.pow(x - (this.x + this.width/2), 2) + Math.pow(y - ( this.y + this.height/2 ), 2) <= Math.pow( this.width/2, 2 ) );
    }

    public void highlight()
    {
        activated = true;
    }

    public void resetHighlited()
    {
        activated = false;
    }

    public void move(int x, int y)
    {
        tmpX = this.x + x;
        tmpY = this.y + y;
    }
    
    public void setPosID( int id )
    {
        this.posid = id;
    }

    public void commit()
    {
        x = tmpX;
        y = tmpY;
    }

    public void draw( Graphics g )
    {
        if( x == tmpX && y == tmpY )
        {
            if( activated ) g.setColor(Color.YELLOW);
            else g.setColor(Color.GRAY);
            g.fillOval(x, y, width, height);
        }
        else
        {
            if( activated ) g.setColor(Color.YELLOW);
            else g.setColor(Color.GRAY);
            g.fillOval(tmpX, tmpY, width, height);
        }
        //if( activated )
        //{
        //    g.setColor(Color.GREEN);
        //    g.drawOval(x, y, width, height);
        //}
    }
}
