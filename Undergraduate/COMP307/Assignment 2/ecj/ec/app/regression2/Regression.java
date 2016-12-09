package ec.app.regression2;

import ec.util.*;
import ec.*;
import ec.gp.*;
import ec.gp.koza.*;
import ec.simple.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * Created by Daniel Braithwaite on 20/04/2016.
 */
public class Regression extends GPProblem implements SimpleProblemForm {
    public static final String P_DATA = "data";

    public double currentX;
    public double[][] regressionData;

    @Override
    public void setup(EvolutionState state, Parameter base) {
        super.setup(state, base);

        try {
            System.out.println(System.getProperty("user.dir"));
            this.regressionData = readRegressionData();
            System.out.println("Regression Data Red");
        } catch (IOException e) {
            throw new RuntimeException();
        }

        if (!(input instanceof DoubleData)) {
            state.output.fatal("GSFEADWSAGGA");
        }
    }

    @Override
    public void evaluate(EvolutionState evolutionState, Individual individual, int i, int i1) {
        DoubleData input = (DoubleData)(this.input);

        // int hits = 0;
        double sum = 0.0;

        for(int j = 0; j < this.regressionData.length; j++) {
            this.currentX = this.regressionData[j][0];
            double expected = this.regressionData[j][1];

            ((GPIndividual) individual).trees[0].child.eval(evolutionState, i1, input, stack, ((GPIndividual) individual), this);

            //System.out.println(String.format("%f : %f", expected, input.x));
            double result = Math.pow(expected - input.x, 2);
            if (result <= 0.01) {
                hits++;
            }
            sum += result;
        }

        KozaFitness f = ((KozaFitness) individual.fitness);
        f.setStandardizedFitness(evolutionState, sum);
        f.hits = hits;
        individual.evaluated = true;
    }

    public static double[][] readRegressionData() throws IOException{
        String file = readFile("../data/part2/regression.txt");
        String[] lines = file.split("\n");
        double[][] data = new double[lines.length][2];

        // Skip first 2 lines
        for(int i = 0; i < lines.length; i++) {
            String[] entry = lines[i].trim().split("\\s+");

            data[i][0] = Double.parseDouble(entry[0]);
            data[i][1] = Double.parseDouble(entry[1]);
        }

        return data;
    }

    private static String readFile(String fileName) throws IOException {
        FileReader fileReader = new FileReader(fileName);
        BufferedReader bufferedReader = new BufferedReader(fileReader);

        String data = "";
        String line = "";

        while((line = bufferedReader.readLine()) != null) {
            data += line + "\n";
        }

        return data.trim();
    }

}
