package DecisionTree;

import DecisionTree.DecisionTree.Instance;
import java.util.ArrayList;

/**
 * Created by Daniel Braithwaite on 16/03/2016.
 */
public class DecisionTreeNode implements DecisionTreeElement {
    private String attr;
    private DecisionTreeElement left;
    private DecisionTreeElement right;

    public DecisionTreeNode(String attr, DecisionTreeElement left, DecisionTreeElement right) {
        this.attr = attr;
        this.left = left;
        this.right = right;
    }

    @Override
    public String classify(ArrayList<String> attrs, Instance i) {
        if (i.getAttr(attr)) {
            return left.classify(attrs, i);
        } else {
            return right.classify(attrs, i);
        }
    }

    public String getTex() {
        String tex = "node {" + attr + "}\n" +
                        "child { " + left.getTex() + "}\n" +
                        "child { " + right.getTex() + "} \n" +
                     "}";

        return tex;
    }

    public String report(int indent) {
        String res = "";

        String strIndent = "";
        for (int i = 0; i < indent; i++) {
            strIndent += " ";
        }

        res += String.format("%s%s = True:\n", strIndent, attr);
        res += left.report(indent + 1);
        res += String.format("%s%s = False:\n", strIndent, attr);
        res += right.report(indent + 1);

        return res;
    }
}
