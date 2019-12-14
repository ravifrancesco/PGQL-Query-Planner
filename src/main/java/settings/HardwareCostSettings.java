package settings;

import org.apache.commons.lang3.SystemUtils;

/**
 * Cost of hardware operations. Can be customized through set methods
 */

public class HardwareCostSettings {

    private int blockSize;
    private double sequentialBlockCost = 1;
    private double randomBlockCost = 4;
    private double sequentialTupleCost = 0.01;
    private double indexTupleCost = 0.005;
    private double cpuOperationCost = 0.0025;

    // constructor
    public HardwareCostSettings() {

        if (SystemUtils.IS_OS_WINDOWS) {
            blockSize = 4096;
        } else if (SystemUtils.IS_OS_UNIX) {
            blockSize = 8192;
        } else {
            blockSize = 8192; // shouldn't enter else statement
        }

    }

    // setters
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

    // getters
    public int getBlockSize() {
        return blockSize;
    }

    public double getSequentialBlockCost() {
        return sequentialBlockCost;
    }

    public double getRandomBlockCost() {
        return randomBlockCost;
    }

    public double getSequentialTupleCost() {
        return sequentialTupleCost;
    }

    public double getIndexTupleCost() {
        return indexTupleCost;
    }

    public double getCpuOperationCost() {
        return cpuOperationCost;
    }

}
