package graph.graphDataSketches.Load;

import com.google.gson.Gson;
import graph.graphDataSketches.Setup.GraphMetadata;

import java.io.FileNotFoundException;
import java.io.FileReader;

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
