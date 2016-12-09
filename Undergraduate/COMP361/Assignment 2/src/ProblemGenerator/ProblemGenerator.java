package ProblemGenerator;

import java.util.Random;

import javax.management.RuntimeErrorException;

public class ProblemGenerator {
	
	public static final int AVERAGE = 1;
	public static final int WORST = 2;
	public static final int BEST = 3;
	
	private static final String[] alphabet = new String[] {"G", "C", "A", "T"};
	
	private static Random r = new Random();
	
	public static String generateProblem(int n, int type) {
		if (type == AVERAGE) {
			return generateRandomProblem(n);
		} else if (type == WORST) {
			return generateWorstCaseProblem(n);
		} else if (type == BEST) {
			return generateBestCaseProblem(n);
		}
		
		throw new RuntimeException("Type not valid");
	}

	private static String generateBestCaseProblem(int n) {
		StringBuilder x = new StringBuilder();
	
		for (int i = 0; i < n; i++) {
			int j = r.nextInt(alphabet.length);

			if (j < alphabet.length) {
				x.append(alphabet[j]);
			}
		}
		
		StringBuilder b = new StringBuilder();
		
		b.append(x.toString())
		 .append(";")
		 .append(x.toString());
		
		return b.toString();
	}

	private static String generateWorstCaseProblem(int n) {
		StringBuilder b = new StringBuilder();
		
		for (int i = 0; i < n * 2; i++) {
			b.append(alphabet[0]);
		}
		
		b.append("; ");
		
		return b.toString();
	}

	private static String generateRandomProblem(int n) {
		StringBuilder x = new StringBuilder();
		StringBuilder y = new StringBuilder();
		
		for (int i = 0; i < n; i++) {
			int j = r.nextInt(alphabet.length);

			if (j < alphabet.length) {
				x.append(alphabet[j]);
			}

			j = r.nextInt(alphabet.length);

			if (j < alphabet.length) {
				y.append(alphabet[j]);
			}
		}
		
		StringBuilder b = new StringBuilder();
		
		b.append(x.toString())
		 .append(";")
		 .append(y.toString());
		
		return b.toString();
	}

}
