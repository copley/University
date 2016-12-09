package BPlusTree;

import java.util.ArrayList;
import java.util.List;

public class BPlusInternalNode<K extends Comparable<? super K>, V> implements
		BPlusNode<K, V> {

	private BPlusInternalNode<K, V> parent;
	private List<K> keys;
	private List<BPlusNode<K, V>> children;

	public BPlusInternalNode(BPlusNode<K, V> node) {
		keys = new ArrayList<K>();
		children = new ArrayList<BPlusNode<K, V>>();

		children.add(node);
	}

	@Override
	public BPlusNode<K, V> add(K k, V v) {
		int index = 0;

		while (index < keys.size() && keys.get(index).compareTo(k) < 0) {
			index++;
		}

		children.get(index).add(k, v);
		return (parent == null) ? this : parent;
		//return children.get(index).add(k, v);
	}
	
	@Override
	public V get(K k) {
		int index = 0;

		while (index < keys.size() && keys.get(index).compareTo(k) <= 0) {
			index++;
		}
		
		return children.get(index).get(k);
	}

	public BPlusNode<K, V> addKey(K k, BPlusNode<K, V> node) {
		int index = 0;

		while (index < keys.size() && keys.get(index).compareTo(k) < 0) {
			index++;
		}

		keys.add(index, k);
		children.add(index + 1, node);
		
		BPlusNode<K, V> newParent = ensureCapacity();

		//return parent;
		return parent == null ? this : parent;
	}

	@Override
	public BPlusNode<K, V> ensureCapacity() {
		// Check to see if our leaf node is to full
		if (keys.size() > BPlusTree.MAX_NODE_SIZE) {
			// In this case we need to re balance the tree

			if (parent == null) {
				parent = new BPlusInternalNode<K, V>(this);
			}

			// Get the middle
			int middle = (int) Math.floor(BPlusTree.MAX_NODE_SIZE / 2.0f);
			K middleKey = keys.remove(middle);

			// Split the current node
			BPlusInternalNode<K, V> splitNode = new BPlusInternalNode<K, V>(children.remove(middle + 1));
			for (int i = middle; keys.size() != middle; i++) {
				K key = keys.remove(middle);

				BPlusNode<K, V> child = children.remove(middle + 1);
				child.setParent(splitNode);
				
				splitNode.addKey(key, child);
			}

			parent.addKey(middleKey, splitNode);

			splitNode.parent = parent;

			return parent;
		}
		
		return null;
	}
	
	public String toString() {
		StringBuilder b = new StringBuilder();

		for (int i = 0; i < keys.size(); i++) {
			b.append("(");
			b.append(children.get(i).toString());
			b.append(")");
			b.append(keys.get(i).toString());
		}
		
		b.append("(");
		b.append(children.get(children.size()-1));
		b.append(")");
 
		return b.toString();
	}

	@Override
	public BPlusLeafNode<K, V> getStartingLeafNode() {
		return children.get(0).getStartingLeafNode();
	}

	@Override
	public void setParent(BPlusInternalNode<K, V> p) {
		parent = p;
	}
}
