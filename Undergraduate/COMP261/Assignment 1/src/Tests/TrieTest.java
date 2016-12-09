package Tests;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import Trie.Trie;

public class TrieTest {

	private Trie trie;
	private String[] words = {"and", "because", "at", "the", "run", "race", "java", "good"};
	
	@Before
	public void initTrie() {
		trie = new Trie();
	}
	
	@Test
	public void testAdd() {
		assertTrue(trie.add("hello"));
	}
	
	@Test
	public void testAddCollection() {
		ArrayList<String> s = new ArrayList<String>();
		for (String word : words) {
			s.add(word);
		}
		
		trie.addCollection(s);
		
		assertTrue(trie.contains(words[0]));
		assertTrue(trie.contains(words[2]));
		assertTrue(trie.contains(words[4]));
	}
	
	@Test
	public void testAddNull() {
		assertFalse(trie.add(null));
	}
	
	@Test
	public void testMultipleAddAndContains() {
		// Add all the words to the try
		for (String word : words) {
			assertTrue(trie.add(word));
		}
		
		// Make sure all the addded words are contained in the trie
		for (String word : words) {
			assertTrue(trie.contains(word));
		}
	}
	
	@Test
	public void testContains() {
		assertTrue(trie.add("hello"));
		assertTrue(trie.contains("hello"));
		
		// Should return false becausese word not in trie
		assertFalse(trie.contains("because"));
	}
	
	@Test
	public void testContainsNull() {
		assertFalse(trie.contains(null));
	}
	
	@Test
	public void testDuplicateAdd() {
		assertTrue(trie.add("hello"));
		
		// Adding the same word again should mean the add function
		// should return false
		assertFalse(trie.add("hello"));
	}
	
	@Test
	public void testCaseSensitive() {
		assertTrue(trie.add("hello"));
		assertFalse(trie.add("Hello"));
		assertFalse(trie.add("HELLO"));
	}
	
	@Test
	public void testWordCompletion() {
		for (String word : words) {
			assertTrue(trie.add(word));
		}
		
		assertTrue(trie.findPossibleCompletions("ra").contains("race"));
		
		ArrayList<String> a = trie.findPossibleCompletions("a");
		assertTrue(a.contains("and"));
		assertTrue(a.contains("at"));
		assertFalse(a.contains("race"));
	}
	
	@Test
	public void testWordCompletionOnWordNotInTrie() {
		for (String word : words) {
			assertTrue(trie.add(word));
		}
		
		assertTrue(trie.findPossibleCompletions("hello").size() == 0);
	}
	
	@Test
	public void testRemove() {
		assertTrue(trie.add("hello"));
		assertTrue(trie.contains("hello"));
		
		assertTrue(trie.remove("hello"));
		assertFalse(trie.contains("hello"));
	}
	
	@Test
	public void testRemoveNull() {
		assertFalse(trie.remove(null));
	}
	
	@Test
	public void testRemoveWordNotInTrie() {
		for (String word : words) {
			assertTrue(trie.add(word));
		}
		
		assertFalse(trie.remove("hello"));
	}
	
	@Test
	public void testPunctuation() {
		assertTrue(trie.add("hello-world"));
		assertTrue(trie.contains("hello-world"));
		
		assertTrue(trie.findPossibleCompletions("hello").contains("hello-world"));
	}
	
	@Test
	public void testStreetNames() {
		assertTrue(trie.add("HazleWood Ave"));
		
		assertTrue(trie.contains("HazleWood Ave"));
		
		assertTrue(trie.findPossibleCompletions("Hazle").contains("hazlewood ave"));
	}
}
