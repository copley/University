package GeneticRegression;

import org.jgap.InvalidConfigurationException;
import org.jgap.gp.*;
import org.jgap.gp.function.Add;
import org.jgap.gp.function.Divide;
import org.jgap.gp.function.Multiply;
import org.jgap.gp.function.Subtract;
import org.jgap.gp.impl.DeltaGPFitnessEvaluator;
import org.jgap.gp.impl.GPConfiguration;
import org.jgap.gp.impl.GPGenotype;
import org.jgap.gp.impl.TournamentSelector;
import org.jgap.gp.terminal.Terminal;
import org.jgap.gp.terminal.Variable;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Objects;

/**
 * Created by Daniel Braithwaite on 27/04/2016.
 */
public class RegressionProblem extends GPProblem {

    public static Variable vx;

    public static void main(String[] args) throws Exception {

        double[][] regressionData = readRegressionData();

        GPConfiguration config = new GPConfiguration();
        config.setGPFitnessEvaluator(new DeltaGPFitnessEvaluator());

        config.setMaxInitDepth(5);
        config.setMaxCrossoverDepth(5);

        config.setPopulationSize(1024);
        config.setFitnessFunction(new RegressionFitnessFunction(regressionData));

        config.setMutationProb(0.05f);
        config.setCrossoverProb(0.9f);
        config.setReproductionProb(0.05f);

        config.setSelectionMethod(new TournamentSelector(3));



        GPProblem problem = new RegressionProblem(config);
        GPGenotype gp = problem.create();
        gp.setVerboseOutput(true);
        gp.evolve(200);
        gp.outputSolution(gp.getAllTimeBest());
        problem.showTree(gp.getAllTimeBest(), "mathproblem_best.png");
    }

    public RegressionProblem(GPConfiguration a_conf)
            throws InvalidConfigurationException {
        super(a_conf);
    }

    @Override
    public GPGenotype create() throws InvalidConfigurationException {
        GPConfiguration conf = getGPConfiguration();

        Class[] types = {
                CommandGene.DoubleClass,
        };

        Class[][] argTypes = {
                {}
        };

        CommandGene[][] nodesSet = {
                {
                        vx = Variable.create(conf, "X", CommandGene.DoubleClass),
                        new Add(conf, CommandGene.DoubleClass),
                        new Subtract(conf, CommandGene.DoubleClass),
                        new Multiply(conf, CommandGene.DoubleClass),
                        new Divide(conf, CommandGene.DoubleClass),

                        new Terminal(conf, CommandGene.DoubleClass, 0d, 1d, false)
                }
        };

        return GPGenotype.randomInitialGenotype(conf, types, argTypes, nodesSet, 23, true);
    }

    public static class RegressionFitnessFunction extends GPFitnessFunction {

        double[][] data;

        public RegressionFitnessFunction(double[][] d) {
            this.data = d;
        }

        @Override
        protected double evaluate(IGPProgram igpProgram) {
            float sum = 0;
            Object[] noargs = new Object[0];

            for (int i = 0; i < data.length; i++) {
                double x = data[i][0];
                double y = data[i][1];

                vx.set(x);

                try {
                    double res = igpProgram.execute_double(0, noargs);
                    //float res = igpProgram.execute_float(0, noargs);
                    sum += Math.pow(res - y, 2);
                } catch (ArithmeticException e) {
                    // Should never happen
                }
            }

            return sum;
        }
    }


    public static double[][] readRegressionData() throws IOException {
        String file = readFile("data/part2/regression.txt");
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
