package DecisionTree;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import DecisionTree.DecisionTree.Instance;
/**
 * Created by Daniel Braithwaite on 16/03/2016.
 */
public class DecisionTreeTest {
//    public static final String TRAINING_DATA = "data/part2/and.dat";
//    public static final String TEST_DATA = "data/part2/and.dat";

//    public static final String TRAINING_DATA = "data/part2/golf-training.dat";
//    public static final String TEST_DATA = "data/part2/golf-test.dat";

    public static final String TRAINING_DATA = "data/part2/hepatitis-training.dat";
    public static final String TEST_DATA = "data/part2/hepatitis-test.dat";

    public static void main(String[] args) throws IOException {
        String[] dataLines = readFile(TRAINING_DATA).split("\n");

        String[] categories = dataLines[0].split("\\s+");
        String positiveCategory = categories[0];

        ArrayList<String> attrs = new ArrayList<>(Arrays.asList(dataLines[1].split("\\s+")));

        ArrayList<Instance> instances = new ArrayList<>();

        for (int i = 2; i < dataLines.length; i++) {
            String[] d = dataLines[i].split("\\s+");
            String category = d[0];

            boolean[] a = stringsToBools(Arrays.copyOfRange(d, 1, d.length));
            instances.add(new Instance(category, attrs, a, category.equals(positiveCategory)));

//            if (!dataLines[i].replaceAll("\t", " ").trim().equals(instances.get(instances.size()-1).toString())) {
//                throw new RuntimeException("Parsing Failure:\nExpected: " + instances.get(instances.size()-1) + "\nGot: " + dataLines[i].replaceAll("\t", " "));
//            }
        }

        DecisionTree dt = new DecisionTree(attrs, instances);
        System.out.println(dt.toString());
//        System.out.println(dt.getTex());


        String[] testLines = readFile(TEST_DATA).split("\n");
        ArrayList<Instance> testInstances = new ArrayList<>();

        for (int i = 2; i < testLines.length; i++) {
            String[] d = testLines[i].split("\\s+");
            String category = d[0];

            boolean[] a = stringsToBools(Arrays.copyOfRange(d, 1, d.length));
            testInstances.add(new Instance(category, attrs, a, category.equals(positiveCategory)));
        }

//        testDecisionTree(dt, instances);
        testDecisionTree(dt, testInstances);
    }

    private static void testDecisionTree(DecisionTree dt, ArrayList<Instance> instances) {
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
            } else {
                System.out.println("Classification Error: Expected -> " + instance.getCategory() + ", Got -> " + category);
            }
        }

        System.out.println(String.format("Base Classifier: %f", ((float) baseCorrect) / ((float) instances.size())));
        System.out.println(String.format("Decision Tree Classifier: %f", ((float) dtCorrect) / ((float) instances.size())));
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
