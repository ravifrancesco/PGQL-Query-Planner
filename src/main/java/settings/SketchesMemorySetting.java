package settings;

/**
 * Package containing settings for the sketches and methods to change them
 * For better understanding please refer to DataSketches library documentation.
 *
 * Could be individual for each column (needs appropriate implementation)
 *
 */

public class SketchesMemorySetting {

    int mostFrequentVertexItemsNum = 64;
    int mostFrequentEdgeItemsNum = 64;
    int defaultFrequentItemsNum = 32;

    int quantileSketchVertexK = 128;
    int quantileSketchEdgeK = 128;
    int quantileSketchDefaultK = 128;

    // more can be added

    // setters
    public void setMostFrequentVertexItemsNum(int mostFrequentVertexItemsNum) {
        this.mostFrequentVertexItemsNum = mostFrequentVertexItemsNum;
    }

    public void setMostFrequentEdgeItemsNum(int mostFrequentEdgeItemsNum) {
        this.mostFrequentEdgeItemsNum = mostFrequentEdgeItemsNum;
    }

    public void setQuantileSketchVertexK(int quantileSketchVertexK) {
        this.quantileSketchVertexK = quantileSketchVertexK;
    }

    public void setQuantileSketchEdgeK(int quantileSketchEdgeK) {
        this.quantileSketchEdgeK = quantileSketchEdgeK;
    }

    // getters
    public int getMostFrequentVertexItemsNum() {
        return mostFrequentVertexItemsNum;
    }

    public int getMostFrequentEdgeItemsNum() {
        return mostFrequentEdgeItemsNum;
    }

    public int getDefaultFrequentItemsNum() {
        return defaultFrequentItemsNum;
    }

    public int getQuantileSketchVertexK() {
        return quantileSketchVertexK;
    }

    public int getQuantileSketchEdgeK() {
        return quantileSketchEdgeK;
    }

    public int getQuantileSketchDefaultK() {
        return quantileSketchDefaultK;
    }

}
