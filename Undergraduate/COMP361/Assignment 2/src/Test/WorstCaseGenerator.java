package Test;

import java.util.Random;

import DynamicStringAlign.DynamicStringAlign;

public class WorstCaseGenerator {
	public static final int ITTERATIONS = -1;
	public static final int MAX_SIZE = 30;
	public static final String[] alphabet = new String[] { "G", "C", "T", "A" };

	public static void main(String[] args) {
		int correct = 0;

		Random r = new Random();

		int i = 0;
		while (i <= ITTERATIONS + 1 || ITTERATIONS == -1) {
			i++;
			int size = 10;

			// Generate solution
			String s1 = "";
			String s2 = "";

			for (int j = 0; j < size; j++) {
				int n = r.nextInt(alphabet.length);

				if (n < alphabet.length) {
					s1 += alphabet[n];
				}

				n = r.nextInt(alphabet.length);

				if (n < alphabet.length) {
					s2 += alphabet[n];
				}
			}

			System.out.println("\n\nPROBLEM: " + i);
			System.out.println("X: " + s1);
			System.out.println("Y: " + s2);

			String[] dynamicSolutuon = DynamicStringAlign.stringAlign(s1, s2);

			System.out.println(dynamicSolutuon[0]);
			System.out.println(dynamicSolutuon[1]);

			int dynamicCost = alignmentCost(dynamicSolutuon[0],
					dynamicSolutuon[1]);

			if (dynamicCost == -2 * size) {
				break;
			}
		}
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