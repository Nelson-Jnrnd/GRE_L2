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
    /**
     * Destination node on which the algorithm is applied.
     */
    private Node target;

    /**
     * Iteration the algorithm is currently on.
     */
    private long iteration;

    /** if the algorithm has been initialized. */
    private boolean isInitialized;

    /**
     * Priority queue of the nodes to visit.
     */
    private final DijkstraPriorityQueue nodePriorityQueue;

    public Digraph<Node, SimpleWeightedEdge<Node>> getGraph() {
        return graph;
    }
    public long getIteration() {
        return iteration;
    }

    @Override
    public String getName() {
        return "Dijkstra";
    }

    /**
     * Create a new Dijkstra instance.
     * @param graph Graph on which the algorithm is applied.
     */
    public Dijkstra(Digraph<Node, SimpleWeightedEdge<Node>> graph) {
        Objects.requireNonNull(graph, "Graph cannot be null");
        this.graph = graph;
        this.nbVertices = graph.getNVertices();
        this.nodePriorityQueue = new DijkstraPriorityQueue(nbVertices);
        this.markedNodes = new MarkedNode[nbVertices];
        this.isInitialized = false;
    }

    /**
     * Initialize the algorithm.
     * @param source Source node.
     * @param target Destination node. If null, the algorithm will compute the shortest path to all the nodes.
     * @throws IllegalArgumentException if the source or the target node is not in the graph.
     * @throws NullPointerException if the source node is null.
     */
    protected void initialize(Node source, Node target) {
        Objects.requireNonNull(source, "Source node cannot be null");
        if(!graph.getVertices().contains(source) || (target != null && !graph.getVertices().contains(target))) {
            throw new IllegalArgumentException("Source or target node is not in the graph");
        }
        this.source = source;
        this.target = target;
        iteration = 0;
        nodePriorityQueue.clear();

        // Initialize the nodes marks
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
        isInitialized = true;
    }

    /**
     * Get the MarkedNode with the given index.
     * @param id Index of the node.
     * @return Node with the given index.
     * @throws IndexOutOfBoundsException if the index is out of bounds.
     */
    protected MarkedNode getMarkedNodeById(int id) {
        if(id < 0 || id >= nbVertices) {
            throw new IndexOutOfBoundsException("Invalid node id");
        }
        return markedNodes[id];
    }

    /**
     * Compute the shortest path from the source to all the other nodes.
     * @param source Source node.
     * @param target Destination node (optional), if null, the algorithm will compute the shortest path to all the nodes.
     * @throws IllegalArgumentException if the source or target node is not in the graph.
     * @throws NullPointerException if the source node is null.
     */
    public void run(Node source, Node target) {
        Objects.requireNonNull(source, "Source node cannot be null");
        initialize(source, target);
        while (!doIteration()){
        }
    }

    /**
     * Do an iteration of the algorithm.
     * @return true if the algorithm has finished, false otherwise.
     * @throws IllegalStateException if the algorithm has not been initialized.
     */
    protected boolean doIteration() {
        if(!isInitialized) {
            throw new IllegalStateException("The algorithm has not been initialized");
        }
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

    /**
     * Update the distance of a node if the remomovedNode improved the path.
     * @param edge Edge between the removed node and the successor.
     * @param removedNode Node removed from the queue.
     * @throws NullPointerException if the edge or the removed node is null.
     */
    protected void processEdge(SimpleWeightedEdge<Node> edge, MarkedNode removedNode) {
        Objects.requireNonNull(edge, "Edge cannot be null");
        Objects.requireNonNull(removedNode, "Removed node cannot be null");
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
     * Get the path from the source to the given node.
     * @param destination Node to which the path is computed.
     * @return The path from the source to the given node.
     * @throws IllegalArgumentException if the destination is not in the graph.
     * @throws NullPointerException if the destination is null.
     * @throws NoPathException if there is no path from the source to the destination.
     * @throws IllegalStateException if the algorithm has not been initialized.
     */
    public Path getShortestPath(Node destination) {
        Objects.requireNonNull(destination, "Destination node cannot be null");
        if(destination.id() >= nbVertices) {
            throw new IllegalArgumentException("Destination node is not in the graph");
        }
        if(!isInitialized) {
            throw new IllegalStateException("Algorithm has not been initialized");
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

    // Exception thrown if the algorithm can't find a path
    public class NoPathException extends IllegalArgumentException {
        public NoPathException() {
            super("The destination " + target + " can't be reached");
        }
    }
}
