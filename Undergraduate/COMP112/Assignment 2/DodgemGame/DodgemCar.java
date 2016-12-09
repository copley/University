// This program is copyright VUW.
// You are granted permission to use it to construct your answer to a COMP112 assignment.
// You may not distribute it in any other way without permission.

import ecs100.*;
import java.awt.Color;
import java.util.*;

/** DodgemCar
 * Represents a single DodgemCar that can move around in the arena
 * 
 * A DodgemCar must have fields to store their state:
 *  - a position (x and y)
 *  - a directon of travel (an angle, preferable in radians, ie between -Math.PI and Math.PI
 *  - their remaining life
 *  - their color
 *
 * A DodgemCar needs
 *  - a constructor,
 *  - a method to make it move one unit
 *  - methods to turn the car to the left and the right
 *    A simple design changes the direction of the car directly.
 *    A better design has a "steering wheel": turning the car actually turns the
 *    steering wheel, and the steering wheel makes the direction change a little
 *    bit on each move.
 *  - a method to draw the car on the graphics pane
 *  - methods to check for collisions, either with the wall and obstacle, or with another car
 *    These methods should update the state of the car if there is a collision
 *  - a method to return the remaining life of the car.
 *
 * Drawing a DodgemCar should show the position and direction of the car.
 *   If the runing uses a steering wheel, then the current direction of the
 *   steering should also be shown.
 *   
 * A DodgemCar can collide with a wall, the obstacle, or another DodgemCar.
 * - Colliding with a wall gives a little bit of damage
 * - Colliding with the obstacle should give a lot of damage
 * - Colliding with the other car may give a little damage, but
 *   should also result in jumping back, and the directions of the cars being changed
 *  
 *   For a really nice design, the way the cars bump into each 
 *   other should depend on the relative directions of the cars -
 *   being bumped on the side should have a different effect from
 *   being bumped in the front or the back.
 *   
*/
public class DodgemCar
{
    private static Random r = new Random();
    
    // Constants: Geometry and other parameters
    private double x = 0;
    private double y = 0;
    private static int height = 30;
    private static int width = 30;
    private static int radius = 15;
    private static double speedConstant = 0.1;
  
    // fields for the state of the car
    private boolean colided = false;
    private int health = 0;
    private double direction = 0;
    private double steeringDirection = 0;
    private Color color = null;
    
    

    //Constructor 
    /** 
     * The parameters specify the initial position and direction
     */
    public DodgemCar(double x, double y, double dir, Color color)  
    {
        this.x = x;
        this.y = y;
        this.direction = dir;
        this.color = color;
        
        this.health = 100;
    }
    
    // other methods, eg for turning left & right, drawing, checking collisions, etc

    /**
     * Turn the steering wheel one step more to the left (negative angle)
     * This governs how much the car turns on each move
     * Steering wheel can't turn more than 45 degrees to left or right
     */
    public void turnLeft() { direction -= 45; }

    /**
     * Turn the steering wheel one step more to the right (positive angle)
     * This governs how much the car turns on each move
     * Steering wheel can't turn more than 45 degrees to left or right
    */
    public void turnRight() { direction += 45; }

    /**
     * Moves the car 1 unit forward
     * First changes the current direction according to the position of the
     * steering wheel (takes 20 moves to turn the car by the angle of the steering wheel)
     * Then moves forward by 1 unit
     */
    public void move() 
    {  
        direction += steeringDirection;
        
        double rads = direction * (Math.PI/180);
        
        x += DodgemCar.speedConstant * Math.cos(rads);
        y += DodgemCar.speedConstant * Math.sin(rads);
    }
    
    /**
     * Undo the last move
     */
    public void undoMove()
    {
        direction -= steeringDirection;
        
        double rads = direction * (Math.PI/180);
        
        x -= DodgemCar.speedConstant * Math.cos(rads);
        y -= DodgemCar.speedConstant * Math.sin(rads);
    }

    /** 
     * draw the car and lines that show direction
     */
    public void draw()
    {
        // Make sure the angle is between -360 and 360
        if( direction > 360 ) direction = 360 - direction;
        else if( direction < -360 ) direction = 360 + direction;
        
        // Calculate position of line showing the steering direction    
        int xSteeringDirection = (int) (DodgemCar.width/2 * Math.cos(( direction + ( (steeringDirection*2) * 90 ) ) * (Math.PI/180)));
        int ySteeringDirection = (int) (DodgemCar.height/2 * Math.sin(( direction + ( (steeringDirection*2) * 90 ) ) * (Math.PI/180)));
        
        // Calculate position of line showing the actual direction of the car
        int xDirection = (int) (DodgemCar.width/2 * Math.cos(( direction * (Math.PI/180))));
        int yDirection = (int) (DodgemCar.height/2 * Math.sin(( direction * (Math.PI/180))));
       
        // Display oval and line to screen
        UI.setColor(color);
        UI.fillOval(x, y, DodgemCar.width, DodgemCar.height);
        // Draw steering direction line
        UI.setColor(Color.BLACK);
        UI.drawLine(x + (DodgemCar.width/2), y + (DodgemCar.height/2), x + (DodgemCar.width/2) + xSteeringDirection, y + (DodgemCar.height/2) + ySteeringDirection);
        // Draw actual direction line
        UI.setColor(Color.WHITE);
        UI.drawLine(x + (DodgemCar.width/2), y + (DodgemCar.height/2), x + (DodgemCar.width/2) + xDirection, y + (DodgemCar.height/2) + yDirection);
        
    }

    /**
     * Check whether the car has collided with a wall. (Core)
     * if so, move it back so it isn't overlapping the wall, and 
     * reduce its life by the appropriate amount.
     * 
     * I also made it so if you hit the obstical you bounce off it
     * i think this makes the game more fun
     */
    public boolean checkCollideWall()
    {
        // Colision with left
        if( x <= DodgemGame.ArenaOffsetX )
        {
            x =  DodgemGame.ArenaOffsetX + 1;
            
            // Reduce Health
            health -= 5;
            direction += 180;
            steeringDirection = 0;
            return true;
        }
        // Collision with right
        else if( ( x + DodgemCar.width ) >= ( DodgemGame.ArenaSize + DodgemGame.ArenaOffsetX ) )
        {
            x = ( ( DodgemGame.ArenaSize + DodgemGame.ArenaOffsetX ) - DodgemCar.width - 1 );
            
            // Reduce Health
            health -= 5;
            direction += 180;
            steeringDirection = 0;
            return true;
        }
        // Collision with top
        else if( y <= DodgemGame.ArenaOffsetY )
        {
            y = DodgemGame.ArenaOffsetY + 1;
            
            // Reduce Health
            health -= 5;
            direction += 180;
            steeringDirection = 0;
            return true;
        }
        // Collision with bottom
        else if( ( y + DodgemCar.height ) >= ( DodgemGame.ArenaSize + DodgemGame.ArenaOffsetY ) )
        {
            y = ( ( DodgemGame.ArenaSize + DodgemGame.ArenaOffsetY ) - DodgemCar.height - 1 );
            
            // Reduce Health
            health -= 5;
            direction += 180;
            steeringDirection = 0;
            return true;
        }
        
        return false;
    }        
        
    /**
     * Check whether the car has collided with the obstacle. (Completion)
     * if so, move it back so it isn't overlapping the obstacle, and
     * reduce its life by the appropriate amount.
     * 
     * Got the idea for this from looking at your demo, initially i was going to make the obstical a
     * square but after using your demo it occored to me that the collision detection would be alot easier
     * if it was a circle
     * 
     * I also made it so if you hit a wall you bounce off it to make the game more fun
     */
    public boolean checkCollideObstacle()
    {
        // Check distace between centeres
        double distance = Math.sqrt(Math.pow(((x + DodgemCar.radius) - (DodgemGame.ObstX + DodgemGame.ObstRad)), 2) + Math.pow((y + DodgemCar.radius) - (DodgemGame.ObstY + DodgemGame.ObstRad), 2));
        
        // If overlapping move player back
        if( distance < (DodgemCar.radius + DodgemGame.ObstRad) ) 
        {
            health -= 20;
            undoMove();
            
            direction += ( 90 + DodgemCar.r.nextInt(180));
            steeringDirection = 0;
            
            return true;
        }
        
        return false;

    }



    /** 
     * @return whether this car is touching the other car 
     */
    public boolean checkCollideCar(DodgemCar other)
    {
        // Check distance between centeres
        double distance = Math.sqrt(Math.pow((x + DodgemCar.radius) - (other.getX() + DodgemCar.radius) , 2) + Math.pow((y + DodgemCar.radius) - (other.getY() + DodgemCar.radius) , 2));
        
        if( distance < (2 * DodgemCar.radius))  
        {
            steeringDirection = 0;
            return true;
        }
        
        return false;
    }
    
    // Variable get function
    /**
     * Returns the amount of life left of this car (needed for Completion)
     */
    public double life() { return health; }
    
    /**
     * Returns the x cord of the car
     */
    public double getX() { return x; }
    
    /**
     * Returns the y cord of the car
     */
    public double getY() { return y; }
    
    /**
     * Returns the direction of the car
     */
    public double getAngle() { return direction; }
    
    // Variable set function
    /**
     * Steer the car left
     */
    public void steerLeft() 
    { 
        steeringDirection -= 0.03; 
        if( steeringDirection < -0.4 ) steeringDirection = -0.5;
    }
    
    /**
     * Steer the car right
     */
    public void steerRight() 
    { 
        steeringDirection += 0.03; 
        if( steeringDirection > 0.4 ) steeringDirection = 0.5;
    }
    
    /**
     * Set the direction to the angle passed to the function
     */
    private void setDirection(double angle)
    {
        
        direction = angle;
    }
    
    /**
     * Useful method for debugging: 
     * Returns a String rendering of the DodgemCar Object
     * which can be printed out for debugging.
     * Assumes that you called the fields x, y, and direction; you could change it.
     */
    public String toString() {  return String.format("Car@(%.0f,%.0f)->%s", this.x, this.y, this.direction); }

    /**
     * Update the angles after a colision between two cars
     */
    public static void collisionAngleAdjust(DodgemCar car1, DodgemCar car2)
    {
        // Find the angle qudrents
        int car1Quad = getQuadrent(car1.getAngle());
        int car2Quad = getQuadrent(car2.getAngle());
        
        // Get the angle between the cars
        double angle = Math.atan((car1.getY() - car2.getY()) / (car1.getX() - car2.getX())) * (180/Math.PI);
        
        // Update the car1 angle
        switch( car1Quad )
        {
            case 0: car1.setDirection(225 + angle);
                    break;
            case 1: car1.setDirection(315 + angle);
                    break;
            case 2: car1.setDirection(45 + angle);
                    break;
            case 3: car1.setDirection(135 + angle);
        }
        // Update the car2 angle
        switch( car2Quad )
        {
            case 0: car2.setDirection(225 + angle);
                    break;
            case 1: car2.setDirection(315 + angle);
                    break;
            case 2: car2.setDirection(45 + angle);
                    break;
            case 3: car2.setDirection(135 + angle);
        }
    }

    /**
     * Returns the quadrent the angle is in
     */
    private static int getQuadrent(double angle)
    {
        // Positive angle
        if( angle > 0 && angle <= 90 )  return 0;
        else if( angle > 90 && angle <= 180 ) return 1;
        else if( angle > 180 && angle <= 270 ) return 2;
        else if( angle > 270 && angle <= 360 ) return 3;
        
        // Negative angle
        if( angle < 0 && angle >= -90 )  return 3;
        else if( angle < -90 && angle >= -180 ) return 2;
        else if( angle < -180 && angle >= -270 ) return 1;
        else if( angle < -270 && angle >= -360 ) return 0;

        return -1;
    }
    
    private static double getAdjustedAngle(double angle)
    {
        if( angle > (Math.PI/2)) return (Math.PI/2) - angle;
        else if( angle > Math.PI ) return Math.PI - angle;
        else if( angle > (Math.PI * (3/2)) ) return (Math.PI * (3/2)) - angle;
        
        return angle;
    }

}

