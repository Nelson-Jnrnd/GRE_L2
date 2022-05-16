package jeanrenaud.nelson.comparator;

import graph.core.VertexFactory;
import graph.core.impl.Digraph;
import graph.core.impl.SimpleWeightedEdge;
import graph.core.impl.SimpleWeightedEdgeFactory;
import graph.data.CartesianVertexData;
import graph.reader.CartesianGraphReader;
import jeanrenaud.nelson.dijkstra.BidirectionalDijkstra;
import jeanrenaud.nelson.dijkstra.Dijkstra;
import jeanrenaud.nelson.dijkstra.Path;
import jeanrenaud.nelson.dijkstra.ShortestPathAlgorithm;
import jeanrenaud.nelson.graph.EuclideanDistance;
import jeanrenaud.nelson.graph.Node;
import jeanrenaud.nelson.graph.NodeFactory;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Random;

/**
 * Compare the performance of the different shortest path algorithms.
 * @author Nelson Jeanrenaud
 */
public class ShortestPathAlgorithmComparator {
    /** Random number generator used to generate random vertices. */
    private static final Random random = new Random(20220404);
    /** Algorithms used for the tests */
    private final ShortestPathAlgorithm[] algorithms;
    /** Results of the tests */
    private final List<TestResult> results;

    /**
     * Creates a new instance of ShortestPathAlgorithmComparator.
     * @param algorithms the algorithms to use for the tests.
     */
    public ShortestPathAlgorithmComparator(ShortestPathAlgorithm[] algorithms) {
        Objects.requireNonNull(algorithms);
        if (algorithms.length == 0) {
            throw new IllegalArgumentException("At least one algorithm must be provided");
        }
        this.algorithms = algorithms;
        for (int i = 1; i < algorithms.length; i++) {
            if (!getGraph().equals(algorithms[i].getGraph())) {
                throw new IllegalArgumentException("All algorithms must use the same graph");
            }
        }

        results = new java.util.ArrayList<>();
    }

    /**
     * Get the graph used by the algorithms.
     * @return the graph used by the algorithms.
     */
    public Digraph<Node, SimpleWeightedEdge<Node>> getGraph() {
        return algorithms[0].getGraph();
    }

    /**
     * Run the tests.
     * @param nbRuns the number of runs to perform.
     * @return Instance of this class containing the results.
     */
    public ShortestPathAlgorithmComparator Analyse(int nbRuns) {
        int nbNodes = getGraph().getVertices().size();
        for (int i = 0; i < nbRuns;) {
            try {
                results.add(run(random.nextInt(nbNodes), random.nextInt(nbNodes)));
                i++;
            } catch (Dijkstra.NoPathException e){
                System.out.println("No path found for run: " + i);
            }
        }
        return this;
    }

    /**
     * Run a test.
     * @param idSource the id of the source node.
     * @param idTarget the id of the destination node.
     * @return the result of the test.
     */
    public TestResult run(int idSource, int idTarget) {
        Node source = getGraph().getVertices().get(idSource);
        Node target = getGraph().getVertices().get(idTarget);
        TestResult.AlgorithmResult[] results = new TestResult.AlgorithmResult[algorithms.length];
        for (int i = 0; i < algorithms.length; i++) {
            Instant startTime = Instant.now();
            algorithms[i].run(source, target);
            //algorithms[i].getShortestPath();
            Instant endTime = Instant.now();
            Path path = algorithms[i].getShortestPath();
            results[i] = new TestResult.AlgorithmResult(algorithms[i], Duration.between(startTime, endTime),
                    algorithms[i].getIteration() , path.totalWeight(), path.getNumberOfNodes());
        }
        return new TestResult(source, target, results);
    }

    /**
     * Get the results of the tests in csv format.
     * @param separator the separator to use.
     * @param lineSeparator the line separator to use.
     * @param includeHeader if true, the header will be included.
     * @return the results of the tests in csv format.
     */
    public String toCsv(char separator, char lineSeparator, boolean includeHeader) {
        if(results.isEmpty()){
            return "";
        }
        StringBuilder sb = new StringBuilder();
        if (includeHeader) {
            sb.append(TestResult.getCsvHeader(separator, lineSeparator, results.get(0).getResults()));
        }
        for (TestResult result : results) {
            sb.append(result.toCsv(separator, lineSeparator, false));
        }
        return sb.toString();
    }

    private static final String DATA_FOLDER = "data/";
    private static final String OUTPUT_FOLDER = "output/";

    /**
     * Run tests to compare the Dijkstra algorithm with the Bidirectional Dijkstra algorithm.
     * @param args not used
     * @throws IOException if an error occurs while reading or writing files.
     */
    public static void main(String[] args) throws IOException {
        VertexFactory<Node, CartesianVertexData> nodeFactory = new NodeFactory();
        var graph = new CartesianGraphReader<>(
                nodeFactory                                  /* TODO: Fournir une fabrique de sommets (il
                                                            s'agit d'une interface fonctionnelle) */,
                new SimpleWeightedEdgeFactory<>(new EuclideanDistance<>()    /* TODO: Fournir une fonction de pondération
                                                            renvoyant la distance euclidienne (arrondie
                                                            à l'entier le plus proche) entre l'extrémité
                                                            initiale et l'extrémité finale de l'arête */),
                DATA_FOLDER + "R10000_1.txt"                       /* TODO: Chemin des fichiers */
        ).graph();

        ShortestPathAlgorithmComparator comparator = new ShortestPathAlgorithmComparator(
                new ShortestPathAlgorithm[]{
                        new Dijkstra(graph),
                        new BidirectionalDijkstra(graph)
                });


        comparator.Analyse(1000);
        System.out.println(comparator.toCsv(';', '\n', true));

        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss") ;
            File file = new File(OUTPUT_FOLDER + "result-" + dateFormat.format(new Date()) + ".csv");
            FileWriter fw = new FileWriter(file);
            fw.write(comparator.toCsv(';', '\n', true));
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
