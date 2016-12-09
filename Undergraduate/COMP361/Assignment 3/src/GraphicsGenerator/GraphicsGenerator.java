package GraphicsGenerator;

import graphsearchknapsack0n.GraphSearchKnapsack0N;

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

import Item.Item;
import bruteforceknapsack0n.BruteForceKnapsack0N;

import com.xeiam.xchart.BitmapEncoder;
import com.xeiam.xchart.Chart;
import com.xeiam.xchart.ChartBuilder;
import com.xeiam.xchart.BitmapEncoder.BitmapFormat;

import dynamicknapsack01.DynamicKnapsack01;
import dynamicknapsack0n.DynamicKnapsack0N;

public class GraphicsGenerator {
	private static final boolean plotBruteForce = false;
	
	public static void generateGraphicsFromDataSet(String dataset)
			throws IOException {
		List<Float> xData = new ArrayList<Float>();
		// List<Float> yTheroticalData = new ArrayList<Float>();

		List<Float> yDynamicCaseAvg = new ArrayList<Float>();
		List<Float> yDynamicCaseBest = new ArrayList<Float>();
		List<Float> yDynamicCaseWorst = new ArrayList<Float>();
		List<Float> yDynamicCaseThero = new ArrayList<Float>();
		

		List<Float> yBruteForceCaseAvg = new ArrayList<Float>();
		List<Float> yBruteForceCaseThero = new ArrayList<Float>();

		List<Float> yDynamicCaseAvgN = new ArrayList<Float>();
		List<Float> yDynamicCaseBestN = new ArrayList<Float>();
		List<Float> yDynamicCaseWorstN = new ArrayList<Float>();
		List<Float> yDynamicCaseTheroN = new ArrayList<Float>();

		List<Float> yGraphSearchCaseAvg = new ArrayList<Float>();
		List<Float> yGraphSearchCaseBest = new ArrayList<Float>();
		List<Float> yGraphSearchCaseWorst = new ArrayList<Float>();
		List<Float> yGraphSearchCaseThero = new ArrayList<Float>();

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

			Item[] rand = parseProblme(problemSections[1]);
			Item[] worstGraph = parseProblme(problemSections[2]);
			Item[] worstDynamic = parseProblme(problemSections[3]);
			Item[] best = parseProblme(problemSections[4]);

			// String[] randProblem = problemSections[1].split(";");
			//
			// Item[] items = new Item[randProblem.length];
			//
			// for (int j = 0; j < randProblem.length; j++) {
			// String[] tmp = randProblem[i].split("-");
			//
			//
			// int weight = Integer.parseInt(tmp[0]);
			// int value = Integer.parseInt(tmp[1]);
			// int mult = Integer.parseInt(tmp[2]);
			//
			// items[j] = new Item(j, mult, value, weight);
			// }

			if (plotBruteForce) {
				yBruteForceCaseThero.add((float) Math.pow(2, n));
				
				BruteForceKnapsack0N.bruteForceKnapsackSolve(n, rand);
				yBruteForceCaseAvg.add((float) BruteForceKnapsack0N.barometer);
				BruteForceKnapsack0N.barometer = 0;
			}
			
			// DYNAMIC ALGORYTHM 0-1 //
			// /////////////////////

			// Best Case Dynamic
			DynamicKnapsack01.dynamicKnapsackSolve(n, best);
			yDynamicCaseBest.add((float) DynamicKnapsack01.barometer);
			DynamicKnapsack01.barometer = 0;

			// Worst Case Dynamic
			DynamicKnapsack01.dynamicKnapsackSolve(n, worstDynamic);
			yDynamicCaseWorst.add((float) DynamicKnapsack01.barometer);
			DynamicKnapsack01.barometer = 0;

			// Average Case Dynamic
			DynamicKnapsack01.dynamicKnapsackSolve(n, rand);
			yDynamicCaseAvg.add((float) DynamicKnapsack01.barometer);
			DynamicKnapsack01.barometer = 0;
			
			yDynamicCaseThero.add((float) (n * n + 2*n));


			// DYNAMIC ALGORYTHM 0-N //
			// /////////////////////

			// Best Case Dynamic
			DynamicKnapsack0N.dynamicKnapsackSolve(n, best);
			yDynamicCaseBestN.add((float) DynamicKnapsack0N.barometer);
			DynamicKnapsack0N.barometer = 0;

			// Worst Case Dynamic
			DynamicKnapsack0N.dynamicKnapsackSolve(n, worstDynamic);
			yDynamicCaseWorstN.add((float) DynamicKnapsack0N.barometer);
			DynamicKnapsack0N.barometer = 0;

			// Average Case Dynamic
			DynamicKnapsack0N.dynamicKnapsackSolve(n, rand);
			yDynamicCaseAvgN.add((float) DynamicKnapsack0N.barometer);
			DynamicKnapsack0N.barometer = 0;
			
			yDynamicCaseTheroN.add((float) (n * n + 3*n));

			// GRAPH SEARCH //
			// ////////////////

			// Best Case Graph Search
			GraphSearchKnapsack0N.graphSearchKnapsackSolve(n, best);
			yGraphSearchCaseBest.add((float) GraphSearchKnapsack0N.barometer);
			GraphSearchKnapsack0N.barometer = 0;

			// Worst Case Graph Search
			GraphSearchKnapsack0N.graphSearchKnapsackSolve(n, worstGraph);
			yGraphSearchCaseWorst.add((float) GraphSearchKnapsack0N.barometer);
			GraphSearchKnapsack0N.barometer = 0;

			// Average Case Graph Search
			GraphSearchKnapsack0N.graphSearchKnapsackSolve(n, rand);
			yGraphSearchCaseAvg.add((float) GraphSearchKnapsack0N.barometer);
			GraphSearchKnapsack0N.barometer = 0;
			
			yGraphSearchCaseThero.add((float) (n * n + n));

			System.out.println("Problem Set: " + n);

			xData.add((float) n);
		}

		if (plotBruteForce) {
			drawGraph("BruteForce", xData, new String[] {"Average", "Theoretical"}, yBruteForceCaseAvg, yBruteForceCaseThero);
		}
		
		drawGraph("Dynamic", xData,
				new String[] { "Best", "Worst", "Average", "Theoretical"}, yDynamicCaseBest,
				yDynamicCaseWorst, yDynamicCaseAvg, yDynamicCaseThero);
		
		drawGraph("Dynamic0N", xData,
				new String[] { "Best", "Worst", "Average", "Theoretical"}, yDynamicCaseBestN,
				yDynamicCaseWorstN, yDynamicCaseAvgN, yDynamicCaseTheroN);

		drawGraph("Graph0N", xData, new String[] { "Best", "Worst", "Average", "Theoretical" },
				yGraphSearchCaseBest, yGraphSearchCaseWorst,
				yGraphSearchCaseAvg, yGraphSearchCaseThero);

		drawGraph("GW-DB", xData, new String[] { "Graph Worst Case",
				"Dynamic Best Case" }, yGraphSearchCaseWorst, yDynamicCaseBestN);

		drawGraph("DynamicGraphComparason", xData, new String[] { "Dynamic",
				"Graph Search" }, yDynamicCaseAvgN, yGraphSearchCaseAvg);
	}

	public static Item[] parseProblme(String problem) {
		String[] p = problem.split(";");

		Item[] items = new Item[p.length];

		for (int j = 0; j < p.length; j++) {
			String[] tmp = p[j].split("-");

			int weight = Integer.parseInt(tmp[0]);
			int value = Integer.parseInt(tmp[1]);
			int mult = Integer.parseInt(tmp[2]);

			items[j] = new Item(j, mult, value, weight);
		}

		return items;
	}

	public static void drawGraph(String fileName, List<Float> xData,
			String[] seqNames, List<Float>... seq) {
		Chart chart = new ChartBuilder().width(800).height(800).build();
		chart.setXAxisTitle("Size Of Sequence");
		chart.setYAxisTitle("Steps");

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
