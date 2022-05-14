package jeanrenaud.nelson;

import graph.core.impl.Digraph;
import graph.core.impl.SimpleWeightedEdge;
import jeanrenaud.nelson.dijkstra.Dijkstra;
import jeanrenaud.nelson.dijkstra.MarkedNode;
import jeanrenaud.nelson.dijkstra.Path;
import jeanrenaud.nelson.graph.Node;


public class BidirectionalDijkstra {
    private final DijkstraConditional forward;
    private final DijkstraConditional backward;

    private final Node source;
    private final Node target;

    private long shortestPathLength;
    private Path shortestPath;

    public BidirectionalDijkstra(Digraph<Node, SimpleWeightedEdge<Node>> graph, Node source, Node target) {
        this.source = source;
        this.target = target;
        this.shortestPathLength = Long.MAX_VALUE;
        this.shortestPath = null;

        this.forward = new DijkstraConditional(graph, source);
        this.backward = new DijkstraConditional(graph, target);
        forward.setOther(backward);
        backward.setOther(forward);
    }

    public void run() {
        while (!getNextIteration().doIteration(
                getNextIteration() == forward ? target : source
        )){
        }
    }
    public Path getShortestPath() {
        return shortestPath;
    }

    private Dijkstra getNextIteration() {
        return (forward.getIteration() <= backward.getIteration()) ? forward : backward;
    }


    private class DijkstraConditional extends Dijkstra {
        private DijkstraConditional other;
        public void setOther(DijkstraConditional other) {
            this.other = other;
        }
        public DijkstraConditional(Digraph<Node, SimpleWeightedEdge<Node>> graph, Node source) {
            super(graph, source);
        }
        @Override
        protected boolean isFinished(Node target, MarkedNode removedNode) {
            return super.isFinished(target, removedNode) ||
                    !other.getNodePriorityQueue().contains(other.getMarkedNodeById(removedNode.getNode().id()));
        }

        @Override
        protected void processEdge(SimpleWeightedEdge<Node> edge, MarkedNode removedNode) {
            super.processEdge(edge, removedNode);
            if(other.getMarkedNodeById(edge.to().id()).isShortestPathKnown()) {
            long newShortestPathLength = removedNode.getDistance()
                    + edge.weight()
                    + other.getMarkedNodeById(edge.to().id()).getDistance();
            if(newShortestPathLength < shortestPathLength){
                shortestPathLength = newShortestPathLength;
                Path forwardPath = forward.getShortestPath(removedNode.getNode());
                forwardPath.append(backward.getShortestPath(edge.to()).getReversedPath());
                shortestPath = forwardPath;
            }
        }
        }
    }
}
