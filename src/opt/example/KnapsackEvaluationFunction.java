package opt.example;

import opt.EvaluationFunction;
import shared.Instance;
import util.linalg.Vector;

/**
 * A checker board evaluation function
 * @author Andrew Guillory gtg008g@mail.gatech.edu
 * @version 1.0
 */
public class KnapsackEvaluationFunction implements EvaluationFunction {
    
    /**
     * The weights for the things that can be put in the sack
     */
    private double[] values;
    
    /**
     * The volumes for the things that can be put in the sack
     */
    private double[] volumes;
    
    /**
     * The maximum volume in the knapsack
     */
    private double knapsackVolume;
    
    /**
     * The maximum sum of all items
     */
    private double maxVolumeSum;
    
    /**
     * Make a new knapsack evaluation function
     * @param values the set of values
     * @param v the set of volumes
     * @param knapsackVolume volume of the knapsack
     * @param maxC the maximum counts
     */
    public KnapsackEvaluationFunction(double[] values, double[] v, double knapsackVolume,
            int[] maxC) {
        this.values = values;
        this.volumes = v;
        this.knapsackVolume = knapsackVolume;
        for (int i = 0; i < v.length; i++) {
            maxVolumeSum += maxC[i] * v[i];
        }
    }

    /**
     * @see opt.EvaluationFunction#value(opt.OptimizationData)
     */
    public double value(Instance d) {
        Vector data = d.getData();
        double volume = 0;
        double value = 0;
        for (int i = 0; i < data.size(); i++) {
            volume += volumes[i] * data.get(i);
            value += values[i] * data.get(i);
        }
        if (volume > knapsackVolume) {
            double smallNumber = 1E-10;
            return smallNumber*(maxVolumeSum - volume);
        } else {
            return value;
        }
        
    }

}
