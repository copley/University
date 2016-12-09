package DecisionTree;

import java.util.ArrayList;
import DecisionTree.DecisionTree.Instance;

/**
 * Created by Daniel Braithwaite on 16/03/2016.
 */
public interface DecisionTreeElement {
    String classify(ArrayList<String> attrs, Instance i);

    String report(int indent);

    String getTex();
}
