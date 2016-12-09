package KMeansClustering;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * Created by Daniel Braithwaite on 26/03/2016.
 */
public class Cluster {

    private ArrayList<double[]> features;
    private double[] centroid;

    private Cluster(ArrayList<double[]> features, double[] centroid) {
        this.features = features;
        this.centroid = centroid;
    }

    public ArrayList<double[]> getFeatures() {
        return features;
    }

    public double[] getCentroid() {
        return centroid;
    }

    private static Random r = new Random();

    public static Cluster[] kMeansCluster(int k, ArrayList<double[]> features) {
        double[] max = max(features);
        double[] min = min(features);

        // Compute the ranges
        double[] range = new double[max.length];
        for (int i = 0; i < range.length; i++) {
            range[i] = max[i] - min[i];
        }

        // Generate k centroids
        double[][] centroids = new double[k][max.length];
        for (int i = 0; i < centroids.length; i++) {
            centroids[i] = features.get(r.nextInt(features.size()));

//            for (int j = 0; j < centroids[i].length; j++) {
//                centroids[i] = features.get(r.nextInt(features.size()));
//                //centroids[i][j] = min[j] + r.nextDouble() * (range[j]);
//            }
        }

        boolean converged = false;
        Map<Integer, Integer> assignments = new HashMap<>();
        ArrayList<ArrayList<double[]>> groups = new ArrayList<>();
        for (int i = 0; i < k; i++) {
            groups.add(new ArrayList<double[]>());
        }

        while (!converged) {
            converged = true;

            // Compute what centroid each feature belongs to
            for (int i = 0; i < features.size(); i++) {
                double[] feature = features.get(i);

                double bestDist = Double.MAX_VALUE;
                int c = -1;

                for (int j = 0; j < centroids.length; j++) {
                    double d = distance(feature, centroids[j], range);

                    if (d < bestDist) {
                        bestDist = d;
                        c = j;
                    }
                }

                Integer prev = assignments.get(i) == null ? -1 : assignments.get(i);
                assignments.put(i, c);

                if (prev != c) {
                    converged = false;
                }
            }

            if (converged) { continue; }

            // Empty groups
            for (ArrayList<double[]> g : groups) {
                g.clear();
            }

            // Build up new groups
            for (int i = 0; i < features.size(); i++) {
                groups.get(assignments.get(i)).add(features.get(i));
            }

            // Move centroids
            for (int i = 0; i < groups.size(); i++) {
                if (!groups.get(i).isEmpty()) {
                    centroids[i] = avg(groups.get(i));
                }
            }
        }

        Cluster[] clusters = new Cluster[k];
        for (int i = 0; i < clusters.length; i++) {
            clusters[i] = new Cluster(groups.get(i), centroids[i]);
        }

        return clusters;
    }

    private static double distance(double[] f1, double[] f2, double[] ranges) {
        double sum = 0;

        for (int i = 0; i < f1.length; i++) {
            sum += Math.pow(f1[i] - f2[i], 2)/Math.pow(ranges[i], 2);
        }

        return Math.sqrt(sum);
    }

    private static double[] avg(ArrayList<double[]> features) {
        double[] sum = new double[features.get(0).length];

        for (double[] feature : features) {
            for (int i = 0; i < sum.length; i++) {
                sum[i] += feature[i];
            }
        }

        for (int i = 0; i < sum.length; i++) {
            sum[i] = sum[i]/features.size();
        }

        return sum;
    }

    private static double[] max(ArrayList<double[]> features) {
        double[] max = new double[features.get(0).length];
        for (int i = 0; i < max.length; i++) {
            max[i] = Double.MIN_VALUE;
        }

        for (double[] feature : features) {
            for (int i = 0; i < max.length; i++) {
                if (feature[i] > max[i]) {
                    max[i] = feature[i];
                }
            }
        }

        return max;
    }

    private static double[] min(ArrayList<double[]> features) {
        double[] min = new double[features.get(0).length];
        for (int i = 0; i < min.length; i++) {
            min[i] = Double.MAX_VALUE;
        }

        for (double[] feature : features) {
            for (int i = 0; i < min.length; i++) {
                if (feature[i] < min[i]) {
                    min[i] = feature[i];
                }
            }
        }

        return min;
    }
}
