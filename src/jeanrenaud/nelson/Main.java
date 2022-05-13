
/*
 * Le code rendu se situe uniquement dans ce package (event. sous-packages)
 */
package jeanrenaud.nelson;

import graph.core.Edge;
import graph.core.VertexFactory;
import graph.core.impl.SimpleWeightedEdge;
import graph.core.impl.SimpleWeightedEdgeFactory;
import graph.data.CartesianVertexData;
import graph.reader.CartesianGraphReader;

import java.io.IOException;
import java.util.Arrays;

public class Main {
    /*
     * NE PAS MODIFIER
     * Les fichiers de données sont à placer à la racine de ce répertoire
     */
    private static final String DATA_FOLDER = "data/";

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

        Node source = graph.getVertices().stream().filter(node -> node.id() == 2).findFirst().orElse(null);
        Node target = graph.getVertices().stream().filter(node -> node.id() == 10).findFirst().orElse(null);


        for (SimpleWeightedEdge<Node> nodeSimpleWeightedEdge : graph.getSuccessorList(2)) {
            System.out.println("from:" + nodeSimpleWeightedEdge.from().id() + " to: " + nodeSimpleWeightedEdge.to().id() + " wgt:" + nodeSimpleWeightedEdge.weight());
        }
        Dijkstra dijkstra = new Dijkstra(graph, source, target);
        System.out.println(dijkstra);

        System.out.println("\nRunning...\n");

        dijkstra.run();
        System.out.println(dijkstra);

        System.out.println("\nResult\n");
        dijkstra.getResult().forEach(System.out::println);

        System.out.println("\nShortest path\n");
        System.out.println(Arrays.toString(dijkstra.getShortestPath(target)));

    }
}
