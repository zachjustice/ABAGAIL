package opt.test;

import dist.DiscreteUniformDistribution;
import dist.Distribution;
import opt.*;
import opt.example.FourPeaksEvaluationFunction;
import shared.FixedIterationTrainer;
import shared.writer.CSVWriter;

import java.io.IOException;
import java.util.Arrays;
import java.util.Random;

/**
 * Created by zachjustice on 3/7/16.
 */
public class FindOptimumTest {
    /** The n value */
    private static final int N = 200;
    /** The t value */
    private static final int T = N / 5;
    /** Random number generator */
    private static final Random random = new Random();
    /** The number of items */
    private static final int NUM_ITEMS = 40;
    /** The number of copies each */
    private static final int COPIES_EACH = 4;
    /** The maximum weight for a single element */
    private static final double MAX_WEIGHT = 50;
    /** The maximum volume for a single element */
    private static final double MAX_VOLUME = 50;
    /** The volume of the knapsack */
    private static final double KNAPSACK_VOLUME =
            MAX_VOLUME * NUM_ITEMS * COPIES_EACH * .4;

    public static void main(String[] args) throws IOException {
        int[] ranges = new int[N];
        Arrays.fill(ranges, 2);
        Distribution odd = new DiscreteUniformDistribution(ranges);
        NeighborFunction nf = new DiscreteChangeOneNeighbor(ranges);
        //MutationFunction mf = new DiscreteChangeOneMutation(ranges);
        //CrossoverFunction cf = new SingleCrossOver();
        //Distribution df = new DiscreteDependencyTree(.1, ranges);
        //GeneticAlgorithmProblem gap = new GenericGeneticAlgorithmProblem(ef, odd, mf, cf);
        //ProbabilisticOptimizationProblem pop = new GenericProbabilisticOptimizationProblem(ef, odd, df);
        String[] fields = {"iterations", "optimum"};
        int[] inputs = {30,40,50,60,70,80};
        int[] iterations = {200000, 250000, 250000, 400000, 800000, 1100000 };

        CSVWriter csvWriter = new CSVWriter("optimum_fourpeaks.csv", fields);
        csvWriter.open();

        for(int i = 0; i < 6; i++) {
            EvaluationFunction ef = new FourPeaksEvaluationFunction(inputs[i]);
            HillClimbingProblem hcp = new GenericHillClimbingProblem(ef, odd, nf);
            SimulatedAnnealing sa = new SimulatedAnnealing(100, .95, hcp);
            FixedIterationTrainer fit = new FixedIterationTrainer(sa, iterations[i]);
            fit.train();

            double optimum = ef.value(sa.getOptimal());
            csvWriter.write(inputs[i]+"");
            csvWriter.write(optimum+"");
            csvWriter.nextRecord();

            System.out.println(inputs[i] + " " +ef.value(sa.getOptimal()));
        }

        csvWriter.close();

        //-----------------------------------------
        /*

        int[] knapsack_size = {20,30,40};

        csvWriter = new CSVWriter("optimum_knapsack.csv", fields);
        csvWriter.open();

        for(int i = 0; i < 6; i++) {
            int[] copies = new int[knapsack_size[i]];
            Arrays.fill(copies, COPIES_EACH);
            double[] weights = new double[knapsack_size[i]];
            double[] volumes = new double[knapsack_size[i]];
            for (int j = 0; j < knapsack_size[j]; j++) {
                weights[j] = random.nextDouble() * MAX_WEIGHT;
                volumes[j] = random.nextDouble() * MAX_VOLUME;
            }
            ranges = new int[knapsack_size[i]];
            Arrays.fill(ranges, COPIES_EACH + 1);
            KnapsackEvaluationFunction ef = new KnapsackEvaluationFunction(weights, volumes, KNAPSACK_VOLUME, copies);
            HillClimbingProblem hcp = new GenericHillClimbingProblem(ef, odd, nf);
            SimulatedAnnealing sa = new SimulatedAnnealing(100, .95, hcp);
            FixedIterationTrainer fit = new FixedIterationTrainer(sa, iterations[i]);
            fit.train();

            double optimum = ef.value(sa.getOptimal());
            csvWriter.write(knapsack_size[i]+"");
            csvWriter.write(optimum+"");
            csvWriter.nextRecord();

            System.out.println(knapsack_size[i] + " " +ef.value(sa.getOptimal()));
        }

        csvWriter.close();
        */
    }
}
