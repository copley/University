
/**
 * Write a description of class HeapLinkedNode here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class HeapLinkedNode<E extends Comparable<? super E> >
{
    private HeapLinkedNode<E> left;
    private HeapLinkedNode<E> right;
    private HeapLinkedNode<E> parent;
    
    private int leftHeight;
    private int rightHeight;
    
    private E data;
    
    public HeapLinkedNode( E d )
    {
        data = d;
        
        leftHeight = 0;
        rightHeight = 0;
    }
    
    public void setParent(HeapLinkedNode<E> n)
    {
        parent = n;
    }
    
    public void setData( E d )
    {
        data = d;
    }
    
    public E getData()
    {
        return data;
    }
    
    public HeapLinkedNode<E> getLeft()
    {
        return left;
    }
    
    public HeapLinkedNode<E> getRight()
    {
        return right;
    }
    
    public boolean add( E element )
    {
        // Add if you can
        if( left == null )
        {
            left = new HeapLinkedNode(element);
            left.setParent(this);
            
            leftHeight++;
            
            left.bubbleUp();
            
            return true;
        }
        else if( right == null )
        {
            right = new HeapLinkedNode(element);
            right.setParent(this);
            
            rightHeight++;
            
            right.bubbleUp();
            
            return true;
        }
        
        if( leftHeight > rightHeight ) 
        {
            right.add(element);
            rightHeight++;
        }
        else 
        {
            left.add(element);
            leftHeight++;
        }
        
        return true;
    }
    
    public void sinkDown()
    {
        HeapLinkedNode<E> n = null;
        if( left != null && left.getData().compareTo(data) > 0 ) n = left;
        if( right != null && ( n == null || right.getData().compareTo(left.getData()) > 0 ) ) n = right;
        
        if( n != null )
        {
            E tmp = n.getData();
            n.setData(data);
            data = tmp;
            
            n.sinkDown();
        }
    }
    
    public void bubbleUp()
    {
        if( parent != null && parent.getData().compareTo(data) < 0 )
        {
            E tmp = parent.getData();
            parent.setData(data);
            data = tmp;
            
            parent.bubbleUp();
        }
    }
    
    public E findReplacementRoot()
    {
        if( left == null && right == null )
        {
            parent.delete(this);
            parent = null;
            return data;
        }
        
        if( leftHeight > rightHeight ) 
        {
            leftHeight--;
            return left.findReplacementRoot();
        }
        else 
        {
            rightHeight--;
            return right.findReplacementRoot();
        }
        
    }
    
    protected void delete( HeapLinkedNode<E> n )
    {
        if( left == n ) left = null;
        else if( right == n ) right = null;
    }
}
