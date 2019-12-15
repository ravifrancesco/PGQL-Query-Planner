package settings;

/**
 * Class containing the settings needed to properly read the csv as a graph
 * typeCsvHeader idicates the header used in the csv to indicate the edge/vertex type
 */

public class GraphSettings {

    private static String vertexTypeCsvHeader;
    private static String edgeTypeCsvHeader;

    // constructor
    public GraphSettings(String vertexTypeCsvHeader, String edgeTypeCsvHeader) {
        GraphSettings.vertexTypeCsvHeader = vertexTypeCsvHeader;
        GraphSettings.edgeTypeCsvHeader = edgeTypeCsvHeader;
    }

    // setter
    public void setVertexTypeCsvHeader(String vertexTypeCsvHeader) {
        this.vertexTypeCsvHeader = vertexTypeCsvHeader;
    }

    public void setEdgeTypeCsvHeader(String edgeTypeCsvHeader) {
        this.edgeTypeCsvHeader = edgeTypeCsvHeader;
    }

    // getters
    public String getVertexTypeCsvHeader() {
        return vertexTypeCsvHeader;
    }

    public String getEdgeTypeCsvHeader() {
        return edgeTypeCsvHeader;
    }

}
