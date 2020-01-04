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

        System.out.println(System.getProperty("user.dir"));
        Statistics statistics;
        Settings settings = new Settings();
        SketchesMemorySetting sketchesMemorySetting = new SketchesMemorySetting();
        settings.setSketchesMemorySetting(sketchesMemorySetting);

        GraphSketchCreate graphSketchCreate = new GraphSketchCreate("/home/ravi/coding/PGQL-Query-Planner/IMDB");
        graphSketchCreate.setVertexCsvPath("/home/ravi/Downloads/imdb_csv/imdb_v.csv");
        graphSketchCreate.setEdgeCsvPath("/home/ravi/Downloads/imdb_csv/imdb_e.csv");
        graphSketchCreate.graphToSketches(settings);

        statistics = new Statistics("/home/ravi/coding/PGQL-Query-Planner/IMDB", EstimateBounds.ESTIMATE);

        /*

        DA TESTARE IN SEGUITO

        */

        System.out.println(statistics.getGraphName());

        int vertexTableLength = statistics.getVertexTableLength();
        int averageVertexDegree = statistics.getAverageVertexDegree();
        System.out.println(vertexTableLength);
        System.out.println(averageVertexDegree);


        long start = System.nanoTime();
        for (int i = 0; i < 1000000000; i++) {
            double vertexFilterSelectivity = statistics.getVertexFilterSelectivity("type", "person", Comparators.EQUAL);
        }
        long end = System.nanoTime();
        long mean_time = (long) ((end - start) / (float) 1000000000);
        System.out.println("String time: " +  mean_time + " ns");

        double vertexFilterSelectivity = statistics.getVertexFilterSelectivity("type", "person", Comparators.EQUAL);
        System.out.println(vertexFilterSelectivity);

        start = System.nanoTime();
        for (int i = 0; i < 10000000; i++) {
            double edgeFilterSelecitviy = statistics.getEdgeFilterSelectivity("level", "1", Comparators.EQUAL);
        }
        end = System.nanoTime();
        mean_time = (long) ((end - start) / (float) 10000000);
        System.out.println("Histogram time: " +  mean_time + " ns");

        double edgeFilterSelecitviy = statistics.getEdgeFilterSelectivity("level", "1", Comparators.EQUAL);
        System.out.println(edgeFilterSelecitviy);

        /*Node nodeA = new Node("A");
        Node nodeB = new Node("B");
        Node nodeC = new Node("C");
        Node nodeD = new Node("D");
        Node nodeE = new Node("E");
        Node nodeF = new Node("F");
        Node nodeZ = new Node("Z");

        nodeA.addDestination(nodeB, 10);
        nodeA.addDestination(nodeC, 15);

        nodeB.addDestination(nodeD, 12);
        nodeB.addDestination(nodeF, 15);

        nodeC.addDestination(nodeE, 10);

        nodeD.addDestination(nodeE, 2);
        nodeD.addDestination(nodeF, 1);

        nodeE.addDestination(nodeZ, 3);

        nodeF.addDestination(nodeE, 5);
        nodeF.addDestination(nodeZ, 2);



        Graph graph = new Graph();

        graph.addNode(nodeA);
        graph.addNode(nodeB);
        graph.addNode(nodeC);
        graph.addNode(nodeD);
        graph.addNode(nodeE);
        graph.addNode(nodeF);
        graph.addNode(nodeZ);

        long startTime = System.nanoTime();
        graph = Dijkstra.calculateShortestPathFromSource(graph, nodeA);
        long endTime = System.nanoTime();

        long duration = (endTime - startTime);

        System.out.println(duration+" ns");

        LinkedList<Node> path = new LinkedList<>(nodeZ.getShortestPath());

        Iterator<Node> it = path.iterator();

        while(it.hasNext()){
            System.out.print(it.next().getName() + " --> ");
        }

        //System.out.println(graph.toString());

         */
    }
}
