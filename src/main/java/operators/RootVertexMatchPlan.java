package operators;

import graph.statistics.Statistics;
import operators.utils.Constraint;
import operators.utils.ConstraintsArrayBuilder;
import oracle.pgql.lang.ir.QueryExpression;
import oracle.pgql.lang.ir.QueryVertex;

import java.util.ArrayList;
import java.util.Set;

/**
 * Root Vertex Match Operator
 * Used for matching a starting vertex. For example: SELECT a.age MATCH (a).
 */

public class RootVertexMatchPlan extends ConstraintsArrayBuilder implements QueryPlan, Comparable<QueryPlan> {

    private QueryVertex vertex;

    private QueryPlan child;
    private QueryPlan parent;

    private ArrayList<Constraint> constraints;

    private double operatorCost;
    private int operatorCardinality;

    // constructor
    public RootVertexMatchPlan(QueryVertex queryVertex, QueryPlan parentPlan,
                               Set<QueryExpression> constraintsSet, Statistics statistics) {

        this.parent = parentPlan;
        this.vertex = queryVertex;
        this.constraints = constraintsVertexArrayBuilder(queryVertex, constraintsSet);

        // aggiungere costo

    }

    @Override
    public void computeCardinality(Statistics statistics) {
        // da fare
    }

    @Override
    public void computeCost(Statistics statistics) {
        // da fare
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
    public double getCardinality() {
        return operatorCardinality;
    }

    @Override
    public double getOperatorCost() {
        return operatorCost;
    }

    // compare operators on costs
    @Override
    public int compareTo(QueryPlan queryPlan) {
        return (int) (this.operatorCost - queryPlan.getOperatorCost());
    }

}
