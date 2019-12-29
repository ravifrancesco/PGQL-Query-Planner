package operators;

import exceptions.ColumnDataTypeException;
import graph.statistics.Comparators;
import graph.statistics.Statistics;
import operators.utils.Constraint;
import operators.utils.ConstraintType;
import operators.utils.ConstraintsArrayBuilder;
import oracle.pgql.lang.ir.QueryEdge;
import oracle.pgql.lang.ir.QueryExpression;
import oracle.pgql.lang.ir.QueryVertex;
import settings.GraphSettings;
import settings.HardwareCostSettings;
import settings.Settings;

import java.util.ArrayList;
import java.util.Set;

import static java.lang.Math.log;

/**
 * Edge Match Operator
 * Used for matching an edge between already two matched vertices.
 */

public class EdgeMatchPlan extends ConstraintsArrayBuilder implements QueryPlan, Comparable<QueryPlan>{

    private QueryVertex srcVertex;
    private QueryVertex dstVertex;

    private QueryEdge edgeToFind;

    private QueryPlan child;
    private QueryPlan parent;

    private ArrayList<Constraint> constraintEdgeToFind;

    private double operatorCost;
    private int operatorCardinality;

    GraphSettings graphSettings;
    HardwareCostSettings hardwareCostSettings;

    // constructor
    public EdgeMatchPlan(QueryVertex srcQueryVertex, QueryVertex dstQueryVertex, QueryEdge edgeToFind,
                         QueryPlan parentPlan, Settings settings,
                         Set<QueryExpression> constraintsSet, Statistics statistics) {

        this.parent = parentPlan;
        if (parent instanceof CartesianProductPlan) {
            // è giusto
        } else {
            // altrimenti non ha senso (?)
        }

        this.srcVertex = srcQueryVertex;
        this.dstVertex = dstQueryVertex;

        this.edgeToFind = edgeToFind;

        this.graphSettings = settings.getGraphSettings();
        this.hardwareCostSettings = settings.getHardwareCostSettings();

        this.constraintEdgeToFind = constraintsEdgeArrayBuilder(edgeToFind, constraintsSet);

        this.operatorCost = computeCost(statistics);

    }

    // computes cost of the operator
    @Override
    public double computeCost(Statistics statistics) { // controllare se è giusto

        double binarySearchCost;
        double filterCost;
        double totalEdgeSelectivity;
        double dstSelectivity;
        int noFilterCardinality;

        double indexEdgeCost = this.hardwareCostSettings.getIndexEdgeCost();
        double cpuOperationCost = this.hardwareCostSettings.getCpuOperationCost();
        double edgePropertyCost = this.hardwareCostSettings.getEdgePropertyCost();

        totalEdgeSelectivity = computeTotalEdgeSelectivity(this.constraintEdgeToFind, statistics);

        int vertexCardinality = parent.getCardinality();

        dstSelectivity = statistics.getAverageVertexDegree() / (double) statistics.getVertexTableLength();
        noFilterCardinality = (int) (dstSelectivity * statistics.getAverageVertexDegree() * vertexCardinality);

        binarySearchCost = log(statistics.getAverageVertexDegree()) * vertexCardinality;
        binarySearchCost *= cpuOperationCost;

        filterCost = this.constraintEdgeToFind.size() * (cpuOperationCost + edgePropertyCost);
        filterCost += indexEdgeCost;
        filterCost *= noFilterCardinality;

        this.operatorCardinality = (int) (noFilterCardinality * totalEdgeSelectivity);

        return filterCost + binarySearchCost;

    }

    // computes total selectivity of given edge constraints
    private double computeTotalEdgeSelectivity(ArrayList<Constraint> constraints, Statistics statistics) {
        double selectivity = 1;

        for (Constraint constraint: constraints) {
            try {
                selectivity *= computeConstraintSelectivity(constraint, statistics);
            } catch (ColumnDataTypeException e) {
                e.printStackTrace();
            }
        }

        return selectivity;
    }

    // computes total cardinality of given vertex constraints
    private double computeTotalVertexCardinality(ArrayList<Constraint> constraints, Statistics statistics, int totalCardinality) throws ColumnDataTypeException {

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
        return null;
    }

    @Override
    public QueryPlan getChildPlan() {
        return null;
    }

    // compare operators on costs
    @Override
    public int compareTo(QueryPlan queryPlan) {
        return (int) (this.operatorCost - queryPlan.getOperatorCost());
    }

}
