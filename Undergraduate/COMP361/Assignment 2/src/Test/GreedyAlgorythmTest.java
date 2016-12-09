package Test;

import java.util.Random;

import DynamicStringAlign.DynamicStringAlign;
import GreedyStringAlign.GreedyStringAlign;

public class GreedyAlgorythmTest {
	public static final int ITTERATIONS = 200;
	public static final int MAX_SIZE = 30;
	public static final String[] alphabet = new String[] { "G", "C", "T", "A" };

	public static void main(String[] args) {
		int correct = 0;
		
		Random r = new Random();

		int i = 0;
		while (i <= ITTERATIONS+1 || ITTERATIONS == -1) {
			i++;
			int size = 1 + r.nextInt(MAX_SIZE + 1);

			// Generate solution
			String s1 = "";
			String s2 = "";

			for (int j = 0; j < size; j++) {
				int n = r.nextInt(alphabet.length + 1);

				if (n < alphabet.length) {
					s1 += alphabet[n];
				}

				n = r.nextInt(alphabet.length + 1);

				if (n < alphabet.length) {
					s2 += alphabet[n];
				}
			}
			
			System.out.println("\n\nPROBLEM: " + i);
			System.out.println("X: " + s1);
			System.out.println("Y: " + s2);
			
			String[] greedySolution = GreedyStringAlign.stringAlign(s1, s2);
			String[] dynamicSolutuon = DynamicStringAlign.stringAlign(s1, s2);
			
			int greedyCost = alignmentCost(greedySolution[0], greedySolution[1]);
			int dynamicCost = alignmentCost(dynamicSolutuon[0], dynamicSolutuon[1]);
			
			System.out.println("GREEDY COST: " + greedyCost);
			System.out.println("DYNAMIC COST: " + dynamicCost);
			
			if (greedyCost != dynamicCost) {
				System.out.println("FAILED!");
				
				System.out.println("Greedy Solution: ");
				System.out.println("\t Xg: " + greedySolution[0]);
				System.out.println("\t Yg: " + greedySolution[1]);
				System.out.println("Dynamic Solution: ");
				System.out.println("\t Xd: " + dynamicSolutuon[0]);
				System.out.println("\t Yd: " + dynamicSolutuon[1]);
				
				break;
			} else {
				System.out.println("SUCCESS!");
				correct++;
			}
		}
		
		System.out.println("\n\nDONE!");
		System.out.println("Total Tests: " + ITTERATIONS);
		System.out.println("Number Passed: " + correct );
	}
	
	public static int alignmentCost(String x, String y) {
		int cost = 0;
		
		for (int i = 0; i < x.length(); i++) {
			if (x.charAt(i) == ' ' || y.charAt(i) == ' ') {
				cost -= 2;
			} else if (x.charAt(i) == y.charAt(i)) {
				cost++;
			} else if (x.charAt(i) != y.charAt(i)) {
				cost--;
			}
		}
		
		return cost;
	}
}
