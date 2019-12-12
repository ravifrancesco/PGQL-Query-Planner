package graph.dataSketches.Setup;

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
    public GraphMetadata (String graphName) {
        this.graphName = graphName;
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
    public void setGraphName(String graphName) {
        this.graphName = graphName;
    }

    public void setVertexCsvPath(String vertexCsvPath) {
        this.vertexCsvPath = vertexCsvPath;
    }

    public void setEdgeCsvPath(String edgeCsvPath) {
        this.edgeCsvPath = edgeCsvPath;
    }

    public void setVertexTableLength(int vertexTableLength) {
        this.vertexTableLength = vertexTableLength;
    }

    public void setEdgeTableLength(int edgeTableLength) {
        this.edgeTableLength = edgeTableLength;
    }

    public void setVertexCsvColumns(HashMap<String, GraphColumnSketchesWrite> graphVertexSketches) {

        for (String key: graphVertexSketches.keySet()) {
            vertexCsvColumns.put(key, graphVertexSketches.get(key).isNum());
        }

    }

    public void setEdgeCsvHColumns(HashMap<String, GraphColumnSketchesWrite> graphEdgeSketches) {

        for (String key: graphEdgeSketches.keySet()) {
            vertexCsvColumns.put(key, graphEdgeSketches.get(key).isNum());
        }

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
