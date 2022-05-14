package jeanrenaud.nelson.graph;

import graph.core.impl.Digraph;
import graph.core.impl.SimpleWeightedEdge;

import java.util.*;

public class Dijkstra {
    private final int nbVertices;
    private final Digraph<Node, SimpleWeightedEdge<Node>> graph;
    private final MarkedNode[] markedNodes;
    private final Node source;
    private final Node target;
    private int iteration;
    private final DijkstraPriorityQueue nodePriorityQueue;

    public Dijkstra(Digraph<Node, SimpleWeightedEdge<Node>> graph, Node source, Node target) {
        this.graph = graph;
        this.source = source;
        this.nbVertices = graph.getNVertices();
        this.target = target;
        this.nodePriorityQueue = new DijkstraPriorityQueue(nbVertices);
        this.markedNodes = new MarkedNode[nbVertices];
        Initialize();
    }

    private void Initialize() {
        iteration = 0;
        nodePriorityQueue.clear();

        int index = 0;
        for (Node node : graph.getVertices()) {
            MarkedNode m = (new MarkedNode(node, node.equals(source) ? 0 : Integer.MAX_VALUE, null));
            nodePriorityQueue.add(m);
            markedNodes[index++] = m;
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
        //System.out.println(this);
        while (!doIteration()){
            //System.out.println(this);
        }
    }

    private boolean doIteration() {
        iteration++;
        // Remove the node with the smallest distance from the queue
        MarkedNode removedNode = nodePriorityQueue.poll();
        //System.out.println("removed node " + removedNode);
        // If that distance is infinite, there is no path to the target
        if(removedNode == null || removedNode.getDistance() == Integer.MAX_VALUE) {
            return true;
        }
        // For each successor of the node:
        graph.getSuccessorList(removedNode.getNode().id()).forEach(
                (successorEdge) -> {
                    long newDistance = removedNode.getDistance() + successorEdge.weight();
                    MarkedNode successor = markedNodes[successorEdge.to().id()];
                    // If the distance to the successor is greater than the distance to the node plus the edge weight
                    if (newDistance < successor.getDistance()) {
                        // Update the distance to the successor
                        // Update the predecessor of the successor
                        successor.update(newDistance, removedNode);
                        nodePriorityQueue.update(successor);
                    }
                }
        );
        return false;
    }

    @Override
    public String toString() {
        return iteration + " - " + Arrays.toString(markedNodes);
    }

    public Collection<MarkedNode> getResult() {
        return List.of(markedNodes);
    }

    public Path getShortestPath(Node destination) {
        if(destination.id() >= nbVertices) {
            throw new IllegalArgumentException("Destination node is not in the graph");
        }
        MarkedNode targetNode = markedNodes[destination.id()];
        if(!targetNode.isShortestPathKnown()) {
            throw new IllegalArgumentException("The destination " + destination + " can't be reached");
        }
        return new Path(markedNodes[source.id()], targetNode);
    }
}
