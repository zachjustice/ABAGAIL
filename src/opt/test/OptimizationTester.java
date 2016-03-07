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
import shared.MaximizationTrainer;
import shared.Trainer;
import shared.writer.CSVWriter;

import java.io.IOException;

/**
 * Runs optimization problems and prints results to file.
 * @author Zach Justice zachjustice123@gmail.com
 * @version 1.0
 */
public class OptimizationTester {
    private HillClimbingProblem hcp;
    private GeneticAlgorithmProblem gap;
    private ProbabilisticOptimizationProblem pop;
    private String resultsFileName;

    public OptimizationTester(HillClimbingProblem hcp, GeneticAlgorithmProblem gap, ProbabilisticOptimizationProblem pop, String resultsFileName) {
        this.hcp = hcp;
        this.gap = gap;
        this.pop = pop;
        this.resultsFileName = resultsFileName;
    }

    public void test(int numIterations, int iterationStep, int numRepeats, EvaluationFunction ef) throws IOException {
        Double[][][] results = new Double[4][numRepeats][numIterations];

        for (int i = iterationStep; i <= numIterations * iterationStep; i += iterationStep) {
            System.out.println("Iterations: " + i);
            for (int j = 0; j < numRepeats; j++) {
                System.out.print(" " + j);
                RandomizedHillClimbing rhc = new RandomizedHillClimbing(hcp);
                Trainer fit = new FixedIterationTrainer(rhc, i);
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

        writeResults(results, numIterations, iterationStep, numRepeats);
    }

    public Double[][] simpleMaximizationTest(EvaluationFunction ef, double maximizationValue) {
        Double[][] results = new Double[4][3];

        RandomizedHillClimbing rhc = new RandomizedHillClimbing(hcp);
        Trainer fit = new MaximizationTrainer(rhc, ef, maximizationValue);
        double start = System.nanoTime();
        results[0][0] = fit.train();
        double end = System.nanoTime();
        results[0][1] = end - start;
        results[0][2] = ef.value(rhc.getOptimal());

        SimulatedAnnealing sa = new SimulatedAnnealing(100, .95, hcp);
        fit = new MaximizationTrainer(sa, ef, maximizationValue);
        start = System.nanoTime();
        results[1][0] = fit.train();
        end = System.nanoTime();
        results[1][1] = end - start;
        results[1][2] = ef.value(rhc.getOptimal());

        StandardGeneticAlgorithm ga = new StandardGeneticAlgorithm(200, 150, 25, gap);
        fit = new MaximizationTrainer(ga, ef, maximizationValue);
        start = System.nanoTime();
        results[2][0] = fit.train();
        end = System.nanoTime();
        results[2][1] = end - start;
        results[2][2] = ef.value(rhc.getOptimal());

        MIMIC mimic = new MIMIC(200, 100, pop);
        fit = new MaximizationTrainer(mimic, ef, maximizationValue);
        start = System.nanoTime();
        results[3][0] = fit.train();
        end = System.nanoTime();
        results[3][1] = end - start;
        results[3][2] = ef.value(rhc.getOptimal());

        return results;
    }

    private void writeResults(Double[][][] results, int numIterations, int iterationStep, int numRepeats) throws IOException {
        // fields are number of iterations each algorithm is tested at
        String[] fields = new String[numIterations];

        for (int i = 0; i < numIterations; i++) {
            fields[i] = Integer.toString(i * iterationStep + iterationStep);
        }

        // print results to csv
        CSVWriter csvWriter = new CSVWriter(resultsFileName, fields);
        csvWriter.open();

        for (Double[][] result : results) { // loop over each algorithm
            for (Double[] aResult : result) { // loop over each repeat of jth iterations
                for (int k = 0; k < result[k].length; k++) { // loop over iterations for that repeat
                    csvWriter.write(aResult[k] + "");
                }
                csvWriter.nextRecord();
            }

            csvWriter.write("");
            csvWriter.nextRecord();
        }

        csvWriter.close();

    }
}
