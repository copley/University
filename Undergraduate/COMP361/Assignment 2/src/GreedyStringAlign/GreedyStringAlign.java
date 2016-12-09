package GreedyStringAlign;

public class GreedyStringAlign {
	public static String[] stringAlign(String x, String y) {
		String alignedX = "";
		String alignedY = "";
		
		// x string pointer
		int i = 0;
		// y string pointer
		int j = 0;
		
		while (i < x.length() || j < y.length()) {
			
			// If we have reached the end of x then add a space to x for
			// every character we have left to add to y
			if (i == x.length()) {
				alignedX += " ";
				alignedY += y.substring(j, j+1);
				
				j++;
				continue;
			} else if (j == y.length()) {
				alignedY += " ";
				alignedX += x.substring(i, i+1);
				
				i++;
				continue;
			}
			
			else if(compareStringIndexs(x, i, y, j+1) && compareStringIndexs(x, i+1, y, j+2)) {
				alignedX += " ";
				alignedY += y.substring(j, j+1);
				
				j++;
				
				continue;
			} else if (compareStringIndexs(x, i+1, y, j) && compareStringIndexs(x, i+2, y, j+1)) {
				alignedY += " ";
				alignedX += x.substring(i, i+1);
				
				i++;
				
				continue;
			} else if (compareStringIndexs(x, i+2, y, j+2)) {
				alignedX += x.substring(i, i+1);
				alignedY += y.substring(j, j+1);
				
				i++;
				j++;
				
				continue;
			} else if (!compareStringIndexs(x, i, y, j) && !compareStringIndexs(x, i+1, y, j+1)) {
				if (compareStringIndexs(x, i, y, j+1)) {
					alignedX += " ";
					alignedY += y.substring(j, j+1);
					
					j++;
					
					continue;
				} else if (compareStringIndexs(x, i+1, y, j)) {
					alignedY += " ";
					alignedX += x.substring(i, i+1);
					
					i++;
					
					continue;
				}
			} 
//			else {
//				alignedX += x.substring(i, i+1);
//				alignedY += y.substring(j, j+1);
//				
//				i++;
//				j++;
//			}
			
			alignedX += x.substring(i, i+1);
			alignedY += y.substring(j, j+1);
			
			i++;
			j++;
		}
		
		return new String[] {alignedX, alignedY};
	}
	
	private static boolean compareStringIndexs(String s1, int i1, String s2, int i2) {
		if (i1 >= s1.length() || i2 >= s2.length()) {
			return false;
		}
		
		return s1.charAt(i1) == s2.charAt(i2);
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
