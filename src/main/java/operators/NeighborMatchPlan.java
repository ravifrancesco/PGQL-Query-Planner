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

    GraphSettings graphSettings;
    HardwareCostSettings hardwareCostSettings;

    // constructor
    public NeighborMatchPlan(QueryVertex srcQueryVertex, QueryVertex dstQueryVertex,
                             boolean outgoing, QueryPlan parentPlan, Settings settings,
                             Set<QueryExpression> constraintsSet, Statistics statistics) {

        this.parent = parentPlan;
        this.srcVertex = srcQueryVertex;
        this.dstVertex = dstQueryVertex;
        this.outgoing = outgoing;

        this.graphSettings = settings.getGraphSettings();
        this.hardwareCostSettings = settings.getHardwareCostSettings();

        if (this.outgoing) {
            this.constraintsSrc = constraintsVertexArrayBuilder(srcQueryVertex, constraintsSet);
            this.constraintsDst = constraintsVertexArrayBuilder(dstQueryVertex, constraintsSet);
        } else {
            this.constraintsSrc = constraintsVertexArrayBuilder(dstQueryVertex, constraintsSet);
            this.constraintsDst = constraintsVertexArrayBuilder(srcQueryVertex, constraintsSet);
        }


        try {
            this.operatorCost = computeCost(statistics);
        } catch (ColumnDataTypeException e) {
            e.printStackTrace();
        }

    }

    // computes cost of the operator
    @Override
    public double computeCost(Statistics statistics) throws ColumnDataTypeException { // da rivedere

        double indexVertexCost = this.hardwareCostSettings.getIndexVertexCost();
        double cpuOperationCost = this.hardwareCostSettings.getCpuOperationCost();
        double vertexPropertyCost = this.hardwareCostSettings.getVertexPropertyCost();

        int srcVertexCardinality;
        int dstVertexCardinality = computeTotalVertexCardinality(this.constraintsDst, statistics, statistics.getVertexTableLength());
        dstVertexCardinality *= statistics.getAverageVertexDegree();

        srcVertexCardinality = computeTotalVertexCardinality(this.constraintsSrc, statistics, parent.getCardinality());

        double srcVertexScanCost = srcVertexCardinality * indexVertexCost;
        double dstVertexScanCost = dstVertexCardinality * indexVertexCost;

        srcVertexScanCost += srcVertexCardinality * (this.constraintsSrc.size() * (cpuOperationCost + vertexPropertyCost));
        dstVertexScanCost += dstVertexCardinality * (this.constraintsDst.size() * (cpuOperationCost + vertexPropertyCost));

        this.operatorCardinality = dstVertexCardinality;
        return srcVertexScanCost + dstVertexScanCost;

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
