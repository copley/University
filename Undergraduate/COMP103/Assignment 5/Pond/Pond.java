// This program is copyright VUW.
// You are granted permission to use it to construct your answer to a COMP103 assignment.
// You may not distribute it in any other way without permission.

/* Code for COMP103, Assignment 5
 * Name:
 * Usercode:
 * ID:
 */

import ecs100.*;
import java.awt.Color;

/** Uses recursion to
- draw a path of white stepping stones across a pond.
- draw trees around the pond
 */
public class Pond implements UIButtonListener, UIMouseListener {

    // Fields
    private double lastX;
    private double lastY;

    public Pond() {
        UI.addButton("Clear", this);
        UI.addButton("Stepping Stones", this);
        UI.setMouseListener(this);
        drawPond();
        UI.printMessage("Click mouse to draw trees");
    }

    /** Draw a path of stepping stones, starting at (x,y)
     *	with the first stone of the given width, and each successive
     *	stone 80% of the previous stone.
     *	The stones look approximately right if the height of the oval
     *	is 1/4 the width.
     */
    public void steppingStones(double x, double y, double width, int n) 
    {
        if( n >= 1 )
        {
            UI.fillOval(x, y-5, width, width);
            steppingStones( x + width/2, y - (0.5 * width), 0.8 * width, n-1 );
        }
    }

    /** Draw a tree with the base at (xBot, yBot).
     *  The top of the first branch should be at xTop, yTop.
     *  Then draw three smaller trees on the top of this branch
     *   with tops above, to the left, and to the right of this branch.
     */
    public void drawTree(double xBot, double yBot, double xTop, double yTop) 
    {
        UI.drawLine(xBot, yBot, xTop, yTop);

        // Recursion to draw extra trees
        //double sizeX = ( xTop - xBot ) / 2;
        double sizeY = ( yBot - yTop ) / 2;

        if( sizeY > 5 )
        {
            drawTree( xTop, yTop, (xTop - 20), (yTop - sizeY) ); // Left
            drawTree( xTop, yTop, (xTop), (yTop - sizeY) ); // Middle
            drawTree( xTop, yTop, (xTop + 20), (yTop - sizeY) ); // Right
        }
    }

    public void drawPond(){
        UI.clearGraphics();
        UI.setColor(Color.blue);
        UI.fillOval(50, 250, 400, 150);
    }

    /** Respond to button presses */
    public void buttonPerformed(String button) {
        if ( button.equals("Clear")) {
            drawPond();

        }
        else if (button.equals("Stepping Stones")) {
            UI.setColor(Color.white);
            steppingStones(100, 350, 40, 10);
        }
    }

    /** Respond to mouse events */
    public void mousePerformed(String action, double x, double y) {
        if (action.equals("released")) {
            UI.setColor(Color.green.darker().darker());
            drawTree(x, y, x, y-50);
        }
    }

    // Main
    public static void main(String[] arguments){
        new Pond();
    }	

}
