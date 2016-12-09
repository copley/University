package Item;
public class Item implements Comparable<Item> {
		public int mult;
		public int value;
		public int weight;
		public int id;
		private double weightToValue;
		
		public Item(int i, int m, int v, int w) {
			mult = m;
			value = v;
			weight = w;
			
			id = i;
			
			weightToValue = ((double) weight) / value;
		}
		
		public int getMult() {
			return mult;
		}
		
		public int getValue() {
			return value;
		}
		
		public int getWeight() {
			return weight;
		}
		
		public double getRatio() {
			return weightToValue;
		}

		@Override
		public int compareTo(Item arg0) {
			if (weightToValue > arg0.weightToValue) {
				return 1;
			} else if (weightToValue < arg0.weightToValue) {
				return -1;
			}
			
			return 0;
		}
	}
