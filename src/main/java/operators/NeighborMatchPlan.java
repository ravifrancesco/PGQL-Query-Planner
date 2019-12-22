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

import static java.lang.Math.log;

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

    private ArrayList<Constraint> constraints;

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

        this.constraints = constraintsVertexArrayBuilder(dstQueryVertex, constraintsSet);

        this.operatorCost = computeCost(statistics);

    }

    // computes cost of the operator
    @Override
    public double computeCost(Statistics statistics) { // controllare se è giusto

        if (this.outgoing) {
            return computeCostOutgoing(statistics);
        } else {
            return computeCostIngoing(statistics);
        }

    }

    // computes cost if boolean outgoing is true
    private double computeCostOutgoing(Statistics statistics) {

        double searchCost;
        double dstVertexSelectivity;
        double filterCost;
        int noFilterCardinality;

        double indexVertexCost = this.hardwareCostSettings.getIndexVertexCost();
        double cpuOperationCost = this.hardwareCostSettings.getCpuOperationCost();
        double vertexPropertyCost = this.hardwareCostSettings.getVertexPropertyCost();

        int srcVertexCardinality = parent.getCardinality();

        dstVertexSelectivity = computeTotalVertexSelectivity(this.constraints, statistics);
        noFilterCardinality = statistics.getAverageVertexDegree() * srcVertexCardinality;

        searchCost = statistics.getAverageVertexDegree() * cpuOperationCost;
        searchCost *= srcVertexCardinality;

        filterCost = this.constraints.size() * (cpuOperationCost + vertexPropertyCost);
        filterCost += indexVertexCost;
        filterCost *= noFilterCardinality;

        this.operatorCardinality = (int) (noFilterCardinality * dstVertexSelectivity);

        return filterCost + searchCost;

    }

    // computes cost if outgoing is false
    private double computeCostIngoing(Statistics statistics) {

        double binarySearchCost;
        double dstVertexSelectivity;
        double filterCost;
        int noFilterCardinality;

        double indexVertexCost = this.hardwareCostSettings.getIndexVertexCost();
        double cpuOperationCost = this.hardwareCostSettings.getCpuOperationCost();
        double vertexPropertyCost = this.hardwareCostSettings.getVertexPropertyCost();

        int srcVertexCardinality = parent.getCardinality();

        dstVertexSelectivity = computeTotalVertexSelectivity(this.constraints, statistics);
        noFilterCardinality = statistics.getAverageVertexDegree() * statistics.getAverageVertexDegree();

        binarySearchCost = (log(statistics.getAverageVertexDegree()) * srcVertexCardinality) * statistics.getVertexTableLength();
        binarySearchCost *= cpuOperationCost;

        filterCost = this.constraints.size() * (cpuOperationCost + vertexPropertyCost);
        filterCost += indexVertexCost;
        filterCost *= noFilterCardinality;

        this.operatorCardinality = (int) (noFilterCardinality * dstVertexSelectivity);

        return filterCost + binarySearchCost;

    }

    // computes total selectivity of given vertex constraints
    private double computeTotalVertexSelectivity(ArrayList<Constraint> constraints, Statistics statistics){

        double selectivity = 1;

        for (Constraint constraint: constraints) {
            selectivity *= computeConstraintSelectivity(constraint, statistics);
        }

        return selectivity;

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
