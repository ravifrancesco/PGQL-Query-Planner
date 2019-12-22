package graph.statistics;

import exceptions.ColumnDataTypeException;
import graph.dataSketches.load.GraphColumnSketchesRead;
import graph.dataSketches.setup.ColumnDataTypes;

import java.io.IOException;
import java.util.HashMap;

/**
 * This class stores provides methods used by operators to retrieve selectivity of filters
 * The HashMaps contain all the sketches for all the columns for both vertexes and edges
 * TableLengths are used to calculate selectivity
 * Estimate bound provides information of how to make the estimates (see DataSketch documentation)
 */

public class Statistics extends RetrieveStatistics {

    String graphName;

    GetSketches graphSketches;

    int vertexTableLength;
    int edgeTableLength;
    int averageVertexDegree;

    HashMap<String, GraphColumnSketchesRead> vertexSketchesMap;
    HashMap<String, GraphColumnSketchesRead> edgeSketchesMap;

    EstimateBounds estimateBounds; // aggiugnere impostazioni

    // constructor
    public Statistics(String graphName, EstimateBounds estimateBounds) throws IOException {

        this.graphName = graphName;
        this.graphSketches = new GetSketches(graphName);

        this.vertexTableLength = this.graphSketches.getVertexSketches().getVertexTableLength();
        this.edgeTableLength = this.graphSketches.getEdgeSketches().getEdgeTableLength();

        this.vertexSketchesMap = this.graphSketches.getVertexSketches().getVertexSketchesHashMap();
        this.edgeSketchesMap = this.graphSketches.getEdgeSketches().getEdgeSketchesHashMap();

        this.averageVertexDegree = (int) (this.edgeTableLength / this.vertexTableLength);

        this.estimateBounds = estimateBounds;

    }

    // get vertex statistics (value column indicates the column on which filterValue selectivity is computed)
    public double getVertexFilterSelectivity(String valueColumn, String filterValue, Comparators comparator) throws ColumnDataTypeException { //gestire non trovato e tipo sbagliato

        GraphColumnSketchesRead sketchesToRead = vertexSketchesMap.get(valueColumn);

        if (sketchesToRead.getColumnType() == ColumnDataTypes.NUM) {
            try {
                double d = Double.parseDouble(filterValue);
                return getNumSelectivity(d, comparator, sketchesToRead);
            } catch (NumberFormatException nfe) {
                System.out.println("NumberFormatException: " + nfe.getMessage());
                return -1;
            }
        } else if (sketchesToRead.getColumnType() == ColumnDataTypes.STRING) {
            return getStringSelectivity(filterValue, this.estimateBounds, this.vertexTableLength, sketchesToRead);
        } else {
            throw new ColumnDataTypeException();
        }

    }

    // get edge statistics (value column indicates the column on which filterValue selectivity is computed)
    public double getEdgeFilterSelectivity(String valueColumn, String filterValue, Comparators comparator) throws ColumnDataTypeException { //gestire non trovato e tipo sbagliato

        GraphColumnSketchesRead sketchesToRead = edgeSketchesMap.get(valueColumn);

        if (sketchesToRead.getColumnType() == ColumnDataTypes.NUM) {
            try {
                double d = Double.parseDouble(filterValue);
                return getNumSelectivity(d, comparator, sketchesToRead);
            } catch (NumberFormatException nfe) {
                System.out.println("NumberFormatException: " + nfe.getMessage());
                return -1;
            }
        } else if (sketchesToRead.getColumnType() == ColumnDataTypes.STRING) {
            return getStringSelectivity(filterValue, this.estimateBounds, this.edgeTableLength, sketchesToRead);
        } else {
            throw new ColumnDataTypeException();
        }

    }

    // getters
    public String getGraphName() {
        return graphName;
    }

    public int getVertexTableLength() {
        return vertexTableLength;
    }

    public int getEdgeTableLength() {
        return edgeTableLength;
    }

    public int getAverageVertexDegree() {
        return averageVertexDegree;
    }

    public EstimateBounds getEstimateBounds() {
        return estimateBounds;
    }

}
