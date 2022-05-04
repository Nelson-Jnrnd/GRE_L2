package jeanrenaud.nelson;

import graph.core.impl.Digraph;
import graph.core.impl.SimpleWeightedEdge;

import java.util.*;

public class Dijkstra {
    private final int nbVertices;
    private final Digraph<Node, SimpleWeightedEdge<Node>> graph;
    private final Node source;

    private int iteration;

    private LinkedList<MarkedNode> unkownShortestPathVertices;
    private LinkedList<MarkedNode> shortestPathVertices;

    public Dijkstra(Digraph<Node, SimpleWeightedEdge<Node>> graph, Node source) {
        this.graph = graph;
        this.source = source;
        this.nbVertices = graph.getNVertices();
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
            if(current.distance == Integer.MAX_VALUE) {
                break; // No more path to explore
            }
            graph.getSuccessorList(current.graphIndex).forEach(
                    (successorEdge) -> {
                        int newDistance = current.distance + (int)successorEdge.weight(); //TODO int to long
                        Node successor = successorEdge.to();

                        // Search for the node in the queue
                        MarkedNode successorMarkedNode = unkownShortestPathVertices.stream()
                                .filter(node -> node.node.equals(successor))
                                .findFirst()
                                .orElse(null);
                        if(successorMarkedNode == null) {
                           // throw new IllegalArgumentException("The node " + successor + " is not in the queue");
                        } else if(newDistance < successorMarkedNode.distance) {
                            // The node is in the queue but the new distance is shorter
                            successorMarkedNode.update(newDistance, current);
                            shortestPathVertices.add(successorMarkedNode);
                        }
                    }
            );
        }
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
                .filter(node -> node.node.equals(destination))
                .findFirst()
                .orElse(null);
        if(target == null) {
            throw new IllegalArgumentException("The destination " + destination + " can't be reached");
        }
        MarkedNode current = target;
        List<Integer> path = new ArrayList<>();
        while(current != null) {
            path.add(current.node.id());
            current = current.previous;
        }
        Collections.reverse(path);
        return path.stream().mapToInt(Integer::intValue).toArray();
    }
    public static class MarkedNode implements Comparable<MarkedNode> {
        private final int graphIndex;
        private final Node node;
        private int distance;
        private MarkedNode previous;

        public MarkedNode(int graphIndex, Node node, int distance, MarkedNode previous) {
            this.graphIndex = graphIndex;
            this.node = node;
            this.distance = distance;
            this.previous = previous;
        }

        @Override
        public int compareTo(MarkedNode o) {
            return Integer.compare(distance, o.distance);
        }

        public void update(int distance, MarkedNode previous) {
            this.distance = distance;
            this.previous = previous;
        }

        @Override
        public String toString() {
            return "MarkedNode{" +
                    "graphIndex=" + graphIndex +
                    ", node=" + node.id() +
                    ", distance=" + distance +
                    ", previous=" + (previous == null ? null : previous.node.id()) +
                    '}';
        }
    }
}
