package graph.dataSketches.setup;

import exceptions.ColumnDataTypeException;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import settings.Settings;

import java.io.File;
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
 * Possible data types are stored in enum ColumnDataTypes
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

    private Settings settings;

    // constructor
    public GraphSketchCreate(String graphName) {
        this.graphName = graphName;
    }

    // handles parsing, creation and save of edge and vertex csv and saves metadata to .json
    public void graphToSketches(Settings settings) throws IOException, ColumnDataTypeException {

        this.settings = settings;

        this.vertexTableLength = csvToSketches(this.graphVertexSketches, this.vertexCsvPath, CsvTypes.VERTEX);
        this.edgeTableLength = csvToSketches(this.graphEdgeSketches, this.edgeCsvPath, CsvTypes.EDGE);
        saveGraphSketchesToFile(this.graphVertexSketches);
        saveGraphSketchesToFile(this.graphEdgeSketches);

        GraphMetadata graphMetadata = new GraphMetadata(this.graphName, this.vertexTableLength, this.edgeTableLength,
                                                        this.graphVertexSketches, this.graphEdgeSketches);
        graphMetadata.saveMetadataToJson();

    }

    // handles the parsing of the Csv
    private int csvToSketches(HashMap<String, GraphColumnSketchesWrite> graphSketches,
                               String csvPath, CsvTypes type) throws IOException {

        List<String> csvHeaders;
        int tableLength;

        try (
                Reader reader = Files.newBufferedReader(Paths.get(csvPath));
                CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT
                        .withFirstRecordAsHeader()
                        .withIgnoreHeaderCase()
                        .withTrim());
        ) {
            csvHeaders = csvParser.getHeaderNames();
            System.out.println(csvHeaders.toString()); //Testing purposes
            tableLength = createSketches(graphSketches, csvParser, csvHeaders, type);
            return tableLength;
        } catch (ColumnDataTypeException e) {
            e.printStackTrace();
        }

        return -1;

    }

    // saves the sketches to .bin files in graph directory
    private void saveGraphSketchesToFile(HashMap<String, GraphColumnSketchesWrite> graphSketches) throws ColumnDataTypeException { //Gestire directory di salvataggio

        for (String key: graphSketches.keySet()) {
            graphSketches.get(key).saveColumnSketchToFile(this.graphName, key);
        }

    }

    // handles the creation and update of the sketches during the parsing of the Csv
    private int createSketches(HashMap<String, GraphColumnSketchesWrite> graphSketches,
                                CSVParser csvParser, List<String> csvHeaders, CsvTypes type) throws ColumnDataTypeException {

        boolean initialized = false;
        int tableLength = 0;

        for (CSVRecord csvRecord: csvParser) {
            if (!initialized) {
                initializeSketchesMap(graphSketches, csvRecord, csvHeaders, type);
                initialized = true;
            }
            addValueToSketch(graphSketches, csvRecord, csvHeaders);
            ++tableLength;
        }

        return tableLength;

    }

    // initializes 2 sketches for each column of the Csv
    private void initializeSketchesMap (HashMap<String, GraphColumnSketchesWrite> graphSketches,
                                        CSVRecord csvRecord, List<String> csvHeaders, CsvTypes type) throws ColumnDataTypeException {

        for (String header: csvHeaders) {

            String readValue = csvRecord.get(header);

            try {
                double d = Double.parseDouble(readValue);
                graphSketches.put(header, new GraphColumnSketchesWrite(ColumnDataTypes.NUM, type, this.settings));
            } catch (NumberFormatException nfe) {
                graphSketches.put(header, new GraphColumnSketchesWrite(ColumnDataTypes.STRING, type, this.settings));
            } catch (ColumnDataTypeException e) {
                e.printStackTrace();
            }

        }

    }

    // updates the current sketch with new values during the parsing of the Csv
    private void addValueToSketch (HashMap<String, GraphColumnSketchesWrite> graphSketches,
                                 CSVRecord csvRecord, List<String> csvHeaders) throws ColumnDataTypeException {

        for (String key: graphSketches.keySet()) {

            String readValue = csvRecord.get(key);
            GraphColumnSketchesWrite currentGraphColumnSketch = graphSketches.get(key);

            currentGraphColumnSketch.updateColumnSketches(readValue);

        }

    }

    // setters
    public void setVertexCsvPath(String vertexCsvPath) {
        this.vertexCsvPath = vertexCsvPath;
    }

    public void setEdgeCsvPath(String edgeCsvPath) {
        this.edgeCsvPath = edgeCsvPath;
    }

    // getters
    public String getVertexCsvPath() {
        return vertexCsvPath;
    }

    public String getEdgeCsvPath() {
        return edgeCsvPath;
    }

}
