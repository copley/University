package Trie;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Trie Data Structure
 * 
 * For storing strings/sentences to be used in an auto complete system,
 * strings can be added and removed from the Trie. There is also a function to
 * retreve the possible ways to complete a sentence
 * 
 * @author Daniel Braithwaite
 *
 */
public class Trie {
	private TrieNode root;
	
	public Trie() {
		root = new TrieNode();
	}
	
	/**
	 * Takes a string and adds it to the trie, the string is converted to lower case as to
	 * make the search case insentive
	 * 
	 * @param string
	 * @return wether the add was a sccuss
	 */
	public boolean add(String string) {
		// Stop a null string from causing errors
		if (string == null) {
			return false;
		}
		
		String str = formatString(string);
		
		// If the Trie allready contains the string then return false
		if (contains(str)) {
			return false;
		}
		
		root.add(str);
		
		return true;
	}
	
	/**
	 * Handles adding a collection of strings to the Trie
	 * 
	 * @param c
	 */
	public void addCollection(Collection<String> c) {
		for (String s : c) {
			add(s);
		}
	}
	
	/**
	 * Will remove remove a prevously added string from the Trie
	 * 
	 * @param string
	 * @return
	 */
	public boolean remove(String string) {
		// Stop a null string from causing errors
		if (string == null) {
			return false;
		}
		
		String str = formatString(string);
		
		if (!contains(string)) {
			return false;
		}
		
		root.remove(str);
		return true;
	}
	
	/**
	 * Returns a boolean based on wether the supplied string is in the Trie or not
	 * search is case insensitive
	 * 
	 * @param string
	 * @return
	 */
	public boolean contains(String string) {
		// Stop a null string from causing errors
		if (string == null) {
			return false;
		}
		
		String str = formatString(string);
		
		return root.contains(str);
	}
	
	/**
	 * Returns a list of possible ways to complete the current string
	 * 
	 * @param string
	 * @return ArrayList of possible completions
	 */
	public ArrayList<String> findPossibleCompletions(String string) {
		// Stop a null string from causing errors
		if (string == null) {
			return null;
		}
		
		if (string.replaceAll("\\s", "").length() == 0) {
			return new ArrayList<String>();
		}
		
		System.out.println(string);
		
		String str = formatString(string);
		
		return root.findPossibleCompletions(str);
	}
	
	/**
	 * Returns a string that is formatted how the trie expects words to be
	 * at the moment it only makes the string lower case but moving it into its
	 * own function means its easier to change later if i need to
	 * 
	 * @param string
	 * @return formatted version of input
	 */
	private String formatString(String string) {
		String s = string.toLowerCase();
		
		return s;
	}
}
