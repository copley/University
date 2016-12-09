package NaiveBayes;

import javafx.beans.binding.StringBinding;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by danielbraithwt on 5/9/16.
 */
public class NaiveBayesClassifier {

    public static void main(String[] args) throws IOException {
        NaiveBayesClassifier classifier = createClassifier();

        List<Feature> training = readFeatures("data/part1/spamLabelled.dat");
        List<Feature> testing = readFeatures("data/part1/spamUnlabelled.dat");

        System.out.println("Accuracy On Training Data");
        System.out.println(testClassifier(classifier, training));
        System.out.println("\nTesting Data:");
        runClassifier(classifier, testing);
    }

    public static double testClassifier(NaiveBayesClassifier classifier, List<Feature> features) {
        double count = 0;

        for (Feature feature : features) {
            FeatureClass classification = classifier.classify(feature);
            if (classification == feature.getFeatureClass()) {
                count++;
            }
        }

        return ((double) count) / ((double) features.size());
    }

    public static void runClassifier(NaiveBayesClassifier classifier, List<Feature> features) {
        StringBuilder b = new StringBuilder();
        b.append("\\begin{tabular}{|c|c|c|}\n");
        b.append("Class & Spam Prob & Non Spam Prob\\\\\n");
        b.append("\\hline\n");

        for (Feature feature : features) {
            FeatureClass classification = classifier.classify(feature);
            System.out.println(classification.toString());
            b.append(classifier.getClassificationString() + "\\\\\n");
        }


        b.append("\\end{tabular}");
        //System.out.println(b.toString());
    }

    private int n;
    private int numSpam;
    private int numNonSpam;
    private List<Integer[]> spamCount;
    private List<Integer[]> nonSpamCount;
    private String classificationString;

    private NaiveBayesClassifier(List<Feature> training) {
        spamCount = new ArrayList<>();
        nonSpamCount = new ArrayList<>();
        for (int i = 0; i < 12; i++) {
            spamCount.add(new Integer[] {1, 1});
            nonSpamCount.add(new Integer[] {1, 1});
        }

        for (Feature feature : training) {
            List<Integer[]> count = null;
            if (feature.getFeatureClass() == FeatureClass.SPAM) {
                numSpam++;
                count = spamCount;
            } else {
                numNonSpam++;
                count = nonSpamCount;
            }

            for (int i = 0; i < feature.getFeatures().size(); i++) {
//                count.set(i, count.get(i) + feature.getFeatures().get(i));
                Integer[] t = count.get(i);
                t[0] += (1 - feature.getFeatures().get(i));
                t[1] += feature.getFeatures().get(i);
            }
        }

        n = training.size();

        //System.out.print(getProbTable());
    }

    public String getClassificationString() {
        return classificationString;
    }

    public String getProbTable() {
        StringBuilder b = new StringBuilder();

        b.append("\\begin{tabular}{|c|c|c|}\n");
        b.append("\\hline\n");
        b.append("Feature & Spam & Non Spam\\\\");
        b.append("\\hline");

        for (int i = 0; i < spamCount.size(); i++) {
            b.append("$F_" + i + "$");

            b.append(" & ");
            b.append(computeProbility(1, numSpam + 1, spamCount.get(i)));

            b.append(" & ");
            b.append(computeProbility(1, numNonSpam + 1, nonSpamCount.get(i)));
            b.append("\\\\\n");
        }

        b.append("\\\\\n");
        b.append("\\hline\n");
        b.append("\\end{tabular}\n");

        return b.toString();
    }

    public FeatureClass classify(Feature feature) {
        double probSpam = 1;
        double probNonSpam = 1;

        for (int i = 0; i < feature.getFeatures().size(); i++) {
            probSpam *= computeProbility(feature.getFeatures().get(i), numSpam + 1, spamCount.get(i));
            probNonSpam *= computeProbility(feature.getFeatures().get(i), numNonSpam + 1, nonSpamCount.get(i));
        }

        probSpam *= ((double) numSpam+1) / ((double) n+2);
        probNonSpam *= ((double) numNonSpam+1) / ((double) n+2);

        FeatureClass featureClass = probSpam > probNonSpam ? FeatureClass.SPAM : FeatureClass.NOT_SPAM;

        classificationString = String.format("%s & %f & %f", featureClass.toString(), probSpam, probNonSpam);
        return featureClass;
    }

    private static double computeProbility(int f, int n, Integer[] counts) {
        int o = counts[f];

        return ((double) o) / ((double) n);
    }

    private enum FeatureClass {
        SPAM("Spam"),
        NOT_SPAM("Not Spam"),
        UNKNOWN("Unknown");

        String name;

        FeatureClass(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return name;
        }
    }

    private static class Feature {
        private List<Integer> features;
        private FeatureClass featureClass;

        public Feature(List<Integer> features, FeatureClass c) {
            this(features);
            this.featureClass = c;
        }

        public Feature(List<Integer> features) {
            this.features = features;
            this.featureClass = FeatureClass.UNKNOWN;
        }

        public FeatureClass getFeatureClass() {
            return featureClass;
        }

        public List<Integer> getFeatures() {
            return features;
        }
    }

    public static NaiveBayesClassifier createClassifier() throws IOException {
        List<Feature> training = readFeatures("data/part1/spamLabelled.dat");
        //List<Feature> testing = readFeatures("data/part1/spamUnlabelled.dat");

        return new NaiveBayesClassifier(training);
    }


    private static List<Feature> readFeatures(String filename) throws IOException {
        String file = readFile(filename);
        String[] lines = file.split("\n");

        List<Feature> features = new ArrayList<>();

        for (String line : lines) {
            String[] entries = line.replaceAll("\\s+", " ").trim().split(" ");
            List<Integer> f = new ArrayList<>();

            for (int i = 0; i < 12; i++) {
                f.add(Integer.parseInt(entries[i]));
            }

            FeatureClass featureClass = FeatureClass.UNKNOWN;
            if (entries.length == 13) {
                featureClass = (entries[12].equals("0")) ? FeatureClass.NOT_SPAM : FeatureClass.SPAM;
            }

            features.add(new Feature(f, featureClass));
        }

        return features;
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
