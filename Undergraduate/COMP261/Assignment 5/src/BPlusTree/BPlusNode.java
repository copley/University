package BPlusTree;

public interface BPlusNode<K extends Comparable<? super K>, V> {
	public BPlusNode<K, V> add(K k, V v);
	public BPlusNode<K, V> ensureCapacity();
	public V get(K k);
	public BPlusLeafNode<K, V> getStartingLeafNode();
	public void setParent(BPlusInternalNode<K, V> p);
}
