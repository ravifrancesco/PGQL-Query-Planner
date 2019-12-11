package costModel;

import org.apache.commons.lang3.SystemUtils;

public class CostModel {

    private int blockSize;
    private double sequentialBlockCost = 1;
    private double randomBlockCost = 4;
    private double sequentialTupleCost = 0.01;
    private double indexTupleCost = 0.005;
    private double cpuOperationCost = 0.0025;

    //CONSTRUCTOR

    public CostModel() { //CORREGGERE ERRORE SYSTEM UTILS
        if (SystemUtils.IS_OS_WINDOWS) {
            blockSize = 4096;
        } else if (SystemUtils.IS_OS_UNIX) {
            blockSize = 8192;
        } else {
            //Add error "System not recognized"
        }
    }

    //SETTERS

    public void setBlockSize(int blockSize) {
        this.blockSize = blockSize;
    }

    public void setSequentialBlockCost(double sequentialBlockCost) {
        this.sequentialBlockCost = sequentialBlockCost;
    }

    public void setRandomBlockCost(double randomBlockCost) {
        this.randomBlockCost = randomBlockCost;
    }

    public void setSequentialTupleCost(double sequentialTupleCost) {
        this.sequentialTupleCost = sequentialTupleCost;
    }

    public void setIndexTupleCost(double indexTupleCost) {
        this.indexTupleCost = indexTupleCost;
    }

    public void setCpuOperationCost(double cpuOperationCost) {
        this.cpuOperationCost = cpuOperationCost;
    }

    //METHODS

    public double costCalculator (int tupleNum, int tupleSize, boolean sequential) {

        int totalReadingSize;
        int totalReadBlocks;
        double totalCost;

        totalReadingSize = tupleNum * tupleSize;
        totalReadBlocks = (int) Math.ceil(totalReadingSize / (double) blockSize);

        if (sequential) {
            totalCost = tupleNum * sequentialTupleCost + totalReadBlocks * sequentialBlockCost;
        } else {
            totalCost = tupleNum * indexTupleCost + totalReadBlocks * randomBlockCost;
        }

        return totalCost;

    }

}
