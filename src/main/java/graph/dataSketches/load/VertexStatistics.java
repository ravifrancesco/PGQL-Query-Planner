package graph.dataSketches.load;

import graph.dataSketches.setup.GraphMetadata;

import java.io.IOException;
import java.util.HashMap;

/**
 * This class loads and stores sketches of the vertexes of the graph
 */

public class VertexStatistics extends SketchLoader {

    private String vertexCsvPath;
    private int vertexTableLength;
    private HashMap<String, Boolean> vertexCsvColumns;

    private String vertexSketchesPath;

    private HashMap<String, GraphColumnSketchesRead> vertexSketchesHashMap = new HashMap<>();

    // constructor
    public VertexStatistics (GraphMetadata graphMetadata) {
        this.vertexCsvPath = graphMetadata.getVertexCsvPath();
        this.vertexTableLength = graphMetadata.getVertexTableLength();
        this.vertexCsvColumns = graphMetadata.getVertexCsvColumns();
        this.vertexSketchesPath = graphMetadata.returnSketchesDirPath() +  "edge/";
    }

    // create sketches HashMap
    public void loadVertexSketches () throws IOException { //Gestire errori
        for (String key: vertexCsvColumns.keySet()) {
            boolean isNum = vertexCsvColumns.get(key);
            vertexSketchesHashMap.put(key, loadColumnSketch(vertexSketchesPath, key, isNum));
        }
    }

    // getter
    public String getVertexCsvPath() {
        return vertexCsvPath;
    }

    public int getVertexTableLength() {
        return vertexTableLength;
    }

    public HashMap<String, GraphColumnSketchesRead> getVertexSketchesHashMap() {
        return vertexSketchesHashMap;
    }
}
