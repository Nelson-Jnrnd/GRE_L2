package jeanrenaud.nelson.dijkstra;

import graph.core.impl.Digraph;
import graph.core.impl.SimpleWeightedEdge;
import jeanrenaud.nelson.graph.Node;

public interface ShortestPathAlgorithm {
    Digraph<Node, SimpleWeightedEdge<Node>> getGraph();
    void run(Node source, Node target);
    Path getShortestPath();
    long getIteration();
    String getName();
}
