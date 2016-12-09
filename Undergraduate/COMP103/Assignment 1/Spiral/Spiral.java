import ecs100.*;
import java.awt.Color;

/**
 */
public class Spiral implements UIButtonListener
{
    public static final int CELL_WIDTH = 50;
    public static final int CELL_HEIGHT = 50;

    public static final int STARTX = 100;
    public static final int STARTY = 100;

    public Spiral()
    {
        UI.initialise();

        UI.addButton("Line", this);
        UI.addButton("Square", this);
        UI.addButton("Spiral", this);
        UI.addButton("Clear", this);
    }

    public void buttonPerformed(String b)
    {
        UI.clearGraphics();

        if( b.equals("Line") )
        {
            drawLine();
        }
        else if( b.equals("Square") )
        {
            drawSquare();
        }
        else if( b.equals("Spiral") )
        {
            drawSpiral();
        }
    }

    public void drawLine()
    {
        int[] line = new int[10];

        for( int i = 0; i < line.length; i++ )
            line[i] = i+1;

        for( int i = 0; i < line.length; i++ )
        {
            UI.setColor(getColor(line[i], 10));
            UI.fillRect(STARTX + (i * CELL_WIDTH), STARTY, CELL_WIDTH, CELL_HEIGHT);

            UI.setColor(Color.WHITE);
            UI.drawString(String.format("%d", line[i]), STARTX + (i * CELL_WIDTH) + (CELL_HEIGHT/2) - String.format("%d", line[i]).length()*3, STARTY + 5 + (CELL_HEIGHT/2));
        }
    }

    public void drawSquare()
    {
        int[][] square = new int[10][10];

        int k = 1;
        for( int i = 0; i < square.length; i++ )
        {
            for( int j = 0; j < square[i].length; j++ )
            {
                square[i][j] = k;
                k++;
            }
        }

        for( int i = 0; i < square.length; i++ )
        {
            for( int j = 0; j < square[i].length; j++ )
            {
                UI.setColor(getColor(square[i][j], 100));
                UI.fillRect(STARTX + (j * CELL_WIDTH), STARTY + (i * CELL_HEIGHT), CELL_WIDTH, CELL_HEIGHT);

                UI.setColor(Color.WHITE);
                UI.drawString(String.format("%d", square[i][j]), STARTX + (j * CELL_WIDTH) + (CELL_HEIGHT/2) - String.format("%d", square[i][j]).length()*3, STARTY + (i * CELL_HEIGHT) + 5 + (CELL_HEIGHT/2));
            }
        }
    }

    public void drawSpiral()
    {
        int[][] spiral = new int[10][10];

        int k = 1;

        int x = 0;
        int y = 0;

        int xoff = 0;
        int yoff = 0;

        boolean left = false;
        boolean right = true;
        boolean up = false;
        boolean down = false;

        while( k <= 100 )
        {
            spiral[y][x] = k;
            k++;
            
            if( right )
            {
                if( x >= spiral.length-xoff-1 )
                {
                    right = false;
                    down = true;
                    
                    y++;
                }
                else x++;
            }
            else if( left )
            {
                if( x <= xoff )
                {
                    left = false;
                    up = true;
                    
                    yoff++;
                    y--;
                }
                else x--;
            }
            else if( down )
            {
                if( y >= spiral.length-yoff-1 )
                {
                    down = false;
                    left = true;
                    
                    x--;
                }
                else y++;
            }
            else if( up )
            {
                if( y <= yoff )
                {
                    up = false;
                    right = true;

                    xoff++;
                    x++;
                }
                else y--;
            }
        }

        for( int i = 0; i < spiral.length; i++ )
        {
            for( int j = 0; j < spiral[i].length; j++ )
            {
                UI.setColor(getColor(spiral[i][j], 100));
                UI.fillRect(STARTX + (j * CELL_WIDTH), STARTY + (i * CELL_HEIGHT), CELL_WIDTH, CELL_HEIGHT);

                UI.setColor(Color.WHITE);
                UI.drawString(String.format("%d", spiral[i][j]), STARTX + (j * CELL_WIDTH) + (CELL_HEIGHT/2) - String.format("%d", spiral[i][j]).length()*3, STARTY + (i * CELL_HEIGHT) + 5 + (CELL_HEIGHT/2));
            }
        }
    }
    
    private static Color getColor(int num, int total)
    {
        float h = ((float) (100/total) * num ) / (float) 100.0;
        
        return Color.getHSBColor(h, (float) 0.68, (float) 0.66);
    }

    public static void main(String[] args)
    {
        new Spiral();
    }
}
