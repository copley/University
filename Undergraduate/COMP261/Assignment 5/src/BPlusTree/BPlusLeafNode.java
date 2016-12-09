package BPlusTree;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class BPlusLeafNode<K extends Comparable<? super K>, V> implements BPlusNode<K, V>, Iterable<Entry<K, V>> {
	private BPlusInternalNode<K, V> parent;
	private BPlusLeafNode<K, V> next;
	private List<Entry<K, V>> values;

	public BPlusLeafNode() {
		values = new ArrayList<Entry<K, V>>();
	}

	@Override
	public BPlusNode<K, V> add(K k, V v) {
		int index = 0;

		while (index < values.size() && values.get(index).key.compareTo(k) < 0) {
			index++;
		}

		Entry<K, V> e = new Entry<>();
		e.value = v;
		e.key = k;

		values.add(index, e);

		// Ensure the capcaity of the node
		BPlusNode<K, V> newParent = ensureCapacity();
		
		//return parent;
		return parent == null ? this : parent;
	}
	
	@Override
	public V get(K k) {
		for (Entry<K, V> e : values) {
			if (e.key.compareTo(k) == 0) {
				return e.value;
			}
		}
		
		return null;
	}

	public String toString() {
		StringBuilder b = new StringBuilder();

		b.append("[");
		int i = 0;
		for (Entry<K, V> e : values) {
			if (i != 0) {
				b.append(",");
			}

			b.append(e.toString());

			i++;
		}

		b.append("]");

		return b.toString();
	}

	@Override
	public BPlusNode<K, V> ensureCapacity() {
		
		// Check to see if our leaf node is to full
		if (values.size() > BPlusTree.MAX_NODE_SIZE) {
			// In this case we need to re balance the tree

			if (parent == null) {
				parent = new BPlusInternalNode<K, V>(this);
			}

			// Get the middle
			int middle = (int) Math.floor(BPlusTree.MAX_NODE_SIZE / 2.0f);
			Entry<K, V> middleEntry = values.get(middle);

			// Split the current node
			BPlusLeafNode<K, V> splitNode = new BPlusLeafNode<K, V>();
			for (int i = middle; values.size() != middle; i++) {
				Entry<K, V> entry = values.remove(middle);

				splitNode.add(entry.key, entry.value);
			}

			BPlusInternalNode<K, V> newParent = (BPlusInternalNode<K, V>) parent.addKey(middleEntry.key, splitNode);

			splitNode.parent = newParent;
			parent = newParent;

			splitNode.next = next;
			next = splitNode;
			
			// Get the node next to the split node
			//splitNode.next = splitNode.getNextLeafNode(splitNode);

			return newParent == null ? parent : newParent;
		}
		
		return null;
	}

	@Override
	public BPlusLeafNode<K, V> getStartingLeafNode() {
		return this;
	}

	@Override
	public Iterator<Entry<K, V>> iterator() {
		return new Iterator<Entry<K, V>>() {
			
			Iterator<Entry<K, V>> valsIterator = values.iterator();
			
			@Override
			public boolean hasNext() {
				return valsIterator.hasNext();
			}

			@Override
			public Entry<K, V> next() {
				return valsIterator.next();
			}

			@Override
			public void remove() {
				throw new UnsupportedOperationException();
			}
		};
	}
	
	public BPlusLeafNode<K, V> getNext() {
		return next;
	}

	@Override
	public void setParent(BPlusInternalNode<K, V> p) {
		parent = p;
	}
}
