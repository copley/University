package ec.app.classification;

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
public class Classify extends GPProblem implements SimpleProblemForm {
    public static final String P_DATA = "data";

    public int[] data;
    public int[][] classificationData;

    @Override
    public void setup(EvolutionState state, Parameter base) {
        super.setup(state, base);

        try {
            System.out.println(System.getProperty("user.dir"));
            this.classificationData = readClassificationData();
            System.out.println("Classification Data Red");
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

        int hits = 0;

        for(int j = 0; j < this.classificationData.length; j++) {
            this.data = this.classificationData[j];
            int expected = this.data[this.data.length-1];

            ((GPIndividual) individual).trees[0].child.eval(evolutionState, i1, input, stack, ((GPIndividual) individual), this);

            //System.out.println(String.format("%f : %f", expected, input.x));
            //double result = Math.pow(expected - input.x, 2);
            double output = input.x;
            //System.out.println(String.format("%d : %f", expected, output));
            if ((output > 0 && expected > 0) || (output <= 0 && expected <= 0)) {
                hits++;
                //System.out.println(hits);
            }
        }

        double accuracy = ((double) hits) / ((double) classificationData.length);
      //  System.out.println(accuracy);

        KozaFitness f = ((KozaFitness) individual.fitness);
        f.setStandardizedFitness(evolutionState, 1 - accuracy);
        f.hits = hits;
        individual.evaluated = true;
    }

    public static int[][] readClassificationData() throws IOException{
        String file = readFile("../data/part3/breast-cancer-wisconsin.data");
        String[] lines = file.split("\n");
        int[][] data = new int[lines.length][10];

        // Skip first 2 lines
        for(int i = 0; i < lines.length; i++) {
            lines[i] = lines[i].replaceAll("\\?", "-1"); // Handle missing data
            String[] entry = lines[i].trim().split(",");

            for (int j = 1; j < entry.length; j++) {
                data[i][j-1] = Integer.parseInt(entry[j]);
            }

            //System.out.println(data[i][data[i].length-1]);
            if (data[i][data[i].length-1] == 2) {
              data[i][data[i].length-1] = 0;
            } else {
              data[i][data[i].length-1] = 1;
            }
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
