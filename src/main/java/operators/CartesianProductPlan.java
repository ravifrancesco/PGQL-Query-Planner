package operators;

import graph.statistics.Statistics;
import operators.utils.Constraint;
import operators.utils.ConstraintsArrayBuilder;
import oracle.pgql.lang.ir.QueryExpression;
import oracle.pgql.lang.ir.QueryVertex;

import java.util.ArrayList;
import java.util.Set;

/**
 * Cartesian Product operator
 * Used for retrieving the Cartesian product between two already matched vertices
 */

public class CartesianProductPlan extends ConstraintsArrayBuilder implements QueryPlan, Comparable<QueryPlan> {

    private QueryVertex leftVertex;
    private QueryVertex rightVertex;

    private QueryPlan child;
    private QueryPlan parent1;
    private QueryPlan parent2;

    private ArrayList<Constraint> constraintsLeft;
    private ArrayList<Constraint> constraintsRight;

    private double operatorCost;
    private int operatorCardinality;

    // constructor
    public CartesianProductPlan(QueryVertex leftQueryVertex, QueryVertex rightQueryVertex,
                                QueryPlan parentPlan1, QueryPlan parentPlan2,
                                Set<QueryExpression> constraintsSet, Statistics statistics) {

        this.parent1 = parentPlan1;
        this.parent2 = parentPlan2;
        this.leftVertex = leftQueryVertex;
        this.rightVertex = rightQueryVertex;

        this.constraintsLeft = constraintsVertexArrayBuilder(leftQueryVertex, constraintsSet);
        this.constraintsRight = constraintsVertexArrayBuilder(rightQueryVertex, constraintsSet);

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

    public void setParent2(QueryPlan parent2) {
        this.parent2 = parent2; // sets second parent operator for cartesian product
    }

    @Override
    public void setParentPlan(QueryPlan parentPlan) {
        this.parent1 = parentPlan;
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
