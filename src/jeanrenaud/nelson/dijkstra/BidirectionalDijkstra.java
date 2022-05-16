package jeanrenaud.nelson.dijkstra;

import graph.core.impl.Digraph;
import graph.core.impl.SimpleWeightedEdge;
import jeanrenaud.nelson.graph.Node;


public class BidirectionalDijkstra implements ShortestPathAlgorithm {
    private final DijkstraConditional forward;
    private final DijkstraConditional backward;

    private long shortestPathLength;
    private Path shortestPath;

    public BidirectionalDijkstra(Digraph<Node, SimpleWeightedEdge<Node>> graph) {
        this.shortestPathLength = Long.MAX_VALUE;
        this.shortestPath = null;

        this.forward = new DijkstraConditional(graph);
        this.backward = new DijkstraConditional(graph);

        forward.setOther(backward);
        backward.setOther(forward);
    }

    private void initialize(Node source, Node target) {
        this.shortestPathLength = Long.MAX_VALUE;
        this.shortestPath = null;

        forward.initialize(source, target);
        backward.initialize(target, source);
    }

    @Override
    public Digraph<Node, SimpleWeightedEdge<Node>> getGraph() {
        return forward.getGraph();
    }

    public void run(Node source, Node target) {
        initialize(source, target);
        while (!getNextIteration().doIteration()) {
        }
    }

    public Path getShortestPath() {
        return shortestPath;
    }

    @Override
    public long getIteration() {
        return forward.getIteration() + backward.getIteration();
    }

    @Override
    public String getName() {
        return "Bidirectional Dijkstra";
    }

    private Dijkstra getNextIteration() {
        return (forward.getIteration() <= backward.getIteration()) ? forward : backward;
    }


    private class DijkstraConditional extends Dijkstra {
        private DijkstraConditional other;

        public void setOther(DijkstraConditional other) {
            this.other = other;
        }

        public DijkstraConditional(Digraph<Node, SimpleWeightedEdge<Node>> graph) {
            super(graph);
        }

        @Override
        protected boolean isFinished(MarkedNode removedNode) {
            return super.isFinished(removedNode) ||
                    !other.getNodePriorityQueue().contains(other.getMarkedNodeById(removedNode.getNode().id()));
        }

        @Override
        protected void processEdge(SimpleWeightedEdge<Node> edge, MarkedNode removedNode) {
            super.processEdge(edge, removedNode);
            if (other.getMarkedNodeById(edge.to().id()).isShortestPathKnown()) {
                long newShortestPathLength = removedNode.getDistance()
                        + edge.weight()
                        + other.getMarkedNodeById(edge.to().id()).getDistance();
                if (newShortestPathLength < shortestPathLength) {
                    shortestPathLength = newShortestPathLength;
                    Path path = this.getShortestPath(removedNode.getNode());
                    path.push_back(edge); // TODO renvoyer un path au lieu de void pour chainer
                    path.push_back(other.getShortestPath(edge.to()).reversed());
                    shortestPath = path;
                }
            }
        }
    }
}
