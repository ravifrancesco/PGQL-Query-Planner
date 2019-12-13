package graph.statistics;

import com.google.gson.Gson;
import graph.dataSketches.load.EdgeSketches;
import graph.dataSketches.load.VertexSketches;
import graph.dataSketches.setup.GraphMetadata;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/**
 * This class contains the sketches of the vertexes and the edges of the graph.
 * It creates two objects (vertexSketches and edgeSketches) from the json containing
 * all the information about the graph, It enables access to the sketches in order to
 * retrieve statistics. This class is used by Statistics class
 */

public class GetSketches {

    VertexSketches vertexSketches;
    EdgeSketches edgeSketches;

    GraphMetadata graphMetadata;

    // constructor
    public GetSketches(String graphName) throws IOException {

        this.graphMetadata = readMetadataFromJson(graphName);

        this.vertexSketches = new VertexSketches(this.graphMetadata);
        this.edgeSketches = new EdgeSketches(this.graphMetadata);

    }

    // builds GraphMetadata from json file
    public GraphMetadata readMetadataFromJson(String graphName) throws FileNotFoundException {
        String readingPath = '/' + graphName + "/graphMetadata.json"; //Sistemare posizione dati in lettura
        Gson gson = new Gson();
        return gson.fromJson(new FileReader(readingPath), GraphMetadata.class);
    }

    // getters
    public VertexSketches getVertexSketches() {
        return vertexSketches;
    }

    public EdgeSketches getEdgeSketches() {
        return edgeSketches;
    }
}