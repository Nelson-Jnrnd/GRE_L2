
/*
 * Le code rendu se situe uniquement dans ce package (event. sous-packages)
 */
package jeanrenaud.nelson;

import graph.core.VertexFactory;
import graph.core.impl.SimpleWeightedEdgeFactory;
import graph.data.CartesianVertexData;
import graph.reader.CartesianGraphReader;
import jeanrenaud.nelson.dijkstra.BidirectionalDijkstra;
import jeanrenaud.nelson.dijkstra.Dijkstra;
import jeanrenaud.nelson.graph.EuclideanDistance;
import jeanrenaud.nelson.graph.Node;
import jeanrenaud.nelson.graph.NodeFactory;

import java.io.IOException;

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
                new SimpleWeightedEdgeFactory<>(new EuclideanDistance<>()    /* TODO: Fournir une fonction de pondération
                                                            renvoyant la distance euclidienne (arrondie
                                                            à l'entier le plus proche) entre l'extrémité
                                                            initiale et l'extrémité finale de l'arête */),
                DATA_FOLDER + "R10000_1.txt"                       /* TODO: Chemin des fichiers */
        ).graph();

        Node source = graph.getVertices().stream().filter(node -> node.id() == 649).findFirst().orElse(null);
        Node target = graph.getVertices().stream().filter(node -> node.id() == 9407).findFirst().orElse(null);

        Dijkstra dijkstra = new Dijkstra(graph);

        System.out.println(dijkstra);

        System.out.println("\nRunning...\n");

        dijkstra.run(source, target);
        System.out.println(dijkstra);

        System.out.println("\nResult\n");

        System.out.println("\nShortest path\n");
        System.out.println(dijkstra.getShortestPath());
        System.out.println("\n- Bidirectional Dijkstra -\n");
        BidirectionalDijkstra bidirectionalDijkstra = new BidirectionalDijkstra(graph);
        System.out.println("\nRunning...\n");
        bidirectionalDijkstra.run(source, target);
        System.out.println("\nShortest path\n");
        System.out.println(bidirectionalDijkstra.getShortestPath());
    }
}
