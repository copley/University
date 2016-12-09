package NearestNeighbour;

/**
 * Created by Daniel Braithwaite on 9/03/2016.
 */
public class NearestNeighbourNode {
    private double[] features;
    private String category;

    public NearestNeighbourNode(double[] f, String c) {
        features = f;
        category = c;
    }

    public double[] getFeatures() {
        return features;
    }

    public String getCategory() {
        return category;
    }

    public String getTex() {
        String tex = category + " & ";
        for (int i = 0; i < features.length; i++) {
            if (i != 0) {
                tex += " & ";
            }

            tex += features[i] + "cm";
        }

        return tex;
    }
}
