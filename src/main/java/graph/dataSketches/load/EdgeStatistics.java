package graph.dataSketches.load;

import graph.dataSketches.setup.GraphMetadata;

import java.io.IOException;
import java.util.HashMap;

/**
 * This class loads and stores sketches of the edges of the graph
 */

public class EdgeStatistics extends SketchLoader {
    
    private String edgeCsvPath;
    private int edgeTableLength;
    private HashMap<String, Boolean> edgeCsvColumns;

    private String edgeSketchesPath;

    private HashMap<String, GraphColumnSketchesRead> edgeSketchesHashMap = new HashMap<>();

    // constructor
    public EdgeStatistics (GraphMetadata graphMetadata) {
        this.edgeCsvPath = graphMetadata.getEdgeCsvPath();
        this.edgeTableLength = graphMetadata.getEdgeTableLength();
        this.edgeCsvColumns = graphMetadata.getEdgeCsvColumns();
        this.edgeSketchesPath = graphMetadata.returnSketchesDirPath() +  "edge/";
    }

    // create sketches HashMap
    public void loadEdgeSketches () throws IOException { //Gestire errori
        for (String key: edgeCsvColumns.keySet()) {
            boolean isNum = edgeCsvColumns.get(key);
            edgeSketchesHashMap.put(key, loadColumnSketch(edgeSketchesPath, key, isNum));
        }
    }

    // getters
    public String getEdgeCsvPath() {
        return edgeCsvPath;
    }

    public int getEdgeTableLength() {
        return edgeTableLength;
    }

    public HashMap<String, GraphColumnSketchesRead> getEdgeSketchesHashMap() {
        return edgeSketchesHashMap;
    }
}
