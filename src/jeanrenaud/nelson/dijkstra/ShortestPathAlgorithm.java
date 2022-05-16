package jeanrenaud.nelson.dijkstra;

import graph.core.impl.Digraph;
import graph.core.impl.SimpleWeightedEdge;
import jeanrenaud.nelson.graph.Node;

public interface ShortestPathAlgorithm {
    /**
     * Get the graph used by the algorithm.
     * @return the graph used by the algorithm.
     */
    Digraph<Node, SimpleWeightedEdge<Node>> getGraph();

    /**
     * Calculate the shortest path from the source to the target.
     * @param source the source node.
     * @param target the target node.
     */
    void run(Node source, Node target);

    /**
     * Get the shortest path from the source to the target.
     * @return the shortest path from the source to the target.
     */
    Path getShortestPath();
    /**
     * Get the number of iterations performed by the algorithm.
     * @return the number of iterations performed by the algorithm.
     */
    long getIteration();
    /**
     * Get the name of the algorithm.
     * @return the name of the algorithm.
     */
    String getName();
}
