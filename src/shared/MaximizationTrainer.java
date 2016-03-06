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
    private double maximalValue;

    /**
     * Make a new fixed iterations trainer
     * @param t the trainer
     * @param ef used to evaluate if optimum has been reached.
     */
    public MaximizationTrainer(Trainer t, EvaluationFunction ef) {
        this(t, ef, -1);
    }

    /**
     * Make a new fixed iterations trainer
     * @param t the trainer
     * @param ef used to evaulate if optimum has been reached
     * @param maximalValue after reaching this value the trainer stops. Default -1.
     */
    public MaximizationTrainer(Trainer t, EvaluationFunction ef, double maximalValue) {
        trainer = t;
        this.ef = ef;
        this.maximalValue = maximalValue;
    }

    /**
     * @see shared.Trainer#train()
     */
    public double train() {
        int evaluations = 0;
        int isMaximizedCounter = 0;
        double bestOptimum = -1;
        double prevOptimum;
        double currOptimum = 0;
        boolean isMaximized = false;

        while(!isMaximized) {
            trainer.train();
            evaluations++;
            Instance optimum = ((OptimizationAlgorithm)trainer).getOptimal();
            prevOptimum = currOptimum;
            currOptimum = ef.value(optimum);

            if(currOptimum > bestOptimum)
            {
                bestOptimum = currOptimum;
            }

            if(maximalValue == -1)
            {
                if(prevOptimum == bestOptimum && ++isMaximizedCounter > 100)
                {
                    isMaximized = true;
                }
            }
            else
            {
                if(currOptimum == maximalValue && ++isMaximizedCounter > 100)
                {
                    isMaximized = true;
                }
            }
        }

        return evaluations;
    }

}
