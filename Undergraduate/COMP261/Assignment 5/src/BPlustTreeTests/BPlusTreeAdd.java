package BPlustTreeTests;

import static org.junit.Assert.*;

import org.junit.Test;

import BPlusTree.*;

public class BPlusTreeAdd {
	
	private static final int MAX_TEST_SIZE = 20;
	
	@Test
	public void add_test_01() {
		BPlusTree<String, Integer> tree = new BPlusTree<String, Integer>();
		
		// New tree should be empty
		if (!tree.isEmpty()) {
			fail();
		}
		
		tree.put("a", 0);
	
		// Should have size 1
		if (tree.size() != 1) {
			fail();
		}
		
		tree.put("c", 1);
		tree.put("b", 2);
		tree.put("d", 3);
		tree.put("e", 4);
		tree.put("f", 5);
		tree.put("g", 6);
	}
	
	@Test
	public void add_test_02() {
		BPlusTree<Integer, Integer> tree = new BPlusTree<Integer, Integer>();
		
		for (int i = 0; i < MAX_TEST_SIZE; i++) {
			tree.put(i, i);
		}
		
		int current = 0;
		for (Entry<Integer, Integer> e : tree) {
			if (e.value != current) {
				fail();
			}
			
			current++;
		}
	}
	
	@Test
	public void add_test_03() {
		int[] data = new int[] {50, 32, 67, 21, 43, 69, 18, 14, 90, 85, 36, 3, 41, 40, 39, 48, 37};
		
		BPlusTree<Integer, Integer> tree = new BPlusTree<Integer, Integer>();
		
		for (int i = 0; i < data.length; i++) {
			tree.put(data[i], i);
		}
		
		int count = 0;
		for (Entry<Integer, Integer> e : tree) {
			count++;
		}
		
		if (count != data.length) {
			fail("Tree had " + count + " entrys, expecting " + data.length);
		}
	}
}
