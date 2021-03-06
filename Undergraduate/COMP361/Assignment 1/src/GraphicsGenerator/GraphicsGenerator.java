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

import ManhattanSkyline.ManhattanSkyline;

import com.xeiam.xchart.BitmapEncoder;
import com.xeiam.xchart.Chart;
import com.xeiam.xchart.ChartBuilder;
import com.xeiam.xchart.BitmapEncoder.BitmapFormat;

public class GraphicsGenerator {
	public static void generateGraphicsFromDataSet(String dataset) throws IOException {
		List<Float> xData = new ArrayList<Float>();
		List<Float> yLogData = new ArrayList<Float>();
		List<Float> yRandomData = new ArrayList<Float>();
		List<Float> yDisjointData = new ArrayList<Float>();
		List<Float> yOverlappingData = new ArrayList<Float>();
		
		// Get the problem files and sort them by the problem size
		File problemSet = new File(dataset);
		ArrayList<File> problems = new ArrayList<>(Arrays.asList(problemSet.listFiles()));
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
			
			System.out.println("Problem Set: " + n);
			
			xData.add((float) n);
			yLogData.add((float) (n * (Math.log(n) / Math.log(2))));
			yRandomData.add((float) runProblem(problemSections[1]));
			yDisjointData.add((float) runProblem(problemSections[2]));
			yOverlappingData.add((float) runProblem(problemSections[3]));
		}
		
		Chart chart = new ChartBuilder().width(800).height(800).build();
		chart.setXAxisTitle("Num Buildings");
		chart.setYAxisTitle("Steps");
		chart.addSeries("Theroetical", xData, yLogData);
		chart.addSeries("Random", xData, yRandomData);
		chart.addSeries("Disjoint", xData, yDisjointData);
		chart.addSeries("Overlapping", xData, yOverlappingData);
		
		try {
			System.out.println("Saving Graph");
			BitmapEncoder.saveBitmap(chart, "data", BitmapFormat.PNG);
			System.out.println("Graph Saved");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static int runProblem(String problem) {
		String[] problemSections = problem.split(";");
		
		ArrayList<float[]> problemData = new ArrayList<float[]>();
		for (int j = 0; j < problemSections.length; j++) {
			String[] dataPoint  = problemSections[j].split(",");
			problemData.add(new float[] {Float.parseFloat(dataPoint[0]), Float.parseFloat(dataPoint[1]), Float.parseFloat(dataPoint[2])});
		}
		
		ArrayList<float[]> skyline = ManhattanSkyline.constructSkyline(problemData);
		
		int b = ManhattanSkyline.barometer;
		ManhattanSkyline.barometer = 0;
		return b;
	}
	
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
