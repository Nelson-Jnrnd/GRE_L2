package jeanrenaud.nelson;

import graph.core.impl.Digraph;
import graph.core.impl.SimpleWeightedEdge;

import java.util.*;

public class Dijkstra {
    private final int nbVertices;
    private final Digraph<Node, SimpleWeightedEdge<Node>> graph;
    private final Node source;
    private final Node target;
    private int iteration;

    private LinkedList<MarkedNode> unkownShortestPathVertices;
    private LinkedList<MarkedNode> shortestPathVertices;


    public Dijkstra(Digraph<Node, SimpleWeightedEdge<Node>> graph, Node source, Node target) {
        this.graph = graph;
        this.source = source;
        this.nbVertices = graph.getNVertices();
        this.target = target;
        shortestPathVertices = new LinkedList<>();
        unkownShortestPathVertices = new LinkedList<>();

        Initialize();
    }

    private void Initialize() {
        iteration = 0;
        unkownShortestPathVertices.clear();

        List<Node> nodes = graph.getVertices();
        int index = 0;
        for (Node node : nodes) {
            // Distance to the source is 0
            unkownShortestPathVertices.add(new MarkedNode(
                    index++,
                    node,
                    (node.equals(source) ? 0 : Integer.MAX_VALUE),
                    null
            ));
        }
    }

    private static <T extends Comparable<T>> T popMin(List<T> list) {
        T min = list.get(0);
        for (int i = 1; i < list.size(); i++) {
            if (list.get(i).compareTo(min) < 0) {
                min = list.get(i);
            }
        }
        list.remove(min);
        return min;
    }

    public void run() {
        while (!unkownShortestPathVertices.isEmpty()) {
            MarkedNode current = popMin(unkownShortestPathVertices);
            if(current.getDistance() == Integer.MAX_VALUE || current.getNode().id() == target.id()) {
                break; // No more path to explore
            }
            graph.getSuccessorList(current.getNode().id()).forEach(
                    (successorEdge) -> {
                        int newDistance = current.getDistance() + (int)successorEdge.weight(); //TODO int to long
                        Node successor = successorEdge.to();

                        // Search for the node in the queue
                        MarkedNode successorMarkedNode = unkownShortestPathVertices.stream()
                                .filter(node -> node.getNode().equals(successor))
                                .findFirst()
                                .orElse(null);
                        if(successorMarkedNode == null) {
                           throw new IllegalArgumentException("The node " + successor + " is not in the queue");
                        } else if(newDistance < successorMarkedNode.getDistance()) {
                            // The node is in the queue but the new distance is shorter
                            successorMarkedNode.update(newDistance, current);
                            shortestPathVertices.add(successorMarkedNode);
                        }
                    }
            );
        }
    }

    private void doIteration() {
        // Remove the node with the smallest distance from the queue
        // If that distance is infinite, there is no path to the target
        // If that distance is not infinite, add the node to the shortest path
        // For each successor of the node:
            // If the distance to the successor is greater than the distance to the node plus the edge weight
            // Update the distance to the successor
            // Update the predecessor of the successor
    }

    @Override
    public String toString() {
        return "Dijkstra{" +
                "nbVertices=" + nbVertices +
                ", graph=" + graph +
                ", source=" + source +
                ", iteration=" + iteration +
                ", unkownShortestPathVertices=" + unkownShortestPathVertices +
                '}';
    }

    public Collection<MarkedNode> getResult() {
        return shortestPathVertices;
    }

    public int[] getShortestPath(Node destination) {
        MarkedNode target = shortestPathVertices.stream()
                .filter(node -> node.getNode().equals(destination))
                .findFirst()
                .orElse(null);
        if(target == null) {
            throw new IllegalArgumentException("The destination " + destination + " can't be reached");
        }
        MarkedNode current = target;
        List<Integer> path = new ArrayList<>();
        while(current != null) {
            path.add(current.getNode().id());
            current = current.getPrevious();
        }
        Collections.reverse(path);
        return path.stream().mapToInt(Integer::intValue).toArray();
    }
}
