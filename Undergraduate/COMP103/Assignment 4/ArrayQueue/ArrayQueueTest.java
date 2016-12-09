// This program is copyright VUW.
// You are granted permission to use it to construct your answer to a COMP103 assignment.
// You may not distribute it in any other way without permission.


/* Code for COMP103, Assignment 4
 * Name:
 * Usercode:
 * ID:
 */

import java.util.Iterator;
import java.util.Queue;

import static org.junit.Assert.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.JUnitCore;

/** ArrayQueueTest
 *  A JUnit class for testing an ArrayQueue
 */

public class ArrayQueueTest 
{
    private static int ITEMS_TO_INSERT = 20;
    private Queue<String> queue;
    
    @Before
    public void initiliseEmptyQueue()
    {
        queue = new ArrayQueue<String>();
    }
    
    @Test
    public void isEmptyOnCreation()
    {
        assertTrue("A new queue should have 0 items in it", queue.isEmpty());
    }
    
    @Test
    public void offerNullTest()
    {
        assertFalse("Shoudent be able to insert a null value", queue.offer(null));
    }
    
    @Test
    public void testQueueOfferAndPoll()
    {
        // Insert a certian number of items into the queue
        for( int i = 0; i < ITEMS_TO_INSERT; i++ )
            queue.offer("item" + i);
            
        assertTrue("Size should be: " + ITEMS_TO_INSERT, queue.size() == ITEMS_TO_INSERT);
            
        for( int i = 0; i < ITEMS_TO_INSERT; i++ )
            assertTrue("Items not returned in correct order", queue.poll().equals("item"+i));
    }
    
    @Test
    public void testQueuePeek()
    {
        queue.offer("item1");
        queue.offer("item2");
        assertTrue("Peek dosnt return correct item", queue.peek().equals("item1"));
    }
    
    @Test
    public void testEnsureCompacity()
    {
        for( int i = 0; i < 10; i++ )
            queue.offer("item" + i);
            
        for( int i = 0; i < 9; i++ )
            queue.poll();
            
        for( int i = 10; i < ITEMS_TO_INSERT+10; i++ )
            queue.offer("item" + i);
            
        for( int i = 9; i < ITEMS_TO_INSERT+10; i++ )
            assertTrue("Order in queue is not maintaied", queue.poll().equals("item"+i));
    }
    
    @Test
    public void testItterator()
    {   
        for( int i = 0; i < ITEMS_TO_INSERT; i++ )
            queue.offer("item" + i);
          
            
        Iterator iter = queue.iterator();
        assertTrue("Has next should return true", iter.hasNext());
        
        int i = 0;
        while(iter.hasNext())
        {
            assertTrue("Itterator is not going through data in correct order", iter.next().equals("item" + i));
            i++;
        }
        //int i = 0;
        //for( String c : queue )
        //{
        //    assertTrue("Itterator is not going through data in correct order", c.equals("item" + i));
        //    i++;
        //}
    }

    /*# YOUR CODE HERE */

    public static void main(String args[]) {
        org.junit.runner.JUnitCore.main("ArrayQueueTest");
    }

}
