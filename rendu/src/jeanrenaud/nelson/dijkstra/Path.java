package jeanrenaud.nelson.dijkstra;

import graph.core.impl.SimpleWeightedEdge;
import jeanrenaud.nelson.graph.Node;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

/**
 * Represents a non-cyclic path between two nodes.
 * @author Nelson Jeanrenaud
 */
public class Path {

    /**
     * The list of edges that make up the path.
     */
    private final LinkedList<SimpleWeightedEdge<Node>> edges;

    public Path() {
        edges = new LinkedList<>();
    }

    /**
     * Creates a new path from the given list of edges.
     * @param edges the list of edges
     * @throws NullPointerException if the list of edges is empty
     */
    public Path(List<SimpleWeightedEdge<Node>> edges) {
        Objects.requireNonNull(edges, "The list of edges cannot be null");
        this.edges = new LinkedList<>(edges);
    }

    /**
     * Adds an edge at the end of the path.
     * @param edge the edge to add
     * @throws NullPointerException if the edge is null
     */
    public void push_back(SimpleWeightedEdge<Node> edge) {
        Objects.requireNonNull(edge, "The edge cannot be null");
        if (!edges.isEmpty() && edges.getLast().to() != edge.from()) {
            throw new IllegalArgumentException("The edge does not start from the last node of the path");
        }
        edges.addLast(edge);
    }

    /**
     * Append the given path to the end of this path.
     * @param path the path to append
     * @throws NullPointerException if the path is null
     */
    public void push_back(Path path) {
        Objects.requireNonNull(path, "The path cannot be null");
        if (!edges.isEmpty() && !path.edges.isEmpty() && edges.getLast().to() != path.edges.getFirst().from()) {
            throw new IllegalArgumentException("The path does not start from the last node of the path");
        }
        edges.addAll(path.edges);
    }

    /**
     * Adds an edge at the beginning of the path.
     * @param edge the edge to add
     * @throws NullPointerException if the edge is null
     */
    public void push_front(SimpleWeightedEdge<Node> edge) {
        Objects.requireNonNull(edge, "The edge cannot be null");
        if (!edges.isEmpty() && edges.getFirst().from() != edge.to()) {
            throw new IllegalArgumentException("The edge does not end at the first node of the path");
        }
        edges.addFirst(edge);
    }

    /**
     * Append the given path to the beginning of this path.
     * @param path the path to append
     * @throws NullPointerException if the path is null
     */
    public void push_front(Path path) {
        Objects.requireNonNull(path, "The path cannot be null");
        if (!edges.isEmpty() && !path.edges.isEmpty() && edges.getFirst().from() != path.edges.getLast().to()) {
            throw new IllegalArgumentException("The path does not end at the first node of the path");
        }
        edges.addAll(0, path.edges);
    }

    /**
     * Return a path that is the reverse of this path.
     * @return the reversed path
     */
    public Path reversed() {
        Path p = new Path();
        for (SimpleWeightedEdge<Node> edge : edges)
            p.push_front(new SimpleWeightedEdge<>(edge.to(), edge.from(), edge.weight()));
        return p;
    }

    /**
     * Calculates the total weight of the path.
     * @return the total weight of the path
     */
    public long totalWeight() {
        return edges.stream().mapToLong(SimpleWeightedEdge::weight).sum();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        for (SimpleWeightedEdge<Node> edge : edges) {
            sb.append(edge.from().id()).append(" -> ");
            if(edge == edges.getLast())
                sb.append(edge.to().id());
        }
        sb.append("] total cost: ").append(totalWeight());
        return sb.toString();
    }

    /**
     * Returns the list of nodes in the path.
     * @return the list of nodes
     */
    public long getNumberOfNodes() {
        return edges.size() + 1;
    }
}
