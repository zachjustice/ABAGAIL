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

import java.util.Arrays;

/**
 * Copied from ContinuousPeaksTest
 * @version 1.0
 */
public class OldFourPeaksTest {
    /** The n value */
    private static final int N = 200;

    public static void main(String[] args) {
        for(int i = 10; i <= 50; i+= 10) {
            int[] ranges = new int[N];
            Arrays.fill(ranges, 2);
            EvaluationFunction ef = new FourPeaksEvaluationFunction(i);
            Distribution odd = new DiscreteUniformDistribution(ranges);
            NeighborFunction nf = new DiscreteChangeOneNeighbor(ranges);
            MutationFunction mf = new DiscreteChangeOneMutation(ranges);
            CrossoverFunction cf = new SingleCrossOver();
            Distribution df = new DiscreteDependencyTree(.1, ranges);
            HillClimbingProblem hcp = new GenericHillClimbingProblem(ef, odd, nf);
            GeneticAlgorithmProblem gap = new GenericGeneticAlgorithmProblem(ef, odd, mf, cf);
            ProbabilisticOptimizationProblem pop = new GenericProbabilisticOptimizationProblem(ef, odd, df);

            RandomizedHillClimbing rhc = new RandomizedHillClimbing(hcp);
            MaximizationTrainer fit = new MaximizationTrainer(rhc, ef, 200);
            fit.train();
            System.out.println("RHC: " + ef.value(rhc.getOptimal()));

            SimulatedAnnealing sa = new SimulatedAnnealing(1E11, .95, hcp);
            fit = new MaximizationTrainer(sa, ef, 200);
            fit.train();
            System.out.println("SA: " + ef.value(sa.getOptimal()));

            StandardGeneticAlgorithm ga = new StandardGeneticAlgorithm(200, 100, 10, gap);
            fit = new MaximizationTrainer(ga, ef, 200);
            fit.train();
            System.out.println("GA: " + ef.value(ga.getOptimal()));

            MIMIC mimic = new MIMIC(200, 20, pop);
            fit = new MaximizationTrainer(mimic, ef, 200);
            fit.train();
            System.out.println("MIMIC: " + ef.value(mimic.getOptimal()));
        }
    }
}
