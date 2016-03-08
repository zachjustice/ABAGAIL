package opt.test;

import dist.DiscreteDependencyTree;
import dist.DiscreteUniformDistribution;
import dist.Distribution;
import opt.*;
import opt.example.FourPeaksEvaluationFunction;
import opt.ga.*;
import opt.prob.GenericProbabilisticOptimizationProblem;
import opt.prob.MIMIC;
import opt.prob.ProbabilisticOptimizationProblem;
import shared.MaximizationTrainer;

import java.io.IOException;
import java.util.Arrays;

/**
 * Copied from ContinuousPeaksTest
 * @version 1.0
 */
public class FourPeaksOptimizationTest {
    /** The n value */
    private static final int N = 200;

    public static void main(String[] args) throws IOException {
        System.out.println("inputs, iterations, optimum, time");
        for(int t = 10; t <= 50; t+= 10) {
            int[] ranges = new int[N];
            Arrays.fill(ranges, 2);
            EvaluationFunction ef = new FourPeaksEvaluationFunction(t);
            Distribution odd = new DiscreteUniformDistribution(ranges);
            NeighborFunction nf = new DiscreteChangeOneNeighbor(ranges);
            MutationFunction mf = new DiscreteChangeOneMutation(ranges);
            CrossoverFunction cf = new SingleCrossOver();
            Distribution df = new DiscreteDependencyTree(.1, ranges);
            HillClimbingProblem hcp = new GenericHillClimbingProblem(ef, odd, nf);
            GeneticAlgorithmProblem gap = new GenericGeneticAlgorithmProblem(ef, odd, mf, cf);
            ProbabilisticOptimizationProblem pop = new GenericProbabilisticOptimizationProblem(ef, odd, df);

            /*
            // Test Randomized Hill Climbing
            RandomizedHillClimbing rhc = new RandomizedHillClimbing(hcp);

            double start = System.nanoTime();
            MaximizationTrainer fit = new MaximizationTrainer(rhc, ef, 200);
            double evaluations = fit.train();
            double end = System.nanoTime();
            double optimum = ef.value(rhc.getOptimal());
            double time = ( end - start ) / Math.pow(10, 9);

            System.out.println("RHC: " + evaluations + ", " + optimum + ", " + time);

            // Test Simulated Annealing
            SimulatedAnnealing sa = new SimulatedAnnealing(1E11, .95, hcp);

            start = System.nanoTime();
            fit = new MaximizationTrainer(sa, ef, 200);
            evaluations = fit.train();
            end = System.nanoTime();
            optimum = ef.value(sa.getOptimal());
            time = ( end - start ) / Math.pow(10, 9);

            System.out.println("SA: " + evaluations + ", " + optimum + ", " + time);

            // Test genetic algorithm
            StandardGeneticAlgorithm ga = new StandardGeneticAlgorithm(200, 100, 10, gap);

            start = System.nanoTime();
            fit = new MaximizationTrainer(ga, ef, 200);
            evaluations = fit.train();
            end = System.nanoTime();
            optimum = ef.value(ga.getOptimal());
            time = ( end - start ) / Math.pow(10, 9);

            System.out.println("GA: " + evaluations + ", " + optimum + ", " + time);

*/
            // Test MIMIC
            MIMIC mimic = new MIMIC(200, 20, pop);

            double start = System.nanoTime();
            MaximizationTrainer fit = new MaximizationTrainer(mimic, ef, 200);
            double evaluations = fit.train();
            double end = System.nanoTime();
            double optimum = ef.value(mimic.getOptimal());
            double time = ( end - start ) / Math.pow(10, 9);

            System.out.println(t + ", " + evaluations + ", " + optimum + ", " + time);
        }
    }
}

