package Trie;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * A TrieNode used to store the edges between the diffrent nodes and contains functions
 * to peform operations like add, remove, search and complete
 * 
 * @author Daniel Braithwaite
 *
 */
public class TrieNode {
	private HashMap<String, TrieNode> edges;
	private boolean stringEnd;
	
	public TrieNode() {
		edges = new HashMap<String, TrieNode>();
		stringEnd = false;
	}
	
	/**
	 * Adds a string to the Trie, assumes that the string dosnt allready exsist
	 * 
	 * @param string
	 */
	public void add(String string) {
		if (string.length() == 0) {
			stringEnd = true;
			return;
		}
		
		String firstLetter = string.substring(0, 1);
		String remainder = string.substring(1);
		
		// If there is allready an edge with that letter then use it
		// otherwise create an edge
		if (edges.containsKey(firstLetter)) {
			edges.get(firstLetter).add(remainder);
		} else {
			TrieNode node = new TrieNode();
			node.add(remainder);
			
			edges.put(firstLetter, node);
		}
	}
	
	/**
	 * Removes a string from the Trie, assumes that the string exists
	 * in the data structure
	 * 
	 * @param string
	 */
	public void remove(String string) {
		if (string.length() == 0) {
			stringEnd = false;
			return;
		}
		
		String firstLetter = string.substring(0, 1);
		String remainder = string.substring(1);
		
		if (edges.containsKey(firstLetter)) {
			// Recursively remove 
			edges.get(firstLetter).remove(remainder);
			
			// If the node has no other connections then remove it because it
			// isnt part of any other strings
			if (edges.get(firstLetter).numberOfEdges() == 0) {
				edges.remove(firstLetter);
			}
		}
	}
	
	/**
	 * Will return a boolean based on wether a provieded string is in the
	 * data structure
	 * 
	 * @param string
	 * @return wether the string exsists
	 */
	public boolean contains(String string) {
		if (string.length() == 0) {
			if (stringEnd)  {
				return true;
			}
			
			return false;
		}
		
		String firstLetter = string.substring(0, 1);
		
		// If the next letter in the string isnt in the edges then
		// return false because the string dosnt exsist
		if (!edges.containsKey(firstLetter)) {
			return false;
		}
		
		return edges.get(firstLetter).contains(string.substring(1));
	}
	
	/**
	 * Finds the possible ways of completing a given string/sentence
	 * 
	 * @param string
	 * @return ArrayList of possible solutions
	 */
	public ArrayList<String> findPossibleCompletions(String string) {
		ArrayList<String> possibleWords = new ArrayList<String>();
		
		if (string.length() == 0) {
			for (String l : edges.keySet()) {
				ArrayList<String> incomplete = edges.get(l).folowToFinish();
				
				for (String w : incomplete) {
					possibleWords.add(l + w);
				}
			}
		} else {
			String firstLetter = string.substring(0, 1);
		
			if (edges.containsKey(firstLetter)) {
			
				ArrayList<String> incomplete = edges.get(firstLetter).findPossibleCompletions(string.substring(1));
				for (String w : incomplete) {
					possibleWords.add(firstLetter + w);
				}
			}
		}
		
		return possibleWords;
	}
	
	/**
	 * Returns the number of edges connected to this node
	 * 
	 * @return num of edges
	 */
	public int numberOfEdges() {
		return edges.keySet().size();
	}
	
	/**
	 * Collects all the finish states below this node, i.e. all the ways
	 * to finish the sentence
	 * 
	 * @return ArrayList of strings/senteces
	 */
	private ArrayList<String> folowToFinish() {
		ArrayList<String> possibleWords = new ArrayList<String>();
		
		if (stringEnd) {
			possibleWords.add("");
		}
		
		
		for (String l : edges.keySet()) {
			ArrayList<String> incomplete = edges.get(l).folowToFinish();
			
			for (String w : incomplete) {
				possibleWords.add(l + w);
			}
			//possibleWords.addAll(edges.get(l).folowToFinish());
		}
		
		return possibleWords;
	}
}
