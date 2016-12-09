package Perceptron;
import Perceptron.PBMImage;

import java.util.Random;

/**
 * Created by Daniel Braithwaite on 22/03/2016.
 */
public class Perceptron {

    private static Random r = new Random(123);

    Feature[] features;
    double[] weights;

    public Perceptron(int f, int width, int height) {
        features = new Feature[f+1];
        weights = new double[f+1];

        features[0] = new DummyFeature();
        weights[0] = 0;//r.nextDouble();
        for (int i = 1; i < f + 1; i++) {
            features[i] = new RandomFeature(4, width, height);
            weights[i] = 0;//r.nextDouble();
        }
    }

    public boolean train(PBMImage image, int type) {
        int classification = classify(image);

        if (classification == type) {
            return true;
        }

        // Compute wether we are adding or subtracting
        int sgn = type - classification;

        int[] featureVector = computeFeatureVector(image);
        for (int i = 0; i < featureVector.length; i++) {
            weights[i] = weights[i] + (sgn * (1) * featureVector[i]);
        }

        return false;
    }

    public int classify(PBMImage image) {
        int[] featureVector = computeFeatureVector(image);

        double sum = 0;
        for (int i = 0; i < featureVector.length; i++) {
            sum += featureVector[i] * weights[i];
        }

        return sum > 0 ? 1 : 0;
    }

    private int[] computeFeatureVector(PBMImage image) {
        int[] featureVector = new int[features.length];
        for (int i = 0; i < features.length; i++) {
            featureVector[i] = features[i].evaluate(image);
        }

        return featureVector;
    }

    public static interface Feature {
        int evaluate(PBMImage image);
    }

    public static class DummyFeature implements Feature {

        @Override
        public int evaluate(PBMImage image) {
            return 1;
        }
    }

    public static class RandomFeature implements Feature {

        int[] row;
        int[] col;
        boolean[] sgn;

        public RandomFeature(int connection, int width, int height) {
            row = new int[connection];
            col = new int[connection];
            sgn = new boolean[connection];

            for (int i = 0; i < connection; i++) {
                row[i] = r.nextInt(width);
                col[i] = r.nextInt(height);
                sgn[i] = r.nextBoolean();
            }
        }

        @Override
        public int evaluate(PBMImage image) {
            int sum = 0;
            for (int i = 0; i < row.length; i++) {
                if (image.get(row[i], col[i]) == sgn[i]) {
                    sum++;
                }
            }

            return (sum >= 3) ? 1 : 0;
        }
    }
}
