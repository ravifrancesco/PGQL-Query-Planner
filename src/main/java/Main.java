import graph.dataSketches.setup.GraphSketchCreate;
import graph.statistics.EstimateBounds;
import graph.statistics.Statistics;
import heuristic.utils.Dijkstra;
import heuristic.utils.Graph;
import heuristic.utils.Node;
import settings.Settings;

import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;

public class Main {
    public static void main(String args[]) throws IOException {

        Statistics statistics;
        Settings settings = new Settings();

        GraphSketchCreate graphSketchCreate = new GraphSketchCreate("IMDB_GRAPH");
        graphSketchCreate.setVertexCsvPath("Path per il CSV dei vertex");
        graphSketchCreate.setEdgeCsvPath("Path per il CSV degli edge");

        statistics = new Statistics("IMDB_GRAPH", EstimateBounds.ESTIMATE);

        /*

        DA TESTARE IN SEGUITO

        System.out.println(statistics.getGraphName());

        int vertexTableLength = statistics.getVertexTableLength();
        int averageVertexDegree = statistics.getAverageVertexDegree();

        double vertexFilterSelectivity = statistics.getEdgeFilterSelectivity();
        double edgeFilterSelecitviy = statistics.getEdgeFilterSelectivity();
        */


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
