package shared;

import opt.EvaluationFunction;
import opt.OptimizationAlgorithm;

/**
 * Trainer for training to a given fitness or an actual optimum of the problem.
 * @author Zach Justice zachjustice123@gmail.com
 * @version 1.0
 */
public class MaximizationTrainer implements Trainer{
    /**
     * The inner trainer
     */
    private Trainer trainer;

    /**
     * Evaluation function to determine if optimum/maximal value has been reached
     */
    private EvaluationFunction ef;

    /**
     * Value to train towards. Once this value is reached, stopped training.
     */
    private double maximizationValue;

    /**
     * Make a new fixed iterations trainer
     * @param t the trainer
     * @param ef used to evaulate if optimum has been reached
     * @param maximizationValue after reaching this value the trainer stops. Default -1.
     */
    public MaximizationTrainer(Trainer t, EvaluationFunction ef, double maximizationValue) {
        trainer = t;
        this.ef = ef;
        this.maximizationValue = maximizationValue;
    }

    /**
     * @see shared.Trainer#train()
     */
    public double train() {
        int evaluations = 0;
        double currOptimum = 0;
        double bestOptimum = 0;

        while(currOptimum < maximizationValue) {
            trainer.train();
            evaluations++;
            Instance optimum = ((OptimizationAlgorithm)trainer).getOptimal();
            currOptimum = ef.value(optimum);

            if(currOptimum > bestOptimum)
            {
                bestOptimum = currOptimum;
            }

            //System.out.println("Evaluations: " + evaluations + " CurrOptimium: " + currOptimum + " bestOptimum: " + bestOptimum);
        }

        return evaluations;
    }

}
