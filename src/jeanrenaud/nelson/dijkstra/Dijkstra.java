package jeanrenaud.nelson.dijkstra;

import graph.core.impl.Digraph;
import graph.core.impl.SimpleWeightedEdge;
import jeanrenaud.nelson.graph.Node;

import java.util.*;

/**
 * Implementation of the Dijkstra algorithm on a weighted non-oriented graph.
 * @author Nelson Jeanrenaud
 */
public class Dijkstra implements ShortestPathAlgorithm {
    /**
     * Number of nodes in the graph.
     */
    private final int nbVertices;


    /**
     * Graph on which the algorithm is applied.
     */
    private final Digraph<Node, SimpleWeightedEdge<Node>> graph;

    /**
     * List of all the nodes in the graph with their distance Lambda from the source and previous node.
     */
    private final MarkedNode[] markedNodes;

    /**
     * Source node on which the algorithm is applied.
     */
    private Node source;

    private Node target;

    private long iteration;


    private final DijkstraPriorityQueue nodePriorityQueue;
    public Digraph<Node, SimpleWeightedEdge<Node>> getGraph() {
        return graph;
    }
    protected DijkstraPriorityQueue getNodePriorityQueue() {
        return nodePriorityQueue;
    }
    public long getIteration() {
        return iteration;
    }

    @Override
    public String getName() {
        return "Dijkstra";
    }

    public Dijkstra(Digraph<Node, SimpleWeightedEdge<Node>> graph) {
        this.graph = graph;
        this.nbVertices = graph.getNVertices();
        this.nodePriorityQueue = new DijkstraPriorityQueue(nbVertices);
        this.markedNodes = new MarkedNode[nbVertices];
    }

    protected void initialize(Node source, Node target) {
        this.source = source;
        this.target = target;
        iteration = 0;
        nodePriorityQueue.clear();

        int index = 0;
        for (Node node : graph.getVertices()) {
            MarkedNode m = (new MarkedNode(
                    node,
                    node.equals(source) ? 0 : Integer.MAX_VALUE,
                    null,
                    null));
            nodePriorityQueue.add(m);
            markedNodes[index++] = m;
        }
    }

    protected MarkedNode getMarkedNodeById(int id) {
        return markedNodes[id];
    }

    /**
     * Compute the shortest path from the source to all the other nodes.
     */
    public void run(Node source, Node target) {
        initialize(source, target);
        while (!doIteration()){
        }
    }

    /**
     * Do an iteration of the algorithm.
     * @return true if the algorithm has finished, false otherwise.
     */
    public boolean doIteration() {
        iteration++;
        // Remove the node with the smallest distance from the queue
        MarkedNode removedNode = nodePriorityQueue.poll();
        // If that distance is infinite, there is no path to the target
        // If the removed node is the target, we are done
        // If the there is no node in the queue, we are done
        if (isFinished(removedNode)) return true;
        // For each successor of the node:
        graph.getSuccessorList(removedNode.getNode().id()).forEach(
                (successorEdge) -> processEdge(successorEdge, removedNode)
        );
        return false;
    }

    protected void processEdge(SimpleWeightedEdge<Node> edge, MarkedNode removedNode) {
        long newDistance = removedNode.getDistance() + edge.weight();
        MarkedNode successor = markedNodes[edge.to().id()];
        // If the distance to the successor is greater than the distance to the node plus the edge weight
        if (newDistance < successor.getDistance()) {
            // Update the distance to the successor
            // Update the predecessor of the successor
            successor.update(newDistance, removedNode, edge);
            nodePriorityQueue.update(successor);
        }
    }

    /**
     * Check if the algorithm has finished.
     * @param removedNode Node that has been removed from the queue.
     * @return true if the algorithm has finished, false otherwise.
     */
    protected boolean isFinished(MarkedNode removedNode) {
        return removedNode == null || removedNode.getDistance() == Integer.MAX_VALUE || (target != null && removedNode.getNode().id() == target.id());
    }

    @Override
    public String toString() {
        return iteration + " - " + Arrays.toString(markedNodes);
    }

    /**
     * Get the state of the nodes
     * @return The state of the nodes.
     */
    public Collection<MarkedNode> getResult() {
        return List.of(markedNodes);
    }

    /**
     * Get the path from the source to the given node.
     * @param destination Node to which the path is computed.
     * @return The path from the source to the given node.
     */
    public Path getShortestPath(Node destination) {
        Objects.requireNonNull(destination);
        if(destination.id() >= nbVertices) {
            throw new IllegalArgumentException("Destination node is not in the graph");
        }
        MarkedNode targetNode = markedNodes[destination.id()];
        if(!targetNode.isShortestPathKnown()) {
            throw new NoPathException();
        }
        Path path = new Path();
        MarkedNode currentNode = targetNode;
        while(currentNode.getPreviousEdge() != null) {
            path.push_front(currentNode.getPreviousEdge());
            currentNode = currentNode.getPrevious();
        }
        return path;
    }

    @Override
    public Path getShortestPath() {
        return getShortestPath(target);
    }

    // Custom exception if the algorithm can't find a path
    public class NoPathException extends IllegalArgumentException {
        public NoPathException() {
            super("The destination " + target + " can't be reached");
        }
    }
}
