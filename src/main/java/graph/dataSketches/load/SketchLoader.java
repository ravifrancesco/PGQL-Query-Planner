package graph.dataSketches.load;

import graph.dataSketches.setup.ColumnDataTypes;

import java.io.IOException;

/**
 * This class handles the loading of the sketches for the indicated column (pathToFolder). It returns a
 * graphColumnSketchesRead containing sketches depending on column data type. If the loader is loading data from:
 *      - a column of strings, it tells GraphColumnSketchesRead not to read the quantileSketch
 *      - a column of numbs, it tells GraphColumnSketchesRead not to read the mostFrequentSketch and the distinctCounting
 * If new types are added to enum new reading settings must be added
 *
 * The sketch which is set to point to null
 */

public class SketchLoader {

    // loads sketches from bin file
    protected GraphColumnSketchesRead loadColumnSketch(String pathToFolder, String csvHeader, ColumnDataTypes columnType) throws IOException { //Gestire errori

        GraphColumnSketchesRead graphColumnSketchesRead = new GraphColumnSketchesRead();
        String pathToColumnFolder = pathToFolder + csvHeader + '/';

        if (columnType == ColumnDataTypes.NUM) {
            graphColumnSketchesRead.readQuantileSketch(pathToColumnFolder);
            graphColumnSketchesRead.setDistinctCountingSketch(null);
            graphColumnSketchesRead.setMostFrequentSketch(null);
            graphColumnSketchesRead.setColumnType(columnType);
        } else if (columnType == ColumnDataTypes.STRING) {
            graphColumnSketchesRead.readMostFrequentSketch(pathToColumnFolder);
            graphColumnSketchesRead.readDistinctCountingSketch(pathToColumnFolder);
            graphColumnSketchesRead.setQuantileSketch(null);
            graphColumnSketchesRead.setColumnType(columnType);
        }

        return graphColumnSketchesRead;

    }
}
