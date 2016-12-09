package BPlusTree;

public class Entry<K, V> {
	public V value;
	public K key;
	
	public String toString() {
		return key.toString() + ":" + value.toString();
	}
}
