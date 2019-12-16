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
 * Cartesian Product operator
 * Used for retrieving the Cartesian product between two already matched vertices
 * ParentPlan1 is the left operator, parentPlan2 is the right operator
 * The construct assumes that parentPlan2 = parentPlan1.parent
 */

public class CartesianProductPlan extends ConstraintsArrayBuilder implements QueryPlan, Comparable<QueryPlan> {

    private QueryVertex leftVertex;
    private QueryVertex rightVertex;

    private Boolean leftOutgoing;
    private Boolean rightOutgoing;

    private QueryPlan child;
    private QueryPlan parent1;
    private QueryPlan parent2;

    private ArrayList<Constraint> constraintsLeft;
    private ArrayList<Constraint> constraintsRight;

    private double operatorCost;
    private int operatorCardinality;

    GraphSettings graphSettings;
    HardwareCostSettings hardwareCostSettings;

    // constructor
    public CartesianProductPlan(QueryVertex leftQueryVertex, QueryVertex rightQueryVertex,
                                QueryPlan parentPlan1, Settings settings,
                                Set<QueryExpression> constraintsSet, Statistics statistics) {

        this.parent1 = parentPlan1;
        this.parent2 = this.parent1.getParentPlan();
        this.leftVertex = leftQueryVertex;
        this.rightVertex = rightQueryVertex;

        this.graphSettings = settings.getGraphSettings();
        this.hardwareCostSettings = settings.getHardwareCostSettings();

        this.constraintsLeft = constraintsVertexArrayBuilder(leftQueryVertex, constraintsSet);
        this.constraintsRight = constraintsVertexArrayBuilder(rightQueryVertex, constraintsSet);

        this.operatorCost = computeCost(statistics);

    }

    // computes cost of the operator
    @Override
    public double computeCost(Statistics statistics) { // da rivedere

        double indexVertexCost = this.hardwareCostSettings.getIndexVertexCost();
        double cpuOperationCost = this.hardwareCostSettings.getCpuOperationCost();
        double vertexPropertyCost = this.hardwareCostSettings.getVertexPropertyCost();

        int leftVertexCardinality = computeTotalVertexCardinality(this.constraintsLeft, statistics, this.parent1.getCardinality());;
        int rightVertexCardinality = computeTotalVertexCardinality(this.constraintsRight, statistics, this.parent2.getCardinality());

        double leftVertexScanCost = leftVertexCardinality * indexVertexCost;
        double rightVertexScanCost = rightVertexCardinality * indexVertexCost;

        leftVertexScanCost += leftVertexCardinality * (this.constraintsLeft.size() * (cpuOperationCost + vertexPropertyCost));
        rightVertexScanCost += rightVertexCardinality * (this.constraintsRight.size() * (cpuOperationCost + vertexPropertyCost));

        this.operatorCardinality = leftVertexCardinality * rightVertexCardinality;
        return leftVertexScanCost + rightVertexScanCost;

    }

    // computes total cardinality of given vertex constraints
    private int computeTotalVertexCardinality(ArrayList<Constraint> constraints, Statistics statistics, int totalCardinality){

        double selectivity = 1;

        for (Constraint constraint: constraints) {
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

    public QueryPlan getParent2() {
        return parent2;
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
