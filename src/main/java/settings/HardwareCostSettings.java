package settings;

import settings.utils.HardwareBenchmark;

/**
 * Cost of hardware operations. Can be customized through set methods
 */

public class HardwareCostSettings extends HardwareBenchmark {

    private double sequentialVertexCost;
    private double indexVertexCost;
    private double indexEdgeCost;

    private double vertexLabelCost;
    private double edgeLabelCost;

    private double vertexPropertyCost;
    private double edgePropertyCost;

    private double cpuOperationCost;

    // constructor
    public HardwareCostSettings() {
        // da fare
    }

    // setters
    public void setSequentialVertexCost(double sequentialVertexCost) {
        this.sequentialVertexCost = sequentialVertexCost;
    }

    public void setIndexVertexCost(double indexVertexCost) {
        this.indexVertexCost = indexVertexCost;
    }

    public void setIndexEdgeCost(double indexEdgeCost) {
        this.indexEdgeCost = indexEdgeCost;
    }

    public void setVertexLabelCost(double vertexLabelCost) {
        this.vertexLabelCost = vertexLabelCost;
    }

    public void setEdgeLabelCost(double edgeLabelCost) {
        this.edgeLabelCost = edgeLabelCost;
    }

    public void setVertexPropertyCost(double vertexPropertyCost) {
        this.vertexPropertyCost = vertexPropertyCost;
    }

    public void setEdgePropertyCost(double edgePropertyCost) {
        this.edgePropertyCost = edgePropertyCost;
    }

    public void setCpuOperationCost(double cpuOperationCost) {
        this.cpuOperationCost = cpuOperationCost;
    }

    // getters
    public double getSequentialVertexCost() {
        return sequentialVertexCost;
    }

    public double getIndexVertexCost() {
        return indexVertexCost;
    }

    public double getIndexEdgeCost() {
        return indexEdgeCost;
    }

    public double getVertexLabelCost() {
        return vertexLabelCost;
    }

    public double getEdgeLabelCost() {
        return edgeLabelCost;
    }

    public double getVertexPropertyCost() {
        return vertexPropertyCost;
    }

    public double getEdgePropertyCost() {
        return edgePropertyCost;
    }

    public double getCpuOperationCost() {
        return cpuOperationCost;
    }

}

