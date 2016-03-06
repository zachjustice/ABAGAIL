package opt.test;

import opt.EvaluationFunction;
import opt.HillClimbingProblem;
import opt.RandomizedHillClimbing;
import opt.SimulatedAnnealing;
import opt.ga.GeneticAlgorithmProblem;
import opt.ga.StandardGeneticAlgorithm;
import opt.prob.MIMIC;
import opt.prob.ProbabilisticOptimizationProblem;
import shared.FixedIterationTrainer;
import shared.writer.CSVWriter;

import java.io.IOException;

/**
 * Created by zachjustice on 3/6/16.
 */
public class OptimizationTester {
    private EvaluationFunction ef;
    private HillClimbingProblem hcp;
    private GeneticAlgorithmProblem gap;
    private ProbabilisticOptimizationProblem pop;
    private String resultsFileName;

    public OptimizationTester(EvaluationFunction ef, HillClimbingProblem hcp, GeneticAlgorithmProblem gap, ProbabilisticOptimizationProblem pop, String resultsFileName) {
        this.ef = ef;
        this.hcp = hcp;
        this.gap = gap;
        this.pop = pop;
        this.resultsFileName = resultsFileName;
    }

    public void test(int numIterations, int iterationStep, int numRepeats) throws IOException {
        Double[][][] results = new Double[4][numIterations][numRepeats];

        for (int i = iterationStep; i <= numIterations * iterationStep; i += iterationStep) {
            System.out.println("Iterations: " + i);
            for (int j = 0; j < numRepeats; j++) {
                System.out.print(" " + j);
                RandomizedHillClimbing rhc = new RandomizedHillClimbing(hcp);
                FixedIterationTrainer fit = new FixedIterationTrainer(rhc, i);
                fit.train();
                results[0][(i / iterationStep) - 1][j] = ef.value(rhc.getOptimal());
                //System.out.println(ef.value(rhc.getOptimal()));

                SimulatedAnnealing sa = new SimulatedAnnealing(100, .95, hcp);
                fit = new FixedIterationTrainer(sa, i);
                fit.train();
                results[1][(i / iterationStep) - 1][j] = ef.value(sa.getOptimal());
                //System.out.println(ef.value(sa.getOptimal()));

                StandardGeneticAlgorithm ga = new StandardGeneticAlgorithm(200, 150, 25, gap);
                fit = new FixedIterationTrainer(ga, i);
                fit.train();
                results[2][(i / iterationStep) - 1][j] = ef.value(ga.getOptimal());
                //System.out.println(ef.value(ga.getOptimal()));

                MIMIC mimic = new MIMIC(200, 100, pop);
                fit = new FixedIterationTrainer(mimic, i);
                fit.train();
                results[3][(i / iterationStep) - 1][j] = ef.value(mimic.getOptimal());
                //System.out.println(ef.value(mimic.getOptimal()));
            }
            System.out.println();
        }

        // fields are number of iterations each algorithm is tested at
        String[] fields = new String[numIterations];

        for (int i = 0; i < numIterations; i++) {
            fields[i] = Integer.toString(i * iterationStep + iterationStep);
        }

        // print results to csv
        CSVWriter csvWriter = new CSVWriter(resultsFileName, fields);
        csvWriter.open();

        for (Double[][] result : results) { // loop over each algorithm
            for (int k = 0; k < numRepeats; k++) { // loop over each repeat of jth iterations
                for (int j = 0; j < numIterations; j++) { // loop over iterations for that repeat
                    csvWriter.write(result[j][k] + "");
                }
                csvWriter.nextRecord();
            }

            csvWriter.write("");
            csvWriter.nextRecord();
        }

        csvWriter.close();
    }
}
