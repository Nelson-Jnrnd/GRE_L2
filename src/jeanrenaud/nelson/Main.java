
/*
 * Le code rendu se situe uniquement dans ce package (event. sous-packages)
 */
package jeanrenaud.nelson;

import graph.core.Edge;
import graph.core.VertexFactory;
import graph.core.impl.SimpleWeightedEdgeFactory;
import graph.data.CartesianVertexData;
import graph.reader.CartesianGraphReader;

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
                new SimpleWeightedEdgeFactory<>(new EuclidianDistance<>()    /* TODO: Fournir une fonction de pondération
                                                            renvoyant la distance euclidienne (arrondie
                                                            à l'entier le plus proche) entre l'extrémité
                                                            initiale et l'extrémité finale de l'arête */),
                DATA_FOLDER + "R15_1.txt"                       /* TODO: Chemin des fichiers */
        ).graph();

        System.out.println(graph.getNVertices());
    }
}
