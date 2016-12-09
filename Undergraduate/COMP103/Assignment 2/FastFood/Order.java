// This program is copyright VUW.
// You are granted permission to use it to construct your answer to a COMP103 assignment.
// You may not distribute it in any other way without permission.

/* Code for COMP103 Assignment
 * Name:
 * Usercode:
 * ID:
 */

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import ecs100.UI;

public class Order 
{
    private ArrayList<Food> order;

    public Order(int diffculty) 
    {
        order = new ArrayList<Food>();
        Random r = new Random();

        
        
        do
        {
            int choice = r.nextInt(diffculty);

            switch( choice )
            {
                case 0: order.add(new Food(FoodType.FISH));
                break;
                case 1: order.add(new Food(FoodType.CHIPS));
                break;
                case 2: order.add(new Food(FoodType.BURGER));
                break;
                case 3: order.add(new Food(FoodType.HOTDOG));
                break;
                case 4: order.add(new Food(FoodType.SMOOTHIE));
                default: break;
            }

        } while( r.nextDouble() < 0.7 && order.size() <= 7 );

    }

    /** The order is ready as long as there every item that is
     *  wanted is also ready.
     */
    public boolean isReady() 
    {
        for( int i = 0; i < order.size(); i++ )
            if( !order.get(i).isFoodReady() ) return false;
        
        return true;
    }

    /** If the item is wanted but not already in the order,
     *  then put it in the order and return true, to say it was successful.
     *  If the item not wanted, or is already in the order,
     *  then return false to say it failed.
     */
    public boolean addItemToOrder(FoodType t)
    {
        for( int i = 0; i < order.size(); i++ )
        {
            if( order.get(i).getType() == t && !order.get(i).isFoodReady() )
            {
                order.get(i).foodIsReady();
                return true;
            }
        }
        
        return false;
    }

    /** Computes and returns the price of an order.
     *  Core: Uses constants: 2.50 for fish, 1.50 for chips, 5.00 for burger
     *  to add up the prices of each item
     *  Completion: Uses a map of prices to look up prices
     */
    public double getPrice() 
    {
        double price = 0;
        
        for( int i = 0; i < order.size(); i++ ) price += order.get(i).getPrice();
        
        return price;
    }

    public void draw(int y) 
    {
        int x = 10;
        for( int i = 0; i < order.size(); i++ )
        {
            order.get(i).draw(x, y);
            x += 40;
        }
    }
}
