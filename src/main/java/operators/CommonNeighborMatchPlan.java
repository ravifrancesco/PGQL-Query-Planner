package operators;

import exceptions.ColumnDataTypeException;
import graph.statistics.Comparators;
import graph.statistics.Statistics;
import operators.utils.Constraint;
import operators.utils.ConstraintType;
import operators.utils.ConstraintsArrayBuilder;
import oracle.pgql.lang.ir.QueryExpression;
import oracle.pgql.lang.ir.QueryVertex;
import settings.GraphSettings;
import settings.HardwareCostSettings;
import settings.Settings;

import java.util.ArrayList;
import java.util.Set;

/**
 * Common Neighbor Match Operator
 * Used for matching a common neighbor of two already matched vertices.
 */

public class CommonNeighborMatchPlan extends ConstraintsArrayBuilder implements QueryPlan, Comparable<QueryPlan> {

    private QueryVertex leftVertex;
    private QueryVertex rightVertex;
    private QueryVertex vertexToFind;

    private QueryPlan child;
    private QueryPlan parent1;
    private QueryPlan parent2;

    private ArrayList<Constraint> constraintsLeft;
    private ArrayList<Constraint> constraintsRight;
    private ArrayList<Constraint> constraintsVertexToFind;

    private double operatorCost;
    private int operatorCardinality;

    GraphSettings graphSettings;
    HardwareCostSettings hardwareCostSettings;

    // constructor
    public CommonNeighborMatchPlan(QueryVertex leftQueryVertex, QueryVertex rightQueryVertex, QueryVertex vertexToFind,
                                   QueryPlan parentPlan1, Settings settings,
                                   Set<QueryExpression> constraintsSet, Statistics statistics) {

        this.parent1 = parentPlan1;
        if (parent1 instanceof CartesianProductPlan) {
            this.parent2 = null;
        } else {
            this.parent2 = this.parent1.getParentPlan();
        }

        this.leftVertex = leftQueryVertex;
        this.rightVertex = rightQueryVertex;
        this.vertexToFind = vertexToFind;

        this.graphSettings = settings.getGraphSettings();
        this.hardwareCostSettings = settings.getHardwareCostSettings();

        this.constraintsLeft = constraintsVertexArrayBuilder(leftQueryVertex, constraintsSet);
        this.constraintsRight = constraintsVertexArrayBuilder(rightQueryVertex, constraintsSet);
        this.constraintsVertexToFind = constraintsVertexArrayBuilder(vertexToFind, constraintsSet);

        try {
            this.operatorCost = computeCost(statistics);
        } catch (ColumnDataTypeException e) {
            e.printStackTrace();
        }

    }

    // computes cost of the operator
    @Override
    public double computeCost(Statistics statistics) throws ColumnDataTypeException { // ci sto ancora ragionando

        double indexVertexCost = this.hardwareCostSettings.getIndexVertexCost();
        double cpuOperationCost = this.hardwareCostSettings.getCpuOperationCost();
        double vertexPropertyCost = this.hardwareCostSettings.getVertexPropertyCost();

        int leftVertexCardinality = computeTotalVertexCardinality(this.constraintsLeft, statistics, this.parent1.getCardinality());
        int rightVertexCardinality;
        if (this.parent2 == null) {
            rightVertexCardinality = computeTotalVertexCardinality(this.constraintsRight, statistics, this.parent1.getCardinality());
        } else {
            rightVertexCardinality = computeTotalVertexCardinality(this.constraintsRight, statistics, this.parent2.getCardinality());
        }

        double leftVertexScanCost = leftVertexCardinality * indexVertexCost;
        double rightVertexScanCost = rightVertexCardinality * indexVertexCost;

        leftVertexScanCost += leftVertexCardinality * (this.constraintsLeft.size() * (cpuOperationCost + vertexPropertyCost));
        rightVertexScanCost += rightVertexCardinality * (this.constraintsRight.size() * (cpuOperationCost + vertexPropertyCost));

        this.operatorCardinality = leftVertexCardinality * rightVertexCardinality;
        return leftVertexScanCost + rightVertexScanCost;

    }

    // computes total cardinality of given vertex constraints
    private int computeTotalVertexCardinality(ArrayList<Constraint> constraints, Statistics statistics, int totalCardinality) throws ColumnDataTypeException {

        double selectivity = 1;

        for (Constraint constraint: constraints) {
            selectivity *= computeConstraintSelectivity(constraint, statistics);
        }

        return (int) (totalCardinality * selectivity);

    }

    // computes selectivity of one vertex constraint
    private double computeConstraintSelectivity(Constraint constraint, Statistics statistics) throws ColumnDataTypeException {

        if (constraint.getType() == ConstraintType.HAS_LABEL) {
            String vertexLabel = graphSettings.getVertexTypeCsvHeader();
            String label = constraint.getAttribute();
            return statistics.getVertexFilterSelectivity(vertexLabel, label, Comparators.EQUAL);
        } else if (constraint.getType() == ConstraintType.WHERE) {
            String attribute = constraint.getAttribute();
            String value = constraint.getValue();
            return statistics.getVertexFilterSelectivity(attribute, value, constraint.getConstraintComparator());
        } else {
            return 1; // shouldn't enter else statement
        }

    }

    // setters
    @Override
    public void setChildPlan(QueryPlan childPlan) {
        this.child = childPlan;
    }

    @Override
    public void setParentPlan(QueryPlan parentPlan) {
        this.parent1 = parentPlan;
    }

    public void setParent2(QueryPlan parent2) {
        this.parent2 = parent2; // sets second parent operator for cartesian product
    }

    @Override
    public void setOperatorCost(double operatorCost) {
        this.operatorCost = operatorCost;
    }

    // getters
    @Override
    public int getCardinality() {
        return operatorCardinality;
    }

    @Override
    public double getOperatorCost() {
        return operatorCost;
    }

    @Override
    public QueryPlan getParentPlan() {
        return parent1;
    }

    @Override
    public QueryPlan getChildPlan() {
        return parent2;
    }

    // compare operators on costs
    @Override
    public int compareTo(QueryPlan queryPlan) {
        return (int) (this.operatorCost - queryPlan.getOperatorCost());
    }

}
