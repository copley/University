package NearestNeighbour;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Daniel Braithwaite on 9/03/2016.
 */
public class NearestNeighbourTest {
    public static final String TRAINING_DATA = "data/part1/iris-training.txt";
    public static final String TEST_DATA = "data/part1/iris-test.txt";

    public static void main(String[] args) {
        double[] ranges = new double[] {7.3 - 4.3, 4.4 - 2.0, 6.9 - 1.0, 2.5 - 0.1};
        NearestNeighbourClassifier classifier = new NearestNeighbourClassifier(ranges, 1);

        ArrayList<NearestNeighbourNode> trainingData = parseNodes(TRAINING_DATA);
        classifier.train(trainingData);

        System.out.println("Testing K1");
        double aK1 = testClassifier(classifier, TEST_DATA, false);

        System.out.println("\nTesting K3");
        classifier.setK(3);
        double aK3 = testClassifier(classifier, TEST_DATA, false);

        System.out.println("Accuricy (K1): " + aK1);
        System.out.println("Accuricy (K3): " + aK3);
    }

    private static double testClassifier(NearestNeighbourClassifier classifier, String testFile, boolean texTable) {

        String tex = "";

        int correct = 0;
        ArrayList<NearestNeighbourNode> testData = parseNodes(testFile);
        for (NearestNeighbourNode n : testData) {
            tex += n.getTex() + " & ";

            String category = classifier.classify(n.getFeatures());
            if (category.equals(n.getCategory())) {
                correct++;
                tex += category + "\\\\\n";
            } else {
                tex += "\\textbf{" + category + "}\\\\\n";
                System.out.println("Classification Error: Expected -> " + n.getCategory() + ", Got -> " + category);
            }


        }

        if (texTable) {
            System.out.println(tex);
        }

        return ((double) correct)/((double) testData.size());
    }

    private static ArrayList<NearestNeighbourNode> parseNodes(String file) {
        ArrayList<NearestNeighbourNode> nodes = new ArrayList<>();

        try {
            String[] trainingLines = readFile(file).split("\n");

            for (String line : trainingLines) {
                String[] sections = line.split("  ");
                double[] f = new double[] {Double.parseDouble(sections[0]),
                        Double.parseDouble(sections[1]),
                        Double.parseDouble(sections[2]),
                        Double.parseDouble(sections[3])};

                String c = sections[4];
                nodes.add(new NearestNeighbourNode(f, c));
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
