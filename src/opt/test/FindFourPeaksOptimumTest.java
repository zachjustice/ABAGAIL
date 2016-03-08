package opt.test;

import dist.DiscreteUniformDistribution;
import dist.Distribution;
import opt.*;
import opt.example.FourPeaksEvaluationFunction;
import shared.FixedIterationTrainer;
import shared.writer.CSVWriter;

import java.io.IOException;
import java.util.Arrays;

/**
 * Run simulated annealing a bunch to determine optimum.
 * @author Zach Justice zachjustice123@gmail.com
 * @version 1.0
 */
public class FindFourPeaksOptimumTest {
    /** The n value */
    private static final int N = 200;

    public static void main(String[] args) throws IOException {
        int[] ranges = new int[N];
        Arrays.fill(ranges, 2);
        Distribution odd = new DiscreteUniformDistribution(ranges);
        NeighborFunction nf = new DiscreteChangeOneNeighbor(ranges);

        String[] fields = {"iterations", "optimum"};
        int[] inputs = {30,40,50,60,70,80};
        int[] iterations = {200000, 250000, 250000, 400000, 800000, 1100000 };

        CSVWriter csvWriter = new CSVWriter("found_optimum_fourpeaks_with_SA.csv", fields);
        csvWriter.open();

        for(int i = 0; i < 6; i++) {
            System.out.println(inputs[i]);

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

    }
}
