package graph.statistics;

import graph.dataSketches.load.GraphColumnSketchesRead;
import org.apache.datasketches.frequencies.ItemsSketch;
import org.apache.datasketches.quantiles.DoublesSketch;
import org.apache.datasketches.theta.Sketch;

/**
 * This class provides the necessary methods to retrieve statistics from the sketches
 * For additional information see DataSketch documentation.
 */

public class RetrieveStatistics {

    // get string selectivity
    public double getStringSelectivity(String valueToFind, EstimateBounds estimateBounds, Integer tableLength,
                                       GraphColumnSketchesRead sketchesToRead) {

        ItemsSketch<String> mostFrequentSketch = sketchesToRead.getMostFrequentSketch();
        Sketch distinctCountSketch = sketchesToRead.getDistinctCountingSketch();

        double estimatedCardinality = searchInMostFrequent(valueToFind, mostFrequentSketch, estimateBounds);

        if (estimatedCardinality > 0) {
            return estimatedCardinality / tableLength;
        } else {
            estimatedCardinality = getCardinalityOfReamining(distinctCountSketch, mostFrequentSketch, tableLength, estimateBounds);
            return estimatedCardinality / tableLength;
        }

    }

    // get num selectivity
    public double getNumSelectivity(final double valueToFind, Comparators comparator,
                                    GraphColumnSketchesRead sketchesToRead) { // da rivedere

        DoublesSketch quantileSketchToRead = sketchesToRead.getQuantileSketch();
        double[] splitPoints = createSplitPoints(valueToFind, comparator);
        double[] selecitivties = quantileSketchToRead.getPMF(splitPoints);

        if (comparator == Comparators.EQUAL) {
            return selecitivties[1];
        } else if (comparator == Comparators.NOT_EQUAL) {
            return selecitivties[0] + selecitivties[3];
        } else if (comparator == Comparators.LESS) {
            return selecitivties[0];
        } else if (comparator == Comparators.GREATER) {
            return selecitivties[3];
        } else if (comparator == Comparators.LESS_EQUAL) {
            return selecitivties[0] + selecitivties[1];
        } else if (comparator == Comparators.GREATER_EQUAL) {
            return selecitivties[0] + selecitivties[3];
        } else {
            return 0;
        }

    }

    // get columnMostFrequent and check if value is contained, otherwise return 0
    private double searchInMostFrequent(String valueToFind, ItemsSketch<String> mostFrequentSketch,
                                      EstimateBounds estimateBounds) {

        if (estimateBounds == EstimateBounds.ESTIMATE) {
            return mostFrequentSketch.getEstimate(valueToFind);
        } else if (estimateBounds == EstimateBounds.LOWERBOUND) {
            return mostFrequentSketch.getLowerBound(valueToFind);
        } else if (estimateBounds == EstimateBounds.UPPERBOUND) {
            return mostFrequentSketch.getUpperBound(valueToFind);
        }
        return 0; // non dovrebbe succedere, gestire errori

    }

    // get cardinality of items not in mostFrequentSketch
    private double getCardinalityOfReamining(Sketch distinctCountSketch, ItemsSketch<String> mostFrequentSketch,
                                             int tableLength, EstimateBounds estimateBounds) {

        double distinctNumber = 0; // non dovrebbe rimanre 0, gestire errori
        int frequentItems = mostFrequentSketch.getCurrentMapCapacity();
        long totalFrequency = mostFrequentSketch.getStreamLength();

        if (estimateBounds == EstimateBounds.ESTIMATE) {
            distinctNumber = distinctCountSketch.getEstimate();
        } else if (estimateBounds == EstimateBounds.LOWERBOUND) {
            distinctNumber = distinctCountSketch.getLowerBound(2); //gestire stdv
        } else if (estimateBounds == EstimateBounds.UPPERBOUND) {
            distinctNumber = distinctCountSketch.getUpperBound(2); //gestire stdv
        }

        return (distinctNumber - frequentItems) / (tableLength - totalFrequency);

    }

    // create split points for num selectivity
    private double[] createSplitPoints(double value, Comparators comparator){
        if (comparator == Comparators.EQUAL) {
            return new double[] {value, value};
        } else if (comparator == Comparators.NOT_EQUAL) {
            return new double[] {value, value};
        } else if (comparator == Comparators.LESS) {
            return new double[] {value};
        } else if (comparator == Comparators.GREATER) {
            return new double[] {value};
        } else if (comparator == Comparators.LESS_EQUAL) {
            return new double[] {value, value};
        } else if (comparator == Comparators.GREATER_EQUAL) {
            return new double[] {value, value};
        } else {
            return null;
        }
    }


}
