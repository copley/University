// This program is copyright VUW.
// You are granted permission to use it to construct your answer to a COMP112 assignment.
// You may not distribute it in any other way without permission.

/* Code for COMP112 Assignment 1  
 * Name: Daniel Thomas Braithwaite
 * Usercode: danielbraithwt
 * ID: 300313770
 */

//import java.util.*;
import ecs100.*;

import java.awt.Color;
import java.util.Random;
import java.awt.Graphics2D;


/** Makes a dirty floor (a grey rectrobotAngle), and then makes a robot floor
    cleaner run around on the floor to clean it all up.
    The robot will go forward one unit on each step in its current
    direction, and erase the "dirt" (by erasing where it was, moving
    one step, and redrawing itself).
    When it is about to take a step, the program must check that
    it won't go over edges of the floor, and change its direction
    to a new random direction if it would go over the edge.

    Hints:
    - You can use Math.random() to create a random number (between 0.0 and 1.0)
    - If the robot needs to move a distance s in the direction d, then it can
      work out the amounts to move in the x and y directions respectively using
      basic trigonometry:
        s * Math.cos(d * Math.PI/180)   and   s * Math.sin(d * Math.PI/180)
      (assuming that d is measured in degrees from 0 to 360)
    - Write one method with the main loop, and a separate method for
      drawing the floor cleaner. It should show the direction the
      floor cleaner is heading in some way. 
 */

public class FloorCleaner
{
    private Obstical[] obsticals;
    
    // Sizing variables
    private int robotDiamater = 20;
    private int robotHeadDiamater = 10;
    private int robotHeadRadius = 5;
    
    private int floorHeight = 200;
    private int floorWidth = 200;
    
    // Position Variables
    private double robotX = 125;
    private double robotY = 125;
    private double robotHeadX = 138;
    private double robotHeadY = 138;
    private int robotAngle = 0;
    
    // Controlls the speed of the robot
    private double robotSpeedConstant = 0.1;
    
    /**
     * cleanFloor is the main simulation loop.
     */
    public void cleanFloor()
    {
        obsticals = new Obstical[1];
        Random r = new Random();
        
        // Initilize the obsticals
        for( int i = 0; i < obsticals.length; i++ ) 
        { 
            int x = 50 + r.nextInt(150);
            int y = 50 + r.nextInt(50);
            int height = 20;
            int width = 20;
            
            obsticals[i] = new Obstical(x, y, height, width);
        }
        
        // Set UI to only repaint if UI.repaintGraphics() is called
        UI.setImmediateRepaint(false);
        
        UI.initialise();
        UI.setColor(Color.GRAY);
        UI.fillRect(0, 0, floorWidth, floorHeight);
        UI.setColor(Color.RED);
        UI.fillOval(robotX, robotY, robotDiamater, robotDiamater);
        
        UI.repaintGraphics();
        
        while( true )
        {
            // Replace the current curcle with white space
            UI.setColor(Color.WHITE);
            UI.fillOval(robotX, robotY, robotDiamater, robotDiamater);
            
            // Check for wall colisions
            // If collision with left
            if( robotX <= 0 )
            {
                robotX = 2;
                
                getNewAngle();
                //robotAngle = r.nextInt(170);
            }
            // If collision with right
            else if( robotX + robotDiamater > floorWidth )
            {
                robotX = floorWidth - ( robotDiamater + 2 );
                getNewAngle();
                //robotAngle = 190 + r.nextInt(170);
            }
            // If collision with top
            else if( robotY <= 0 )
            {
                robotY = 2;
                getNewAngle();
                //robotAngle = 100 + r.nextInt(170);
            }
            // If collision with bottom
            else if( robotY + robotDiamater > floorHeight )
            {
                robotY = floorHeight - ( robotDiamater + 2 );
                getNewAngle();
                //robotAngle = -80 + r.nextInt(170);
            }
            
            for( int i = 0; i < obsticals.length; i++ )
            {
              Integer tmp = isCollided(obsticals[i]);
              if( tmp != null ) robotAngle = tmp;
            }
            
            draw();
        }
    }
    
    /**
     * Function chooses a new angle for the robot to take
     */
    private void getNewAngle()
    {
        Random r = new Random();
        int choice = r.nextInt(2);
        
        if( ( robotAngle <= 0 && robotAngle > -90 ) || robotAngle >= 270 )
        {
            if( choice == 1 ) robotAngle = (100 + r.nextInt(80));
            else robotAngle = 1 * (190 + r.nextInt(80));
        }
        else if( ( robotAngle >= 180 && robotAngle < 270 ) || ( robotAngle <= -90 && robotAngle > -180 ) )
        {
            if( choice == 1 ) robotAngle = 1 * (10 + r.nextInt(80));
            else robotAngle = (100 + r.nextInt(80));
        }
        else if( ( robotAngle <= -180 && robotAngle > -270 ) || ( robotAngle <= 180 && robotAngle > 90 ) )
        {
            if( choice == 1 ) robotAngle = 1 * (280 + r.nextInt(80));
            else robotAngle = 1 * (190 + r.nextInt(80));
        }
        else if( ( robotAngle <= 90 && robotAngle > 0 ) || robotAngle < -270 )
        {
            if( choice == 1 ) robotAngle = 1 * (190 + r.nextInt(80));
            else robotAngle = 1 * (280 + r.nextInt(80));
        }
    }
    
    /**
     * Cleans the floor in a way that is more effecant than the
     * random method. The idea of hugging the left and just moving in
     * towards the center of the circle would work for any shape room,
     * however if you wanted this program it would require some changing
     * but the underlying concept would work. This function will work for any sized rectangualr room
     * 
     * I also gave the robot extra sucking power ( e.g. remove a square from around it ) otherwise it wouldent reach the corners
     * 
     */
    public void cleanFloorWithStrat()
    {
        // Set UI to only repaint if UI.repaintGraphics() is called
        UI.setImmediateRepaint(false);
        
        // Initilise the UI and create the floor
        UI.initialise();
        UI.setColor(Color.GRAY);
        UI.fillRect(0, 0, floorWidth, floorHeight);
        
        // Set up the vatiables to the values it needs to start with
        robotX = 0;
        robotY = 0;
        robotAngle = 90;
        int compleatedCorners = 0;
        int offsetX = 0;
        int offsetY = 0;
        boolean lapFinished = false;
        
        // Draw the initial robot
        UI.setColor(Color.RED);
        UI.fillOval(robotX, robotY, robotDiamater, robotDiamater);
        
        UI.repaintGraphics();
        
        // Loop that controlls the floor cleaning
        while( true )
        {
            UI.eraseRect(robotX, robotY, robotDiamater, robotDiamater);
            
            if( !canGoFoward(offsetX, offsetY, lapFinished) )
            {
                // Make sure the angle is between -360 to 360
                if( robotAngle == 360 ) robotAngle = 0;
                else if( robotAngle == -360 ) robotAngle = 0;
                
                
                if( compleatedCorners == 3 )
                {
                    compleatedCorners = 0;
                    lapFinished = false;
                    
                    offsetX += robotDiamater;
                    offsetY += robotDiamater;

                    robotAngle -= 90;
                }
                else
                {
                    robotAngle -= 90;
                    compleatedCorners++;
                    
                    if( compleatedCorners == 3 ) lapFinished = true;
                }
                
            }
            
            draw();
        }
        
    }
    
    /**
     * Returns true if the robot is able to move
     * foward
     */
    private boolean canGoFoward(int offsetX, int offsetY, boolean lapFinished)
    {
        if( ( offsetX + robotDiamater ) > (int) robotX && offsetY == (int) robotY && lapFinished ) 
        {
            robotX = offsetX + robotDiamater;
            
            // To get a smooth trasnition, because if i dont move the robot like this
            // it will detect collisions, at this is an easier way to fix the problem
            robotAngle -= 90;
            while( robotY < ( offsetX + robotDiamater )  )
            {
                UI.eraseRect(robotX, robotY, robotDiamater, robotDiamater);
                draw();
            }
            
            robotAngle += 90;
            
            return false; 
        }
        
        if( (robotY + robotDiamater) > ( floorHeight - offsetY + 1 ) ) 
        {
            robotY = ( floorHeight - offsetY - robotDiamater );
            return false;
        }
        else if( robotY < offsetY - 1 ) 
        {
            robotY = offsetY;
            return false;
        }
        else if( robotX < offsetX - 1 ) 
        {
            robotX = offsetX;
            return false;
        }
        else if( (robotX + robotDiamater) > ( floorWidth - offsetX + 1 ) ) 
        {
            robotX = ( floorWidth - offsetX - robotDiamater );
            return false;
        }
        
        return true;
        
    }
    
    /**
     * Draws the roobot to the screen with a line showing its direction
     */
    public void draw()
    {   
        // Convert robotAngle to radians
        double rads = robotAngle * Math.PI/180;
        
        // Update robot position variables
        robotX += robotSpeedConstant * Math.cos(rads);
        robotY += robotSpeedConstant * Math.sin(rads);
        
        int xHead = (int) (9 * Math.cos(rads));
        int yHead = (int) (9 * Math.sin(rads));
        
        UI.setColor(Color.RED);
        UI.fillOval(robotX, robotY, robotDiamater, robotDiamater);
        
        UI.setColor(Color.GREEN);
        UI.drawLine(robotX + 10, robotY + 10, robotX + 10 + xHead, robotY + 10 + yHead);
      
        
        if( obsticals != null )
        {
            for( int i = 0; i < obsticals.length; i++ )
                obsticals[i].draw();
        }
        
        UI.repaintGraphics();
    }
    
    /**
     * Function performs the collision for the obsticals, if the robot
     * collides with an obstical this will move the robot and return the new robotAngle
     * for the robot to travle in
     * 
     * For this i dident need to know what side the collision occored on so i could just 
     * see if the bounding boxes overlapped
     * 
     * Origonnaly i was trying to test for a collision on each side but this was to complicated
     * so i did some research and found a much simpler way of doing what i wanted to do 
     * Resource: https://www.cs.sjsu.edu/~teoh/teaching/previous/cs134_sp07/lectures/Lecture10_CollisionDetection.ppt ( Slide 4 )
     * 
     * NOTE: Had to slow program down because if it went to fast the ball would sometimes get stuck, will try to fix this if
     * i have time
     */
    
    private Integer isCollided( Obstical o )
    {
        Random r = new Random();
        
        // Test to see if the obstical and the bounding box of the robot overlap
        int xOverlap = o.x - (int) robotX;
        int yOverlap = o.y - (int) robotY;
        
        boolean test = Math.abs(xOverlap) <= robotDiamater;
        
        if( Math.abs(xOverlap) <= robotDiamater && Math.abs(yOverlap) <= robotDiamater )
        {
            int direction = 180 - robotAngle;
            
            if( direction < 0 ) return 20 + r.nextInt(160);
            else return 200 + r.nextInt(160);
        }
     
        return null;
    }
    
    /**
     * Private obstical class, takes 4 parameters as input which set up
     * the obsticals size and position, it also has a draw function that
     * will display the obstical to the screen
     */
    private class Obstical
    {
        private int x = 0;
        private int y = 0;
        private int width = 0;
        private int height = 0;
        
        public Obstical( int x, int y, int width, int height )
        {
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
        }
        
        public void draw()
        {
            UI.setColor(Color.BLACK);
            UI.fillRect(x, y, width, height);
        }
        
    }

    // Main
    /** Create a new FloorCleaner object and call cleanFloor.   */
    public static void main(String[] arguments)
    {
        boolean enableStrat = UI.askBoolean("Do you want to enable the better stratigie?: ");
        
        FloorCleaner fc =new FloorCleaner();
        
        if( enableStrat ) fc.cleanFloorWithStrat();
        else fc.cleanFloor();
    }        


}