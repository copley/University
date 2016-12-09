package GeneticClassification;

import org.jgap.InvalidConfigurationException;
import org.jgap.gp.*;
import org.jgap.gp.function.*;
import org.jgap.gp.impl.DeltaGPFitnessEvaluator;
import org.jgap.gp.impl.GPConfiguration;
import org.jgap.gp.impl.GPGenotype;
import org.jgap.gp.impl.TournamentSelector;
import org.jgap.gp.terminal.Terminal;
import org.jgap.gp.terminal.Variable;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * Created by Daniel Braithwaite on 27/04/2016.
 */
public class ClassificationProblem extends GPProblem {

    public static Variable[] vxs;

    public static void main(String[] args) throws Exception {

        int[][] classificationData = readClassificationData("data/part3/breast-cancer-wisconsin-training.data");
        int[][] testingData = readClassificationData("data/part3/breast-cancer-wisconsin-testing.data");

        GPConfiguration config = new GPConfiguration();
        config.setGPFitnessEvaluator(new DeltaGPFitnessEvaluator());

        config.setMaxInitDepth(3);
        config.setMaxCrossoverDepth(3);

        config.setPopulationSize(1024);
        config.setFitnessFunction(new ClassificationFitnessFunction(classificationData));

        config.setMutationProb(0.05f);
        config.setCrossoverProb(0.9f);
        config.setReproductionProb(0.05f);

        config.setSelectionMethod(new TournamentSelector(50));

        GPProblem problem = new ClassificationProblem(config);
        GPGenotype gp = problem.create();
        gp.setVerboseOutput(true);
        gp.evolve(150);

        gp.outputSolution(gp.getAllTimeBest());
        problem.showTree(gp.getAllTimeBest(), "mathproblem_best.png");

        // Evaluate on testing data
        ClassificationFitnessFunction testing = new ClassificationFitnessFunction(testingData);
        IGPProgram solution = gp.getAllTimeBest();
        double accuracy = 1 - testing.evaluate(solution);
        System.out.println(String.format("Accuracy On Testing: %f", accuracy));
    }

    public ClassificationProblem(GPConfiguration a_conf)
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

        int[] minDepths = new int[] {4};
        int[] maxDepths = new int[] {4};

        vxs = new Variable[9];

        CommandGene[][] nodesSet = {
                {
                        vxs[0] = Variable.create(conf, "X0", CommandGene.DoubleClass),
                        vxs[1] = Variable.create(conf, "X1", CommandGene.DoubleClass),
                        vxs[2] = Variable.create(conf, "X2", CommandGene.DoubleClass),
                        vxs[3] = Variable.create(conf, "X3", CommandGene.DoubleClass),
                        vxs[4] = Variable.create(conf, "X4", CommandGene.DoubleClass),
                        vxs[5] = Variable.create(conf, "X5", CommandGene.DoubleClass),
                        vxs[6] = Variable.create(conf, "X6", CommandGene.DoubleClass),
                        vxs[7] = Variable.create(conf, "X7", CommandGene.DoubleClass),
                        vxs[8] = Variable.create(conf, "X8", CommandGene.DoubleClass),
                        new Add(conf, CommandGene.DoubleClass),
                        new Subtract(conf, CommandGene.DoubleClass),
                        new Multiply(conf, CommandGene.DoubleClass),
                        new Divide(conf, CommandGene.DoubleClass),
//                        new Sine(conf, CommandGene.DoubleClass),
//                        new Exp(conf, CommandGene.DoubleClass),
//                        new Pow(conf, CommandGene.DoubleClass),

                        new Terminal(conf, CommandGene.DoubleClass, 0d, 1d, false)
                }
        };

        return GPGenotype.randomInitialGenotype(conf, types, argTypes, nodesSet, minDepths, maxDepths, 15, true);
    }

    public static class ClassificationFitnessFunction extends GPFitnessFunction {

        int[][] data;

        public ClassificationFitnessFunction(int[][] d) {
            this.data = d;
        }

        @Override
        protected double evaluate(IGPProgram igpProgram) {
            int count = 0;
            Object[] noargs = new Object[0];

            for (int i = 0; i < data.length; i++) {
                double expected = data[i][data[i].length-1];

                for (int j = 0; j < data[i].length-1; j++) {
                    vxs[j].set((double) data[i][j]);
                }

                try {
                    double res = igpProgram.execute_double(0, noargs);
                    if ((expected > 0 && res > 0) || (expected <= 0 && res <= 0)) {
                        count++;
                    }
                } catch (ArithmeticException e) {
                    // Should never happen
                }
            }

            double accuracy = ((double) count) / ((double) data.length);

            return 1 - accuracy;
        }
    }


    public static int[][] readClassificationData(String path) throws IOException{
        String file = readFile(path);
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
