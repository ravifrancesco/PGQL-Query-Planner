package operators;

import graph.statistics.Statistics;
import settings.HardwareCostSettings;
import oracle.pgql.lang.ir.*;

/**
 * Reachability Operator
 * Used for matching vertices connected via a PATH expression. For example:
 *      PATH p as ()->()()
 *    SELECT src.age, dst.age MATCH (src)-/:p* /->(dst)
 */

public class ReachabilityPlan implements QueryPlan, Comparable<QueryPlan> {

    private double operatorCost;
    private int operatorCardinality;

    private QueryPlan child;
    private QueryPlan parent;

    private HardwareCostSettings hardwareCostSettings = new HardwareCostSettings();

    // constructor
    // da fare

    //@Override
    public void computeCardinality(Statistics statistics) {

    }

    @Override
    //da fare
    public double computeCost(Statistics statistics) {
        return 0;
    }

    // setters
    @Override
    public void setChildPlan(QueryPlan childPlan) {
        this.child = childPlan;
    }

    @Override
    public void setParentPlan(QueryPlan parentPlan) {
        this.parent = parentPlan;
    }

    @Override
    public void setOperatorCost(double operatorCost) {
        this.operatorCost = operatorCost;
    }

    // getters
    @Override
    public int getCardinality() {
        return this.operatorCardinality;
    }

    @Override
    public double getOperatorCost() {
        return operatorCost;
    }

    @Override
    public QueryPlan getParentPlan() { return null; }

    @Override
    public QueryPlan getChildPlan() { return null;}

    // compare operators on costs
    @Override
    public int compareTo(QueryPlan queryPlan) {
        return (int) (this.operatorCost - queryPlan.getOperatorCost());
    }

}
