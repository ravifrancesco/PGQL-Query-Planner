import exceptions.ColumnDataTypeException;
import graph.dataSketches.setup.GraphSketchCreate;
import graph.statistics.Comparators;
import graph.statistics.EstimateBounds;
import graph.statistics.Statistics;
import settings.Settings;
import settings.SketchesMemorySetting;

import java.io.IOException;

public class Main {
    public static void main(String args[]) throws IOException, ColumnDataTypeException {

        String localPath = System.getProperty("user.dir");

        Statistics statistics;
        Settings settings = new Settings();
        SketchesMemorySetting sketchesMemorySetting = new SketchesMemorySetting();
        settings.setSketchesMemorySetting(sketchesMemorySetting);

        GraphSketchCreate graphSketchCreate = new GraphSketchCreate(localPath + "/IMDB");
        graphSketchCreate.setVertexCsvPath(localPath + "/imdb_csv/imdb_v.csv");
        graphSketchCreate.setEdgeCsvPath(localPath + "/imdb_csv/imdb_e.csv");
        graphSketchCreate.graphToSketches(settings);

        statistics = new Statistics(localPath + "/IMDB", EstimateBounds.ESTIMATE);

        int vertexTableLength = statistics.getVertexTableLength();
        int edgeTableLength = statistics.getEdgeTableLength();
        System.out.println("-------------------------------------------");
        System.out.println("Total vertex num: " + vertexTableLength);
        System.out.println("Total edge num: " + vertexTableLength);
        System.out.println("-------------------------------------------");

        int testNum = 1000000;

        System.out.println("Computing vertex [type == person] selectivity");
        System.out.println("Performing " + testNum + " tests...");

        long start = System.nanoTime();
        for (int i = 0; i < testNum; i++) {
            double vertexFilterSelectivity = statistics.getVertexFilterSelectivity("type", "person", Comparators.EQUAL);
        }
        long end = System.nanoTime();
        long mean_time = (long) ((end - start) / (float) testNum);
        System.out.println("\n > Average time: " +  mean_time + " ns");

        double vertexFilterSelectivity = statistics.getVertexFilterSelectivity("type", "person", Comparators.EQUAL);
        System.out.println(" > Result: " + vertexFilterSelectivity);

        System.out.println("-------------------------------------------");
        System.out.println("Computing edge [level == 1] selectivity");
        System.out.println("Performing " + testNum + " tests...");

        start = System.nanoTime();
        for (int i = 0; i < testNum; i++) {
            double edgeFilterSelecitviy = statistics.getEdgeFilterSelectivity("level", "1", Comparators.EQUAL);
        }
        end = System.nanoTime();
        mean_time = (long) ((end - start) / (float) testNum);
        System.out.println("\n > Average time: " +  mean_time + " ns");

        double edgeFilterSelecitviy = statistics.getEdgeFilterSelectivity("level", "1", Comparators.EQUAL);
        System.out.println(" > Result: " + edgeFilterSelecitviy);
        System.out.println("-------------------------------------------");

    }
}
