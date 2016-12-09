package dynamicknapsack0n;

import java.util.HashMap;
import java.util.Map;

import Item.Item;

public class DynamicKnapsack0N {
	public static int barometer = 0;

	public static int[] dynamicKnapsackSolve(int capacity, Item[] items) {
		
		Map<Integer, Integer> itemMapping = new HashMap<>();

		for (int i = 0, j = 1, k = 0; k < items.length; i++, j++) {
			barometer++;
			itemMapping.put(i, k);

			if (j == items[k].mult) {
				j = 0;
				k++;
			}
		}


		int[][] T = new int[itemMapping.size()][capacity + 1];

		for (int i = 0; i < T.length; i++) {
			for (int j = 0; j < T[i].length; j++) {
				barometer++;

				if (j - items[itemMapping.get(i)].weight < 0) {
					T[i][j] = max(getTableValue(T, i - 1, j), 0);
				} else {
					T[i][j] = max(getTableValue(T, i - 1, j),
							getTableValue(T, i - 1, j - items[itemMapping.get(i)].weight)
									+ items[itemMapping.get(i)].value, 0);
				}
			}
		}

		// Reconstruct the solution
		int[] solution = new int[itemMapping.size()];

		int i = T.length - 1;
		int j = T[i].length - 1;

		while (i >= 0 && j > 0) {
			barometer++;
			
			if (getTableValue(T, i, j) == getTableValue(T, i - 1, j
					- items[itemMapping.get(i)].weight)
					+ items[itemMapping.get(i)].value) {
				solution[itemMapping.get(i)]++;

				j = j - items[itemMapping.get(i)].weight;
				i = i - 1;
			} else {
				i--;
			}
		}
		
		System.out.println(barometer);

		return solution;
	}

	private static int max(Integer... vals) {
		int max = Integer.MIN_VALUE;

		for (Integer i : vals) {
			max = Math.max(max, i);
		}

		return max;
	}

	private static int getTableValue(int[][] T, int i, int j) {
		if (i < 0 || j < 0 || i > T.length || j > T[i].length) {
			// return Integer.MIN_VALUE;
			return 0;
		}

		return T[i][j];
	}

	public static void main(String[] args) {
		int capacity = 10;
		int[] mult = new int[] { 1, 1, 1, 1 };
		int[] weights = new int[] { 7, 5, 5, 4 };
		int[] values = new int[] { 49, 30, 25, 24 };

		Item[] items = new Item[values.length];
		for (int i = 0; i < mult.length; i++) {
			items[i] = new Item(i, mult[i], values[i], weights[i]);
		}

		int[] solution = dynamicKnapsackSolve(capacity, items);
		for (int i : solution) {
			System.out.print(i + " : ");
		}
	}
}
