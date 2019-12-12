package graphStatistics;

import org.apache.datasketches.ArrayOfStringsSerDe;
import org.apache.datasketches.frequencies.ItemsSketch;
import org.apache.datasketches.quantiles.DoublesSketch;
import org.apache.datasketches.quantiles.UpdateDoublesSketch;
import org.apache.datasketches.theta.UpdateSketch;

import java.io.FileOutputStream;
import java.io.IOException;

enum CsvType {
    EDGE,
    VERTEX
}

/**
 * This class contains the sketches for each column and some methods used by class graphDataSketches
 * The constructor takes boolean number as argument (wich indicates if column contains numbers or stirngs):
 *      - it creates a distinctCountingSketch
 *      - if boolean number == true it creates a quantileSketch
 *      - if boolean number == false it creates a mostFrequentSketch
 *
 * Enum CsvType is used to determine if column is from edge or vertex Csv and for determining directory for .bin files
 *
 * The sketches are saved in the object as private attributes
 */

public class graphColumnSketches {

    private UpdateSketch distinctCountingSketch; //Distinct counting
    private ItemsSketch<String> mostFrequentSketch; //Most frequent
    private UpdateDoublesSketch quantileSketch; //Quantile Sketch

    private boolean isNum;

    private CsvType type;

    // constructor
    public graphColumnSketches(Boolean number, CsvType type) {

        this.type = type;
        this.isNum = number;

        createDistinctCountingSketch();

        if (isNum) {
            createQuantileSketch();
            this.mostFrequentSketch = null;
        } else {
            createMostFrequentSketch();
            this.quantileSketch = null;
        }

    }

    // handles update of sketches in this object
    public void updateColumnSketches(String readValue) {

        if (this.isNum) {
            try {
                double newValue = Double.parseDouble(readValue);
                this.distinctCountingSketch.update(newValue);
                this.quantileSketch.update(newValue);
            } catch (NumberFormatException nfe) {
                //Aggiungere gestione errori
            }
        } else {
            this.distinctCountingSketch.update(readValue);
            this.mostFrequentSketch.update(readValue);
        }

    }

    // handles saving of sketches in this object to bin file in directory /graphName/columnName/
    public void saveColumnSketchToFile(String graphName, String columnName) { //Sistemare posizione di salvataggio e nomi

        String columnDir = createSavingDirectoryPatch(graphName, columnName);

        if (this.isNum) {
            saveQuantileSketchToFile(columnDir);
        } else {
            saveMostFrequentSketchToFile(columnDir);
        }
        saveDistinctCountingSketchToFile(columnDir);
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
            outDistinctCountingSketch.write(distinctCountingSketch.compact().toByteArray());
            outDistinctCountingSketch.close();
        } catch (IOException ioe) {
            //Aggiungere gestione errori
        }
    }

    // returns true if column contains numbers, otherwise returns false
    private Boolean isNum() {
        return isNum;
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

        if (this.type == CsvType.VERTEX) {
            columnDir = '/' + graphName + '/' + "vertex" + '/' + columnName;
        } else if (this.type == CsvType.EDGE) {
            columnDir = '/' + graphName + '/' + "edge" + '/' + columnName;
        } else {
            columnDir = null; //Aggiungere gestione type sbagliato
        }

        return columnDir;

    }

}
