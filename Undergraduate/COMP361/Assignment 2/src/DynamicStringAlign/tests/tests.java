package DynamicStringAlign.tests;

import static org.junit.Assert.*;

import org.junit.Test;

import DynamicStringAlign.DynamicStringAlign;
import GreedyStringAlign.GreedyStringAlign;

public class tests {

	@Test
	public void test_01() {
		String x = "";
		String y = "G";
		
		assertTrue(getCost(x, y) == -2);
	}
	
	@Test
	public void test_02() {
		String x = "G";
		String y = "";
		
		assertTrue(getCost(x, y) == -2);
	}
	
	@Test
	public void test_03() {
		String x = "";
		String y = "AC";
	
		assertTrue(getCost(x, y) == -4);
	}
	
	@Test
	public void test_04() {
		String x = "AAA";
		String y = "ATAG";
		
		assertTrue(getCost(x, y) == -1);
	}
	
	@Test
	public void test_05() {
		String x = "AAA";
		String y = "GGG";
		
		//assertTrue(getCost(x, y) == -1);
		getCost(x, y);
	}
	
	@Test
	public void test_06() {
		String x = "GATA";
		String y = "ATAG";
		
		getCost(x, y);
		//assertTrue(getCost(x, y) == -1);
	}
	
	private int getCost(String x, String y) {
		String[] aligned = DynamicStringAlign.stringAlign(x, y);
		System.out.println("\t" + aligned[0] + "\n\t" + aligned[1]);
		System.out.println(DynamicStringAlign.alignmentCost(aligned[0], aligned[1]));
		return DynamicStringAlign.alignmentCost(aligned[0], aligned[1]);
	}

}
