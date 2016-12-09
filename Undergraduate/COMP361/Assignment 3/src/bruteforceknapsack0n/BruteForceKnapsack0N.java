package bruteforceknapsack0n;

import java.io.ObjectInputStream.GetField;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import Item.Item;

public class BruteForceKnapsack0N {
	
	public static int barometer = 0;
	
	public static List<Integer> bruteForceKnapsackSolve(int capcacity, Item[] items) {
		
		boolean running = true;
		
		int[] bestSolution = null;
		int bestValue = Integer.MIN_VALUE;
		
		int[] workingSolution = new int[items.length];
		
		while (true) {
			barometer++;
			workingSolution[0]++;
			
			// Manage overflow
			for (int i = 0; i < workingSolution.length; i++) {
				if (workingSolution[i] > items[i].mult && i != workingSolution.length-1) {
					
					workingSolution[i] = 0;
					workingSolution[i+1]++;
				} else {
					break;
				}
			}
			
			// Check to see if we have reached the end
			if (workingSolution[workingSolution.length-1] > items[workingSolution.length-1].mult) {
				break;
			}
			
			int solutionWeight = getSolutionWeight(items, workingSolution);
			
			if (solutionWeight <= capcacity) {
				int solutionValue = getSolutionValue(items, workingSolution);
				
				if (solutionValue > bestValue) {
					bestValue = solutionValue;
					bestSolution = Arrays.copyOf(workingSolution, workingSolution.length);
				}
			}
		}
		
		System.out.println(bestValue);
		
		return null;//new ArrayList<Integer>(bestSolution);
	}
	
	private static int getSolutionWeight(Item[] items, int[] solution) {
		int wieght = 0;
		
		for (int i = 0; i < items.length; i++) {
			wieght += items[i].weight * solution[i];
		}
		
		return wieght;
	}
	
	private static int getSolutionValue(Item[] items, int[] solution) {
		int value = 0;
		
		for (int i = 0; i < items.length; i++) {
			value += items[i].value * solution[i];
		}
		
		return value;
	}
	
	public static void main(String[] args) {
		int capacity = 10;
		int[] m = new int[] {1, 1, 1, 1};
		int[] weights = new int[] {7, 5, 5 , 4};
		int[] values = new int[] {49, 30, 25, 24};
		
		Item[] items = new Item[values.length];
		for (int i = 0; i < m.length; i++) {
			items[i] = new Item(i, m[i], values[i], weights[i]);
		}
		
		List<Integer> solution = bruteForceKnapsackSolve(capacity, items);
	}
}
