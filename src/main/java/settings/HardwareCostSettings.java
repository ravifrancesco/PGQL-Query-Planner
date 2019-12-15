package settings;

import org.apache.commons.lang3.SystemUtils;

/**
 * Cost of hardware operations. Can be customized through set methods
 * Attributes are static as hardware performance is the same for the entire program
 */

public class HardwareCostSettings {

    static private int blockSize;
    static private double sequentialBlockCost = 1;
    static private double randomBlockCost = 4;
    static private double sequentialTupleCost = 0.01;
    static private double indexTupleCost = 0.005;
    static private double cpuOperationCost = 0.0025;

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
        HardwareCostSettings.blockSize = blockSize;
    }

    public void setSequentialBlockCost(double sequentialBlockCost) {
        HardwareCostSettings.sequentialBlockCost = sequentialBlockCost;
    }

    public void setRandomBlockCost(double randomBlockCost) {
        HardwareCostSettings.randomBlockCost = randomBlockCost;
    }

    public void setSequentialTupleCost(double sequentialTupleCost) {
        HardwareCostSettings.sequentialTupleCost = sequentialTupleCost;
    }

    public void setIndexTupleCost(double indexTupleCost) {
        HardwareCostSettings.indexTupleCost = indexTupleCost;
    }

    public void setCpuOperationCost(double cpuOperationCost) {
        HardwareCostSettings.cpuOperationCost = cpuOperationCost;
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
