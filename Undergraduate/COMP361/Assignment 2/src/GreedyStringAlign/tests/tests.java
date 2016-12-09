package GreedyStringAlign.tests;

import static org.junit.Assert.*;

import org.junit.Test;

import GreedyStringAlign.GreedyStringAlign;

public class tests {

	@Test
	public void test_01() {
		String x = "GATCGGCAT";
		String y = "CAATGTGAATC";
		
		assertTrue(getCost(x, y) == -3);
	}
	
	@Test
	public void test_02() {
		String x = "CAGTCAGTCAGTCAGT";
		String y = "CATTCAGTCAGTCAGT";
		
		assertTrue(getCost(x, y) == 14);
	}
	
	@Test
	public void test_03() {
		String x = "CAGTCAGTCAGCAGT";
		String y = "CATTTCAGTCAGTCAG";
		
		assertTrue(getCost(x, y) == 6);
	}
	
	private int getCost(String x, String y) {
		String[] aligned = GreedyStringAlign.stringAlign(x, y);
		System.out.println(GreedyStringAlign.alignmentCost(aligned[0], aligned[1]));
		return GreedyStringAlign.alignmentCost(aligned[0], aligned[1]);
	}

}
