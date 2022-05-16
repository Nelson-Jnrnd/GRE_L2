package jeanrenaud.nelson.dijkstra;

import graph.core.impl.SimpleWeightedEdge;
import jeanrenaud.nelson.graph.Node;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

/**
 * Represents a path between two nodes.
 */
public class Path {

    private LinkedList<SimpleWeightedEdge<Node>> edges;

    public Path() {
        edges = new LinkedList<>();
    }
    public Path(List<SimpleWeightedEdge<Node>> edges) {
        this.edges = new LinkedList<>(edges);
    }

    public void push_back(SimpleWeightedEdge<Node> edge) {
        Objects.requireNonNull(edge);
        if (!edges.isEmpty() && edges.getLast().to() != edge.from()) {
            throw new IllegalArgumentException("The edge does not start from the last node of the path");
        }
        edges.addLast(edge);
    }

    public void push_back(Path path) {
        Objects.requireNonNull(path);
        if (!edges.isEmpty() && !path.edges.isEmpty() && edges.getLast().to() != path.edges.getFirst().from()) {
            throw new IllegalArgumentException("The path does not start from the last node of the path");
        }
        edges.addAll(path.edges);
    }

    public void push_front(SimpleWeightedEdge<Node> edge) {
        Objects.requireNonNull(edge);
        if (!edges.isEmpty() && edges.getFirst().from() != edge.to()) {
            throw new IllegalArgumentException("The edge does not end at the first node of the path");
        }
        edges.addFirst(edge);
    }

    public void push_front(Path path) {
        if (!edges.isEmpty() && !path.edges.isEmpty() && edges.getFirst().from() != path.edges.getLast().to()) {
            throw new IllegalArgumentException("The path does not end at the first node of the path");
        }
        edges.addAll(0, path.edges);
    }

    public Path reversed() {
        Path p = new Path();
        for (SimpleWeightedEdge<Node> edge : edges)
            p.push_front(new SimpleWeightedEdge<>(edge.to(), edge.from(), edge.weight()));
        return p;
    }

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
    public long getNumberOfNodes() {
        return edges.size() + 1;
    }
}
