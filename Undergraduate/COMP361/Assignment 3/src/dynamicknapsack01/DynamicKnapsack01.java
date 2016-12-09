package dynamicknapsack01;
import java.util.ArrayList;
import java.util.List;

import Item.Item;


public class DynamicKnapsack01 {
	public static int barometer = 0;
	
	public static int[] dynamicKnapsackSolve(int capacity, Item[] items) {
		int[][] T = new int[items.length][capacity+1];
		
		for (int i = 0; i < T.length; i++) {
			for (int j = 0; j < T[i].length; j++) {
				barometer++;
				
				if (j-items[i].weight < 0) {
					T[i][j] = max(getTableValue(T, i-1, j), 0);
				} else {
					T[i][j] = max(getTableValue(T, i-1, j), getTableValue(T, i-1, j-items[i].weight) + items[i].value, 0);
				}
			}
		}
		
		
		int[] solution = new int[items.length];
		
		int i = T.length-1;
		int j = T[i].length-1;
		
		while (i >= 0 && j > 0) {
			barometer++;
			
			if (getTableValue(T, i, j) == getTableValue(T, i-1, j-items[i].weight) + items[i].value) {
				solution[i] = 1;
				
				j = j - items[i].weight;
				i = i-1;
			} else {
				i--;
			}
		}
		
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
			//return Integer.MIN_VALUE;
			return 0;
		}
		
		return T[i][j];
	}
	
	public static void main(String[] args) {
		int capacity = 10;
		int[] weights = new int[] {7, 5, 5 , 4};
		int[] values = new int[] {49, 30, 25, 24};
		
		//int[] solution = dynamicKnapsackSolve(capacity, weights, values);
	}
}
