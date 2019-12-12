package graph.statistics;

import com.google.gson.Gson;
import graph.dataSketches.load.EdgeStatistics;
import graph.dataSketches.load.VertexStatistics;
import graph.dataSketches.setup.GraphMetadata;

import java.io.FileNotFoundException;
import java.io.FileReader;

/**
 * Da continuare
 */

public class Statistics {

    VertexStatistics vertexStatistics;
    EdgeStatistics edgeStatistics;

    GraphMetadata graphMetadata;

    // constructor
    public Statistics (String graphName) throws FileNotFoundException {
        this.graphMetadata = readMetadataFromJson(graphName);
    }

    // builds GraphMetadata from json file
    public GraphMetadata readMetadataFromJson (String graphName) throws FileNotFoundException {
        String readingPath = '/' + graphName + "/graphMetadata.json"; //Sistemare posizione dati in lettura
        Gson gson = new Gson();
        return gson.fromJson(new FileReader(readingPath), GraphMetadata.class);
    }



}
