package GraphicsGenerator;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import ApproximationJobSchedule.ApproximationJobSchedule;
import Job.Job.JOB_TYPE;
import OptimalJobSchedule.OptimalJobSchedule;
import ProbabilisticJobSchedule.ProbabilisticJobSchedule;

import com.xeiam.xchart.BitmapEncoder;
import com.xeiam.xchart.Chart;
import com.xeiam.xchart.ChartBuilder;
import com.xeiam.xchart.BitmapEncoder.BitmapFormat;

public class GraphicsGenerator {

	public static void generateGraphicsFromDataSet(String dataset)
			throws IOException {
		List<Float> xData = new ArrayList<Float>();
		// List<Float> yTheroticalData = new ArrayList<Float>();

		List<Float> yAproxCaseAvg = new ArrayList<Float>();
		List<Float> yAproxCaseAvg10 = new ArrayList<Float>();
		List<Float> yAproxCaseAvg20 = new ArrayList<Float>();
		List<Float> yAproxCaseWorst = new ArrayList<Float>();
		
		List<Float> yApproxComplexity = new ArrayList<>();

		List<Float> yProbCaseAvg = new ArrayList<Float>();
		List<Float> yProbCaseAvg10 = new ArrayList<Float>();
		List<Float> yProbCaseAvg20 = new ArrayList<Float>();
		List<Float> yProbCaseWorst = new ArrayList<Float>();
		
		List<Float> yProbComplexity = new ArrayList<>();

		// Get the problem files and sort them by the problem size
		File problemSet = new File(dataset);
		ArrayList<File> problems = new ArrayList<>(Arrays.asList(problemSet
				.listFiles()));
		Collections.sort(problems, new Comparator<File>() {

			@Override
			public int compare(File f1, File f2) {
				int n1 = Integer.parseInt(f1.getName().split("-")[1]);
				int n2 = Integer.parseInt(f2.getName().split("-")[1]);

				return n1 - n2;
			}

		});

		System.out.println("Generating Graph...");

		for (int i = 0; i < problems.size(); i++) {
			File problem = problems.get(i);
			BufferedReader in = new BufferedReader(new FileReader(problem));
			StringBuilder b = new StringBuilder();
			String line = null;

			while ((line = in.readLine()) != null) {
				b.append(line);
			}

			String problemString = b.toString();
			String[] problemSections = problemString.split(":");

			int n = Integer.parseInt(problemSections[0]);

			JOB_TYPE[] rand = parseProblme(problemSections[1]);
			JOB_TYPE[] worst = parseProblme(problemSections[2]);

			int m = (int) (n);
			System.out.println(m);

			// yAproxCaseThero.add((float) 1.5);
			// yProbCaseThero.add((float) 1.5);

			// APPROX //

			yAproxCaseAvg.add((float) (ApproximationJobSchedule.scheduleJobs(5,
					rand) / OptimalJobSchedule.scheduleJobs(5, rand)));
			
			
			yAproxCaseAvg10.add((float) ((float) ApproximationJobSchedule.scheduleJobs(10,
					rand) / (float) OptimalJobSchedule.scheduleJobs(10, rand)));
			
			yAproxCaseAvg20.add((float) ((float) ApproximationJobSchedule.scheduleJobs(20,
					rand) / (float) OptimalJobSchedule.scheduleJobs(20, rand)));
			
			yApproxComplexity.add((float) ApproximationJobSchedule.BAROMETER);
			System.out.println(ApproximationJobSchedule.BAROMETER);
			
			yAproxCaseWorst.add((float) ( (float)ApproximationJobSchedule.scheduleJobs(n,
					worst) / (float) OptimalJobSchedule.scheduleJobs(n, worst)));
			
			
			// PROB //
			
			yProbCaseAvg.add((float) (ProbabilisticJobSchedule.scheduleJobs(5,
					rand) / (float) OptimalJobSchedule.scheduleJobs(5, rand)));
			
			yProbCaseAvg10.add((float) ((float) ProbabilisticJobSchedule.scheduleJobs(10,
					rand) / (float) OptimalJobSchedule.scheduleJobs(10, rand)));
			
			yProbCaseAvg20.add((float) ((float) ProbabilisticJobSchedule.scheduleJobs(20,
					rand) / (float) OptimalJobSchedule.scheduleJobs(20, rand)));
			
			yProbComplexity.add((float) ProbabilisticJobSchedule.BAROMETER);
			System.out.println(ProbabilisticJobSchedule.BAROMETER);
			
			yProbCaseWorst.add((float) ( (float)ProbabilisticJobSchedule.scheduleJobs(n,
					worst) / (float) OptimalJobSchedule.scheduleJobs(n, worst)));
			

			System.out.println("Problem Set: " + n);

			xData.add((float) n);
		}

		drawGraph("Approx", "Ratio", xData, new String[] { "Avg (5)", "Avg (10)", "Avg (20)", "Worst" }, yAproxCaseAvg, yAproxCaseAvg10, yAproxCaseAvg20, yAproxCaseWorst);
		drawGraph("Prob", "Ratio", xData, new String[] { "Avg (5)", "Avg (10)", "Avg (20)"}, yProbCaseAvg, yProbCaseAvg10, yProbCaseAvg20);
		
		drawGraph("CompareRatios", "Ratio", xData, new String[] { "Approx (20)", "Prob (20)"}, yAproxCaseAvg20, yProbCaseAvg20);
		drawGraph("CompareComplexity", "Steps", xData, new String[] { "Approx", "Prob"}, yApproxComplexity, yProbComplexity);
	}

	public static JOB_TYPE[] parseProblme(String problem) {
		String[] p = problem.split(";");

		JOB_TYPE[] jobs = new JOB_TYPE[p.length];

		for (int j = 0; j < p.length; j++) {
			int t = Integer.parseInt(p[j]);

			jobs[j] = JOB_TYPE.values()[t];
		}

		return jobs;
	}

	public static void drawGraph(String fileName, String xaxis, List<Float> xData,
			String[] seqNames, List<Float>... seq) {
		Chart chart = new ChartBuilder().width(800).height(800).build();
		chart.setXAxisTitle("Size Of Sequence");
		chart.setYAxisTitle(xaxis);

		for (int i = 0; i < seqNames.length; i++) {
			chart.addSeries(seqNames[i], xData, seq[i]);
		}

		// chart.addSeries("Brute Force", xData, yBruteForceCase);
		// chart.addSeries("Dynamic", xData, yDynamicCase);
		// chart.addSeries("Graph Search", xData, yGraphSearchCase);

		try {
			System.out.println("Saving Graph");
			BitmapEncoder.saveBitmap(chart, fileName, BitmapFormat.PNG);
			System.out.println("Graph Saved");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// public static int runDynamicProblem(String problem) {
	// String[] problemSections = problem.split(";");
	//
	// String[] aligned = DynamicKnapsack0N.dynamicKnapsackSolve(capacity, mult,
	// weights, value)
	//
	// int b = DynamicStringAlign.barometer;
	// DynamicStringAlign.barometer = 0;
	// return b;
	// }

	public static void main(String[] args) {
		try {
			generateGraphicsFromDataSet("problem-set");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
