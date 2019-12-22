package operators;

import graph.statistics.Statistics;
import oracle.pgql.lang.ir.QueryVertex;
import settings.GraphSettings;
import settings.HardwareCostSettings;
import settings.Settings;

/**
 * Cartesian Product operator
 * Used for retrieving the Cartesian product between two already matched vertices
 * ParentPlan1 is the left operator, parentPlan2 is the right operator
 * The construct assumes that parentPlan2 = parentPlan1.parent
 */

public class CartesianProductPlan implements QueryPlan, Comparable<QueryPlan> {

    private QueryVertex leftVertex;
    private QueryVertex rightVertex;

    private Boolean leftOutgoing;
    private Boolean rightOutgoing;

    private QueryPlan child;
    private QueryPlan parent1;
    private QueryPlan parent2;

    private double operatorCost;
    private int operatorCardinality;

    GraphSettings graphSettings;
    HardwareCostSettings hardwareCostSettings;

    // constructor
    public CartesianProductPlan(QueryVertex leftQueryVertex, QueryVertex rightQueryVertex,
                                QueryPlan parentPlan1, Settings settings, Statistics statistics) {

        this.parent1 = parentPlan1;
        this.parent2 = this.parent1.getParentPlan();
        this.leftVertex = leftQueryVertex;
        this.rightVertex = rightQueryVertex;

        this.graphSettings = settings.getGraphSettings();
        this.hardwareCostSettings = settings.getHardwareCostSettings();

        this.operatorCost = computeCost(statistics);

    }

    // computes cost of the operator
    @Override
    public double computeCost(Statistics statistics) { // controllare se è giusto

        double cpuOperationCost = this.hardwareCostSettings.getCpuOperationCost();

        int leftVertexCardinality = parent1.getCardinality();
        int rightVertexCardinality = parent2.getCardinality();

        double cartesianProductCost = rightVertexCardinality * leftVertexCardinality * cpuOperationCost;

        this.operatorCardinality = leftVertexCardinality * rightVertexCardinality;

        return cartesianProductCost;

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
