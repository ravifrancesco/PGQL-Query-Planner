package graph.dataSketches.setup;

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

    private String graphName;

    private String vertexCsvPath;
    private String edgeCsvPath;

    private int vertexTableLength;
    private int edgeTableLength;

    private HashMap<String, GraphColumnSketchesWrite> graphVertexSketches = new HashMap<>();
    private HashMap<String, GraphColumnSketchesWrite> graphEdgeSketches = new HashMap<>();

    // constructor
    public GraphSketchCreate(String graphName) {
        this.graphName = graphName;
    }

    // handles parsing, creation and save of edge and vertex csv and saves metadata to .json
    public void graphToSketches() throws IOException {
        this.vertexTableLength = csvToSketches(graphVertexSketches, vertexCsvPath, CsvType.VERTEX);
        this.edgeTableLength = csvToSketches(graphEdgeSketches, edgeCsvPath, CsvType.EDGE);
        GraphMetadata graphMetadata = new GraphMetadata(graphName, vertexTableLength, edgeTableLength,
                                                        graphVertexSketches, graphEdgeSketches);
        graphMetadata.saveMetadataToJson();
    }

    // handles the parsing of the Csv
    private int csvToSketches(HashMap<String, GraphColumnSketchesWrite> graphSketches,
                               String csvPath, CsvType type) throws IOException {

        List<String> csvHeaders;
        int tableLength;

        try (
                Reader reader = Files.newBufferedReader(Paths.get(vertexCsvPath));
                CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT
                        .withFirstRecordAsHeader()
                        .withIgnoreHeaderCase()
                        .withTrim());
        ) {
            csvHeaders = csvParser.getHeaderNames();
            System.out.println(csvHeaders.toString()); //Testing purposes
            tableLength = createSketches(graphSketches, csvParser, csvHeaders, type);
            return tableLength;
        }

    }

    // saves the sketches to .bin files in graph directory
    private void saveGraphSketchesToFile(HashMap<String, GraphColumnSketchesWrite> graphSketches) { //Gestire directory di salvataggio

        for (String key: graphSketches.keySet()) {
            graphSketches.get(key).saveColumnSketchToFile(this.graphName, key);
        }

    }

    // handles the creation and update of the sketches during the parsing of the Csv
    private int createSketches(HashMap<String, GraphColumnSketchesWrite> graphSketches,
                                CSVParser csvParser, List<String> csvHeaders, CsvType type) {

        boolean initialized = false;
        int tableLength = 0;

        for (CSVRecord csvRecord: csvParser) {
            if (!initialized) {
                initializeSketchesMap(graphSketches, csvRecord, csvHeaders, type);
                initialized = true;
            }
            updateSketches(graphSketches, csvRecord, csvHeaders);
            ++tableLength;
        }

        return tableLength;

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
