package BPlustTreeTests;

import static org.junit.Assert.*;

import org.junit.Test;

import BPlusTree.BPlusTree;

public class BPlusTreeGet {

	@Test
	public void get_test_01() {
		BPlusTree<String, Integer> bpt = new BPlusTree<>();

		bpt.put("a", 0);

		// Should return the correct value asociated with
		// 'a'
		if (bpt.find("a") != 0) {
			fail();
		}

		bpt.put("c", 1);

		// Should return the correct value asociated with
		// 'c'
		if (bpt.find("c") != 1) {
			fail();
		}
		
		bpt.put("b", 2);
		bpt.put("d", 3);
		bpt.put("e", 4);
		bpt.put("f", 5);
		bpt.put("g", 6);
		
		if (bpt.find("g") != 6) {
			fail();
		}
	}
}
