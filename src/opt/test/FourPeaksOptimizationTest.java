package opt.test;

import dist.DiscreteDependencyTree;
import dist.DiscreteUniformDistribution;
import dist.Distribution;
import opt.*;
import opt.example.FourPeaksEvaluationFunction;
import opt.ga.*;
import opt.prob.GenericProbabilisticOptimizationProblem;
import opt.prob.ProbabilisticOptimizationProblem;
import shared.writer.CSVWriter;

import java.io.IOException;
import java.util.Arrays;

/**
 * Run 4 algorithms for various sizes of T until a maximal value is reached for that T.
 * @version 1.0
 */
public class FourPeaksOptimizationTest {
    /** The n value */
    private static final int N = 200;

    /** Optimum for four peaks */
    private static final int FOUR_PEAKS_OPTIMUM = 200;

    public static void main(String[] args) throws IOException {
        int[] ranges = new int[N];
        Arrays.fill(ranges, 2);
        Distribution odd = new DiscreteUniformDistribution(ranges);
        NeighborFunction nf = new DiscreteChangeOneNeighbor(ranges);
        MutationFunction mf = new DiscreteChangeOneMutation(ranges);
        CrossoverFunction cf = new SingleCrossOver();
        Distribution df = new DiscreteDependencyTree(.1, ranges);

        String[] fields = {""};
        CSVWriter csvWriter = new CSVWriter("fourpeaks_optimization_test_results.csv", fields);
        csvWriter.open();

        for(int t=30; t <=80; t+=10) {
            System.out.println("T: " + t);
            EvaluationFunction ef = new FourPeaksEvaluationFunction(t);
            HillClimbingProblem hcp = new GenericHillClimbingProblem(ef, odd, nf);
            GeneticAlgorithmProblem gap = new GenericGeneticAlgorithmProblem(ef, odd, mf, cf);
            ProbabilisticOptimizationProblem pop = new GenericProbabilisticOptimizationProblem(ef, odd, df);

            OptimizationTester tester = new OptimizationTester(hcp, gap, pop, "fourpeaks_optimization_results.csv");
            Double[][] results = tester.simpleMaximizationTest(ef, FOUR_PEAKS_OPTIMUM);

            csvWriter.write("T: " + t);
            csvWriter.nextRecord();

            for (Double[] result : results) {
                for(double aResult : result) {
                    System.out.print( aResult  + " ");
                    csvWriter.write(aResult + "");
                }
                System.out.println();
                csvWriter.nextRecord();
            }
        }

        csvWriter.close();
    }
}

