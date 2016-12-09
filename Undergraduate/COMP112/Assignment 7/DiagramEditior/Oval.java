import java.awt.Graphics;
import java.awt.Color;
import java.util.ArrayList;

/**
 * Write a description of class Oval here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Oval implements Shape
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
    private int tmpWidth;
    private int tmpHeight;

    private int id;

    private int orderValue;

    private ArrayList<GrabPoint> grabPoints;
    private GrabPoint currentlyHeld;

    private ArrayList<int[]> changeStack;
    private ArrayList<String> textChangeStack;

    private String textToDisplay;

    public Oval(int x, int y, int h, int w, int id, int o)
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
        grabPoints.add(new GrabPoint( x-5, (y + height/2)-5, 10, 10, 1 ));
        grabPoints.add(new GrabPoint( (x + width/2)-5, (y)-5, 10, 10, 2 ));
        grabPoints.add(new GrabPoint( (x + width)-5, (y + height/2)-5, 10, 10, 3 ));
        grabPoints.add(new GrabPoint( (x + width/2)-5, (y + height)-5, 10, 10, 4 ));
    }

    public void draw(Graphics g, boolean b)
    {
        if( deleted ) return;

        if( selected )
        {
            g.setColor(color);
            g.fillOval(tmpX, tmpY, tmpWidth, tmpHeight);

            g.setColor(Color.CYAN);
            g.drawOval(tmpX, tmpY, tmpWidth, tmpHeight);

            if( b )
            {
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
            g.fillOval(x, y, width, height);

            g.setColor(Color.BLACK);
            g.drawOval(x, y, width, height);

            if( textToDisplay != null )
            {
                g.setColor(Color.BLACK);
                g.drawString(textToDisplay, x - ( textToDisplay.length() * 3 ) + ( width/2 ), y + ( height/2 ) );
            }
        }

    }

    public void setColor(Color c)
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

    public boolean isWithinBounds(int x, int y)
    {
        if( deleted ) return false;

        double a = 0;
        double b = 0;
        double distance = 0;

        if( x == tmpX && y == tmpY )
        {
            if( width > height )
            {
                a = width/2.0;
                b = height/2.0;

                distance = Math.pow( (x - ((this.x + ((float)width)/2) )) / a, 2) + Math.pow( (y - ((this.y + ((float)height)/2) )) / b, 2);
            }
            else 
            {
                a = height/2.0;
                b = width/2.0;

                distance = Math.pow( (x - ((this.x + ((float)width)/2) )) / b, 2) + Math.pow( (y - ((this.y + ((float)height)/2) )) / a, 2);
            }
        }
        else
        {
            if( tmpWidth > tmpHeight )
            {
                a = tmpWidth/2.0;
                b = tmpHeight/2.0;

                distance = Math.pow( (x - ((this.tmpX + ((float)tmpWidth)/2) )) / a, 2) + Math.pow( (y - ((this.tmpY + ((float)tmpHeight)/2) )) / b, 2);
            }
            else 
            {
                a = tmpHeight/2.0;
                b = tmpWidth/2.0;

                distance = Math.pow( (x - ((this.tmpX + ((float)tmpWidth)/2) )) / b, 2) + Math.pow( (y - ((this.tmpY + ((float)tmpHeight)/2) )) / a, 2);
            }
        }

        return ( distance <= 1 );
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

    public void move( int x, int y )
    {
        tmpX = this.x + x;
        tmpY = this.y + y;
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

    public int getID()
    {
        return id;
    }

    public int[] getCenter()
    {
        if( x == tmpX && y == tmpY ) return new int[] {x + width/2, y + height/2};
        else return new int[] {tmpX + tmpWidth/2, tmpY + tmpHeight/2};
    }

    public String toString()
    {
        String text = "";
        if( textToDisplay != null ) text = textToDisplay;

        return String.format("OVAL:[X[%d], Y[%d], H[%d], W[%d], O[%d]; COLOR[%d]; ID[%d]; STR[%s]];;\n", x, y, height, width, orderValue, color.getRGB(), id, text);
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
