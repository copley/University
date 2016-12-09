package UnionFind;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class UnionFind implements Iterable<UnionFindElement> {
	Map<Integer, UnionFindNode> sets = new HashMap<>();
	
	public UnionFind(Collection<? extends UnionFindElement> elements) {
		
		// Get all the elements and insert them into the hash set
		for (UnionFindElement e : elements) {
			UnionFindNode n = new UnionFindNode();
			n.depth = 0;
			n.element = e;
			n.parent = null;
			n.children = new ArrayList<UnionFindElement>();
			
			sets.put(e.getID(), n);
		}
		
		// Map to keep track of what has been removed
		Map<Integer, UnionFindNode> removed = new HashMap<>();
		
		// Create the sets
		for (UnionFindNode n : sets.values()) {
			// Make sure its still in the sets
			if (removed.containsKey(n.element.getID())) {
				continue;
			}
			
			for (UnionFindElement e: n.element.getConnectedNodes()) {
				UnionFindNode neighbour = sets.get(e.getID());
				
				UnionFindNode r = union(n, neighbour);
				
				if (r != null) {
					removed.put(r.element.getID(), r);
				}
			}
		}
		
		// Clean up the sets map
		for (UnionFindNode n : removed.values()) {
			sets.remove(n.element.getID());
		}
		
	}
	
	private UnionFindNode findParent(UnionFindNode n) {
		if (n.parent == null) {
			return n;
		}
		
		return findParent(n.parent);
	}
	
	private UnionFindNode union(UnionFindNode x, UnionFindNode y) {
		UnionFindNode removed = null;
		
		UnionFindNode xRoot = findParent(x);
		UnionFindNode yRoot = findParent(y);
		
		if (xRoot == yRoot) {
			return removed;
		}
		
		if (xRoot.depth < yRoot.depth) {
			xRoot.parent = yRoot;
			//sets.remove(xRoot.element.getID());
			removed =  xRoot;
		} else {
			yRoot.parent = xRoot;
			//sets.remove(yRoot.element.getID());
			removed =  yRoot;
		}
		
		if (x.depth == y.depth) {
			xRoot.depth++;
		}
		
		return removed;
	}

	@Override
	public Iterator<UnionFindElement> iterator() {
		Set<UnionFindElement> rootNodes = new HashSet<>();
		
		for (UnionFindNode n : sets.values()) {
			rootNodes.add(n.element);
		}
		
		return rootNodes.iterator();
	}
}
