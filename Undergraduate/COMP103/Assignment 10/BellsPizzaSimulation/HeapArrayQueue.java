// This program is copyright VUW.
// You are granted permission to use it to construct your answer to a COMP103 assignment.
// You may not distribute it in any other way without permission.

import java.util.*;
/**
 * Implements a priority queue based on a heap that is
 * represented with an array.
 */
public class HeapArrayQueue <E extends Comparable<? super E> > extends AbstractQueue <E> { 

    @SuppressWarnings("unchecked") 
    private E[] data = (E[])(new Comparable[7]);
    private int count = 0;

    public int size() {
        return count;
    }

    public boolean isEmpty() { 
        return size() == 0; 
    }

    /**
     * Returns the element with the top priority in the queue. 
     * 
     * HINT: This is like 'poll()' without the removal of the element. 
     * 
     * @returns the next element if present, or 'null' if the queue is empty.
     */
    public E peek() {
        return data[0];
    }

    /**
     * Removes the element with the top priority from the queue and returns it.
     * 
     * HINT: The 'data' array should contain a heap so the element with the top priority
     * sits at index '0'. After its removal, you need to restore the heap property again,
     * using 'sinkDownFromIndex(...)'.
     * 
     * @returns the next element in the queue, or 'null' if the queue is empty.
     */
    public E poll() {
        if( count == 0 ) return null;

        E toReturn = data[0];

        count--;
        if( count > 1 )
        {
            data[0] = data[count];
            data[count] = null;
            sinkDownFromIndex(0);
        }

        return toReturn;
    }

    /**
     * Enqueues an element.
     * 
     * If the element to be added is 'null', it is not added. 
     * 
     * HINT: Make use of 'ensureCapacity' to make sure that the array can 
     * accommodate one more element. 
     * 
     * @param element - the element to be added to the queue
     * 
     * @returns true, if the element could be added
     */
    public boolean offer(E element) 
    {
        if( element == null ) return false;
        ensureCapacity();

        data[count] = element;
        bubbleUpFromIndex(count);

        count++;

        return true;
    }

    private void sinkDownFromIndex(int nodeIndex) 
    {
        int child1 = 2 * nodeIndex + 1;
        int child2 = 2 * nodeIndex + 2;

        int biggest = 0;
        if( data[child1] == null ) biggest = child2;
        else if( data[child2] == null ) biggest = child1;
        else biggest = (data[child1].compareTo(data[child2]) > 0) ? child1 : child2;

        while( data[biggest].compareTo(data[nodeIndex]) > 0 )
        {
            swap(nodeIndex, biggest);

            nodeIndex = biggest;
            child1 = 2 * nodeIndex + 1;
            child2 = 2 * nodeIndex + 2;

            //biggest = (data[child1].compareTo(data[child2]) > 0) ? child1 : child2;
            if( ( child1 >= data.length || data[child1] == null ) && ( child2 >= data.length || data[child2] == null ) ) return;
            
            if( data[child1] == null ) biggest = child2;
            else if( data[child2] == null ) biggest = child1;
            else biggest = (data[child1].compareTo(data[child2]) > 0) ? child1 : child2;
        }
    }

    private void bubbleUpFromIndex(int nodeIndex) 
    {
        int parent = (nodeIndex-1)/2;

        while( data[nodeIndex].compareTo(data[parent]) < 0 )
        {
            swap(nodeIndex, parent);

            nodeIndex = parent;
            parent = (nodeIndex-1)/2;
        }
    }

    /**
     * Swaps two elements in the supporting array.
     */
    private void swap(int from, int to) {
        E temp = data[from];
        data[from] = data[to];
        data[to] = temp;
    }

    /**
     *  Increases the size of the supporting array, if necessary
     */
    private void ensureCapacity() {
        if (count == data.length) {
            @SuppressWarnings("unchecked") 
            E[] newData = (E[])new Comparable[data.length * 2];

            // copy data elements
            for (int loop = 0; loop < count; loop++) {
                newData[loop] = data[loop];
            }
            data = newData;
        }
        return;
    }

    // no iterator implementation required for this assignment
    public Iterator<E> iterator() { 
        return null; 
    }
}
