package ec.app.classification;

import ec.*;
import ec.gp.*;
import ec.util.*;

/**
 * Created by Daniel Braithwaite on 20/04/2016.
 */
public class Sin extends GPNode {
    @Override
    public String toString() {
        return "SIN";
    }

    @Override
    public int expectedChildren() {
        return 1;
    }

    @Override
    public void eval(EvolutionState evolutionState, int i, GPData gpData, ADFStack adfStack, GPIndividual gpIndividual, Problem problem) {
        double result;
        DoubleData rd = ((DoubleData) gpData);

        children[0].eval(evolutionState, i, gpData, adfStack, gpIndividual, problem);
        rd.x = Math.sin(rd.x);
    }
}
