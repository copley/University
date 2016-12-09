package QuadTree;

import java.awt.Point;
import java.util.ArrayList;

/**
 * QuadTree Class
 * 
 * Wrapper class for the QuadTree, provides methods to store data in
 * QuadTreeNodes the quad tree nodes store a pice of data with type E at the
 * point P in the quad tree
 * 
 * @author danielbraithwt
 * 
 * @param <E> - Type of data to store at each point
 */
public class QuadTree<E> {
	public QuadTreeNode<E> root;

	/**
	 * Takes the bounds of the Quad Tree 
	 * 
	 * @param x
	 * @param y
	 * @param height
	 * @param width
	 */
	public QuadTree(int x, int y, int height, int width) {
		root = new QuadTreeNode<E>(x, y, height, width);
	}

	/**
	 * Adds a location to the QuadTree and stores the data varable
	 * at that point
	 * 
	 * @param l
	 * @param data
	 * @return wether the point was added
	 * 
	 * @throws IllegalArgumentException - If p or data is null
	 * @throws IndexOutOfBoundsException - If p is outside the range of the quad tree
	 */
	public boolean add(Point p, E data) {
		if (p == null || data == null) {
			throw new IllegalArgumentException();
		}
		// Make sure that it can be contained by the root node
		// if it cant then its outside the bounds of the QuadTree
		if (!root.fits(p)) {
			System.out.println(p);
			throw new IndexOutOfBoundsException();
		}

		// If the root cant fit the point then return it
		if (!root.fits(p)) {
			return false;
		}

		boolean inserted = false;
		QuadTreeNode<E> n = root;

		while (!inserted) {
			if (n == null) {
				return false;
			}

			// If the node has spaces then add it, otherwise if the node is divided
			// then find the best child to put the point into, finally if the other two
			// options fail we must subdivide the node
			if (n.hasSpaces()) {
				n.add(p, data);
				inserted = true;
			} else if (n.isDivided()) {
				n = n.getBestQuad(p);
			} else {
				n.subdivide();
			}
		}

		return true;
	}

	/**
	 * Takes a location and will get the data stored at it if the point isnt in
	 * the tree then it will return the data at the closest point to it, will check
	 * quads within an area around the point p
	 * 
	 * @param p
	 * @return data at closest node to l
	 * 
	 * @throws IllegalArgumentException - If p is null
	 * @throws IndexOutOfBoundsException - If p is outside the range of the quad tree
	 */
	public E get(Point p) {
		if (p == null) {
			throw new IllegalArgumentException();
		}
		// Make sure that it can be contained by the root node
		// if it cant then its outside the bounds of the QuadTree
		if (!root.fits(p)) {
			throw new IndexOutOfBoundsException();
		}

		E data = null;
		Point closest = null;
		boolean noneBetter = false;
		int radius = 200;

		while (!noneBetter && radius < 1000) {
			noneBetter = true;
			
			// Calculate the range around the point we want to look at
			Circle range = new Circle(p, radius);
			
			// Create an arraylist to hold the nodes the range intersects, then add the root node
			// to it
			ArrayList<QuadTreeNode<E>> intersected = new ArrayList<QuadTreeNode<E>>();
			intersected.add(root);

			// While there is a node left in the intersected list
			while (intersected.size() != 0) {
				// Remove the first one from the list
				QuadTreeNode<E> current = intersected.remove(0);

				// If its divided then add all of the children that intersect
				// with the range. Otherwise find the point in the quad that is closest
				// to the point p, if it is closer than our current closest point then store
				// it
				if (current.isDivided()) {
					intersected.addAll(current.getIntersecting(range));
				} else {
					Point best = current.getClosestPoint(p);
					
					// A quad could have 0 points stored in it so we
					// have to check for null
					if (best == null) {
						continue;
					}

					if (closest == null) {
						closest = best;
						data = current.getEzact(best);
						
						noneBetter = false;
					} else if (p.distance(best) < p.distance(closest)) {
						closest = best;
						data = current.getEzact(best);
						
						noneBetter = false;
					}
				}
			}
			
			// Increase the radius so if nothing was found then we can expand our search
			radius += 100;
		}

		return data;
	}
}
