package graphsearchknapsack0n;

import java.io.ObjectInputStream.GetField;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

import Item.Item;

public class GraphSearchKnapsack0N {

	public static int barometer = 0;

	public static int[] graphSearchKnapsackSolve(int capacity, Item[] items) {

		// List<Item> itm = new ArrayList<>();
		//
		// for (int i = 0, j = 1, k = 0; k < items.length; i++, j++) {
		// barometer++;
		// Item tmp = new Item(i, 1, items[k].value, items[k].weight);
		// itm.add(tmp);
		//
		// if (j == items[k].mult) {
		// j = 0;
		// k++;
		// }
		// }
	
		Map<Integer, Integer> itemMapping = new HashMap<>();

		for (int i = 0, j = 1, k = 0; k < items.length; i++, j++) {
			barometer++;
			itemMapping.put(i, k);

			if (j == items[k].mult) {
				j = 0;
				k++;
			}
		}

		Node root = new Node();
		Node bestNode = root;

		Map<String, Node> nodes = new HashMap<>();
		List<Node> fringe = new ArrayList<Node>();
		fringe.add(root);
		nodes.put("0,0", root);

		while (fringe.size() != 0) {
			barometer++;

			Node current = fringe.remove(fringe.size() - 1);

			if (current.i + 1 > itemMapping.size()) {
				continue;
			}

			boolean notTakeNew = getNode(nodes, current.i + 1, current.j,
					current.value, current);

			Node notTake = nodes.get((current.i + 1) + "," + current.j);

			if (notTakeNew) {
				fringe.add(notTake);
			}

			if (current.j + items[itemMapping.get(current.i)].weight <= capacity) {
				// Create the node for if we do take the item
				boolean takeNew = getNode(nodes, current.i + 1,
						current.j + items[itemMapping.get(current.i)].weight, current.value
								+ items[itemMapping.get(current.i)].value, current);
				Node take = nodes.get((current.i + 1) + ","
						+ (current.j + items[itemMapping.get(current.i)].weight));

				if (take.value > bestNode.value) {
					bestNode = take;
				}

				if (takeNew) {
					fringe.add(take);
				}
			}
		}

		int[] solution = new int[items.length];

		Node current = bestNode;
		while (current.prev != null) {
			barometer++;
			Node p = current.prev;

			if (current.j != p.j) {
				solution[itemMapping.get(p.i)]++;
			}

			current = p;
		}

		return solution;
	}

	public static boolean getNode(Map<String, Node> nodes, int i, int j,
			int value, Node current) {
		if (nodes.containsKey(i + "," + j)) {
			Node n = nodes.get(i + "," + j);
			if (n.value < value) {
				n.value = value;
				n.prev = current;
			}

			return false;
		}

		Node n = new Node();
		n.i = i;
		n.j = j;
		n.value = value;
		n.prev = current;

		nodes.put(i + "," + j, n);

		return true;
	}

	private static class Node {
		public int i;
		public int j;
		public int value;
		public Node prev;

		@Override
		public String toString() {
			return "(" + i + "," + j + ")";
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + i;
			result = prime * result + j;
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			Node other = (Node) obj;
			if (i != other.i)
				return false;
			if (j != other.j)
				return false;
			return true;
		}
	}

	public static void main(String[] args) {
		int capacity = 10;
		int[] m = new int[] { 1, 1, 1, 1 };
		int[] weights = new int[] { 7, 5, 5, 4 };
		int[] values = new int[] { 49, 30, 25, 24 };

		Item[] items = new Item[values.length];
		for (int i = 0; i < m.length; i++) {
			items[i] = new Item(i, m[i], values[i], weights[i]);
		}

		int[] solution = graphSearchKnapsackSolve(capacity, items);
		// System.out.println(solution);
		for (int i : solution) {
			System.out.print(i + " : ");
		}
	}
}
