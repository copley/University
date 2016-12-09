package ec.app.classification;

import ec.*;
import ec.gp.*;
import ec.util.*;
import java.util.Random;

/**
 * Created by Daniel Braithwaite on 20/04/2016.
 */
public class R extends GPNode {

    private static Random r = new Random();

    private double val;

    public R() {
      val = r.nextDouble();
      System.out.println(val);
    }

    @Override
    public String toString() {
        return String.format("%f", val);
    }

    @Override
    public int expectedChildren() {
        return 0;
    }

    @Override
    public void eval(EvolutionState evolutionState, int i, GPData gpData, ADFStack adfStack, GPIndividual gpIndividual, Problem problem) {
        double result;
        DoubleData rd = ((DoubleData) gpData);
        rd.x = val;
    }
}
