package graph.dataSketches.setup;

import org.apache.datasketches.ArrayOfStringsSerDe;
import org.apache.datasketches.frequencies.ItemsSketch;
import org.apache.datasketches.quantiles.DoublesSketch;
import org.apache.datasketches.quantiles.UpdateDoublesSketch;
import org.apache.datasketches.theta.UpdateSketch;

import java.io.FileOutputStream;
import java.io.IOException;

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

    // constructor
    public GraphColumnSketchesWrite(ColumnDataTypes columnType, CsvTypes csvType) {

        this.csvType = csvType;
        this.columnType = columnType;

        if (columnType == ColumnDataTypes.NUM) {
            createQuantileSketch();
            this.mostFrequentSketch = null;
        } else if (columnType == ColumnDataTypes.STRING){
            createDistinctCountingSketch();
            createMostFrequentSketch();
            this.quantileSketch = null;
        } else {
            // aggiungere gestione altri tipi
        }

    }

    // handles update of sketches in this object
    public void updateColumnSketches(String readValue) {

        if (this.columnType == ColumnDataTypes.NUM) {
            try {
                double newValue = Double.parseDouble(readValue);
                this.quantileSketch.update(newValue);
            } catch (NumberFormatException nfe) {
                //Aggiungere gestione errori
            }
        } else if (this.columnType == ColumnDataTypes.STRING){
            this.distinctCountingSketch.update(readValue);
            this.mostFrequentSketch.update(readValue);
        } else {
            // aggiungere gestione altri tipi
        }

    }

    // handles saving of sketches in this object to bin file in directory /graphName/columnName/
    public void saveColumnSketchToFile(String graphName, String columnName) { //Sistemare posizione di salvataggio e nomi

        String columnDir = createSavingDirectoryPatch(graphName, columnName);

        if (this.columnType == ColumnDataTypes.NUM) {
            saveQuantileSketchToFile(columnDir);
        } else if (this.columnType == ColumnDataTypes.STRING){
            saveDistinctCountingSketchToFile(columnDir);
            saveMostFrequentSketchToFile(columnDir);
        } else {
            // aggiungere gestione altri tipi
        }
    }

    // handles saving of distinctCountingSketch to .bin file
    private void saveDistinctCountingSketchToFile(String columnDir) {
        try {
            FileOutputStream outQuantileSketch = new FileOutputStream(columnDir + "/QuantileSketch.bin");
            outQuantileSketch.write(this.quantileSketch.compact().toByteArray());
            outQuantileSketch.close();
        } catch (IOException ioe) {
            //Aggiungere gestione errori
        }
    }

    // handles saving of mostFrequentSketch to .bin file
    private void saveMostFrequentSketchToFile(String columnDir) {
        try {
            FileOutputStream outMostFrequentSketch = new FileOutputStream(columnDir + "/MostFrequentSketch.bin");
            outMostFrequentSketch.write(this.mostFrequentSketch.toByteArray(new ArrayOfStringsSerDe()));
            outMostFrequentSketch.close();
        } catch (IOException ioe) {
            //Aggiungere gestione errori
        }
    }

    // handles saving of quantileSketch to .bin file
    private void saveQuantileSketchToFile(String columnDir) {
        try {
            FileOutputStream outDistinctCountingSketch = new FileOutputStream(columnDir + "/DistinctCountingSketch.bin");
            outDistinctCountingSketch.write(this.distinctCountingSketch.compact().toByteArray());
            outDistinctCountingSketch.close();
        } catch (IOException ioe) {
            //Aggiungere gestione errori
        }
    }

    // returns true if column contains numbers, otherwise returns false
    public ColumnDataTypes columnDataTypes() {
        return columnType;
    }

    // handles creation of distinctCountingSketch object
    private void createDistinctCountingSketch() {
        this.distinctCountingSketch = UpdateSketch.builder().build(); // gestire dimensione sketch
    }

    // handles creation of mostFrequentSketch object
    private void createMostFrequentSketch() {
        this.mostFrequentSketch = new ItemsSketch<String>(64); //Gestire dimensione sketch
    }

    // handles creation of quantileSketch object
    private void createQuantileSketch () { // default k=128, gestire dimensione
        this.quantileSketch = DoublesSketch.builder().build();
    }

    // handles creation of saving directory path
    private String createSavingDirectoryPatch(String graphName, String columnName) {

        String columnDir;

        if (this.csvType == CsvTypes.VERTEX) {
            columnDir = '/' + graphName + '/' + "vertex" + '/' + columnName;
        } else if (this.csvType == CsvTypes.EDGE) {
            columnDir = '/' + graphName + '/' + "edge" + '/' + columnName;
        } else {
            columnDir = null; //Aggiungere gestione type sbagliato
        }

        return columnDir;

    }

}
