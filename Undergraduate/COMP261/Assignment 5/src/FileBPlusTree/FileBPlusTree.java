package FileBPlusTree;

import java.io.IOException;
import java.util.Iterator;
import java.util.TreeMap;

import BlockFile.Block;
import BlockFile.BlockFile;

public class FileBPlusTree<K extends Comparable<? super K>, V> implements Iterable<FileBPlusTree.Entry<K, V>> {

	protected static final int POINTER_SIZE = 4;
	
	protected BlockFile file;
	protected int blockSize;
	protected int keyBytes;
	protected int valueBytes;
	
	protected ObjectByteParser<K, V> parser;
	
	protected int numInternalNodeKeys;
	protected int numLeafNodePairs;
	
	protected int rootLocation;
	
	protected Header header;
	
	public FileBPlusTree(String fileName, int blockSize, ObjectByteParser<K, V> p) throws IOException {
		try { file = new BlockFile(fileName, blockSize); }
		catch (IOException e) {
			System.out.println("Failed to read block file");
			e.printStackTrace();
		}
		
		parser = p;
		
		// Set all the sizing variables
		this.blockSize = blockSize;
		this.keyBytes = parser.numKeyBytes;
		this.valueBytes = parser.numValueBytes;
		
		// Calculate limits for node sizes
		this.numInternalNodeKeys = (blockSize - 9) / (keyBytes + POINTER_SIZE);
		this.numLeafNodePairs = (blockSize - 9) / (keyBytes + valueBytes);
		
		// Create the header
		header = Header.getHeader(file, blockSize);
		rootLocation = header.getRoot();
		
		// Ensure the root exsists, the only time
		// we need to check is if the rootLocation is 1
		if (rootLocation == 1) {
			
			// If only the header exsists
			if (file.getSize() == 1) {

				new LeafNode<K, V>();
			}
		}
	}

	public void put(K key, V val) throws IOException {
		int currentLocation = rootLocation;

		Node<K, V> current = parseNode(currentLocation);

		while (current.find(key) != null) {
			currentLocation = current.find(key);
			current = parseNode(currentLocation);
		}
		
		current.put(key, val);
		//Integer newRoot = current.put(key, val);
		
		// Find the new root
		current = parseNode(rootLocation);
		
		Integer newRoot = null;
		while(current.getParent() != 0) {
			newRoot = current.getParent();
			current = parseNode(newRoot);
		}
		
		if (newRoot != null) {
			rootLocation = newRoot;
			header.setRoot(rootLocation);
		}
	}

	public V find(K key) throws IOException {
		int currentLocation = rootLocation;

		Node<K, V> current = parseNode(currentLocation);

		while (current.get(key) == null) {
			currentLocation = current.find(key);
			current = parseNode(currentLocation);
		}

		return current.get(key);
	}
	
	private Integer getFirstLeafNode() throws IOException {
		int currentLocation = rootLocation;

		Node<K, V> current = parseNode(currentLocation);

		while (current.getLeftMost() != null) {
			currentLocation = current.getLeftMost();
			current = parseNode(currentLocation);
		}
		
		return currentLocation;
	}

	private Node<K, V> parseNode(int index) throws IOException {
		Block block = new Block(file.read(index));

		// Check the header and determin what type of node it is
		if (block.getByte(0) == 0) {
			return new InternalNode<K, V>(block, index);
		} else {
			return new LeafNode<K, V>(block, index);
		}
	}
	
	@Override
	public Iterator<Entry<K, V>> iterator() {
		try {
			return new Iterator<Entry<K, V>>() {

				LeafNode<K, V> current = (LeafNode<K, V>) parseNode(getFirstLeafNode());
				Iterator<K> currentEntryIterator = current.entries.keySet().iterator();
				
				@Override
				public boolean hasNext() {
					return currentEntryIterator.hasNext() || current.next != 0; 
				}

				@Override
				public Entry<K, V> next() {
					Entry<K, V> e = new Entry<>();
					e.key = currentEntryIterator.next();
					e.val = current.entries.get(e.key);
					
					if (!currentEntryIterator.hasNext() && current.next != 0) {
						try { 
							current = (LeafNode<K, V>) parseNode(current.next); 
						} catch (IOException e1) { throw new RuntimeException("Some error occored when reading from the b+ tree file"); }
						
						currentEntryIterator = current.entries.keySet().iterator();
					}
					
					return e;
				}

				@Override
				public void remove() {
					// TODO Auto-generated method stub
					
				}
				
			};
		} catch (IOException e) { throw new RuntimeException("An error occored when reading from the file"); }
	}

	private interface Node<K, V> {
		public Integer find(K key);
		public V get(K key);
		public Integer put(K key, V val) throws IOException;
		
		public Integer getBlockNum();
		
		public void setParent(Integer num);
		public Integer getParent();
		
		public Integer getLeftMost();
		
		public void save() throws IOException;
	}

	private class InternalNode<M extends K, N extends V> implements Node<K, V> {
		private Block block;
		private int blockNum;

		private TreeMap<K, Integer> entries;
		private int leftMost;
		
		private Integer parent;

		public InternalNode(Integer left) throws IOException {
			block = new Block(blockSize);
			blockNum = file.write(block.getBytes());
			
			parent = 0;
			
			leftMost = left;
			
			entries = new TreeMap<>();
		}

		public InternalNode(Block b, int index) {
			block = b;
			blockNum = index;

			entries = new TreeMap<>();
			
			parent = block.getInt(1);

			// Get the right most pointer
			leftMost = block.getInt(5);
			
			int offset = 9;

			while (offset + (keyBytes + POINTER_SIZE) < (block.length() - POINTER_SIZE)) {
				//String s = block.getString(offset, keyBytes).replace("\0", "");
				K key = parser.convertBytesToKey(block.getBytes(offset, keyBytes));
				offset += keyBytes;
				
				int location = block.getInt(offset);
				offset += POINTER_SIZE;

				if (key != null) {
					entries.put(key, location);
				}
			}

		}

		@Override
		public Integer find(K key) {
			
			if (entries.firstKey().compareTo(key) > 0) {
				return leftMost;
			}
			
			Integer child = 0;
			
			for (K s : entries.keySet()) {
				if (s.compareTo(key) <= 0) {
					child = entries.get(s);
				} else {
					return child;
				}
			}
			
			return child;
		}

		@Override
		public V get(K key) {
			return null;
		}

		@Override
		public Integer put(K key, V val) {
			return find(key);
		}
		
		public Integer putKey(K key, Integer child) throws IOException {
			entries.put((M) key, child);
			
			Integer newParent = ensureCapacity();
			save();
			
			return newParent;
			
			//if (newParent != null) {
			//	return newParent;
			//}
			
			//return parent == 0 ? blockNum : parent;
		}
		
		private Integer ensureCapacity() throws IOException {
			if (entries.size() >= numInternalNodeKeys) {
				
				// Get the parent
				InternalNode<K, V> parentNode;
				if (parent == 0) {
					parentNode = new InternalNode<>(this.getBlockNum());
					parent = parentNode.getBlockNum();
				} else {
					parentNode = (InternalNode<K, V>) parseNode(parent);
				}
				
				Object[] keys = entries.keySet().toArray();
				
				int middle = numInternalNodeKeys/2;
				K middleKey = (K) keys[middle];
				
				InternalNode<K ,V> splitNode = new InternalNode<>(entries.remove(middleKey));
				
				for (int i = middle; entries.size() != middle; i++) {
					K currentKey = (K) keys[i+1];
					Integer childBlockId = entries.remove(currentKey);
					
					Node<K, V> child = parseNode(childBlockId);
					child.setParent(splitNode.getBlockNum());
					child.save();
					
					splitNode.putKey(currentKey, childBlockId);
				}
				
				Integer newParent = parentNode.putKey(middleKey, splitNode.getBlockNum());
				parentNode.save();
				
				splitNode.setParent(parent);
				
				
				// Save all the nodes
				splitNode.save();
				
				return newParent;
			}
			
			return null;
		}

		@Override
		public Integer getBlockNum() {
			return blockNum;
		}

		@Override
		public void setParent(Integer num) {
			parent = num;
		}

		@Override
		public void save() throws IOException {
			
			block = new Block(blockSize);
			
			block.setInt(parent, 1);
			
			// Write the initial pointer
			block.setInt(leftMost, 5);
			
			int offset = 9;
			Object[] keys = entries.keySet().toArray();
			
			for (int i = 0; i < keys.length; i++) {

				block.setBytes(parser.convertKeyToBytes((K) keys[i]), offset);
				offset += keyBytes;
				
				//block.setBytes(parser.convertValueToBytes((V) entries.get((K) keys[i])), offset);
				block.setInt(entries.get((K) keys[i]), offset);
				offset += POINTER_SIZE;
			}
			
			file.write(block.getBytes(), blockNum);
		}
		
		public String toString() {
			StringBuilder b = new StringBuilder();
			
			b.append("[");
			
			Object[] keys = entries.keySet().toArray();
			for (int i = 0; i < keys.length; i++) {
				if (i != 0) {
					b.append(", ");
				}
				
				String key = (String) keys[i];
				
				b.append(key);
			}
			
			b.append("]");
			
			return b.toString();
		}

		@Override
		public Integer getLeftMost() {
			return leftMost;
			
		}

		@Override
		public Integer getParent() {
			return parent;
		}
	}

	private class LeafNode<M extends K, N extends V> implements Node<K, V> {
		private Block block;
		private int blockNum;

		private TreeMap<K, V> entries;
		
		private Integer parent;
		
		private Integer next;

		public LeafNode() throws IOException {
			block = new Block(blockSize);
			block.setByte((byte) 1, 0);
			blockNum = file.write(block.getBytes());
			
			entries = new TreeMap<>();
		}

		public LeafNode(Block b, int index) {
			block = b;
			blockNum = index;
			
			entries = new TreeMap<>();

			parent = block.getInt(1);
			
			next = block.getInt(5);
			
			// keep count of the offset in the block.
			// starting with 5 to skip the header
			int offset = 9;

			// While we can still read a key value pair
			while (offset + (valueBytes + keyBytes) < block.length()) {
				K key = (K) parser.convertBytesToKey(block.getBytes(offset, keyBytes));
				offset += keyBytes;

				V i = (V) parser.convertBytesToValue(block.getBytes(offset, valueBytes));//block.getInt(offset);
				offset += valueBytes;

				if (key != null) {
					// Add to the tree map
					entries.put(key, i);
				}
			}
		}

		@Override
		public Integer find(K key) {
			return null;
		}

		@Override
		public V get(K key) {
			return entries.get(key);
		}

		@Override
		public Integer put(K key, V val) throws IOException {
			entries.put(key, val);
			
			Integer newRoot = ensureCapacity();
			save();
			
			return newRoot;//parent == 0 ? blockNum : parent;
		}
		
		private Integer ensureCapacity() throws IOException {
			if (entries.size() >= numLeafNodePairs) {
				
				InternalNode<K, V> parentNode;
				if (parent == 0) {
					parentNode = new InternalNode<>(blockNum);
					parent = parentNode.getBlockNum();
				} else {
					parentNode = (InternalNode<K, V>) parseNode(parent);
				}
				
				Object[] keys = entries.keySet().toArray();
				
				LeafNode<K, V> right = new LeafNode<>();
				right.setParent(parent);
				
				// Set up the links beween the leaf nodes
				right.next = next;
				next = right.getBlockNum();
				
				int middle = keys.length/2;
				K middleKey = (K) keys[middle];
				
				for (int i = middle; i < keys.length; i++) {
					K key = (K) keys[i];
					
					right.put(key, entries.remove(key));
				}
				right.save();
				
				Integer newRoot = parentNode.putKey(middleKey, right.blockNum);
				parentNode.save();
				
				return newRoot;
			}
			
			return null;
		}

		@Override
		public Integer getBlockNum() {
			return blockNum;
		}

		@Override
		public void setParent(Integer num) {
			parent = num;	
		}

		@Override
		public void save() throws IOException {
			block = new Block(blockSize);
			
			block.setByte((byte) 1, 0);
			
			block.setInt(parent, 1);
			
			block.setInt(next, 5);
			
			int offset = 9;
			Object[] keys = entries.keySet().toArray();
			
			for (int i = 0; i < keys.length; i++) {
				K key = (K) keys[i];
				
				block.setBytes(parser.convertKeyToBytes(key), offset);
				offset += keyBytes;
				
				//block.setBytes(block.getBytes(offset, valueBytes), offset);
				block.setBytes(parser.convertValueToBytes(entries.get(key)), offset);
				offset += valueBytes;
			}
			
			file.write(block.getBytes(), blockNum);
		}
		
		public String toString() {
			StringBuilder b = new StringBuilder();
			
			b.append("[");
			
			
			Object[] keys = entries.keySet().toArray();
			for (int i = 0; i < keys.length; i++) {
				if (i != 0) {
					b.append(", ");
				}
				
				String key = (String) keys[i];
				
				b.append(key + ":" + entries.get(key));
			}
			
			b.append("]");
			
			return b.toString();
		}

		@Override
		public Integer getLeftMost() {
			return null;
		}

		@Override
		public Integer getParent() {
			return parent;
		}
	}
	
	public static abstract class ObjectByteParser<M, N> {
		
		protected int numKeyBytes;
		protected int numValueBytes;
		
		public ObjectByteParser(int kb, int vb) {
			numKeyBytes = kb;
			numValueBytes = vb;
		}
		
		public byte[] keyToBytes(M key) {
			byte[] bytes = new byte[numKeyBytes];
			convertBytesToKey(bytes);
			
			if (bytes.length > numKeyBytes) {
				throw new IllegalArgumentException("Key is to long!");
			}
			
			return bytes;
		}
		
		public M keyFromBytes(byte[] bytes) {
			if (bytes.length > numKeyBytes) {
				throw new IllegalArgumentException("Key is to long!");
			}
			
			return convertBytesToKey(bytes);
		}
		
		public byte[] valueToBytes(N val) {
			byte[] bytes = new byte[numKeyBytes];
			convertValueToBytes(val);
			
			if (bytes.length > numValueBytes) {
				throw new IllegalArgumentException("Value is to long!");
			}
			
			return bytes;
		}
		
		public N valueFromBytes(byte[] bytes) {
			if (bytes.length > numValueBytes) {
				throw new IllegalArgumentException("Value is to long!");
			}
			
			return convertBytesToValue(bytes);
		}
		
		protected abstract byte[] convertKeyToBytes(M key);
		protected abstract M convertBytesToKey(byte[] bytes);
		protected abstract byte[] convertValueToBytes(N val);
		protected abstract N convertBytesToValue(byte[] bytes);
	}
	
	protected static class Header {
		BlockFile file;
		Block block;
		
		private Header(BlockFile f, Block b) throws IOException {
			file = f;
			block = b;
			
			setRoot(1);
		}
		
		public void setRoot(int index) throws IOException {
			block.setInt(index, 0);
			file.write(block.getBytes(), 0);
		}
		
		public int getRoot() {
			return block.getInt(0);
		}
		
		public static Header getHeader(BlockFile file, int size) throws IOException {
			Block h;
			
			// If file is empty then we need to make 
			// a new header for the file, other wise get the first block
			if (file.getSize() == 0) {
				h = new Block(size);
				file.write(h.getBytes());
			} else {
				h = new Block(file.read(0));
			}
			
			return new Header(file, h);
		}
	}
	
	public static class Entry<M, N> {
		public M key;
		public N val;
		
		public String toString() {
			return key.toString() + " : " + val.toString();
		}
	}
}
