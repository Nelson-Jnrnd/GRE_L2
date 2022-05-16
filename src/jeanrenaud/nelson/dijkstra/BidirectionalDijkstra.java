package jeanrenaud.nelson.dijkstra;

import graph.core.impl.Digraph;
import graph.core.impl.SimpleWeightedEdge;
import jeanrenaud.nelson.graph.Node;

/**
 * Bidirectional Dijkstra algorithm.
 * Uses two Dijkstra algorithms to find the shortest path between two nodes.
 * @author Nelson Jeanrenaud
 */
public class BidirectionalDijkstra implements ShortestPathAlgorithm {
    /** Dijkstra algorithm to find the shortest path from the source node to the target node.*/
    private final DijkstraConditional forward;
    /** Dijkstra algorithm to find the shortest path from the target node to the source node.*/
    private final DijkstraConditional backward;
    /** Shortest path found by the algorithm so far */
    private long shortestPathLength;

    /**
     * Constructor.
     * @param graph the graph to use.
     */
    public BidirectionalDijkstra(Digraph<Node, SimpleWeightedEdge<Node>> graph) {
        this.shortestPathLength = Long.MAX_VALUE;

        this.forward = new DijkstraConditional(graph);
        this.backward = new DijkstraConditional(graph);

        forward.setOther(backward);
        backward.setOther(forward);
    }

    /**
     * Initializes the two Dijkstra algorithms.
     * @param source the source node.
     * @param target the target node.
     */
    private void initialize(Node source, Node target) {
        this.shortestPathLength = Long.MAX_VALUE;

        forward.initialize(source, target);
        backward.initialize(target, source);
    }

    @Override
    public Digraph<Node, SimpleWeightedEdge<Node>> getGraph() {
        return forward.getGraph();
    }

    @Override
    public void run(Node source, Node target) {
        initialize(source, target);
        while (!getNextIteration().doIteration()) {
        }
    }

    @Override
    public Path getShortestPath() {
        // We check which algorithm found the shortest path and build the path accordingly.
        if(shortestPathLength == forward.getLocalshortestPathLength()) {
            return forward.joinPath();
        }
        return backward.joinPath();
    }


    @Override
    public long getIteration() {
        return forward.getIteration() + backward.getIteration();
    }


    @Override
    public String getName() {
        return "Bidirectional Dijkstra";
    }

    /**
     * Returns which algorithm need to perform the next iteration.
     * The rule is that the bidirectional dijkstra algorithm will alternate between performing a forward and a backward iteration.
     * @return the algorithm that need to perform the next iteration.
     */
    private Dijkstra getNextIteration() {
        return (forward.getIteration() <= backward.getIteration()) ? forward : backward;
    }

    /**
     * Dijkstra algorithm used by the bidirectional dijkstra algorithm.
     * It implements additionnal rules to determine if the algorithm should stop or not.
     * And ways to join the paths found by the two algorithms.
     */
    private class DijkstraConditional extends Dijkstra {
        /**
         * Get the shortest path found by this algorithm so far.
         * @return the shortest path found by this algorithm so far.
         */
        public long getLocalshortestPathLength() {
            return localshortestPathLength;
        }
        /** the shortest path found by this algorithm so far.*/
        private long localshortestPathLength;
        /**
         * The connecting node between the two algorithms for the shortest path found so far.TODO maybe remove
         */
        private Node shortestPathNode;
        /**
         * The connecting edge between the two algorithms for the shortest path found so far.
         */
        private SimpleWeightedEdge<Node> shortestPathEdge;
        /**
         * The other algorithm in the bidirectional dijkstra algorithm.
         */
        private DijkstraConditional other;

        /**
         * Set the other algorithm in the bidirectional dijkstra algorithm.
         * @param other the other algorithm in the bidirectional dijkstra algorithm.
         */
        public void setOther(DijkstraConditional other) {
            this.other = other;
        }

        /**
         * Initialize the algorithm.
         * @param graph the graph to use.
         */
        public DijkstraConditional(Digraph<Node, SimpleWeightedEdge<Node>> graph) {
            super(graph);
        }

        /**
         * Initialize the algorithm.
         * @param source the source node.
         * @param target the target node.
         */
        @Override
        protected void initialize(Node source, Node target) {
            super.initialize(source, target);
            localshortestPathLength = Long.MAX_VALUE;
            shortestPathNode = null;
            shortestPathEdge = null;
        }

        @Override
        protected boolean isFinished(MarkedNode removedNode) {
            // If the node has already been visited by the other algorithm, we stop the algorithm.
            return super.isFinished(removedNode) ||
                    other.getMarkedNodeById(removedNode.getNode().id()).isShortestPathKnown();
        }

        /**
         * Overlaod the method to update the shortest path if we can connect the two algorithms.
         * @param edge the edge being processed.
         * @param removedNode the node being processed.
         */
        @Override
        protected void processEdge(SimpleWeightedEdge<Node> edge, MarkedNode removedNode) {
            super.processEdge(edge, removedNode);
            if (other.getMarkedNodeById(edge.to().id()).isShortestPathKnown()) {
                long newShortestPathLength = removedNode.getDistance()
                        + edge.weight()
                        + other.getMarkedNodeById(edge.to().id()).getDistance();
                // If the new path is shorter than the shortest path found so far, we update the shortest path found so far.
                // We don't build the path yet because we don't know if it is the shortest path or not. And building is a costly operation.
                if (newShortestPathLength < shortestPathLength) {
                    shortestPathLength = newShortestPathLength;
                    localshortestPathLength = newShortestPathLength;
                    shortestPathNode = removedNode.getNode();
                    shortestPathEdge = edge;
                }
            }
        }

        /**
         * Create the shortest path by connection the paths of the two algorithms.
         * @return the shortest path found.
         */
        public Path joinPath(){
            Path path = this.getShortestPath(shortestPathNode);
            path.push_back(shortestPathEdge); // TODO renvoyer un path au lieu de void pour chainer
            path.push_back(other.getShortestPath(shortestPathEdge.to()).reversed());
            return path;
        }
    }
}
