// This program is copyright VUW.
// You are granted permission to use it to construct your answer to a COMP103 assignment.
// You may not distribute it in any other way without permission.

/* Code for COMP103 Assignment
 * Name:
 * Usercode:
 * ID:
 */

import ecs100.*;
import java.util.*;

/** The FastFood game involves customers who generate orders, and the player
 *  who has to fulfill the orders by assembling the right collection of food items.
 *  The goal of the game is to make as much money as possible before
 *  the player gets too far behind the customers and is forced to give up.
 *
 *  The game presents the player with a a queue of orders in a fast food outlet.
 *  The player has to fullfill the customer orders by adding the correct items to
 *  the order at the head of the queue.  
 *  When the order is ready, the player can deliver the order, which will
 *  take it off the queue, and will add the price of the order to the balance.
 *  Whenever the player adds an item to the order that doesn't belong in the
 *  order, the price of the item is subtracted from the balance.
 *  The player can practice by generating orders using the Practice button.
 *  Once the game is started, the orders are generated automatically.
 */
public class FastFood implements UIButtonListener, UIKeyListener
{
    public static HashMap<FoodType, Double> prices;
    private Queue<Order> orders;
    private double balance;

    public FastFood() 
    {
        UI.initialise();
        UI.setImmediateRepaint(false);

        orders = new ArrayDeque<Order>();
        
        prices = new HashMap<FoodType, Double>();
        prices.put(FoodType.FISH, 1.50);
        prices.put(FoodType.CHIPS, 2.50);
        prices.put(FoodType.BURGER, 5.00);
        prices.put(FoodType.HOTDOG, 3.50);
        prices.put(FoodType.SMOOTHIE, 6.00);

        UI.setKeyListener(this);
        UI.addButton("Practice Order", this);
        UI.addButton("Add Fish", this);
        UI.addButton("Add Chips", this);
        UI.addButton("Add Burger", this);
        UI.addButton("Add Hotdog", this);
        UI.addButton("Add Smoothie", this);
        UI.addButton("Deliver Order", this);
        UI.addButton("Start Game", this);
        
        printInstructions();

        drawOrders();
        this.run(); 
    }

    public void printInstructions()
    {
        // Print the instructions 
        UI.println("INSTRUCTIONS!");
        UI.println("q - Add chips to order");
        UI.println("w - Add a fish to order");
        UI.println("e - Add a burger to order");
        UI.println("r - Add a hotdog to order");
        UI.println("t - Add a smoothie to order");
        UI.println("Enter - Start the game, when the game isnt running");
        UI.println("Enter - Submit the order, when the game is running\n");
    }
    
    /** Respond to the buttons */
    public void buttonPerformed(String name)
    {
        if ("Practice Order".equals(name))  
        {
            if( !gameRunning) generateOrder();
            else UI.println("You in a game, you cant add a practice order");
        }
        else if ("Add Fish".equals(name))   {addItem("Fish");}
        else if ("Add Chips".equals(name))  {addItem("Chips");}
        else if ("Add Burger".equals(name)) {addItem("Burger");}
        else if (name.equals("Add Hotdog")) {addItem("Hotdog");}
        else if (name.equals("Add Smoothie")) {addItem("Smoothie");}
        else if ("Deliver Order".equals(name)) {deliverOrder();}
        else if ("Start Game".equals(name)) {startGame();}
        drawOrders();
    }

    public void keyPerformed(String key)
    {
        if( key.equals("q") ) addItem("Fish");
        else if( key.equals("w") ) addItem("Chips");
        else if( key.equals("e") ) addItem("Burger");
        else if( key.equals("r") ) addItem("Hotdog");
        else if( key.equals("t") ) addItem("Smoothie");
        else if( key.equals("Enter") && !gameRunning ) startGame();
        else if( key.equals("Enter") && gameRunning )  deliverOrder();

        drawOrders();
    }

    /** Create a new order and put it on the queue to be processed */
    public void generateOrder() 
    {
        int diffculty = 3;
        if( balance > 100 ) diffculty++;
        if( balance > 200 ) diffculty++;
        
        orders.add(new Order(diffculty));

        drawOrders();
    }

    /** As long as there is an order in the queue, adds the specified
     *  item to the order at the head of the queue,
     *  If adding the item fails (ie, it isn't one of the items
     *  that are wanted by the order) then the price
     *  of the item is deducted from the current balance.
     */
    public void addItem(String item)
    {
        if( orders.size() == 0 ) 
        {
            UI.println("There are no orders!");
            return;
        }
        
        boolean result = false;
        
        if( item.equals("Fish") ) result = orders.peek().addItemToOrder(FoodType.FISH);
        else if( item.equals("Chips") ) result = orders.peek().addItemToOrder(FoodType.CHIPS);
        else if( item.equals("Burger") ) result = orders.peek().addItemToOrder(FoodType.BURGER);
        else if( item.equals("Hotdog") ) result = orders.peek().addItemToOrder(FoodType.HOTDOG);
        else if( item.equals("Smoothie") ) result = orders.peek().addItemToOrder(FoodType.SMOOTHIE);
        
        if( !result )
        {
            if( item.equals("Fish") ) balance -= prices.get(FoodType.FISH);
            else if( item.equals("Chips") ) balance -= prices.get(FoodType.CHIPS);
            else if( item.equals("Burger") ) balance -= prices.get(FoodType.BURGER);
            else if( item.equals("Hotdog") ) result = orders.peek().addItemToOrder(FoodType.HOTDOG);
            else if( item.equals("Smoothie") ) result = orders.peek().addItemToOrder(FoodType.SMOOTHIE);
        }
    }

    /** As long as there is an order at the front of the queue and it is ready,
     *  take the first order off the queue, compute the price of the order,
     *  and update the total balance by adding the order price.
     *  If there is not a ready order on the queue, it prints a warning message
     */
    public void deliverOrder()
    {
        if( !orders.isEmpty() )
        {
            if( orders.peek().isReady() ) 
            {
                balance += orders.poll().getPrice();
            } 
            else UI.println("WARNING: Dont cheat people out of food!");

            drawOrders();
        }
    }

    /** Draw the queue of orders on the Graphics pane.
     *  Also draws the current balance in the top left corner
     */
    public void drawOrders() 
    {
        UI.clearGraphics();

        UI.drawString(String.format("$%.2f", balance), 10, 10);

        int y = 20;
        for( Order o : orders )
        {
            o.draw(y);
            y += 50;
        }

        UI.repaintGraphics();
    }

    // In the game version, the orders must be automatically generated.
    // The methods below will reset the queue and the current balance,
    // and will then set the gameRunning field to true. This will make
    // the run method start generating orders.
    // The run method is called from the main method, and therefore is in the main
    // thread, which executes concurrently with all the GUI buttons.
    // run  does nothing until the gameRunning field is set to be true
    // Once the gameRunning field is true, then it will generate orders automatically,
    // every timeBetweenOrders milliseconds. It will also makde the games speed up
    // gradually, by steadily reducing the timeBetweenOrders.
    // You do not need to write these methods code.

    private boolean gameRunning = false;
    private long timeBetweenOrders = 3500;

    private void startGame()
    {
        UI.clearGraphics();
        UI.clearText();
        printInstructions();
        orders.clear();
        balance = 0;
        timeBetweenOrders = 3500;
        gameRunning = true;
        //   nextOrder = 0; nextSpeedup = 0;// I don't think they are needed
    }

    public void run() 
    {
        long timeBetweenSpeedups = 3500;
        long timeNextOrder = 0;
        long timeNextSpeedup = 0;
        while (true) 
        {
            UI.sleep(100); // Wait at least 100 milliseconds between actions.
            long now = System.currentTimeMillis();
            if (!gameRunning) continue;  // if gameRunning is false, then don't generate orders
            if (now >= timeNextOrder)
            {
                timeNextOrder = now + timeBetweenOrders;
                generateOrder();
                drawOrders();
            }
            if (now >= timeNextSpeedup) 
            {   // get faster steadily.
                if (timeBetweenOrders > 1000) timeBetweenOrders -= 100; 
                timeNextSpeedup = now + timeBetweenSpeedups;
            }
            if (orders.size() > 20) 
            {
                UI.println("Oh no! You have too many orders waiting! Game over...");
                orders.clear();
                gameRunning = false;
                //break;
            }
        }
    }

    public static void main(String args[]) 
    {
        FastFood ff = new FastFood();
    }
}
