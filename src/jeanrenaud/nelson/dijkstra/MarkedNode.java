package jeanrenaud.nelson.dijkstra;

import graph.core.impl.SimpleWeightedEdge;
import jeanrenaud.nelson.graph.Node;

/**
 * Encapsulate a node, it's distance lambda from the source node and the previous node in the shortest path.
 * @author Nelson Jeanrenaud
 * @see Dijkstra
 */
public class MarkedNode implements Comparable<MarkedNode> {
    /** Node marked by the algorithm. */
    private final Node node;
    /** Previous node in the shortest path to the source. Can be null */
    private MarkedNode previous;


    /** Previous edge in the shortest path to the source. Can be null */
    private SimpleWeightedEdge<Node> previousEdge;
    /** Distance from the source node. */
    private long distance;
    /**
     * True if the node has been removed from the priority queue by the Dijkstra Algorithm.
     */
    private boolean shortestPathKnown;

    public Node getNode() {
        return node;
    }

    public MarkedNode getPrevious() {
        return previous;
    }

    public SimpleWeightedEdge<Node> getPreviousEdge() {
        return previousEdge;
    }

    public long getDistance() {
        return distance;
    }

    public boolean isShortestPathKnown() {
        return shortestPathKnown;
    }

    /**
     * Mark the node as having been removed from the priority queue.
     */
    public void shortestPathDiscovered() {
        shortestPathKnown = true;
    }

    /**
     *
     * @param node Node to encapsulate
     * @param distance Distance from the source node
     * @param previous Previous node in the shortest path to the source
     */
    public MarkedNode(Node node, long distance, MarkedNode previous, SimpleWeightedEdge<Node> previousEdge) {
        this.node = node;
        this.distance = distance;
        this.previous = previous;
        this.previousEdge = previousEdge;
        this.shortestPathKnown = false;
    }

    /**
     * Update the distance and the previous node in the shortest path to the source.
     * @param distance Distance from the source node
     * @param previous Previous node in the shortest path to the source
     */
    public void update(long distance, MarkedNode previous, SimpleWeightedEdge<Node> previousEdge) {
        this.distance = distance;
        this.previous = previous;
        this.previousEdge = previousEdge;
    }
    @Override
    public int compareTo(MarkedNode o) {
        return Long.compare(distance, o.distance);
    }


    @Override
    public String toString() {
        return node.id() + ": (" + distance + ", " + (previous == null ? null : previous.node.id()) + ")" + (shortestPathKnown ? "*" : "");
    }
}
