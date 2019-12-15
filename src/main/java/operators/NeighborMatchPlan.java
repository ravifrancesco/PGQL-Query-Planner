package operators;

import graph.statistics.Statistics;
import operators.utils.Constraint;
import operators.utils.ConstraintsArrayBuilder;
import oracle.pgql.lang.ir.QueryExpression;
import oracle.pgql.lang.ir.QueryVertex;

import java.util.ArrayList;
import java.util.Set;

/**
 * Neighbor Match Operator
 * Used for matching the neighbors of a given vertex. The operator can match both incoming and outgoing neighbors.
 */

public class NeighborMatchPlan extends ConstraintsArrayBuilder implements QueryPlan, Comparable<QueryPlan>{

    private QueryVertex srcVertex;
    private QueryVertex dstVertex;

    boolean outgoing;

    private QueryPlan child;
    private QueryPlan parent;

    private ArrayList<Constraint> constraintsSrc;
    private ArrayList<Constraint> constraintsDst;

    private double operatorCost;
    private int operatorCardinality;

    // constructor
    public NeighborMatchPlan(QueryVertex srcQueryVertex, QueryVertex dstQueryVertex,
                             boolean outgoing, QueryPlan parentPlan,
                             Set<QueryExpression> constraintsSet, Statistics statistics) {

        this.parent = parentPlan;
        this.srcVertex = srcQueryVertex;
        this.dstVertex = dstQueryVertex;
        this.outgoing = outgoing;

        this.constraintsSrc = constraintsVertexArrayBuilder(srcQueryVertex, constraintsSet);
        this.constraintsDst = constraintsVertexArrayBuilder(dstQueryVertex, constraintsSet);

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
