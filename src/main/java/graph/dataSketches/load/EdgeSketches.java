package graph.dataSketches.load;

import graph.dataSketches.setup.ColumnDataTypes;
import graph.dataSketches.setup.GraphMetadata;

import java.io.IOException;
import java.util.HashMap;

/**
 * This class loads and stores sketches of the edges of the graph
 * It is created by GetSketches class. The constructor uses the metadata
 * to create the object.
 *
 * It enables sketch updates (has to be implemented)
 *
 */

public class EdgeSketches extends SketchLoader {
    
    private String edgeCsvPath;
    private int edgeTableLength;
    private HashMap<String, ColumnDataTypes> edgeCsvColumns;

    private String edgeSketchesPath;

    private HashMap<String, GraphColumnSketchesRead> edgeSketchesHashMap = new HashMap<>();

    // constructor
    public EdgeSketches(GraphMetadata graphMetadata) throws IOException {
        this.edgeCsvPath = graphMetadata.getEdgeCsvPath();
        this.edgeTableLength = graphMetadata.getEdgeTableLength();
        this.edgeCsvColumns = graphMetadata.getEdgeCsvColumns();
        this.edgeSketchesPath = graphMetadata.returnSketchesDirPath() +  "edge/";

        this.loadEdgeSketches();

    }

    // create sketches HashMap
    private void loadEdgeSketches () throws IOException { //Gestire errori
        for (String key: this.edgeCsvColumns.keySet()) {
            ColumnDataTypes columnType = this.edgeCsvColumns.get(key);
            this.edgeSketchesHashMap.put(key, loadColumnSketch(this.edgeSketchesPath, key, columnType));
        }
    }

    // update statistics
    public void updateEdgeStatistic() {
        // da costruire
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
