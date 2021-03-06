package ec.app.regression2;

import ec.*;
import ec.gp.*;
import ec.util.*;

/**
 * Created by Daniel Braithwaite on 20/04/2016.
 */
public class Div extends GPNode {
    @Override
    public String toString() {
        return "/";
    }

    @Override
    public int expectedChildren() {
        return 2;
    }

    @Override
    public void eval(EvolutionState evolutionState, int i, GPData gpData, ADFStack adfStack, GPIndividual gpIndividual, Problem problem) {
        double result;
        DoubleData rd = ((DoubleData) gpData);

        children[0].eval(evolutionState, i, gpData, adfStack, gpIndividual, problem);
        result = rd.x;

        children[1].eval(evolutionState, i, gpData, adfStack, gpIndividual, problem);

        if (rd.x != 0) {
          rd.x = result / rd.x;
        } else {
          rd.x = 0;
        }
    }
}
