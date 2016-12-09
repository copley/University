package DecisionTree;

import DecisionTree.DecisionTree.Instance;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by Daniel Braithwaite on 30/03/2016.
 */
public class DecisionTreeTestRuns {
    public static final int NUM_SETS = 10;
    public static final String TRAINING_DATA = "data/part2/hepatitis-training.dat";
    public static final String TEST_DATA = "data/part2/hepatitis-test.dat";

    public static void main(String[] args) throws IOException {
        double sumAccuracy = 0;

        for (int s = 1; s <= NUM_SETS; s++) {
            String num = String.format("%d", s);
            while (num.length() < String.format("%d", NUM_SETS).length()) {
                num = "0" + num;
            }

            String testFile = "data/part2/hepatitis-test-run" + num + ".dat";
            String trainingFile = "data/part2/hepatitis-training-run" + num + ".dat";

            String[] dataLines = readFile(trainingFile).split("\n");

            String[] categories = dataLines[0].split("\\s+");
            String positiveCategory = categories[0];

            ArrayList<String> attrs = new ArrayList<String>(Arrays.asList(dataLines[1].split("\\s+")));

            ArrayList<Instance> instances = new ArrayList<>();

            for (int i = 2; i < dataLines.length; i++) {
                String[] d = dataLines[i].split("\\s+");
                String category = d[0];

                boolean[] a = stringsToBools(Arrays.copyOfRange(d, 1, d.length));
                instances.add(new Instance(category, attrs, a, category.equals(positiveCategory)));
            }

            DecisionTree dt = new DecisionTree(attrs, instances);
            //System.out.println(dt.toString());


            String[] testLines = readFile(testFile).split("\n");
            ArrayList<Instance> testInstances = new ArrayList<>();

            for (int i = 2; i < testLines.length; i++) {
                String[] d = testLines[i].split("\\s+");
                String category = d[0];

                boolean[] a = stringsToBools(Arrays.copyOfRange(d, 1, d.length));
                testInstances.add(new Instance(category, attrs, a, category.equals(positiveCategory)));
            }

            double correct = testDecisionTree(dt, testInstances);
            sumAccuracy += correct;
        }

        double average = sumAccuracy/NUM_SETS;
        System.out.println(String.format("\nAverage Accuracy: %f", average));
    }

    private static double testDecisionTree(DecisionTree dt, ArrayList<Instance> instances) {
        String baseClassifier = dt.getBaseCategory();

        int baseCorrect = 0;
        int dtCorrect = 0;

        for (Instance instance : instances) {
            String category = dt.classify(instance);

            if (instance.getCategory().equals(baseClassifier)) {
                baseCorrect++;
            }

            if(instance.getCategory().equals(category)) {
                dtCorrect++;
            }
        }

        System.out.println(String.format("Base Classifier: %f", ((float) baseCorrect)/((float) instances.size())));
        System.out.println(String.format("Decision Tree Classifier: %f", ((float) dtCorrect)/((float) instances.size())));

        return ((double) dtCorrect)/((double) instances.size());
    }

    private static boolean[] stringsToBools(String[] strings) {
        boolean[] res = new boolean[strings.length];

        for (int i = 0; i < strings.length; i++) {
            res[i] = Boolean.parseBoolean(strings[i]);
        }

        return res;
    }

    private static String readFile(String fileName) throws IOException {
        FileReader fileReader = new FileReader(fileName);
        BufferedReader bufferedReader = new BufferedReader(fileReader);

        String data = "";
        String line = "";

        while((line = bufferedReader.readLine()) != null) {
            data += line + "\n";
        }

        return data.trim();
    }
}
