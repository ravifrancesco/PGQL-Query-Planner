package graph.dataSketches.setup;

import exceptions.ColumnDataTypeException;
import exceptions.UnexpectedCSVType;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.apache.datasketches.ArrayOfStringsSerDe;
import org.apache.datasketches.frequencies.ItemsSketch;
import org.apache.datasketches.quantiles.DoublesSketch;
import org.apache.datasketches.quantiles.DoublesSketchBuilder;
import org.apache.datasketches.quantiles.UpdateDoublesSketch;
import org.apache.datasketches.theta.UpdateSketch;
import settings.Settings;
import settings.SketchesMemorySetting;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import static java.lang.Math.pow;

/**
 * This class contains the sketches for each column and some methods used by class graphDataSketches
 * The constructor takes ColumnDataTypes as argument (wich indicates type of data contained in the column):
 *      - if columnType is NUM true it creates a quantileSketch
 *      - if columnType is STRING false it creates a mostFrequentSketch and a distinctCountingSketch
 *      - other types can be added in enum ColumnDataTypes and data type specific methods can be added
 *
 * Enum CsvType is used to determine if column is from edge or vertex Csv and for determining directory for .bin files
 *
 * The sketches are saved in the object as private attributes
 */

public class GraphColumnSketchesWrite {

    private UpdateSketch distinctCountingSketch; //Distinct counting
    private ItemsSketch<String> mostFrequentSketch; //Most frequent
    private UpdateDoublesSketch quantileSketch; //Quantile Sketch

    private ColumnDataTypes columnType;

    private CsvTypes csvType;

    private SketchesMemorySetting sketchesMemorySetting;

    // constructor
    public GraphColumnSketchesWrite(ColumnDataTypes columnType, CsvTypes csvType, Settings settings) throws ColumnDataTypeException {

        this.csvType = csvType;
        this.columnType = columnType;

        this.sketchesMemorySetting = settings.getSketchesMemorySetting();

        if (columnType == ColumnDataTypes.NUM) {
            createQuantileSketch();
            this.mostFrequentSketch = null;
        } else if (columnType == ColumnDataTypes.STRING){
            createDistinctCountingSketch();
            createMostFrequentSketch();
            this.quantileSketch = null;
        } else {
            throw new ColumnDataTypeException();
        }

    }

    // handles update of sketches in this object
    public void updateColumnSketches(String readValue) throws ColumnDataTypeException {

        if (this.columnType == ColumnDataTypes.NUM) {
            try {
                double newValue = Double.parseDouble(readValue);
                this.quantileSketch.update(newValue);
            } catch (NumberFormatException nfe) {
                System.out.println("NumberFormatException: " + nfe.getMessage());
            }
        } else if (this.columnType == ColumnDataTypes.STRING){
            this.distinctCountingSketch.update(readValue);
            this.mostFrequentSketch.update(readValue);
        } else {
            throw new ColumnDataTypeException();
        }

    }

    // handles saving of sketches in this object to bin file in directory /graphName/columnName/
    public void saveColumnSketchToFile(String graphName, String columnName) throws ColumnDataTypeException { //Sistemare posizione di salvataggio e nomi

        String columnDir = null;
        try {
            columnDir = createSavingDirectoryPatch(graphName, columnName);
        } catch (UnexpectedCSVType unexpectedCSVType) {
            unexpectedCSVType.printStackTrace();
        }

        if (this.columnType == ColumnDataTypes.NUM) {
            saveQuantileSketchToFile(columnDir);
        } else if (this.columnType == ColumnDataTypes.STRING){
            saveDistinctCountingSketchToFile(columnDir);
            saveMostFrequentSketchToFile(columnDir);
        } else {
            throw new ColumnDataTypeException();
        }
    }

    // handles saving of distinctCountingSketch to .bin file
    private void saveQuantileSketchToFile(String columnDir) {

        //try (FileOutputStream outQuantileSketch = new FileOutputStream(columnDir + "QuantileSketch.bin")) {
        try (FileOutputStream outQuantileSketch = FileUtils.openOutputStream((new File(columnDir + "QuantileSketch.bin")))) {
            outQuantileSketch.write(this.quantileSketch.compact().toByteArray());
            outQuantileSketch.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // handles saving of mostFrequentSketch to .bin file
    private void saveMostFrequentSketchToFile(String columnDir) {
        //try (FileOutputStream outMostFrequentSketch = new FileOutputStream(columnDir + "MostFrequentSketch.bin")) {
        try (FileOutputStream outMostFrequentSketch = FileUtils.openOutputStream((new File(columnDir + "MostFrequentSketch.bin")))) {
            outMostFrequentSketch.write(this.mostFrequentSketch.toByteArray(new ArrayOfStringsSerDe()));
            outMostFrequentSketch.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // handles saving of quantileSketch to .bin file
    private void saveDistinctCountingSketchToFile(String columnDir) {

        //try (FileOutputStream outDistinctCountingSketch = new FileOutputStream(columnDir + "DistinctCountingSketch.bin")) {
        try (FileOutputStream outDistinctCountingSketch = FileUtils.openOutputStream((new File(columnDir + "DistinctCountingSketch.bin")))) {
            outDistinctCountingSketch.write(this.distinctCountingSketch.compact().toByteArray());
            outDistinctCountingSketch.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // returns true if column contains numbers, otherwise returns false
    public ColumnDataTypes columnDataTypes() {
        return columnType;
    }

    // handles creation of distinctCountingSketch object
    private void createDistinctCountingSketch() {
        this.distinctCountingSketch = UpdateSketch.builder().build();
    }

    // handles creation of mostFrequentSketch object
    private void createMostFrequentSketch() {

        int mostFrequentItemsNum;

        // set mostFrequentItemSketch dimension, please refer to DataSKetches documentation
        if (this.csvType == CsvTypes.VERTEX) {
            mostFrequentItemsNum = this.sketchesMemorySetting.getMostFrequentVertexItemsNum();
        } else if (this.csvType == CsvTypes.EDGE) {
            mostFrequentItemsNum = this.sketchesMemorySetting.getMostFrequentEdgeItemsNum();
        } else {
            mostFrequentItemsNum = this.sketchesMemorySetting.getDefaultFrequentItemsNum(); // default value, shouldn't enter the else statement
        }

        int hashMapSize = (int) pow(2, mostFrequentItemsNum);
        this.mostFrequentSketch = new ItemsSketch<String>(hashMapSize);

    }

    // handles creation of quantileSketch object
    private void createQuantileSketch () {

        int quantileSketchK;

        // set quantileSketch k, please refer to DataSKetches documentation
        if (this.csvType == CsvTypes.VERTEX) {
            quantileSketchK = this.sketchesMemorySetting.getQuantileSketchVertexK();
        } else if (this.csvType == CsvTypes.EDGE) {
            quantileSketchK = this.sketchesMemorySetting.getQuantileSketchEdgeK();
        } else {
            quantileSketchK = this.sketchesMemorySetting.getQuantileSketchDefaultK(); // default value, shouldn't enter the else statement
        }

        DoublesSketchBuilder quantileSketchBuilder = DoublesSketch.builder().setK(quantileSketchK);
        this.quantileSketch = quantileSketchBuilder.build();

    }

    // handles creation of saving directory path
    private String createSavingDirectoryPatch(String graphName, String columnName) throws UnexpectedCSVType {

        String columnDir;

        if (this.csvType == CsvTypes.VERTEX) {
            columnDir = graphName + '/' + "vertex" + '/' + columnName + '/';
        } else if (this.csvType == CsvTypes.EDGE) {
            columnDir = graphName + '/' + "edge" + '/' + columnName + '/';
        } else {
            columnDir = null; //Aggiungere gestione type sbagliato
            throw new UnexpectedCSVType();
        }

        return columnDir;

    }

    // getters
    public ColumnDataTypes getColumnType() {
        return columnType;
    }

    public UpdateSketch getDistinctCountingSketch() {
        return distinctCountingSketch;
    }

    public ItemsSketch<String> getMostFrequentSketch() {
        return mostFrequentSketch;
    }

    public UpdateDoublesSketch getQuantileSketch() {
        return quantileSketch;
    }

}
