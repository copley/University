package ProblemGenerator;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

public class ProblemSetGenerator {
	private static final int MIN_SIZE = 20;
	private static final int INCREMENT = 100;
	private static final int NUM_INCREMENT = 20;
	
	public static void generateProblemSet(String setName, int maxSize) {
		// Create the directory
		File dir = new File(setName);
		dir.mkdir();
		
		for (int i = MIN_SIZE; i <= MIN_SIZE + (INCREMENT * NUM_INCREMENT); i += INCREMENT) {
			System.out.println(i);
			
			String averageCase = ProblemGenerator.generateProblem(i, ProblemGenerator.AVERAGE);
			String worstCase = ProblemGenerator.generateProblem(i, ProblemGenerator.WORST);

			try {
				PrintWriter writer = new PrintWriter(setName + "/problem-" + i, "UTF-8");
				
				writer.write(String.format("%d", i));
				writer.write(":");
				
				//writer.write(averageCase);
				writer.write(averageCase + ":");
				writer.write(worstCase);
				
				writer.close();
			} catch (FileNotFoundException e) { e.printStackTrace(); } 
			  catch (UnsupportedEncodingException e) { e.printStackTrace(); }
		}
	}
	
	public static void main(String[] args) {
		ProblemSetGenerator.generateProblemSet("problem-set", 1000);
	}
}
