package graph.dataSketches.load;

import java.io.IOException;

/**
 * This class handles the loading of the 2 sketches for the indicated column (pathToFolder)
 * It returns a graphColumnSketchesRead containing the 2 sketches. If the loader is loading data from:
 *      - a column of strings, it tells GraphColumnSketchesRead not to read the quantileSketch
 *      - a column of numbs, it tells GraphColumnSketchesRead not to read the mostFrequentSketch
 * The sketch which is set to point to null
 */

public class SketchLoader {

    // loads sketches from bin file
    protected GraphColumnSketchesRead loadColumnSketch(String pathToFolder, String csvHeader, Boolean isNum) throws IOException { //Gestire errori

        GraphColumnSketchesRead graphColumnSketchesRead = new GraphColumnSketchesRead();
        String pathToColumnFolder = pathToFolder + csvHeader + '/';

        graphColumnSketchesRead.readDistinctCountingSketch(pathToColumnFolder);
        if (isNum) {
            graphColumnSketchesRead.readQuantileSketch(pathToColumnFolder);
            graphColumnSketchesRead.setMostFrequentSketch(null);
        } else {
            graphColumnSketchesRead.readMostFrequentSketch(pathToColumnFolder);
            graphColumnSketchesRead.setQuantileSketch(null);
        }

        return graphColumnSketchesRead;

    }
}
