package ProblemGenerator;

import java.util.Random;

import Job.Job.JOB_TYPE;

public class ProblemGenerator {
	
	public static final int AVERAGE = 1;
	public static final int WORST = 2;
	
	private static Random r = new Random();
	
	public static String generateProblem(int n, int type) {
		if (type == AVERAGE) {
			return generateRandomProblem(n);
		} else if (type == WORST) {
			return generateWorstCaseProblem(n);
		} 
		
		throw new RuntimeException("Type not valid");
	}

	private static String generateWorstCaseProblem(int n) {
		StringBuilder b = new StringBuilder();
		
		for (int i = 0; i <= n; i++) {
//			b.append(JOB_TYPE.COMPLEX.ordinal());
//			
//			if (i != n-1) {
//				b.append(";");
//			}
//			
			
			b.append(JOB_TYPE.SIMPLE.ordinal());
			
			b.append(";");
		}
		
		b.append(JOB_TYPE.COMPLEX.ordinal());
		
		return b.toString();
	}

	private static String generateRandomProblem(int n) {
		StringBuilder b = new StringBuilder();
		
		for (int i = 0; i < n; i++) {
			int t = r.nextInt(JOB_TYPE.values().length);
			
			b.append(t);
			
			if (i != n-1) {
				b.append(";");
			}
		}
		
		return b.toString();
	}

}
