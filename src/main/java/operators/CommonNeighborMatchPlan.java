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

import static java.lang.Math.log;
import static java.lang.Math.pow;

/**
 * Common Neighbor Match Operator
 * Used for matching a common neighbor of two already matched vertices.
 */

public class CommonNeighborMatchPlan extends ConstraintsArrayBuilder implements QueryPlan, Comparable<QueryPlan> {

    private QueryVertex leftVertex;
    private QueryVertex rightVertex;
    private QueryVertex vertexToFind;

    boolean leftOutgoing;
    boolean rightOutgoing;

    private QueryPlan child;
    private QueryPlan parent;

    private ArrayList<Constraint> constraintsVertexToFind;

    private double operatorCost;
    private int operatorCardinality;

    GraphSettings graphSettings;
    HardwareCostSettings hardwareCostSettings;

    // constructor
    public CommonNeighborMatchPlan(QueryVertex leftQueryVertex, QueryVertex rightQueryVertex, QueryVertex vertexToFind,
                                   boolean leftOutgoing, boolean rightOutgoing,
                                   QueryPlan parentPlan1, Settings settings,
                                   Set<QueryExpression> constraintsSet, Statistics statistics) {

        this.parent = parentPlan1;
        if (parent instanceof CartesianProductPlan) {
            // è giusto
        } else {
            // altrimenti non ha senso (?)
        }

        this.leftVertex = leftQueryVertex;
        this.rightVertex = rightQueryVertex;
        this.vertexToFind = vertexToFind;

        this.leftOutgoing = leftOutgoing;
        this.rightOutgoing = rightOutgoing;

        this.graphSettings = settings.getGraphSettings();
        this.hardwareCostSettings = settings.getHardwareCostSettings();

        this.constraintsVertexToFind = constraintsVertexArrayBuilder(vertexToFind, constraintsSet);

        this.operatorCost = computeCost(statistics);

    }

    // computes cost of the operator
    @Override
    public double computeCost(Statistics statistics) {

         /*
         Da implementare per i due outgoing
         if (this.leftOutgoing && this.rightOutgoing) {
             return computeCostBothOutgoing(statistics);
         } else if (this.leftOutgoing && !this.rightOutgoing) {

         } else if (!this.leftOutgoing && this.rightOutgoing) {

         } else {

         }
          */

        return computeCostBothOutgoing(statistics);

    }

    // computes cost if leftOutgoing and rightOutgoing are both true ( a -> b <- c )
    private double computeCostBothOutgoing(Statistics statistics) {

        double neighborSearchCost;
        double binarySearchCost;
        double dstVertexSelectivity;
        double filterCost;
        int noFilterCardinality;

        double indexVertexCost = this.hardwareCostSettings.getIndexVertexCost();
        double cpuOperationCost = this.hardwareCostSettings.getCpuOperationCost();
        double vertexPropertyCost = this.hardwareCostSettings.getVertexPropertyCost();

        int vertexCardinality = parent.getCardinality();

        dstVertexSelectivity = computeTotalVertexSelectivity(this.constraintsVertexToFind, statistics);
        noFilterCardinality = (int) (1/(pow(2, statistics.getAverageVertexDegree())) * vertexCardinality);

        neighborSearchCost = (statistics.getAverageVertexDegree() * cpuOperationCost) * 2;
        neighborSearchCost *= vertexCardinality;
        binarySearchCost = (log(statistics.getAverageVertexDegree()) * statistics.getAverageVertexDegree()) * vertexCardinality;
        binarySearchCost *= cpuOperationCost;

        filterCost = this.constraintsVertexToFind.size() * (cpuOperationCost + vertexPropertyCost);
        filterCost += indexVertexCost;
        filterCost *= noFilterCardinality;

        this.operatorCardinality = (int) (noFilterCardinality * dstVertexSelectivity);

        return neighborSearchCost + binarySearchCost + filterCost;

    }

    // computes cost if leftOutgoing is true and rightOutgoing is false ( a -> b -> c ) (DA IMPLEMENTARE)
    private double computeCostLeftOutgoing(Statistics statistics) {

        double neighborSearchCost;
        double binarySearchCost;
        double dstVertexSelectivity;
        double filterCost;
        int noFilterCardinality;

        double indexVertexCost = this.hardwareCostSettings.getIndexVertexCost();
        double cpuOperationCost = this.hardwareCostSettings.getCpuOperationCost();
        double vertexPropertyCost = this.hardwareCostSettings.getVertexPropertyCost();

        int vertexCardinality = parent.getCardinality();

        dstVertexSelectivity = computeTotalVertexSelectivity(this.constraintsVertexToFind, statistics);
        noFilterCardinality = (int) (1 / (pow(2, statistics.getAverageVertexDegree())) * vertexCardinality);

        neighborSearchCost = (statistics.getAverageVertexDegree() * cpuOperationCost) * 2;
        neighborSearchCost *= vertexCardinality;
        binarySearchCost = (log(statistics.getAverageVertexDegree()) * statistics.getAverageVertexDegree()) * vertexCardinality;
        binarySearchCost *= cpuOperationCost;

        filterCost = this.constraintsVertexToFind.size() * (cpuOperationCost + vertexPropertyCost);
        filterCost += indexVertexCost;
        filterCost *= noFilterCardinality;

        this.operatorCardinality = (int) (noFilterCardinality * dstVertexSelectivity);

        return neighborSearchCost + binarySearchCost + filterCost;
    }

    // computes total cardinality of given vertex constraints
    private double computeTotalVertexSelectivity(ArrayList<Constraint> constraints, Statistics statistics, int totalCardinality) throws ColumnDataTypeException {

        double selectivity = 1;

        for (Constraint constraint: constraints) {
            selectivity *= computeConstraintSelectivity(constraint, statistics);
        }

        return selectivity;

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
