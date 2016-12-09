import java.util.*;

/**
 * Write a description of class HeapLinkedQueue here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class HeapLinkedQueue<E extends Comparable<? super E> > extends AbstractQueue<E>
{
    private HeapLinkedNode<E> root;
    private int size;

    public HeapLinkedQueue()
    {
        root = null;
        size = 0;
    }

    public boolean add ( E element )
    {
        // Cant add null elements
        if(element == null ) return false;

        if( root == null ) root = new HeapLinkedNode(element);
        else root.add(element);

        size++;

        return true;
    }

    public boolean offer( E element )
    {
        return add(element);
    }

    public boolean addAll( Collection<? extends E> c )
    {
        for( E element : c ) add(element);

        return true;
    }

    public int size()
    {
        return size;
    }

    public boolean isEmpty()
    {
        return ( size == 0 );
    }

    public void clear()
    {

    }

    public E element()
    {
        return root.getData();
    }

    public E peek()
    {
        return element();
    }

    public E remove()
    {
        if( root == null ) return null;
        else if( size == 1 )
        {
            E data = root.getData();
            root = null;

            size--;

            return data;
        }
        else
        {
            E toReturn = root.getData();

            E data = root.findReplacementRoot();
            root.setData(data);
            root.sinkDown();

            size--;

            return toReturn;
        }
    }

    public E poll()
    {
        return remove();
    }

    public Iterator<E> iterator()
    {
        return new HeapLinkedQueueIterator(root);
    }

    private class HeapLinkedQueueIterator implements Iterator<E>
    {
        Stack<HeapLinkedNode<E>> nodes = new Stack();
        
        public HeapLinkedQueueIterator(HeapLinkedNode<E> n)
        {
            if( n != null ) nodes.push(n);
        }
        
        public boolean hasNext()
        {
            return (nodes.size() != 0);
            
            
        }

        public E next()
        {
            if(nodes.size() == 0) throw new NoSuchElementException();
            
            HeapLinkedNode<E> n = nodes.pop();
            if( n.getLeft() != null ) nodes.push(n.getLeft());
            if( n.getRight() != null ) nodes.push(n.getRight());
            
            return n.getData();
        }

        public void remove()
        {
            throw new UnsupportedOperationException();
        }
    }
}
