package QuadTree;

import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * The node class for the QuadTree, it stores the tree
 * structure
 * 
 * @author danielbraithwt
 *
 * @param <E>
 */
public class QuadTreeNode<E> {
	public static final int QUADTREE_NODE_CAPACITY = 4;
	
	private HashMap<Point, E> points;
	private Rectangle boundary;
	private boolean divided;
	private ArrayList<QuadTreeNode<E>> nodes;

	/**
	 * Takes an x,y,width,height and constructs a QuadTreeNode
	 * for this location, this node and all of its child nodes will
	 * only contain points within this rectangle
	 * 
	 * @param x
	 * @param y
	 * @param height
	 * @param width
	 */
	public QuadTreeNode(int x, int y, int height, int width) {
		points = new HashMap<Point, E>();
		boundary = new Rectangle(x, y, width, height);
		divided = false;
	}
	
	/**
	 * Adds the point p and its data into the node if there is space
	 * 
	 * @param p
	 * @param data
	 * @return wether something was added or not
	 */
	public boolean add(Point p, E data) {
		if (hasSpaces()) {
			points.put(p, data);
			return true;
		}
		
		return false;
	}
	
	/**
	 * Returns wether the node has been divided up into smaller nodes
	 * 
	 * @return wether node is divided
	 */
	public boolean isDivided() {
		return divided;
	}
	
	/**
	 * Returns wether the node has spaces or not
	 * 
	 * @return wether the node has spaces
	 */
	public boolean hasSpaces() {
		return !divided && points.keySet().size() != QUADTREE_NODE_CAPACITY;
	}
	
	/**
	 * Returns wether the current node directly has points contained in it or not
	 * ie this node not its subnodes (if it has subnodes)
	 * 
	 * @return wether the node has points in it or not
	 */
	public boolean hasPoints() {
		return !divided && points.size() != 0;
	}
	
	/**
	 * Finds the number of points contains in this node
	 * 
	 * @return number of points
	 */
	public int numPoints() {
		return divided ? -1 : points.size();
	}
	
	/**
	 * Returns wether the point p is contained within the nodes rectangle
	 * 
	 * @param p
	 * @return wether the point p is contained within the rectangle
	 */
	public boolean fits(Point p) {
		return boundary.contains(p);
	}
	
	/**
	 * Finds all the quads intersecting with the range
	 * 
	 * @param range
	 * @return ArrayList containing intersected quads
	 */
	public ArrayList<QuadTreeNode<E>> getIntersecting(Circle range) {
		if (!divided) {
			return null;
		}
		
		ArrayList<QuadTreeNode<E>> intersecting = new ArrayList<QuadTreeNode<E>>();
		
		for (QuadTreeNode<E> n : nodes) {
			if (n.intersects(range)) {
				intersecting.add(n);
			}
		}
		
		return intersecting;
	}
	
	/**
	 * Takes a range and checks to see if the range intersects with the
	 * quad boundary
	 * 
	 * @param range
	 * @return true if range intersects and otherwise false
	 */
	private boolean intersects(Circle range) {
		return range.intersects(boundary);
	}
	
	/**
	 * If the node is divided then the function returns the subnode that the 
	 * point p lies in, if it lies in any
	 * 
	 * @param p
	 * @return subnode that fits the point p
	 */
	public QuadTreeNode<E> getBestQuad(Point p) {
		if (divided) {
			// Loop through the sub nodes and return the one that contains
			// the point p
			for (QuadTreeNode<E> n : nodes) {
				if (n.fits(p)) {
					return n;
				}
			}
		}
		
		return null;
	}
	
	/**
	 * Takes the current node and divides it up into 4 new nodes, it then inserts
	 * all the points into its new children nodes
	 */
	public void subdivide() {
		if (!divided) {
			divided = true;
			
			// Calculate the width and height of the sub nodes
			int width = (int) Math.ceil(boundary.width/2.0) + 1;
			int height = (int) Math.ceil(boundary.height/2.0) + 1;
			
			// Create ArrayList for the nodes and insert them
			nodes = new ArrayList<QuadTreeNode<E>>();
			
			nodes.add(new QuadTreeNode<E>(boundary.x, boundary.y, height, width));
			nodes.add(new QuadTreeNode<E>(boundary.x + width, boundary.y, height, width));
			nodes.add(new QuadTreeNode<E>(boundary.x, boundary.y + height, height, width));
			nodes.add(new QuadTreeNode<E>(boundary.x + width, boundary.y + height, height, width));
			
			// Take all the points and insert them into the best sub node
			for (Point p : points.keySet()) {
				QuadTreeNode<E> q = this.getBestQuad(p);
				q.add(p, points.get(p));
			}
			
			points = null;
		}
	}

	/**
	 * If the node is divided and the sum of all the point children of the sub nodes
	 * is less than 4, remove the child nodes and add all the points to this node.
	 * 
	 * Should be used when removing to see if the tree can be made more effecant.
	 */
	public void rebuild() {
		if (!divided) {
			return;
		}
		
		int totalChildren = 0;
		for (QuadTreeNode<E> q : nodes) {
			// If there is a divided child then we cant do anything
			if (q.isDivided()) {
				return;
			}
			
			totalChildren += q.numPoints();
		}
		
		// If the sum of all the children contained in the sub nodes
		// is greater than allowed then we cant do anything
		if (totalChildren > QUADTREE_NODE_CAPACITY) {
			return;
		}
		
		// Add all the nodes from the children to this node then remvoe the nodes
		points = new HashMap<Point, E>();
		for (QuadTreeNode<E> q : nodes) {
			points.putAll(q.points);
		}
		
		nodes.clear();
		divided = false;
	}
	
	/** 
	 * If the node is divided then it will seach all its children to find the one
	 * with a point closest to the one provided
	 * 
	 * If the node hasnt been divided then this function will search all
	 * the points contained in it and return the one closest to the point p
	 * 
	 * @param l
	 * @return point closest to p
	 */
	public Point getClosestPoint(Point p) {
		// If the node is not diveded and there are no points then we 
		// cant return anything
		if (!divided && points.keySet().size() == 0) {
			return null;
		}
		
		// Loop through all the points and find the one that is the
		// closest to the point p
		double smallestDistance = Double.MAX_VALUE;
		Point closest = null;
		
		for (Point c : points.keySet()) {
			if (closest == null) {
				closest = c;
				smallestDistance = p.distance(c);
			} else if (p.distance(c) < smallestDistance) {
				smallestDistance = p.distance(c);
				closest = c;
			}
		}
		
		return closest;
	}
	
	/**
	 * Returns the data at the point p, if the point dosnt exsist in the
	 * QuadTree then null is returned, if the node is divided then null is
	 * returned
	 * 
	 * @param p
	 * @return the data at point p in the QuadTree
	 */
	public E getEzact(Point p) {
		if (divided) {
			return null;
		}
		
		for (Point c : points.keySet()) {
			if (c.equals(p)) {
				return points.get(p);
			}
		}
		
		return null;
	}
}
