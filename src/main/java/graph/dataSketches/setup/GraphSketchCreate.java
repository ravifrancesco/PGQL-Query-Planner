package graph.dataSketches.Setup;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;

/**
 * This class is used to parse csv, create sketches for the graph and save them. The constructor takes as arguments:
 *      - vertexCsvPath to parse the vertex Csv and create sketches for each column of the table
 *      - edgeCsvPath to parse the edge Csv and create sketches for each column of the table
 *      - graphName as an identifier for the graph and for the sketches (name can be arbitrary)
 *
 * Sketches for each column are accessed through hashmap graphSketches
 *
 * Check of types contained in a column is on the first raw of the Csv, the script then assumes uniformity of data type
 *
 * More on the creation and save of the sketches in class graphColumnSketches
*/

public class GraphSketchCreate {

    private String vertexCsvPath;
    private String edgeCsvPath;

    private String graphName;

    private HashMap<String, GraphColumnSketchesWrite> graphVertexSketches = new HashMap<>();
    private HashMap<String, GraphColumnSketchesWrite> graphEdgeSketches = new HashMap<>();

    // constructor
    public GraphSketchCreate(String graphName, String vertexCsvPath, String edgeCsvPath) {
        this.graphName = graphName;
        this.vertexCsvPath = vertexCsvPath;
        this.edgeCsvPath = edgeCsvPath;
    }

    // handles parsing, creation and save of edge and vertex csv
    public void graphToSketches() throws IOException {
        csvToSketches(graphVertexSketches, vertexCsvPath, CsvType.VERTEX);
        csvToSketches(graphEdgeSketches, edgeCsvPath, CsvType.EDGE);
    }

    // handles the parsing of the Csv
    private void csvToSketches(HashMap<String, GraphColumnSketchesWrite> graphSketches,
                               String csvPath, CsvType type) throws IOException {

        List<String> csvHeaders;

        try (
                Reader reader = Files.newBufferedReader(Paths.get(vertexCsvPath));
                CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT
                        .withFirstRecordAsHeader()
                        .withIgnoreHeaderCase()
                        .withTrim());
        ) {
            csvHeaders = csvParser.getHeaderNames();
            System.out.println(csvHeaders.toString()); //Testing purposes
            createSketches(graphSketches, csvParser, csvHeaders, type);
        }

    }

    // saves the sketches to .bin files in graph directory
    private void saveGraphSketchesToFile(HashMap<String, GraphColumnSketchesWrite> graphSketches) { //Gestire directory di salvataggio

        for (String key: graphSketches.keySet()) {
            graphSketches.get(key).saveColumnSketchToFile(this.graphName, key);
        }

    }

    // handles the creation and update of the sketches during the parsing of the Csv
    private void createSketches(HashMap<String, GraphColumnSketchesWrite> graphSketches,
                                CSVParser csvParser, List<String> csvHeaders, CsvType type) {

        boolean initialized = false;

        for (CSVRecord csvRecord: csvParser) {
            if (!initialized) {
                initializeSketchesMap(graphSketches, csvRecord, csvHeaders, type);
                initialized = true;
            }
            updateSketches(graphSketches, csvRecord, csvHeaders);
        }

    }

    // initializes 2 sketches for each column of the Csv
    private void initializeSketchesMap (HashMap<String, GraphColumnSketchesWrite> graphSketches,
                                        CSVRecord csvRecord, List<String> csvHeaders, CsvType type) {

        for (String header: csvHeaders) {

            String readValue = csvRecord.get(header);

            try {
                double d = Double.parseDouble(readValue);
                graphSketches.put(header, new GraphColumnSketchesWrite(true, type));
            } catch (NumberFormatException nfe) {
                graphSketches.put(header, new GraphColumnSketchesWrite(false, type));
            }
            
        }

    }

    // updates the sketches with new values during the parsing of the Csv
    private void updateSketches (HashMap<String, GraphColumnSketchesWrite> graphSketches,
                                 CSVRecord csvRecord, List<String> csvHeaders) {

        for (String key: graphSketches.keySet()) {

            String readValue = csvRecord.get(key);
            GraphColumnSketchesWrite currentGraphColumnSketch = graphSketches.get(key);

            currentGraphColumnSketch.updateColumnSketches(readValue);

        }

    }

}
