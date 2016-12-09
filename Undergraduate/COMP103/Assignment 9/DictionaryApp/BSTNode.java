// This program is copyright VUW.
// You are granted permission to use it to construct your answer to a COMP103 assignment.
// You may not distribute it in any other way without permission.

import java.io.*;
import ecs100.*;

/**  
 * Implements a binary search tree node.
 *   
 *  @author: Thomas Kuehne (based on previous code)
 */

public class BSTNode <E extends Comparable<E>> {

    private E value;
    private BSTNode<E> left;
    private BSTNode<E> right;
    private BSTNode<E> parent;
    

    // constructs a node with a value
    public BSTNode(E value) {
        this.value = value;
    }

    // Getters...

    public E getValue() {
        return value;
    }

    public BSTNode<E> getLeft() {
        return left;
    }

    public BSTNode<E> getRight() {
        return right;
    }
    
    public BSTNode<E> getParent() {
        return parent;
    }

    /** 
     * Returns true if the subtree formed by the receiver contains 'item'
     * 
     * CORE
     *
     * ASSUMPTION: 'item' is not 'null'.
     * 
     * HINT: A recursive approach leads to a very short and simple code. 
     * 
     * HINT: use 'compareTo(...)' in order to compare the parameter 
     * with the data in the node. 
     * 
     * HINT: Make sure that you invoke 'compareTo' by always using
     * the same receiver / argument ordering throughout the program, e.g., 
     * always use the item as the receiver of 'compareTo'.
     *
     *@param item - the item to check for
     *@returns true if the subtree contained 'item'
     *
     */
    public boolean contains(E item) {
        if( item == null ) return false;
        
        if( item.compareTo(value) == 0 ) return true;
        else if( item.compareTo(value) < 0 && left != null && left.contains(item) ) return true;
        else if( item.compareTo(value) > 0 && right != null && right.contains(item) ) return true;

        // no matching node was found
        return false;
    }

    /**
     * Adds an item to the subtree formed by the receiver.
     * 
     * CORE
     * 
     * Must not add an item, if it is already in the tree.
     * 
     * HINT: use 'compareTo(...)' in order to compare the parameter 
     * with the data in the node. 
     * 
     * @param item - the value to be added
     * @returns false, if the item was in the subtree already. Returns true otherwise.
     */
    public BSTNode<E> add(E item) {
        if( item.compareTo(value) < 0 )
        {
            if( left == null ) 
            {
                left = new BSTNode(item);
                left.parent = this;
            }
            else left.add(item);
        }
        else
        {
            if( right == null ) 
            {
                right = new BSTNode(item);
                right.parent = this;
            }
            else right.add(item);
        }
        
        int balanceFactor = getBalanceFactor();

        if(balanceFactor == 2)
        {
            if( left.getBalanceFactor() == -1 ) left.rotateLeft();
            return rotateRight();
        }
        else if( balanceFactor == -2 )
        {
            if( right.getBalanceFactor() == 1 ) right.rotateRight();
            return rotateLeft();
        }

        if( parent != null ) return parent;
        return this;

        // not proper code, just needed to enable compilation
        // return false;
    }
    
    public void exchange(BSTNode<E> n, BSTNode<E> o)
    {
        if( left == o ) left = n;
        else if( right == o ) right = n;
    }

    /**
     * Returns the height of the receiver node.
     * 
     * CORE 
     * 
     * HINT: The number of children the receiver node may have, implies
     * four cases to deal with (none, left, right, left & right).
     * 
     * @returns the height of the receiver
     */
    public int height() {
        int l = left == null ? -1 : left.height();
        int r = right == null ? -1 : right.height();

        if( l > r ) return l + 1;
        else return r + 1;
        // just to enable compilation
        // return 0;
    }

    /**
     * Returns the length of the shortest branch in the subtree formed by the receiver.
     * 
     * COMPLETION
     * 
     * @returns the minimum of all branch lenghts starting from the receiver. 
     * 
     */
    public int minDepth() {
        return Math.min(left != null ? left.height()+1 : 1, right != null ? right.height()+1 : 1);
        
        //int l = left == null ? 0 : left.minDepth();
        //int r = right == null ? 0 : right.minDepth();
        
        
        //return 1 + Math.min(l, r);
        //int l = left == null ? -1 : left.minDepth();
        //int r = right == null ? -1 : right.minDepth();

        //if( l < r ) return l + 1;
        //else 
        //{
        //    return r + 1;
        //}

        // just to enable compilation
        // return 0;
    }

    /** 
     *  Removes an item in the subtree formed by the receiver.
     *  
     *  COMPLETION
     *  
     *  ASSUMPTION: The item to be removed does exist. 
     *  The case that it cannot be found, should be dealt with before this method is called.
     *  
     *  Performs two tasks:
     *  1. locates the node to be removed, and
     *  2. replaces the node with a suitable node from its subtrees.
     *  
     *  HINT: use 'compareTo(...)' in order to compare the parameter 
     *  with the data in the node. 
     * 
     *  HINT: For task 2, you should use call method 'replacementSubtreeFromChildren'
     *  to obtain this node. 
     *  
     *  HINT: When replacing a node, it is sufficient to change the value of the existing node
     *  with the value of the node that conceptually replaces it. There is no need to actually 
     *  replace the node object as such. 
     *  
     *  @param item - the item to be removed
     *  @returns the reference to the subtree with the item removed.
     *  
     *  HINT: Often the returned reference will be the receiver node, but it is possible that
     *  the receiver itself needs to be removed. If you use a recursive approach, the
     *  latter case is the base case. 
     *   
     */
    public BSTNode<E> remove(E item) {
        if( item.compareTo(value) == 0 )
        {
            BSTNode<E> n = replacementSubtreeFromChildren(left, right);
            return n;
            
            
        }
        else if( item.compareTo(value) < 0 )
        {
            //this.left = left.remove(item);
            left = left.remove(item);
            if( left != null ) left.parent = this;
        }
        else
        {
            //this.right = right.remove(item);
            right = right.remove(item);
            if( right != null ) right.parent = this;
        }
        
        int balanceFactor = getBalanceFactor();

        if(balanceFactor == 2)
        {
            if( left.getBalanceFactor() == -1 ) left.rotateLeft();
            return rotateRight();
        }
        else if( balanceFactor == -2 )
        {
            if( right.getBalanceFactor() == 1 ) right.rotateRight();
            return rotateLeft();
        }

        // there was no need to replace the receiver node
        return this;  
    }

    /**
     *  Returns a replacement subtree for the receiver node (which is to be removed).
     *  
     *  COMPLETION
     *  
     *  The replacement subtree is determined from the children of the node to be removed.
     *  
     *  HINT: There are several cases:
     *  - node has no children    => return null
     *  - node has only one child => return the child
     *  - node has two children   => return the current subtree but with
     *       a) its (local) root replaced by the leftmost node in the right subtree, and
     *       b) the leftmmost node in the right subtree removed.
     *       
     * @param left - the left subtree from which to include items.       
     * @param right - the right subtree from which to include items.       
     * @returns a reference to a subtree which contains all items from 'left' and 'right' combined.      
     *                                          
     */
    private BSTNode<E> replacementSubtreeFromChildren(BSTNode<E> left, BSTNode<E> right) {
        if( left == null && right == null ) return null;
        if( left != null && right == null ) return left;
        if( right != null && left == null ) return right;
       
        BSTNode<E> l = right.getLeftmostNode();
        this.value = l.value;
        
        this.right = right.remove(value);
        
        //right = right.remove(value);
        
        // not a simple case => return modified node
        return this;
    }

    /**
     *  Returns the leftmost node in the subtree formed by the receiver. 
     *  
     *  COMPLETION
     *  
     *  HINT: The code is very simple. Just keep descending left branches, 
     *  until it is no longer possible. 
     * 
     * @returns a reference to the leftmost node, starting from the receiver.    
     *                                          
     */
    private BSTNode<E> getLeftmostNode() {
        if( left == null ) return this;
        
        BSTNode n = left;
        while( n.getLeft() != null )
        {
            n = n.getLeft();
        }
        
        return n;
    }

    public int getBalanceFactor()
    {
        return ( (left != null ) ? left.height()+1 : 0 ) - ( (right != null ) ? right.height()+1 : 0 );
    }

    public BSTNode<E> rotateLeft()
    {
        BSTNode<E> curParent = parent;
        BSTNode<E> r = right;
        BSTNode<E> n = right.left;
        
        r.left = this;
        parent = r;
        
        this.right = n;
        if( n != null ) n.parent = this;
        
        r.parent = curParent;
        if( curParent != null ) curParent.exchange(r, this);
        return r;
    }

    public BSTNode<E> rotateRight()
    {

        BSTNode<E> curParent = parent;
        BSTNode<E> l = left;
        BSTNode<E> n = left.right;
        
        l.right = this;
        parent = l;
        
        this.left = n;
        if( n != null ) n.parent = this;
        
        l.parent = curParent;
        if( curParent != null ) curParent.exchange(l, this);
        return l;
    }

    /**
     * Prints all the nodes in a subtree to a stream.
     * 
     * @param stream - the output stream 
     */
    public void printAllToStream(PrintStream stream) {
        if (left!=null) 
            left.printAllToStream(stream);

        stream.println(value);

        if (right!=null) 
            right.printAllToStream(stream);
    }

    /**
     * Prints all the nodes in a subtree on the text pane.
     * 
     * Can be useful for debugging purposes, but 
     * is most useful on small sample trees. 
     * 
     * Usage: node.printAll("").
     */
    public void printAll(String indent){
        if (right!=null)
            right.printAll(indent+"    ");

        UI.println(indent + value);

        if (left!=null) 
            left.printAll(indent+"    ");
    }
}
