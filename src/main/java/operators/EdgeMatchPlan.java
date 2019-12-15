package operators;

import graph.statistics.Statistics;
import operators.utils.Constraint;
import operators.utils.ConstraintsArrayBuilder;
import oracle.pgql.lang.ir.QueryEdge;
import oracle.pgql.lang.ir.QueryExpression;

import java.util.ArrayList;
import java.util.Set;

/**
 * Edge Match Operator
 * Used for matching an edge between already two matched vertices.
 */

public class EdgeMatchPlan extends ConstraintsArrayBuilder implements QueryPlan, Comparable<QueryPlan>{

    private QueryEdge edge;

    private QueryPlan child;
    private QueryPlan parent;

    private ArrayList<Constraint> constraints;

    private double operatorCost;
    private int operatorCardinality;

    // constructor
    public EdgeMatchPlan(QueryEdge queryEdge, QueryPlan parentPlan,
                         Set<QueryExpression> constraintsSet, Statistics statistics) {

        this.parent = parentPlan;
        this.edge = queryEdge;
        this.constraints = constraintsEdgeArrayBuilder(queryEdge, constraintsSet);

        // aggiungere costo

    }

    @Override
    public void computeCardinality(Statistics statistics) {

    }

    @Override
    public void computeCost(Statistics statistics) {

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
