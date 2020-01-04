package graph.dataSketches.load;

import graph.dataSketches.setup.ColumnDataTypes;
import org.apache.datasketches.ArrayOfStringsSerDe;
import org.apache.datasketches.frequencies.ItemsSketch;
import org.apache.datasketches.memory.Memory;
import org.apache.datasketches.quantiles.DoublesSketch;
import org.apache.datasketches.theta.Sketch;
import org.apache.datasketches.theta.Sketches;

import java.io.FileInputStream;
import java.io.IOException;

/**
 * This class handles the reading of the .bin files representing the sketches. Sketches are then stored as
 * private attributes accessible with the get methods. Setters are used for the sketches that will point to null
 */

public class GraphColumnSketchesRead {

    private Sketch distinctCountingSketch; //Distinct counting
    private ItemsSketch<String> mostFrequentSketch; //Most frequent
    private DoublesSketch quantileSketch; //Quantile Sketch

    private ColumnDataTypes columnType;

    // reads distinctCountingSketches from .bin
    public void readDistinctCountingSketch(String pathToColumnFolder) throws IOException {
        FileInputStream inDistinctCountingSketch = new FileInputStream( pathToColumnFolder + "DistinctCountingSketch.bin");
        byte[] bytes = new byte[inDistinctCountingSketch.available()];
        inDistinctCountingSketch.read(bytes);
        inDistinctCountingSketch.close();
        this.distinctCountingSketch = Sketches.wrapSketch(Memory.wrap(bytes));
    }

    // reads mostFrequentSketch from .bin
    public void readMostFrequentSketch(String pathToColumnFolder) throws IOException {
        FileInputStream inMostFrequentSketch = new FileInputStream( pathToColumnFolder + "MostFrequentSketch.bin");
        byte[] bytes = new byte[inMostFrequentSketch.available()];
        inMostFrequentSketch.read(bytes);
        inMostFrequentSketch.close();
        this.mostFrequentSketch = ItemsSketch.getInstance(Memory.wrap(bytes), new ArrayOfStringsSerDe());
    }

    // reads quantileSketch from .bin
    public void readQuantileSketch(String pathToColumnFolder) throws IOException {
        FileInputStream inQuantileSketch = new FileInputStream(pathToColumnFolder + "QuantileSketch.bin");
        byte[] bytes = new byte[inQuantileSketch.available()];
        inQuantileSketch.read(bytes);
        inQuantileSketch.close();
        this.quantileSketch = DoublesSketch.wrap(Memory.wrap(bytes));
    }


    // setters
    public void setDistinctCountingSketch(Sketch distinctCountingSketch) {
        this.distinctCountingSketch = distinctCountingSketch;
    }

    public void setMostFrequentSketch(ItemsSketch<String> mostFrequentSketch) {
        this.mostFrequentSketch = mostFrequentSketch;
    }

    public void setColumnType(ColumnDataTypes columnType) {
        this.columnType = columnType;
    }

    public void setQuantileSketch(DoublesSketch quantileSketch) {
        this.quantileSketch = quantileSketch;
    }

    // getters
    public Sketch getDistinctCountingSketch() {
        return distinctCountingSketch;
    }

    public ItemsSketch<String> getMostFrequentSketch() {
        return mostFrequentSketch;
    }

    public DoublesSketch getQuantileSketch() {
        return quantileSketch;
    }

    public ColumnDataTypes getColumnType() {
        return columnType;
    }
}
