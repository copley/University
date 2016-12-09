// This program is copyright VUW.
// You are granted permission to use it to construct your answer to a COMP112 assignment.
// You may not distribute it in any other way without permission.

/* Code for COMP112 Assignment
 * Name: Daniel Thomas Braithwaite
 * Usercode: danielbraithwt
 * ID: 300313770
 */

import ecs100.*;
import java.awt.Color;
import java.util.*;
import java.io.*;


/** DodgemGame
 *  Game with two dodgem cars whose steering is controlled by the players
 *  (uses keys: player 1: S/D for left/right;  player 2: K/L for left/right)
 *  Cars run around at a constant speed in an arena with an enclosing wall and
 *  a round obstacle in the of the arena.
 *  If a car hits the wall or the obstacle, then it gets damaged
 *  If the cars collide then they bump apart and their directions are changed
 *  To win the game, a player needs to make the other car crash into the wall
 *  or obstacle enough times.
 *
 *  Controls:
 *  - key to start (space)
 *  - keys to turn the two cars  (s/d  and k/l)
 *    The simplest control is to change the direction of the car directly
 *    when a key is pressed; a better control is to change the direction of
 *    the "steering wheel", which will make the car change direction as it moves
 *
 *  Display:
 *   program constantly shows
 *   - the arena, obstacle, and the cars
 *   - the damage level of each player
 *
 *  Constants:
 *    This class should contain constants specifying the various parameters of
 *    the game, including the geometry of the arena and obstacle.
 *    - Colliding with a wall gives a little bit of damage
 *    - Colliding with the obstacle should give a lot of damage
 *    - Colliding with the other car may give a little damage, but needs to be very low
 *      to allow a player to try to push the other player into the obstacle.
 */

public class DodgemGame
{

    // Constants for the Geometry of the game.
    // (You may change or add to these if you wish)

    public static final int ArenaSize = 400;
    public static final int ArenaOffsetX = 50;
    public static final int ArenaOffsetY = 70;
    public static final int LeftWall = 30;
    public static final int RightWall = 30+ArenaSize;
    public static final int TopWall = 50;
    public static final int BotWall = TopWall+ArenaSize;
    
    
    public static final int ObstSize = 80;
    public static final int ObstRad = ObstSize/2;
    public static final int ObstX = LeftWall + ArenaSize/2;
    public static final int ObstY = TopWall + ArenaSize/2;

    public static final int Delay = 20;  // milliseconds to delay each step.

    // Fields to store the two cars 
   private DodgemCar car1;
   private DodgemCar car2;
   
   private int car1Score = 0;
   private int car2Score = 0;




    /** Constructor
     * Set up the GUI,
     * Draw the arena
     */
    public DodgemGame()
    {
        UI.initialise();
        UI.setImmediateRepaint(false);
        UI.setKeyListener(new KeyListener());
        
        // Draw arena
        UI.setColor(Color.BLACK);
        UI.drawRect(DodgemGame.ArenaOffsetX, DodgemGame.ArenaOffsetY, DodgemGame.ArenaSize, DodgemGame.ArenaSize);
        
        // Print Info To Terminal
        UI.println("White Line - Car Direction");
        UI.println("Black Line - Car Steering Direction\n");
        UI.println("Player 1: s - Left, d - Right");
        UI.println("Player 2: k - Left, l - Right\n");
        UI.println("Press space to restart");
        
        // Draw initial stats
        // Player 1
        UI.drawString("Player 1:", 180, 20);
        UI.drawString("Wins: " + car1Score, 180, 38);
        UI.setColor(Color.RED);
        UI.fillRect(180, 45, 50, 15);
        UI.setColor(Color.BLACK);
        UI.drawRect(180, 45, 50, 15);
        // Player 2
        UI.drawString("Player 2:", 280, 20);
        UI.drawString("Wins: " + car2Score, 280, 38);
        UI.setColor(Color.RED);
        UI.fillRect(280, 45, 50, 15);
        UI.setColor(Color.BLACK);
        UI.drawRect(280, 45, 50, 15);
        
        UI.repaintGraphics();
    }


    // GUI Methods

    /** Run the game
     * Infinite loop to make the cars move:
     * Each time round the loop:
     *  Check that car1 and car2 are not null, and that both cars have some life left,
     *  If there are no cars yet, or either car is dead,
     *   then just sleep for a short while, to wait for the users to restart the cars.
     *  If the cars exist and have some life, then
     *  - move each car one step,
     *  - call methods on the two cars to check for the different types of collisions
     *    (Core: just with walls; Completion: with obstacle and each other)
     *  - redraw the game (cars, arena, and life status)
     */
    public void run()
    {
        while( true )
        {
            // Program dosnt seem to work withough this
            //UI.println("loop");
            if( ( car1 != null && car2 != null ) )
            {
                if ( car1.life() > 0 && car2.life() > 0 )
                {
                    // Move cars
                    car1.move();
                    car2.move();
                    
                    // Check for collisions
                    car1.checkCollideWall();
                    car2.checkCollideWall();
                    
                    // Check for obsticla collisions
                    car1.checkCollideObstacle();
                    car2.checkCollideObstacle();
                    
                    // Check for player colisions and act accordingley
                    if( car1.checkCollideCar(car2) || car2.checkCollideCar(car1)  )
                    {
                        // Adjust the angles 
                        DodgemCar.collisionAngleAdjust(car1, car2);
                        
                        // Move cars one frame
                        car1.move();
                        car1.move();
                    }
                    
                    // Repaint everything
                    redraw();
                }
                // If car 1 has died then increment car 2 score
                else if( car1.life() <= 0 )
                {
                    // Update player 2 score
                    UI.eraseString("Wins: " + car2Score, 280, 38);
                    car2Score++;
                    UI.drawString("Wins: " + car2Score, 280, 38);
            
                    UI.drawString("Player 2 Wins", 210, 200);
                    
                    UI.repaintGraphics();
                    
                    // Make cars null so the loop wont be entered
                    car1 = null;
                    car2 = null;
                }
                // If car 2 has died then increment car 1 score
                else if( car2.life() <= 0 )
                {
                    // Update player 1 score   
                    UI.eraseString("Wins: " + car1Score, 180, 38);
                    car1Score++;
                    UI.drawString("Wins: " + car1Score, 180, 38);
                    
                    UI.drawString("Player 1 Wins", 210, 200);
                    
                    UI.repaintGraphics();
                    
                    // Make cars null so the loop wont be entered
                    car1 = null;
                    car2 = null;
                }
            }
            else UI.sleep(DodgemGame.Delay);
        }
    }


    // other methods, eg, resetting game, and drawing the game state.
    /**
     * Reset the game with two new cars in the starting positions.
     * ie, create two new DodgemCar objects and assign to car1 and car2
     */
    private void resetGame()
    {   
        car1 = new DodgemCar(100, 200, -90, ColorGenerator.generateColor());
        car2 = new DodgemCar(300, 200, 90, ColorGenerator.generateColor());   
    }

    /**
     * Redraws
     * - the arena and obstacle
     * - the two cars
     * - the status of the cars  (Completion)
     * Hint: make separate methods for the arena and the status
     * Hint: don't forget to repaint the Graphics pane after redrawing everything.
     */
    private void redraw()
    {
        // Draw arena
        UI.setColor(Color.WHITE);
        UI.fillRect(DodgemGame.ArenaOffsetX, DodgemGame.ArenaOffsetY, DodgemGame.ArenaSize, DodgemGame.ArenaSize);
        UI.setColor(Color.BLACK);
        UI.drawRect(DodgemGame.ArenaOffsetX, DodgemGame.ArenaOffsetY, DodgemGame.ArenaSize, DodgemGame.ArenaSize);
        
        // Draw obstical
        UI.setColor(Color.RED);
        UI.fillOval(DodgemGame.ObstX, DodgemGame.ObstY, DodgemGame.ObstSize, DodgemGame.ObstSize);
        
        // Redraw cars
        car1.draw();
        car2.draw();
        
        // Display game stats
        // Player 1
        UI.setColor(Color.BLACK);
        UI.drawString("Player 1:", 180, 20);
        UI.drawString("Wins: " + car1Score, 180, 38);
        UI.eraseRect(180, 45, 50, 15);
        UI.setColor(Color.RED);
        UI.fillRect(180, 45, car1.life()/2, 15);
        UI.setColor(Color.BLACK);
        UI.drawRect(180, 45, 50, 15);
        // Player 2
        UI.drawString("Player 2:", 280, 20);
        UI.drawString("Wins: " + car2Score, 280, 38);
        UI.eraseRect(280, 45, 50, 15);
        UI.setColor(Color.RED);
        UI.fillRect(280, 45, car2.life()/2, 15);
        UI.setColor(Color.BLACK);
        UI.drawRect(280, 45, 50, 15);
        
        UI.repaintGraphics();
    }

    /**
     * Create a new DodgemGame object (which will set up the interface)
     * and then call the run method on it, which will start the game running
     */

    public static void main(String[] arguments)
    {
       DodgemGame game = new DodgemGame();
       game.run();
    }   

    /**
     * Object that listenes for key presses
     */
    private class KeyListener implements UIKeyListener
    {
        /**
         * Respond to keys events
         */
        public void keyPerformed(String key)
        {
            if( key.equals("Space") ) 
            {
                resetGame();
            }
            else if( car1 != null && car2 != null )
            {
                if( key.equals("s") )  car1.steerLeft();
                else if( key.equals("d") ) car1.steerRight();
                else if( key.equals("k") ) car2.steerLeft();
                else if( key.equals("l") ) car2.steerRight();
            }
        
        }
    }
}



