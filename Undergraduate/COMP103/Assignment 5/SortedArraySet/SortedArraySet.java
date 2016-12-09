// This program is copyright VUW.
// You are granted permission to use it to construct your answer to a COMP103 assignment.
// You may not distribute it in any other way without permission.

/* Code for COMP 103, Assignment 5
 * Name:
 * Usercode:
 * ID:
 */

import java.util.*;

/**
 * SortedArraySet - a Set collection;
 *
 * The implementation uses an array and a count to store the items.
 *  The items in the set should be stored in positions
 *  0, 1,... (count-1) of the array
 * The size of the array when the set is first created should be 10. 
 * It keeps the items in order according to the comparator.
 *  Ie, when it adds a new item, it must put it in the right place
 *  When it searches for an item, it should use binary search.
 *  Note, the comparator assumes that the items are Comparable.
 * It does not allow null items or duplicates.
 *  Attempting to add null should throw an exception
 *  Adding an item which is already present should simply return false, without
 *  changing the set.
 * It should always compare items using equals()  (not using ==)
 * When full, it will create a new array of double the current size, and
 *  copy all the items over to the new array
 */

public class SortedArraySet <E> extends AbstractSet <E> {

    // Data fields
    private static int INITIALCAPACITY = 10;
    private E[] data;
    private int count = 0;
    private boolean interpolationSearch = false;

    private Comparator<E> comp;    // use comp to compare items. 

    // --- Constructors --------------------------------------
    /** Constructor to make a new empty set */
    @SuppressWarnings("unchecked")  // this will stop Java complaining about the cast
    public SortedArraySet() 
    {
        comp = new ComparableComparator();
        data = (E[]) new Object[INITIALCAPACITY];

    }

    /** Constructor to make a new empty set, with a given comparator */
    @SuppressWarnings("unchecked")  // this will stop Java complaining about the cast
    public SortedArraySet(Comparator<E> comparator) 
    {
        comp = comparator;
        data = (E[]) new Object[INITIALCAPACITY];

    }

    /** Constructor that takes a whole collection and sorts it all at once */
    @SuppressWarnings("unchecked")  // this will stop Java complaining about the cast
    public SortedArraySet(Collection<E> col) 
    {
        comp = new ComparableComparator();
        data = (E[]) new Object[INITIALCAPACITY];

        comp = new ComparableComparator();

        // Add the collection
        addCollection(col);
    }

    /** Constructor that takes a whole collection and sorts it all at once */
    @SuppressWarnings("unchecked")  // this will stop Java complaining about the cast
    public SortedArraySet(Collection<E> col, Comparator<E> comparator) 
    {
        comp = new ComparableComparator();
        data = (E[]) new Object[INITIALCAPACITY];

        comp = comparator;

        // Add the collection
        addCollection(col);
    }

    // --- Methods --------------------------------------

    /** Returns number of items in collection as integer 
     */
    public int size () 
    {
        return count;
    }

    /** Add the specified item to this set (if it is not a duplicate of an item
     *  already in the set).
     *  Will not add the null value (throws an IllegalArgumentException in this case)
     *  Return true if the collection changes, and false if it did not change.
     *  
     *  I am always going to use binary search here because it tells you where to insert it
     */
    public boolean add(E item) 
    {
        if( item == null ) throw new IllegalArgumentException();

        // Make sure the array will fit the new element
        ensureCapacity();

        int index = findIndexBinarySearch(item);

        if( !item.equals(data[index]) ) 
        {

            // Move all the elements up
            for( int i = count; i > index; i-- )
                data[i] = data[i-1];

            // Insert the new element and update the count
            data[index] = item;
            count++;

            return true;
        }
        return false;
    }

    /**
     * Takes a collection ( Assumes array isnt sorted ) and then used the add function
     * to insert them all into the array
     */
    private void addCollection(Collection<E> col)
    {
        // Sort the collection using collections.sort
        //Collections.sort(col, comp);

        // Use the add function to insert all the elements
        for( E item : col )
            add(item);
    }

    /** Return true if this set contains the specified item. */
    @SuppressWarnings("unchecked")  // stops Java complaining about the call to compare 
    public boolean contains(Object item) 
    {
        if( item == null ) return false;
        E itm = (E) item;

        if( interpolationSearch ) 
        {
            if( findIndexInterpolationSearch(itm) != -1 ) return true;
            return false;
        }
        else 
        {
            int index = findIndexBinarySearch(itm);
            if( itm.equals(data[index]) ) return true;
            return false;
        }

        //if( findIndexInterpolationSearch(itm) != -1 ) return true;
        //return false;
        //int index = findIndexBinarySearch(itm);
        //if( itm.equals(data[index]) ) return true;
        //return false;
    }

    /** Remove an item matching a given item
    *  Return true if the item was present and then removed.
    *  Make no change to the set and return false if the item is not present.
    */
    @SuppressWarnings("unchecked")  // stops Java complaining about the call to compare 
    public boolean remove (Object item) 
    {
        E itm = (E) item;

        int index;
        if( interpolationSearch ) index = findIndexInterpolationSearch(itm);
        else index = findIndexBinarySearch(itm);

        //int index = findIndexBinarySearch(itm);
        if( index != -1 && itm.equals(data[index]) ) 
        {
            // Move all the data down
            for( int i = index; i < count-1; i++ )
                data[i] = data[i+1];

            // Make the last element null and decrement the count
            count--;
            data[count] = null;

            return true;
        }
        return false;

    }

    // It is much more convenient to define the following method 
    // and use it in the methods above.

    /** Find the index of where an item is in the dataarray,
     *  (or where it ought to be, if it's not there).
     *  Assumes that the item is not null.
     *  Uses binary search and requires that the items are kept in order.
     *  Should use  compareTo to compare values */
    private int findIndexBinarySearch(E item)
    {
        int low = 0;
        int high = count;

        while(low != high)
        {
            int mid = (low + high)/2;

            //int comp = item.compareTo(data[mid]);
            int c = comp.compare(item, data[mid]);
            if( c < 0 ) high = mid;
            else if( c > 0 ) low = mid + 1;
            else low = high = mid;
        }

        return low;
    }

    private int findIndexInterpolationSearch(E item)
    {
        if( count == 0 ) return -1;

        int low = 0;
        int high = count-1;
        int mid;

        while(comp.compare(data[low], item) < 0 && comp.compare(data[high], item) > 0)
        {
            mid = low + (int)((high-low) * ((float)(comp.compare(data[low], item)) / (float)comp.compare(data[low], data[high])));
            // This is to avoid the mid value overflowing
            if( mid > high) mid = (low+high)/2;

            int c = comp.compare(item, data[mid]);

            if( c > 0 ) low = mid + 1;
            else if( c < 0 ) high = mid - 1;
            else return mid;
        }

        if( comp.compare(item, data[low]) == 0 ) return low;
        else if( comp.compare(item, data[high]) == 0 ) return high;
        return -1;
    }

    /** Ensure data array has sufficient number of items
    *  to add a new item 
    */
    @SuppressWarnings("unchecked")  // this will stop Java complaining about the cast
    private void ensureCapacity() 
    {
        if (count < data.length) return;
        E[] newArray = (E[]) (new Object[data.length*2]);
        for (int i = 0; i < count; i++)
            newArray[i] = data[i];
        data = newArray;
    }

    // --- Iterator and Comparator --------------------------------------

    /** Return an iterator over the items in this set. */
    public Iterator <E> iterator() 
    {
        return new SortedArraySetIterator(this);
    }

    private class SortedArraySetIterator implements Iterator <E> {
        // needs fields, constructor, hasNext(), next(), and remove()
        private SortedArraySet<E> set;
        private int nextIndex = 0;
        private boolean canRemove = false;

        private SortedArraySetIterator(SortedArraySet<E> s) {
            set = s;
        }

        /** Return true if iterator has at least one more item */
        public boolean hasNext() {
            return (nextIndex < set.count);
        }

        /** Return next item in the set */
        public E next() {
            if (nextIndex >= set.count)
                throw new NoSuchElementException();
            canRemove = true;
            return set.data[nextIndex++];
        }

        /** Remove from the set the last item returned by the iterator.
         *  Can only be called once per call to next.
         */
        public void remove() {
            if (! canRemove)
                throw new IllegalStateException();
            set.remove(set.data[nextIndex-1]);
            canRemove = false;
        }
    }

    /** This is a comparator that assumes that E's are Comparable:
    it casts them to Comparable<E>, and then calls their compare method.
    It will fail if E's are not Comparable - in this case, the set should
    have been constructed with an appropriate comparator.
     */
    private class ComparableComparator implements Comparator<E>{
        @SuppressWarnings("unchecked")  // this will stop Java complaining about the cast
        public int compare(E item, E other){
            Comparable<E> itm = (Comparable<E>) item;
            return itm.compareTo(other);
        }
    }
}

