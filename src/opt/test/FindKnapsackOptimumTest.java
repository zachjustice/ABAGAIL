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
    private static final int COPIES_EACH = 1;
    /** The maximum weight for a single element */
    private static final double MAX_VALUE = 50;
    /** The maximum volume for a single element */
    private static final double MAX_VOLUME = 50;

    public static void main(String[] args) throws IOException {

        String[] fields = {"iterations", "optimum"};
        int[] inputs = {10,20,30};
        int[] iterations = {100000, 100000, 100000};

        for(int i = 0; i < 3; i++) {
            CSVWriter csvWriter = new CSVWriter("find_optimum_knapsack_" + inputs[i] + ".csv", fields);
            csvWriter.open();
            System.out.println("Knapsack size: " + inputs[i]);

            int knapsackSize = inputs[i];
            int[] copies = new int[knapsackSize];
            Arrays.fill(copies, COPIES_EACH);
            double[] values = new double[knapsackSize];
            double[] volumes = new double[knapsackSize];
            for (int j = 0; j < knapsackSize; j++) {
                values[j] = random.nextDouble() * MAX_VALUE;
                volumes[j] = random.nextDouble() * MAX_VOLUME;
            }
            int[] ranges = new int[knapsackSize];
            Arrays.fill(ranges, COPIES_EACH + 1);
            double knapsackVolume = MAX_VOLUME * knapsackSize * COPIES_EACH * .8;

            EvaluationFunction ef = new KnapsackEvaluationFunction(values, volumes, knapsackVolume, copies);
            Distribution odd = new DiscreteUniformDistribution(ranges);
            MutationFunction mf = new DiscreteChangeOneMutation(ranges);
            CrossoverFunction cf = new UniformCrossOver();
            GeneticAlgorithmProblem gap = new GenericGeneticAlgorithmProblem(ef, odd, mf, cf);
            StandardGeneticAlgorithm ga = new StandardGeneticAlgorithm(200, 150, 25, gap);

            int cumulative_iterations = 0;
            for(int j = 100; j <= iterations[i]; j+= 100) {
                FixedIterationTrainer fit = new FixedIterationTrainer(ga, 100);
                fit.train();

                cumulative_iterations += 100;
                double optimum = ef.value(ga.getOptimal());
                csvWriter.write(cumulative_iterations+"");
                csvWriter.write(optimum+"");
                csvWriter.nextRecord();

                System.out.println("cumulative_iterations: " + cumulative_iterations + ", optimum " + optimum );
            }

            csvWriter.write(" ");
            csvWriter.nextRecord();

            csvWriter.close();
        }
    }
}
