package graph.dataSketches.load;

import graph.dataSketches.setup.ColumnDataTypes;
import graph.dataSketches.setup.GraphMetadata;

import java.io.IOException;
import java.util.HashMap;

/**
 * This class loads and stores sketches of the vertexes of the graph
 */

public class VertexStatistics extends SketchLoader {

    private String vertexCsvPath;
    private int vertexTableLength;
    private HashMap<String, ColumnDataTypes> vertexCsvColumns;

    private String vertexSketchesPath;

    private HashMap<String, GraphColumnSketchesRead> vertexSketchesHashMap = new HashMap<>();

    // constructor
    public VertexStatistics (GraphMetadata graphMetadata) throws IOException {
        this.vertexCsvPath = graphMetadata.getVertexCsvPath();
        this.vertexTableLength = graphMetadata.getVertexTableLength();
        this.vertexCsvColumns = graphMetadata.getVertexCsvColumns();
        this.vertexSketchesPath = graphMetadata.returnSketchesDirPath() +  "edge/";

        this.loadVertexSketches();

    }

    // create sketches HashMap
    public void loadVertexSketches () throws IOException { //Gestire errori
        for (String key: this.vertexCsvColumns.keySet()) {
            ColumnDataTypes columnType = this.vertexCsvColumns.get(key);
            this.vertexSketchesHashMap.put(key, loadColumnSketch(this.vertexSketchesPath, key, columnType));
        }
    }

    // update statistics
    public void updateVertexStatistic() {
        // da costruire
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
