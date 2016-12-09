// This program is copyright VUW.
// You are granted permission to use it to construct your answer to a COMP103 assignment.
// You may not distribute it in any other way without permission.

/* Code for COMP 103, Assignment 4
 * Name:
 * Usercode:
 * ID:
 */

import java.util.*;

/**
 * ArraySet - a Set collection;
 *
 * The implementation uses an array and a count to store the items.
 *  The items in the set should be stored in positions
 *  0, 1,... (count-1) of the array
 * The size of the array when the set is first created should be 10. 
 * It does not keep the items in any particular order, and may change the
 *  order of the remaining items when removing items.  Eg, it can always add
 *  a new item at the end, and it can move the last item into the place of an
 *  item being deleted - there is no need to shift all  the items up or down
 *  to keep them in order.
 * It does not allow null items or duplicates.
 *  Attempting to add null should throw an exception
 *  Adding an item which is already present should simply return false, without
 *  changing the set.
 * It should always compare items using equals()  (not using ==)
 * When full, it will create a new array of double the current size, and
 *  copy all the items over to the new array
 */

public class ArraySet <E> extends AbstractSet <E> {

    private static int DEFAULT_SIZE = 10;

    // Data fields
    private Object[] data;

    private int size;

    // Constructors

    @SuppressWarnings("unchecked")  // this will stop Java complaining
    public ArraySet() 
    {
        data = new Object[DEFAULT_SIZE];

        size = 0;
    }
    
    /**
     * This constructor takes a int to define how big the data array should start.
     * It means that if you know roughly how much data you will be adding you can
     * make it faster by not having to use ensureCompacity() as often
     */
    public ArraySet(int startSize)
    {
        data = new Object[startSize];

        size = 0;
    }

    // Methods

    /** Returns number of items in collection as integer 
     */
    public int size () 
    {
        return size;
    }

    /** Add the specified item to this set (if it is not a duplicate of an item
     *  already in the set).
     *  Will not add the null value (throws an IllegalArgumentException in this case)
     *  Return true if the collection changes, and false if it did not change.
     */
    public boolean add(E item) 
    {
        // Check to make sure we can actually insert the data
        if( item == null ) throw new IllegalArgumentException();
        if( contains(item) ) return false;
        
        if( size == data.length ) ensureCapacity();

        data[size] = item;
        size++;
        
        return true;
    }

    /** Return true if this set contains the specified item. */
    public boolean contains(Object item) 
    {
        return findIndex(item) != -1;
    }

    /** Remove an item matching a given item
     *  Return true if the item was present and then removed.
     *  Make no change to the set and return false if the item is not present.
     */
    public boolean remove(Object item) 
    {
        int index = findIndex(item);
        if( index == -1 ) return false;

        // Move and replace data
        if( index != size-1 )
        {
            data[index] = data[size-1];
            data[size] = null;
        }
        else data[index] = null;

        size--;

        return true;
    }

    /** Return an iterator over the items in this set. */
    public Iterator <E> iterator() 
    {
        return new ArraySetIterator();
    }

    /** Ensure data array has sufficient number of items
    *  to add a new item 
    */
    @SuppressWarnings("unchecked")  // this will stop Java complaining
    private void ensureCapacity() 
    {    
        Object[] newData = new Object[data.length*2];

        for( int i = 0; i < data.length; i++ )
        {
            newData[i] = data[i];
        }

        data = newData;
    }

    // You may find it convenient to define the following method and use it in
    // the methods above, but you don't need to do it this way.

    /** Find the index of an item in the dataarray, or -1 if not present
     *  Assumes that the item is not null 
     */
    private int findIndex(Object item) 
    {
        for( int i = 0; i < size; i++ )
            if(data[i].equals(item)) return i;

        return -1;
    }

    private class ArraySetIterator <E> implements Iterator <E> {

        int index;
        boolean removed = true;

        public ArraySetIterator()
        {
            index = 0;
        }

        /** Return true if iterator has at least one more item */
        public boolean hasNext() 
        {
            return (index < size);
        }

        /** Return next item in the set */
        public E next() 
        {
            if( !hasNext() ) throw new NoSuchElementException();
            
            removed = false;
            E toReturn = (E) data[index];
            index++;
            
            return toReturn;
        }

        /** Remove from the set the last item returned by the iterator.
         *  Can only be called once per call to next.
         */
        public void remove() 
        {
            if( !removed ) throw new NoSuchElementException();
            
            ArraySet.this.remove(data[index-1]);
            removed = true;
        }
    }
}

