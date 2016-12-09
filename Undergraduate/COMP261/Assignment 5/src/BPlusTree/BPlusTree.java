package BPlusTree;

import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;

public class BPlusTree<K extends Comparable<? super K>, V> implements Iterable<Entry<K, V>> {
	public static final int MAX_NODE_SIZE = 10;
	
	private int size;
	
	private BPlusNode<K, V> root;
	
	public BPlusTree() {
		root = new BPlusLeafNode<K, V>();
		
		size = 0;
	}
	
	public boolean put(K key, V value) {
		if (find(key) != null) {
			return false;
		}
		
		root = root.add(key, value);
		
		size++;
		
		return true;
	}
	
	public V find(K key) {
		return root.get(key);
	}
	
	public int size() {
		return size;
	}
	
	public boolean isEmpty() {
		return size == 0;
	}

	@Override
	public Iterator<Entry<K, V>> iterator() {
		return new Iterator<Entry<K, V>>() {
			
			BPlusLeafNode<K, V> current = root.getStartingLeafNode();
			Iterator<Entry<K, V>> leafIterator = current.iterator();
			
			@Override
			public boolean hasNext() {
				return (leafIterator.hasNext() || current.getNext() != null);
			}

			@Override
			public Entry<K, V> next() {
				if (leafIterator.hasNext()) {
					return leafIterator.next();
				} else if (current.getNext() != null) {
					current = current.getNext();
					leafIterator = current.iterator();
					
					return leafIterator.next();
				} else {
					throw new NoSuchElementException();
				}
			}

			@Override
			public void remove() {
				throw new UnsupportedOperationException();				
			}
		};
	}
}
