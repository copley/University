package assignment1;

import static org.junit.Assert.*;

import org.junit.Test;

public class MyTests extends BasicMatcher {

	//
	// Regex: "+"; that work
	//
	
	@Test public void test_51() {
		assertEquals(true, match("davebc", "bc+"));
	}
	
	@Test public void test_52() {
		assertEquals(true, match("dbccaa", "bc+"));
	}
	
	@Test public void test_53() {
		assertEquals(true, match("bc", "bc+"));
	}
	
	//
	// Regex: "+"; that dont work
	//
	
	@Test public void test_54() {
		assertEquals(false, match("daveb", "bc+"));
	}
	
	@Test public void test_55() {
		assertEquals(false, match("b", "bc+"));
	}
	
	@Test public void test_56() {
		assertEquals(false, match("bac", "bc+"));
	}
	
	//
	// Regex: Combinations
	//
	
	@Test public void test_57() {
		assertEquals(matchWithLib("abccccc", ".bc*"), match("abccccc", ".bc*"));
	}
	
	@Test public void test_58() {
		assertEquals(matchWithLib("abccccc", ".bc*$"), match("abccccc", ".bc*$"));
	}
	
	@Test public void test_59() {
		assertEquals(matchWithLib("abcccccgdsfh", ".bc*$"), match("abcccccgdsfh", ".bc*$"));
	}
	
	@Test public void test_60() {
		assertEquals(matchWithLib("abccc", "^a.bc*"), match("abccc", "^a.bc*"));
	}
	
	@Test public void test_61() {
		assertEquals(matchWithLib("", "^."), match("", "^."));
	}
	
	@Test public void test_62() {
		assertEquals(matchWithLib("", "a*"), match("", "a*"));
	}
	
	@Test public void test_63() {
		assertEquals(matchWithLib("xbfc", "^a*.b.c"), match("xbfc", "^a*.b.c"));
	}
	
	@Test public void test_64() {
		assertEquals(matchWithLib("abcdef", "^a*.b$"), match("abcdef", "^a*.b$"));
	}
	
	@Test public void test_65() {
		assertEquals(matchWithLib("", ".$"), match("", ".$"));
	}
	
	@Test public void test_66() {
		assertEquals(matchWithLib("ab", ".+"), match("ab", ".+"));
	}
	
	@Test public void test_67() {
		assertEquals(matchWithLib("ab", ".*"), match("ab", ".*"));
	}
	
	@Test public void test_68() {
		assertEquals(matchWithLib("", ".*"), match("", ".*"));
	}
	
	@Test public void test_69() {
		assertEquals(matchWithLib("a", "^.+"), match("a", "^.+"));
	}
	
	@Test public void test_70() {
		assertEquals(matchWithLib("", "^.+"), match("", "^.+"));
	}
	
	@Test public void test_71() {
		assertEquals(matchWithLib("a", ".+$"), match("a", ".+$"));
	}
	
	@Test public void test_72() {
		assertEquals(matchWithLib("", ".+$"), match("", ".+$"));
	}
	
	//
	// Regex "^.a+c*$";
	//
	
	@Test public void test_73() {
		assertEquals(matchWithLib("gaccccc", "^.a+c*$"), match("gaccccc", "^.a+c*$"));
	}
	
	@Test public void test_74() {
		assertEquals(matchWithLib("gacccccf", "^.a+c*$"), match("gacccccf", "^.a+c*$"));
	}
	
	@Test public void test_75() {
		assertEquals(matchWithLib("gc", "^.a+c*$"), match("gc", "^.a+c*$"));
	}
	
	@Test public void test_76() {
		assertEquals(matchWithLib("gaaaac", "^.a+c*$"), match("gaaaac", "^.a+c*$"));
	}
	
	@Test public void text_77() {
		assertEquals(matchWithLib("", ".+"), match("", ".+"));
	}
	
	@Test public void text_78() {
		assertEquals(matchWithLib("", "$*^"), match("", "$*^"));
	}
	
	@Test public void text_79() {
		try {
			match(null, null);
			fail();
		} catch(IllegalArgumentException e) {
			
		}
	}

}
