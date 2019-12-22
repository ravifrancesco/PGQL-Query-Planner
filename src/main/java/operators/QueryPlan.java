package operators;

import exceptions.ColumnDataTypeException;
import graph.statistics.Statistics;

/**
 * QueryPlan interface implemented by all operators classes
 */

public interface QueryPlan{

    // compute cost
    public double computeCost(Statistics statistics) throws ColumnDataTypeException;

    // SETTERS

    // sets child operator plan
    public void setChildPlan(QueryPlan childPlan);

    // sets parent operator plan
    public void setParentPlan(QueryPlan parentPlan);

    // sets operator cost
    public void setOperatorCost(double operatorCost);

    // GETTERS

    // gets operator cardinality (used for computing operator cost)
    public int getCardinality();

    // gets operator
    public double getOperatorCost();

    // gets parent plan
    public QueryPlan getParentPlan();

    // gets child plan
    public QueryPlan getChildPlan();

}