import java.awt.Graphics;
import java.awt.Color;
import java.util.ArrayList;

/**
 * Write a description of class Rectangle here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Rectangle implements Shape
{
    private int x;
    private int y;
    private int height;
    private int width;
    private Color color;

    private boolean selected;
    private boolean deleted;

    private int tmpX;
    private int tmpY;
    private int tmpHeight;
    private int tmpWidth;

    private int id;

    private int orderValue;

    private String textToDisplay;

    private ArrayList<GrabPoint> grabPoints;
    private GrabPoint currentlyHeld;

    private ArrayList<int[]> changeStack;
    private ArrayList<String> textChangeStack;

    public Rectangle(int x, int y, int h, int w, int id, int o)
    {
        this.x = x;
        this.y = y;
        this.tmpX = x;
        this.tmpY = y;
        this.height = h;
        this.width = w;
        this.tmpHeight = h;
        this.tmpWidth = w;

        this.id = id;

        this.orderValue = o;

        selected = false;
        deleted = false;

        color = Color.WHITE;

        changeStack = new ArrayList<int[]>();
        textChangeStack = new ArrayList<String>();

        // Set up the grab points
        grabPoints = new ArrayList<GrabPoint>();
        grabPoints.add(new GrabPoint( x-5, (y + h/2)-5, 10, 10, 1 ));
        grabPoints.add(new GrabPoint( (x + w/2)-5, (y)-5, 10, 10, 2 ));
        grabPoints.add(new GrabPoint( (x + w)-5, (y + h/2)-5, 10, 10, 3 ));
        grabPoints.add(new GrabPoint( (x + w/2)-5, (y + h)-5, 10, 10, 4 ));

    }

    public void move( int x, int y )
    {
        tmpX = this.x + x;
        tmpY = this.y + y;
    }

    public int[] getCenter()
    {
        if( x == tmpX && y == tmpY ) return new int[] {x + width/2, y + height/2};
        else return new int[] {tmpX + tmpWidth/2, tmpY + tmpHeight/2};
    }

    public void startScale( int x, int y )
    {
        for( int i = 0; i < grabPoints.size(); i++ )
        {
            if( grabPoints.get(i).hasBeenClicked(x, y) ) 
            {
                currentlyHeld = grabPoints.get(i);
                System.out.println("[!] Grab point clicked");
            }
        }

        if( currentlyHeld != null ) currentlyHeld.highlight();
    }

    public void scale( int x, int y )
    {
        if( currentlyHeld == null ) return;

        grabPoints.clear();

        // Left side
        if( currentlyHeld.getPosID() == 1 )
        {
            tmpX = this.x + x;
            tmpY = this.y;
            tmpWidth = this.width - x;
            tmpHeight = this.height;
        }
        // Top side
        else if( currentlyHeld.getPosID() == 2 )
        {
            tmpY = this.y + y;
            tmpX = this.x;
            tmpWidth = this.width;
            tmpHeight = this.height - y;
        }
        // Right side
        else if( currentlyHeld.getPosID() == 3 )
        {
            tmpX = this.x;// + x;
            tmpY = this.y;
            tmpWidth = this.width + x;
            tmpHeight = this.height;
        }
        // Bottom side
        else if( currentlyHeld.getPosID() == 4 )
        {
            tmpX = this.x;
            tmpY = this.y;// + y;
            tmpWidth = this.width;
            tmpHeight = this.height + y;
        }

    }

    public boolean isWithinBounds(int x, int y)
    {
        if( deleted ) return false;

        if( x == tmpX && y == tmpY ) return ( ( x > this.x && x < this.x + this.width ) && ( y > this.y && y < this.y + this.height ) );
        else return ( ( x > this.tmpX && x < this.tmpX + this.tmpWidth ) && ( y > this.tmpY && y < this.tmpY + this.tmpHeight ) );
    }

    public void select()
    {
        selected = true;
    }

    public void deSelect()
    {
        selected = false;

        if( currentlyHeld != null ) 
        {
            currentlyHeld.resetHighlited();
            currentlyHeld = null;
        }
    }

    public void commit()
    {
        // Add prevous attrubutes to the stack
        changeStack.add(new int[] {x, y, height, width, color.getRGB(), orderValue});
        if( textToDisplay != null ) textChangeStack.add(String.format("%s", textToDisplay));
        else textChangeStack.add("");

        x = tmpX;
        y = tmpY;
        width = tmpWidth;
        height = tmpHeight;

        // Reset the grab points
        grabPoints.clear();
        grabPoints.add(new GrabPoint( x-5, (y + height/2)-5, 10, 10, 1 ));
        grabPoints.add(new GrabPoint( (x + width/2)-5, (y)-5, 10, 10, 2 ));
        grabPoints.add(new GrabPoint( (x + width)-5, (y + height/2)-5, 10, 10, 3 ));
        grabPoints.add(new GrabPoint( (x + width/2)-5, (y + height)-5, 10, 10, 4 ));

        currentlyHeld = null;
    }

    public boolean undo()
    {
        if( changeStack.size() == 0 ) 
        {
            delete();
            return false;
        }

        int[] previous = changeStack.remove(changeStack.size()-1);

        tmpX = x = previous[0];
        tmpY = y = previous[1];
        tmpHeight = height = previous[2];
        tmpWidth = width = previous[3];
        color = new Color(previous[4]);
        orderValue = previous[5];

        String prevText = textChangeStack.remove(textChangeStack.size()-1);
        if( !prevText.equals("") ) textToDisplay = prevText;
        else textToDisplay = null;

        return true;
    }

    public void draw( Graphics g, boolean b )
    {
        if( deleted ) return;

        if( selected )
        {
            g.setColor(color);
            g.fillRect(tmpX, tmpY, tmpWidth, tmpHeight);

            g.setColor(Color.CYAN);
            g.drawRect(tmpX, tmpY, tmpWidth, tmpHeight);

            if( b )
            {
                System.out.println("[!] Drawing Grab Points");
                for( int i = 0; i < grabPoints.size(); i++ ) grabPoints.get(i).draw(g);
            }

            if( textToDisplay != null )
            {
                g.setColor(Color.BLACK);
                g.drawString(textToDisplay, tmpX - ( textToDisplay.length() * 3 ) + ( tmpWidth/2 ), tmpY + ( tmpHeight/2 ) );
            }
        }
        else
        {
            g.setColor(color);
            g.fillRect(x, y, width, height);

            g.setColor(Color.BLACK);
            g.drawRect(x, y, width, height);

            if( textToDisplay != null )
            {
                g.setColor(Color.BLACK);
                g.drawString(textToDisplay, x - ( textToDisplay.length() * 3 ) + ( width/2 ), y + ( height/2 ) );
            }
        }

    }

    public void setColor( Color c )
    {
        //commit();

        color = c;
    }

    public void setText( String text )
    {
        //commit();

        textToDisplay = text;
    }

    public void removeText()
    {
        //commit();

        textToDisplay = null;
    }

    public int getID()
    {
        return id;
    }

    public String toString()
    {
        String text = "";
        if( textToDisplay != null ) text = textToDisplay;
        
        return String.format("RECTANGLE:[X[%d], Y[%d], H[%d], W[%d], O[%d]; COLOR[%d]; ID[%d]; STR[%s]];;\n", x, y, height, width, orderValue, color.getRGB(), id, text);
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

    public void setOrderValue( int o )
    {
        //commit();

        orderValue = o;
    }

    public int getOrderValue()
    {
        return orderValue;
    }
}
