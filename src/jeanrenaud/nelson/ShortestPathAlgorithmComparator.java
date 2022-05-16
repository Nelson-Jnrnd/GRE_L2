package jeanrenaud.nelson;

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
import jeanrenaud.nelson.graph.EuclidianDistance;
import jeanrenaud.nelson.graph.Node;
import jeanrenaud.nelson.graph.NodeFactory;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Random;

public class ShortestPathAlgorithmComparator {
    private static final Random random = new Random(20220404);
    private final ShortestPathAlgorithm[] algorithms;

    private final List<TestResult> results;

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

        results = new java.util.ArrayList<>(); // TODO check if this is the best way to do it
    }

    public Digraph<Node, SimpleWeightedEdge<Node>> getGraph() {
        return algorithms[0].getGraph();
    }

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

    public TestResult run(int idSource, int idTarget) {
        Node source = getGraph().getVertices().get(idSource);
        Node target = getGraph().getVertices().get(idTarget);
        TestResult.AlgorithmResult[] results = new TestResult.AlgorithmResult[algorithms.length];
        for (int i = 0; i < algorithms.length; i++) {
            Instant startTime = Instant.now();
            algorithms[i].run(source, target);
            algorithms[i].getShortestPath();
            Instant endTime = Instant.now();
            Path path = algorithms[i].getShortestPath();
            results[i] = new TestResult.AlgorithmResult(algorithms[i], Duration.between(startTime, endTime),
                    algorithms[i].getIteration() , path.totalWeight(), path.getNumberOfNodes());
        }
        return new TestResult(source, target, results);
    }

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
    /*
     * NE PAS MODIFIER
     * Les fichiers de données sont à placer à la racine de ce répertoire
     */
    private static final String DATA_FOLDER = "data/";
    private static final String OUTPUT_FOLDER = "output/";

    public static void main(String[] args) throws IOException {
        VertexFactory<Node, CartesianVertexData> nodeFactory = new NodeFactory();
        var graph = new CartesianGraphReader<>(
                nodeFactory                                  /* TODO: Fournir une fabrique de sommets (il
                                                            s'agit d'une interface fonctionnelle) */,
                new SimpleWeightedEdgeFactory<>(new EuclidianDistance<>()    /* TODO: Fournir une fonction de pondération
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
