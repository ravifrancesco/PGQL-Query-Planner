package settings.utils;

public class HardwareBenchmark {

    /*
    static var graph = session.readGraphWithProperties("../examples/graphs/imdb_csv/imdb.json");

    var r = new Random();
    var N = (int) graph.getNumVertices();
    var E = (int) graph.getNumEdges();

    var NUM_TEST = 1000000

    // Generate some random integer used to access random vertices;
    var random_v = new ArrayList<Integer>();
    for (int i = 0; i < NUM_TEST; i++) {
        random_v.add(r.nextInt(N) + 1);
    }

    // Measure the average vertex access time;
    var start = System.currentTimeMillis();
    random_v.forEach(v -> {
        graph.getVertex(v);
    });
    var end = System.currentTimeMillis();
    var mean_time = (end - start) / (float) NUM_TEST;

    // Generate some random integer used to access random edges;
    var random_e = new ArrayList<Integer>();
    for (int i = 0; i < NUM_TEST; i++) {
        random_e.add(r.nextInt(E));
    }

    // Measure the average vertex access time;
    var start = System.currentTimeMillis();
    random_e.forEach(e -> {
        graph.getEdge(e);
    });
    var end = System.currentTimeMillis();
    var mean_time = (end - start) / (float) NUM_TEST;

    // Generate some random vertices used to access their property;
    var random_v2 = new ArrayList<PgxVertex>();
    for (int i = 0; i < NUM_TEST; i++) {
        random_v2.add(graph.getVertex(r.nextInt(N) + 1));
    }

    // Measure the average vertex property access time;
    var start = System.currentTimeMillis();
    random_v2.forEach(v -> {
        try {
            v.getProperty("name");
        } catch (Exception e) {
            println(e);
        }
    });
    var end = System.currentTimeMillis();
    var mean_time = (end - start) / (float) NUM_TEST;

    // Measure the average vertex labels access time;
    var start = System.currentTimeMillis();
    random_v2.forEach(v -> {
        try {
            v.getLabels();
        } catch (Exception e) {
            println(e);
        }
    });
    var end = System.currentTimeMillis();
    var mean_time = (end - start) / (float) NUM_TEST;
    */

}
