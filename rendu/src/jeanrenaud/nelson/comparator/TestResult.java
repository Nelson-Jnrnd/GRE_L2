package jeanrenaud.nelson.comparator;

import jeanrenaud.nelson.dijkstra.ShortestPathAlgorithm;
import jeanrenaud.nelson.graph.Node;

import java.time.Duration;
import java.util.Objects;

/**
 * Represent a test result for a comparison between multiple algorithms.
 * @see ShortestPathAlgorithmComparator
 * @author Nelson Jeanrenaud
 */
public class TestResult {
    /** Source node the test has been performed on. */
    private final Node source;
    /** Target node the test has been performed on. */
    private final Node target;
    /** Results of the different algorithms. */
    private final AlgorithmResult[] results;

    /**
     * Create a new test result.
     * @param source source node
     * @param target target node
     * @param results results of the different algorithms
     * @throws NullPointerException if source, target or results are null
     */
    public TestResult(Node source, Node target, AlgorithmResult[] results) {
        Objects.requireNonNull(source, "source cannot be null");
        Objects.requireNonNull(target, "target cannot be null");
        Objects.requireNonNull(results, "results cannot be null");
        this.source = source;
        this.target = target;
        this.results = results;
    }

    /**
     * Get the source node.
     * @return source node
     */
    public Node getSource() {
        return source;
    }

    /**
     * Get the target node.
     * @return target node
     */
    public Node getTarget() {
        return target;
    }

    /**
     * Get the results of the different algorithms.
     * @return results of the different algorithms
     */
    public AlgorithmResult[] getResults() {
        return results;
    }

    /**
     * Return the CSV header for the tests.
     * @param separator column separator used
     * @param lineSeparator line separator used
     * @param results results of the different algorithms
     * @return CSV header
     */
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

    /**
     * Return the CSV representation of the test result.
     * @param separator column separator used
     * @param lineSeparator line separator used
     * @return CSV representation
     */
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

    /**
     * Represent a test result for a single algorithm.
     * @author Nelson Jeanrenaud
     */
    public static class AlgorithmResult {
        /** Algorithm used. */
        private final ShortestPathAlgorithm algorithm;
        /** Execution time. */
        private final Duration time;
        /** Number of iterations. */
        private final long nbIteration;
        /** Path found length */
        private final long pathLength;
        /** Path found weight. */
        private final long pathWeight;

        /**
         * Construct a new test result.
         * @param algorithm algorithm used
         * @param time execution time
         * @param nbIterations number of iterations
         * @param pathWeight path weight
         * @param pathLength path length
         * @throws IllegalArgumentException if the path length is negative
         * @throws IllegalArgumentException if the path weight is negative
         * @throws IllegalArgumentException if the number of iterations is negative
         * @throws IllegalArgumentException if the execution time is negative
         * @throws NullPointerException if the algorithm is null
         * @throws NullPointerException if the execution time is null
         */
        public AlgorithmResult(ShortestPathAlgorithm algorithm, Duration time, long nbIterations,
                               long pathWeight, long pathLength) {
            Objects.requireNonNull(algorithm, "algorithm cannot be null");
            Objects.requireNonNull(time, "time cannot be null");
            if (pathLength < 0) {
                throw new IllegalArgumentException("path length cannot be negative");
            }
            if (pathWeight < 0) {
                throw new IllegalArgumentException("path weight cannot be negative");
            }
            if (nbIterations < 0) {
                throw new IllegalArgumentException("number of iterations cannot be negative");
            }
            if (time.isNegative()) {
                throw new IllegalArgumentException("execution time cannot be negative");
            }

            this.algorithm = algorithm;
            this.time = time;
            this.nbIteration = nbIterations;
            this.pathLength = pathLength;
            this.pathWeight = pathWeight;
        }

        /**
         * Return the algorithm used.
         * @return algorithm used
         */
        public ShortestPathAlgorithm getAlgorithm() {
            return algorithm;
        }

        /**
         * Return the execution time.
         * @return execution time
         */
        public Duration getTime() {
            return time;
        }

        /**
         * Return the number of iterations.
         * @return number of iterations
         */
        public long getNbIterations() {
            return nbIteration;
        }

        /**
         * Return the path length.
         * @return path length
         */
        public long getPathLength() {
            return pathLength;
        }

        /**
         * Return the path weight.
         * @return path weight
         */
        public long getPathWeight() {
            return pathWeight;
        }

        /**
         * Return the CSV representation of the test result.
         * @param separator column separator used
         * @return CSV representation
         */
        public String toCsv(char separator) {
            return Long.toString(time.toMillis()) + separator + getNbIterations()
                    + separator + getPathWeight() + separator + getPathLength();
        }
    }

}
