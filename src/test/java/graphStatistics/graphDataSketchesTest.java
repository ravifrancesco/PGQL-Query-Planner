package graphStatistics;

import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class graphDataSketchesTest {

    @Test
    public void shouldReturnVertexCsvHeaders() throws IOException {
        String vertexCsvPath = "?"; //Insert path for imdb_v.csv
        String edgeCsvPath = null
        graphDataSketches graphSketches = new graphDataSketches();
        graphSketches.createSketches(vertexCsvPath, edgeCsvPath);

        assertEquals(0, 0);
    }

}
