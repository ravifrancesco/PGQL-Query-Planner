package settings;

import graph.statistics.EstimateBounds;

/**
 * Package containing settings for how to retrieve statistics.
 *
 * Could be individual for each column and for each operator (needs appropriate implementation)
 *
 */

public class StatisticsSettings {

    EstimateBounds estimateBound;

    // setters
    public void setEstimateBoundToDefault() {
        this.estimateBound = EstimateBounds.ESTIMATE;
    }

    public void setEstimateBoundToLowerBound() {
        this.estimateBound = EstimateBounds.LOWERBOUND;
    }

    public void setEstimateBoundToUpperrBound() {
        this.estimateBound = EstimateBounds.UPPERBOUND;
    }

    // getters
    public EstimateBounds getEstimateBound() {
        return estimateBound;
    }
}
