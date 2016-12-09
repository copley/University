// This program is copyright VUW.
// You are granted permission to use it to construct your answer to a COMP112 assignment.
// You may not distribute it in any other way without permission.

/* Code for COMP112 Assignment 
 * Name: Daniel Thomas Braithwaite
 * Usercode: danielbraithwt
 * ID: 300313770
 */

import ecs100.*;
import java.util.*;
import java.io.*;
import java.awt.Color;
import javax.swing.JColorChooser;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.util.ArrayList;



/** Tracer
 *  The program displays an image on the screen consisting of a tangle of
 *  microtubules.  
 *  It will enable the user to measure the lengths of the microtubles 
 *  by tracing out polylines (sequences of connected straight line segments)
 *  over the image.
 *  After the user clicks the "Start Line" button, the program will clear all lines,
 *   redisplay the image, and get ready to start a new line
 *  As the user clicks on points on the image, it will build up a "polyline" connecting
 *   the points.  For the first point on the line, it just draws a dot.
 *   For each remaining point the user clicks on, the program will draw
 *   a new line segment from the previous point.
 *  It also keeps track of the total length of the line, adding the length of the
 *   new segment to the total on each click.
 *  When the user clicks the "Line Length" button, it will print out the total length of the line.
 *  When the user clicks the "Choose Image" button, it will allow the user to select a different
 *   image, and will restart the line.
 *
 *  You will need
 *  - fields to record the previous point on the polyline, and the length so far.
 *  - a constructor to set up the GUI (including the mouse listener)
 *  - methods to respond to events (buttons and mouse)
 *  - possibly additional "helper" methods.
 */
public class Tracer implements UIButtonListener, UIMouseListener
{
    // Fields
    // Current Distance
    private double totalLineLength = 0;
    
    // Image Variable
    BufferedImage backgroundImage = null;
    
    // Array List To Stort
    ArrayList<int[]> cords = null;

    // Constructor
    /** 
     *  Construct a new Tracer object and set up the GUI
     */
    public Tracer()
    {
        UI.initialise();
        UI.setImmediateRepaint(false);
        UI.setColor(Color.GREEN);
        
        backgroundImage = loadImage("image01.jpg");
        
        // Set the mouse listener
        UI.setMouseMotionListener(this);
        
        // Create the buttons
        UI.addButton("Start Line", this);
        UI.addButton("Line Length", this);
        UI.addButton("Choose Image", this);
        
        // Draw image to the screen
        startLine();
    }

    private BufferedImage loadImage(String url)
    {
        // Used the java website to figure out how to do this
        // http://docs.oracle.com/javase/tutorial/2d/images/loadimage.html
        BufferedImage i = null;
        
        try{ i = ImageIO.read(new File(url)); }
        catch (IOException e) { UI.println("An error occored when reading the file"); }
            
        return i;
    }
    
    // GUI Methods

    /** Respond to button presses */
    public void buttonPerformed(String button)
    {
        if( button.equals("Start Line") ) { startLine(); }
        else if( button.equals("Line Length") ) { UI.println("Line Length: " + totalLineLength + " pixles"); }
        else if( button.equals("Choose Image") )
        {
            // Display popup to get new file location then set it to the background image
            // and redisplay the polyline
            String URL = UIFileChooser.open();
            setNewImage(URL);
            startLine();
        }
    }

    /** 
     * Respond to mouse events, particularly to "released" 
     */
    public void mousePerformed(String action, double x, double y) 
    {
        
        if( cords != null )
        {
            if( action.equals("released") )
            {
                // If not first click draw line connecting dots
                if( cords.size() != 0 ) 
                {
                    // Update the distace
                    totalLineLength += Math.hypot((cords.get(cords.size()-1)[0] -  x), (cords.get(cords.size()-1)[1] - y));
                }
                
                if( x <= backgroundImage.getWidth() && y <= backgroundImage.getWidth() )
                {
                    // Add new point cord to the cords arraylist
                    cords.add(new int[] {(int)x, (int)y});
                }
                
                // Draw polyline to the screen
                drawPolyline();
                UI.repaintGraphics();
            }
            else if( action.equals("moved") )
            {
                if( cords.size() != 0 )
                {
                    // Draw the polyline and image to the screen
                    drawPolyline();
                    
                    // Draw line from prev point to current mouse pos
                    UI.setColor(Color.GREEN);
                    UI.drawLine(cords.get(cords.size()-1)[0], cords.get(cords.size()-1)[1], x, y);
                    UI.repaintGraphics();
                }
            }
        }
    }

    /**
     * Redraws the background and then the polyline
     */
    private void drawPolyline()
    {
        UI.setColor(Color.MAGENTA);
        
        UI.clearGraphics(false);
        UI.drawImage(backgroundImage, 0, 0);
        
        // Draw the little ovals
        for( int i = 0; i < cords.size(); i++ )
        {
            UI.fillRect(cords.get(i)[0] - 3, cords.get(i)[1] - 3, 6, 6);
        }
        
        // Draw the lines linking the the points together
        for( int i = 0; i < cords.size()-1; i++ )
        {
            UI.drawLine(cords.get(i)[0], cords.get(i)[1], cords.get(i+1)[0], cords.get(i+1)[1]);
        }
    }

    // other methods: you don't have to define this method, but it may be useful

    /**
     * Takes a file URL as input and sets it to the current image
     * NOTE: This function does NOT change the image currently displayed
     */
    private void setNewImage(String imageURL) { backgroundImage = loadImage(imageURL); }
    
    /**
     * Clear the screen, redisplay the image, and get ready to start a new line.
     * Sets the values of the fields storing the current point to -1
     */
    private void startLine()
    {
        // Reset everything
        totalLineLength = 0;
        cords = new ArrayList<int[]>();
        
        // Display Image
        UI.clearGraphics();
        if( backgroundImage != null ) UI.drawImage(backgroundImage, 0, 0);
        UI.repaintGraphics();
        
        // Clear the text field
        UI.clearText();
    }

    /*# END OF YOUR CODE */

    // Main
    public static void main(String[] arguments)
    {
        new Tracer();
    }        


}
