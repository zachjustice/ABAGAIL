package opt.test;

import dist.DiscreteUniformDistribution;
import dist.Distribution;
import opt.EvaluationFunction;
import opt.example.KnapsackEvaluationFunction;
import opt.ga.*;
import shared.FixedIterationTrainer;
import shared.writer.CSVWriter;

import java.io.IOException;
import java.util.Arrays;
import java.util.Random;

/**
 * Run Genetic Algorithm for a really long time to determine optimum.
 * @author zachjustice
 */
public class FindKnapsackOptimumTest {
    /** Random number generator */
    private static final Random random = new Random(1);
    /** The number of copies each */
    private static final int COPIES_EACH = 4;
    /** The maximum weight for a single element */
    private static final double MAX_WEIGHT = 50;
    /** The maximum volume for a single element */
    private static final double MAX_VOLUME = 50;

    public static void main(String[] args) throws IOException {

        String[] fields = {"iterations", "optimum"};
        int[] inputs = {20,30,40};
        int[] iterations = {600000, 600000, 600000};

        for(int i = 0; i <= 3; i++) {
            CSVWriter csvWriter = new CSVWriter("find_optimum_knapsack_" + inputs[i] + ".csv", fields);
            csvWriter.open();
            System.out.println("Knapsack size: " + inputs[i]);

            int knapsackSize = inputs[i];
            int[] copies = new int[knapsackSize];
            Arrays.fill(copies, COPIES_EACH);
            double[] weights = new double[knapsackSize];
            double[] volumes = new double[knapsackSize];
            for (int j = 0; j < knapsackSize; j++) {
                weights[j] = random.nextDouble() * MAX_WEIGHT;
                volumes[j] = random.nextDouble() * MAX_VOLUME;
            }
            int[] ranges = new int[knapsackSize];
            Arrays.fill(ranges, COPIES_EACH + 1);
            double knapsackVolume = MAX_VOLUME * knapsackSize * COPIES_EACH * .4;

            double start = System.currentTimeMillis();
            EvaluationFunction ef = new KnapsackEvaluationFunction(weights, volumes, knapsackVolume, copies);
            Distribution odd = new DiscreteUniformDistribution(ranges);
            MutationFunction mf = new DiscreteChangeOneMutation(ranges);
            CrossoverFunction cf = new UniformCrossOver();
            GeneticAlgorithmProblem gap = new GenericGeneticAlgorithmProblem(ef, odd, mf, cf);
            StandardGeneticAlgorithm ga = new StandardGeneticAlgorithm(200, 150, 25, gap);
            double end = System.currentTimeMillis();
            System.out.println("time to setup: " + (end - start));

            int cumulative_iterations = 0;
            for(int j = 50000; j <= iterations[i]; j+= 50000) {
                FixedIterationTrainer fit = new FixedIterationTrainer(ga, 50000);
                start = System.currentTimeMillis();
                fit.train();
                end = System.currentTimeMillis();
                System.out.println("time to setup: " + (end - start));

                start = System.currentTimeMillis();
                cumulative_iterations += 50000;
                double optimum = ef.value(ga.getOptimal());
                csvWriter.write(cumulative_iterations+"");
                csvWriter.write(optimum+"");
                csvWriter.nextRecord();

                end  = System.currentTimeMillis();
                System.out.println("time to write: " + (end - start));
            }

            csvWriter.write(" ");
            csvWriter.nextRecord();

            csvWriter.close();
            System.out.println(inputs[i] + " " + ef.value(ga.getOptimal()));
        }
    }
}
