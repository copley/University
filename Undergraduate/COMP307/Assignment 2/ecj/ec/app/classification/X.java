package ec.app.classification;

import ec.*;
import ec.gp.*;
import ec.util.*;
import java.util.Random;

/**
 * Created by Daniel Braithwaite on 20/04/2016.
 */
public class X extends GPNode {
    protected int index = -1;

    public X(int i) {
      index = i;
    }

    @Override
    public String toString() {
        return "x[" + index + "]";
    }

    @Override
    public int expectedChildren() {
        return 0;
    }

    @Override
    public void eval(EvolutionState evolutionState, int i, GPData gpData, ADFStack adfStack, GPIndividual gpIndividual, Problem problem) {
        DoubleData rd = ((DoubleData) gpData);
        rd.x = ((Classify) problem).data[index];
    }
}
