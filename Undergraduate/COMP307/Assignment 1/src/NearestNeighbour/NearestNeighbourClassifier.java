package NearestNeighbour;

import java.lang.reflect.Array;
import java.util.*;

/**
 * Created by Daniel Braithwaite on 9/03/2016.
 */
public class NearestNeighbourClassifier {
    private ArrayList<NearestNeighbourNode> data;
    private double[] featureRanges;
    private int k;

    public NearestNeighbourClassifier(double[] ranges, int k) {
        this(ranges);
        this.k = k;
    }

    public NearestNeighbourClassifier(double[] ranges) {
        this.data = new ArrayList<>();
        this.featureRanges = ranges;
        this.k = 1;
    }

    public void train(ArrayList<NearestNeighbourNode> nodes) {
        data.addAll(nodes);
    }

    public String classify(final double[] features) {
        Map<String, Integer> categorys = new HashMap<>();

        // Sort the nodes by there distance to the input
        Collections.sort(data, new Comparator<NearestNeighbourNode>() {
            @Override
            public int compare(NearestNeighbourNode o1, NearestNeighbourNode o2) {
                double dist1 = distance(o1.getFeatures(), features);
                double dist2 = distance(o2.getFeatures(), features);
//
                return Double.compare(dist1, dist2);
            }
        });

//        data.sort((o1, o2) -> {
//
//        });

        // Collect up the k best ones
        for (int i = 0; i < k; i++) {
            NearestNeighbourNode n = data.get(i);

            if (!categorys.containsKey(n.getCategory())) {
                categorys.put(n.getCategory(), 0);
            }

            categorys.put(n.getCategory(), categorys.get(n.getCategory()) + 1);
        }

        // Find the category with best count
        int maxCategory = 0;
        String category = null;
        for (String c : categorys.keySet()) {
            if (categorys.get(c) > maxCategory) {
                category = c;
            }
        }

        return category;
    }

    public void setK(int k) {
        this.k = k;
    }

    private double distance(double[] f1, double[] f2) {
        double sum = 0;

        for (int i = 0; i < f1.length; i++) {
            sum += Math.pow(f1[i] - f2[i], 2)/Math.pow(featureRanges[i], 2);
        }

        return Math.sqrt(sum);
    }
}
