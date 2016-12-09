package ec.app.regression2;

import ec.*;
import ec.gp.*;
import ec.util.*;

/**
 * Created by Daniel Braithwaite on 20/04/2016.
 */
public class X extends GPNode {
    @Override
    public String toString() {
        return "x";
    }

    @Override
    public int expectedChildren() {
        return 0;
    }

    @Override
    public void eval(EvolutionState evolutionState, int i, GPData gpData, ADFStack adfStack, GPIndividual gpIndividual, Problem problem) {
        double result;
        DoubleData rd = ((DoubleData) gpData);
        rd.x = ((Regression) problem).currentX;
    }
}
