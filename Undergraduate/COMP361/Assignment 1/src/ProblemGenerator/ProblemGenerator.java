package ProblemGenerator;

import java.util.Random;

import javax.management.RuntimeErrorException;

public class ProblemGenerator {
	
	public static final int RANDOM = 1;
	public static final int DISJOINT = 2;
	public static final int OVERLAPPING = 3;
	
	private static Random r = new Random();
	
	public static String generateProblem(int n, int type) {
		if (type == RANDOM) {
			return generateRandomProblem(n);
		} else if (type == DISJOINT) {
			return generateDisjointProblem(n);
		} else if (type == OVERLAPPING) {
			return generateOverlappingProblem(n);
		}
		
		throw new RuntimeException("Type not valid");
	}

	private static String generateOverlappingProblem(int n) {
		StringBuilder b = new StringBuilder();
		
		int currentX = 0;
		
		for (int i = 0; i < n; i++) {
			int width = r.nextInt(100);
			int height = r.nextInt(100);
			
			b.append(String.format("%d,%d,%d;", currentX, currentX + width, height));
			
			currentX += width/3 + r.nextInt(width/2 + 1);
		}
		
		return b.toString();
	}

	private static String generateDisjointProblem(int n) {
		StringBuilder b = new StringBuilder();
		
		int currentX = 0;
		
		for (int i = 0; i < n; i++) {
			int width = r.nextInt(100);
			int height = r.nextInt(100);
			
			b.append(String.format("%d,%d,%d;", currentX, currentX + width, height));
			
			currentX += width + 10;
		}
		
		return b.toString();
	}

	private static String generateRandomProblem(int n) {
		StringBuilder b = new StringBuilder();
		
		int currentX = 0;
		
		for (int i = 0; i < n; i++) {
			int width = r.nextInt(100);
			int height = r.nextInt(100);
			
			b.append(String.format("%d,%d,%d;", currentX, currentX + width, height));
			
			currentX += r.nextInt(width + 20);
		}
		
		return b.toString();
	}

}
