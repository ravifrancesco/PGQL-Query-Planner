package operators;

import graph.statistics.Statistics;

/**
 * QueryPlan interface implemented by all operators classes
 */

public interface QueryPlan{

    // compute cardinality
    public void computeCardinality(Statistics statistics);

    // compute cost
    public void computeCost(Statistics statistics);

    // SETTERS

    // sets child operator plan
    public void setChildPlan(QueryPlan childPlan);

    // sets parent operator plan
    public void setParentPlan(QueryPlan parentPlan);

    // sets operator cost
    public void setOperatorCost(double operatorCost);

    // GETTERS

    // gets operator cardinality (used for computing operator cost)
    public double getCardinality();

    // gets operator
    public double getOperatorCost();

}