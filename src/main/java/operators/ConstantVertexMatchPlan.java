package operators;

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
 * Constant Vertex Match Operator
 * Used for matching a starting vertex which is unique. For example: SELECT a.age MATCH (a) WHERE id(a) = 1
 */

public class ConstantVertexMatchPlan extends ConstraintsArrayBuilder implements QueryPlan, Comparable<QueryPlan> {

    private QueryVertex vertex;

    private QueryPlan child;
    private QueryPlan parent;

    private ArrayList<Constraint> constraints;

    private double operatorCost;
    private int operatorCardinality;

    GraphSettings graphSettings;
    HardwareCostSettings hardwareCostSettings;

    // constructor
    public ConstantVertexMatchPlan(QueryVertex queryVertex, QueryPlan parentPlan, Settings settings,
                                   Set<QueryExpression> constraintsSet, Statistics statistics) {

        this.parent = parentPlan;
        this.vertex = queryVertex;
        this.constraints = constraintsVertexArrayBuilder(queryVertex, constraintsSet);

        this.graphSettings = settings.getGraphSettings();
        this.hardwareCostSettings = settings.getHardwareCostSettings();

        this.constraints = constraintsVertexArrayBuilder(queryVertex, constraintsSet);

        this.operatorCost = computeCost(statistics);

    }

    @Override
    public double computeCost(Statistics statistics) { // controllare se è giusto

        double srcVertexScanCost;

        double sequentialVertexCost = this.hardwareCostSettings.getSequentialVertexCost();
        double cpuOperationCost = this.hardwareCostSettings.getCpuOperationCost();
        double vertexPropertyCost = this.hardwareCostSettings.getVertexPropertyCost();

        int queryVertexCardinality = statistics.getVertexTableLength();

        this.operatorCardinality = computeTotalVertexCardinality(statistics, queryVertexCardinality);

        srcVertexScanCost = sequentialVertexCost;
        srcVertexScanCost += this.constraints.size() * (cpuOperationCost + vertexPropertyCost);
        srcVertexScanCost *= queryVertexCardinality;

        return srcVertexScanCost;

    }

    // computes total cardinality of given vertex constraints
    private int computeTotalVertexCardinality(Statistics statistics, int totalCardinality){

        double selectivity = 1;

        for (Constraint constraint: this.constraints) {
            selectivity *= computeConstraintSelectivity(constraint, statistics);
        }

        return (int) (totalCardinality * selectivity);

    }

    // computes selectivity of one vertex constraint
    private double computeConstraintSelectivity(Constraint constraint, Statistics statistics) {

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
        this.parent = parentPlan;
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
        return parent;
    }

    @Override
    public QueryPlan getChildPlan() {
        return child;
    }

    // compare operators on costs
    @Override
    public int compareTo(QueryPlan queryPlan) {
        return (int) (this.operatorCost - queryPlan.getOperatorCost());
    }

}
