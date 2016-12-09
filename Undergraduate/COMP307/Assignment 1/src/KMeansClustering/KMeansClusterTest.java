package KMeansClustering;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Daniel Braithwaite on 26/03/2016.
 */
public class KMeansClusterTest {

    public static final String TRAINING_DATA = "data/part1/iris-training.txt";
    public static final String TEST_DATA = "data/part1/iris-test.txt";

    public static void main(String[] args) {
        ArrayList<double[]> trainingData = parseNodes(TRAINING_DATA);
        Cluster[] clusters = Cluster.kMeansCluster(3, trainingData);

        for (int i = 0; i < clusters.length; i++) {
            System.out.println(String.format("Group %d: %d", i+1, clusters[i].getFeatures().size() ));
        }
    }

    private static ArrayList<double[]> parseNodes(String file) {
        ArrayList<double[]> nodes = new ArrayList<>();

        try {
            String[] trainingLines = readFile(file).split("\n");

            for (String line : trainingLines) {
                String[] sections = line.split("  ");
                double[] f = new double[] {Double.parseDouble(sections[0]),
                        Double.parseDouble(sections[1]),
                        Double.parseDouble(sections[2]),
                        Double.parseDouble(sections[3])};

                String c = sections[4];
                nodes.add(f);
            }
        } catch (IOException e) { e.printStackTrace(); }

        return nodes;
    }

    private static String readFile(String fileName) throws IOException {
        FileReader fileReader = new FileReader(fileName);
        BufferedReader bufferedReader = new BufferedReader(fileReader);

        String data = "";
        String line = "";

        while((line = bufferedReader.readLine()) != null) {
            data += line + "\n";
        }

        return data;
    }
}
