package ProblemGenerator;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

public class ProblemSetGenerator {
	public static void generateProblemSet(String setName, int maxSize) {
		// Create the directory
		File dir = new File(setName);
		dir.mkdir();
		
		for (int i = 10000; i <= maxSize; i += 10000) {
			System.out.println(i);
			
			String randProblem = ProblemGenerator.generateProblem(i, ProblemGenerator.RANDOM);
			String disjointProblem = ProblemGenerator.generateProblem(i, ProblemGenerator.DISJOINT);
			String overlappingProblem = ProblemGenerator.generateProblem(i, ProblemGenerator.OVERLAPPING);
			
			try {
				PrintWriter writer = new PrintWriter(setName + "/problem-" + i, "UTF-8");
				
				writer.write(String.format("%d", i));
				writer.write(":");
				
				writer.write(randProblem + ":");
				writer.write(disjointProblem + ":");
				writer.write(overlappingProblem);
				
				writer.close();
			} catch (FileNotFoundException e) { e.printStackTrace(); } 
			  catch (UnsupportedEncodingException e) { e.printStackTrace(); }
		}
	}
	
	public static void main(String[] args) {
		ProblemSetGenerator.generateProblemSet("problem-set", 100000);
	}
}
