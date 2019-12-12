package graphDataSketch;

import com.google.gson.Gson;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

/**
 * This class stores essential graph information (includes get and set methods)
 */

public class GraphMetadata {

    private String graphName; //Sistemare posizione dati in lettura

    private String vertexCsvPath;
    private String edgeCsvPath;

    private int vertexTableLength;
    private int edgeTableLength;

    private List<String> vertexCsvHeaders;
    private List<String> edgeCsvHeaders;

    // constructor for GraphSketchCreate class
    public GraphMetadata (String graphName, String vertexCsvPath, String edgeCsvPath) {
        this.graphName = graphName;
    }

    // saves this object to file in /graphName/graphMetadata.json
    public void saveMetadataToJson () throws IOException {
        String savingPath = returnSketchesDirPath() + "/graphMetadata.json";
        Gson gson = new Gson();
        gson.toJson(this, new FileWriter(savingPath));
    }

    // construct from json file
    public GraphMetadata readMetaDataFromJson (String graphName) throws FileNotFoundException {
        String readingPath = '/' + graphName + "/graphMetadata.json";
        Gson gson = new Gson();
        return gson.fromJson(new FileReader(readingPath), GraphMetadata.class);
    }

    // returns path for sketches
    public String returnSketchesDirPath () {
        return '/' + this.graphName;
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

    public void setVertexCsvHeaders(List<String> vertexCsvHeaders) {
        this.vertexCsvHeaders = vertexCsvHeaders;
    }

    public void setEdgeCsvHeaders(List<String> edgeCsvHeaders) {
        this.edgeCsvHeaders = edgeCsvHeaders;
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

    public List<String> getVertexCsvHeaders() {
        return vertexCsvHeaders;
    }

    public List<String> getEdgeCsvHeaders() {
        return edgeCsvHeaders;
    }

}
