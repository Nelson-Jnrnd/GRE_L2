
/*
 * Le code rendu se situe uniquement dans ce package (event. sous-packages)
 */
package jeanrenaud.nelson;

import graph.core.impl.SimpleWeightedEdgeFactory;
import graph.reader.CartesianGraphReader;

import java.io.IOException;

public class Main {
    /*
     * NE PAS MODIFIER
     * Les fichiers de données sont à placer à la racine de ce répertoire
     */
    private static final String DATA_FOLDER = "data/";

    public static void main(String[] args) throws IOException {
        var graph = new CartesianGraphReader<>(
                null                                    /* TODO: Fournir une fabrique de sommets (il
                                                            s'agit d'une interface fonctionnelle) */,
                new SimpleWeightedEdgeFactory<>(null    /* TODO: Fournir une fonction de pondération
                                                            renvoyant la distance euclidienne (arrondie
                                                            à l'entier le plus proche) entre l'extrémité
                                                            initiale et l'extrémité finale de l'arête */),
                DATA_FOLDER + "?"                       /* TODO: Chemin des fichiers */
        ).graph();
    }
}
