package ProblemGenerator;

import java.util.Random;

import javax.management.RuntimeErrorException;

public class ProblemGenerator {
	
	public static final int AVERAGE = 1;
	public static final int WORST_GRAPH = 2;
	public static final int BEST = 3;
	public static final int WORST_DYNAMIC = 2;
	
	private static Random r = new Random();
	
	public static String generateProblem(int n, int type) {
		if (type == AVERAGE) {
			return generateRandomProblem(n);
		} else if (type == WORST_GRAPH) {
			return generateWorstCaseProblemGraphSearch(n);
		} else if (type == WORST_DYNAMIC) {
			return generateWorstCaseProblemDynamicProgramming(n);
		} else if (type == BEST) {
			return generateBestCaseProblem(n);
		}
		
		throw new RuntimeException("Type not valid");
	}

	private static String generateBestCaseProblem(int n) {
		StringBuilder b = new StringBuilder();
		
		for (int i = 0; i < n; i++) {
			int weight = n;
			int value = n;
			int mult = 1;
			
			b.append(weight).append("-").append(value).append("-").append(mult);
			
			if (i != n-1) {
				b.append(";");
			}
		}
		
		return b.toString();
	}

	private static String generateWorstCaseProblemGraphSearch(int n) {
		StringBuilder b = new StringBuilder();
		
		for (int i = 0; i < n; i++) {
			int weight = i+1;
			int value = 1;
			int mult = 1;
			
			b.append(weight).append("-").append(value).append("-").append(mult);
			
			if (i != n-1) {
				b.append(";");
			}
		}
		
		return b.toString();
	}
	
	private static String generateWorstCaseProblemDynamicProgramming(int n) {
		StringBuilder b = new StringBuilder();
		
		for (int i = 0; i < n; i++) {
			int weight = 1;
			int value = 1;
			int mult = 1;
			
			b.append(weight).append("-").append(value).append("-").append(mult);
			
			if (i != n-1) {
				b.append(";");
			}
		}
		
		return b.toString();
	}

	private static String generateRandomProblem(int n) {
		StringBuilder b = new StringBuilder();
		
		for (int i = 0; i < n; i++) {
			int weight = 1 + r.nextInt(n);
			int value = 1 + r.nextInt(n);
			int mult = 1; //+ r.nextInt(n/2);
			
			b.append(weight).append("-").append(value).append("-").append(mult);
			
			if (i != n-1) {
				b.append(";");
			}
		}
		
		return b.toString();
	}

}
