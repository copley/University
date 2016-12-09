package DynamicStringAlign;

public class DynamicStringAlign {
	private static final int SPACE_X = 0;
	private static final int SPACE_Y = 1;
	private static final int MATCH = 2;
	private static final int EMPTY = 3;
	
	public static int barometer = 0;
	
	public static String[] stringAlign(String x, String y) {
		Cell[][] alignmentTable = new Cell[x.length()+1][y.length()+1];
		
		alignmentTable[0][0] = new Cell();
		alignmentTable[0][0].type = EMPTY;
		barometer++;
		
		for (int i = 1; i < alignmentTable.length; i++) {
			barometer++;
			
			alignmentTable[i][0] = new Cell();
			alignmentTable[i][0].cost = -2 * i;
			alignmentTable[i][0].type = SPACE_X;
			
			alignmentTable[i][0].x = 0;
			alignmentTable[i][0].y = i-1;
			
			alignmentTable[i][0].previous = alignmentTable[i-1][0];
		}
		
		for (int j = 1; j < alignmentTable[0].length; j++) {
			barometer++;
			
			alignmentTable[0][j] = new Cell();
			alignmentTable[0][j].cost = -2 * j;
			alignmentTable[0][j].type = SPACE_Y;
			
			alignmentTable[0][j].x = j-1;
			alignmentTable[0][j].y = 0;
			
			alignmentTable[0][j].previous = alignmentTable[0][j-1];
		}
		
		// Fill out the table
		for (int i = 1; i < alignmentTable.length; i++) {
			for (int j = 1; j < alignmentTable[i].length; j++) {
				barometer++;
				
				// Use recurance to calculate the best cost for current square
				int cost = max((x.charAt(i-1) == y.charAt(j-1) ? 1 : -1) + alignmentTable[i-1][j-1].cost,
						-2 + alignmentTable[i-1][j].cost,
						-2 + alignmentTable[i][j-1].cost);
				
				Cell current = new Cell();
				current.x = i-1;
				current.y = j-1;
				current.cost = cost;
				
				if (current.cost == max((x.charAt(i-1) == y.charAt(j-1) ? 1 : -1) + alignmentTable[i-1][j-1].cost)) {
					current.previous = alignmentTable[i-1][j-1];
					current.type = MATCH;
				} else if (current.cost == -2 + alignmentTable[i-1][j].cost) {
					current.previous = alignmentTable[i-1][j];
					current.type = SPACE_Y;
				} else if (current.cost == -2 + alignmentTable[i][j-1].cost) {
					current.previous = alignmentTable[i][j-1];
					current.type = SPACE_X;
				}
				
				alignmentTable[i][j] = current;
			}
		}
		
		// Compute the alignments, note that if the type is EMPTY
		// then we dont want to do anything as the cell represents the 
		// cost of aligning two empty strings
		String alignedX = "";
		String alignedY = "";
		Cell current = alignmentTable[x.length()][y.length()];//alignmentTable[0][0];
		
		while (current != null) {
			barometer++;
			
			if (current.type == MATCH) {
				alignedX = x.charAt(current.x) + alignedX;
				alignedY = y.charAt(current.y) + alignedY;
			} else if (current.type == SPACE_X) {
				alignedX = " " + alignedX;
				alignedY = y.charAt(current.y) + alignedY;
			} else if (current.type == SPACE_Y) {
				alignedX = x.charAt(current.x) + alignedX;
				alignedY = " " + alignedY;
			}
			
			current = current.previous;
		}
		
		return new String[] {alignedX, alignedY};
	}
	
	private static int max(int... nums) {
		int m = Integer.MIN_VALUE;
		for (int n : nums) {
			if (n > m) {
				m = n;
			}
		}
		
		return m;
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
	
	
	private static class Cell {
		public int type;
		public int x;
		public int y;
		public int cost;
		public Cell previous;
	}
	
	public static void main(String[] args) {
		String x = "GATCGGCAT";
		String y = "CAATGTGAATC";
		
		String[] aligned = stringAlign(x, y);
		int cost = alignmentCost(aligned[0], aligned[1]);
		
		System.out.println("Strings:");
		System.out.println("X: " + aligned[0]);
		System.out.println("Y: " + aligned[1]);
		System.out.println("COST: " + cost);
	}
}
