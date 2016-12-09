package DecisionTree;

import DecisionTree.DecisionTree.Instance;

import java.util.ArrayList;

/**
 * Created by Daniel Braithwaite on 16/03/2016.
 */
public class DecisionTreeLeaf implements DecisionTreeElement {
    private String category;
    private double prob;

    public DecisionTreeLeaf(String c, double prob) {
        this.category = c;
        this.prob = prob;
    }

    @Override
    public String classify(ArrayList<String> attrs, Instance i) {
        return category;
    }

    public String report(int indent) {
        String res = "";
        for (int i = 0; i < indent; i++) {
            res += " ";
        }

        return String.format("%s Class %s, prob = %f\n", res, category, prob);
    }

    @Override
    public String getTex() {
        return "node {" + category + "}";
    }
}
