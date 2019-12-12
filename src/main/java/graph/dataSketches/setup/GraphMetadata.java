package graph.dataSketches.setup;

import com.google.gson.Gson;

import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;

/**
 * This class stores essential graph information (includes get and set methods)
 * Information can be saved as .json at /graphName/
 */

public class GraphMetadata {

    private String graphName;

    private String vertexCsvPath;
    private String edgeCsvPath;

    private int vertexTableLength;
    private int edgeTableLength;

    private HashMap<String, Boolean> vertexCsvColumns = new HashMap<>();
    private HashMap<String, Boolean> edgeCsvColumns = new HashMap<>();

    // constructor
    public GraphMetadata (String graphName, int vertexTableLength, int edgeTableLength,
                          HashMap<String, GraphColumnSketchesWrite> graphVertexSketches,
                          HashMap<String, GraphColumnSketchesWrite> graphEdgeSketches) {
        this.graphName = graphName;
        this.vertexTableLength = vertexTableLength;
        this.edgeTableLength = edgeTableLength;

        for (String key: graphVertexSketches.keySet()) {
            this.vertexCsvColumns.put(key, graphVertexSketches.get(key).isNum());
        }

        for (String key: graphEdgeSketches.keySet()) {
            this.vertexCsvColumns.put(key, graphEdgeSketches.get(key).isNum());
        }

    }

    // saves this object to file in /graphName/graphMetadata.json
    public void saveMetadataToJson () throws IOException {
        String savingPath = returnSketchesDirPath() + "graphMetadata.json";
        Gson gson = new Gson();
        gson.toJson(this, new FileWriter(savingPath));
    }

    // returns path for sketches
    public String returnSketchesDirPath () { //Sistemare posizione dati in scrittura
        return '/' + this.graphName + '/';
    }

    // setters
    public void setVertexTableLength(int vertexTableLength) {
        this.vertexTableLength = vertexTableLength;
    }

    public void setEdgeTableLength(int edgeTableLength) {
        this.edgeTableLength = edgeTableLength;
    }

    // getters
    public String getGraphName() {
        return graphName;
    }

    public String getVertexCsvPath() {
        return vertexCsvPath;
    }

    public String getEdgeCsvPath() {
        return edgeCsvPath;
    }

    public int getVertexTableLength() {
        return vertexTableLength;
    }

    public int getEdgeTableLength() {
        return edgeTableLength;
    }

    public HashMap<String, Boolean> getVertexCsvColumns() {
        return vertexCsvColumns;
    }

    public HashMap<String, Boolean> getEdgeCsvColumns() {
        return edgeCsvColumns;
    }

}
