package jeanrenaud.nelson;

import jeanrenaud.nelson.dijkstra.Path;
import jeanrenaud.nelson.dijkstra.ShortestPathAlgorithm;
import jeanrenaud.nelson.graph.Node;

import java.time.Duration;

public class TestResult {
    private static final String CSV_SEPARATOR = ",";
    private final Node source;
    private final Node target;
    private final AlgorithmResult[] results;

    public TestResult(Node source, Node target, AlgorithmResult[] results) {
        this.source = source;
        this.target = target;
        this.results = results;
    }

    public TestResult(Node source, Node target) {
        this(source, target, null);
    }

    public Node getSource() {
        return source;
    }

    public Node getTarget() {
        return target;
    }

    public AlgorithmResult[] getResults() {
        return results;
    }

    public static String getCsvHeader(char separator, char lineSeparator, AlgorithmResult[] results) {
        StringBuilder sb = new StringBuilder();
        sb.append("source").append(separator)
                .append("target");
        for (AlgorithmResult result : results) {
            sb.append(separator).append(result.getAlgorithm().getName()).append(" time").append(separator)
                    .append(result.getAlgorithm().getName()).append(" iteration").append(separator)
                    .append(result.getAlgorithm().getName()).append(" path weight").append(separator)
                    .append(result.getAlgorithm().getName()).append(" path length");
        }
        return sb.append(lineSeparator).toString();
    }

    public String toCsv(char separator, char lineSeparator, boolean includeHeader) {
        if (results.length == 0) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        if (includeHeader) {
            sb.append(getCsvHeader(separator, lineSeparator, results));
        }
        sb.append(source.id()).append(separator)
                .append(target.id());
        for (AlgorithmResult result : results) {
            sb.append(separator).append(result.toCsv(separator));
        }
        return sb.append(lineSeparator).toString();
    }

    public static class AlgorithmResult {
        private final ShortestPathAlgorithm algorithm;
        private final Duration time;

        private final long nbIteration;
        private final long pathLength;
        private final long pathWeight;

        public AlgorithmResult(ShortestPathAlgorithm algorithm, Duration time, long nbIterations,
                               long pathWeight, long pathLength) {
            this.algorithm = algorithm;
            this.time = time;
            this.nbIteration = nbIterations;
            this.pathLength = pathLength;
            this.pathWeight = pathWeight;
        }

        public ShortestPathAlgorithm getAlgorithm() {
            return algorithm;
        }

        public Duration getTime() {
            return time;
        }

        public long getNbIterations() {
            return nbIteration;
        }

        public long getPathLength() {
            return pathLength;
        }

        public long getPathWeight() {
            return pathWeight;
        }

        public String toCsv(char separator) {
            return Long.toString(time.toMillis()) + separator + getNbIterations()
                    + separator + getPathWeight() + separator + getPathLength();
        }
    }

}
