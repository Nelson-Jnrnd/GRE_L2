package jeanrenaud.nelson.dijkstra;

import graph.core.impl.SimpleWeightedEdge;
import jeanrenaud.nelson.graph.Node;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

// TODO fix le calcul de la distance totale.
// TODO surement refactor en utilisant les arcs
/**
 * Represents a path between two nodes.
 */
public class Path{
    /** First node of the path */
    private final MarkedNode start;
    /** Last node of the path */
    private MarkedNode end;

    /** List of all the nodes in the path */
    private final LinkedList<MarkedNode> path;

    /**
     * Creates a new path between two nodes.
     * @param start first node of the path
     * @param end last node of the path
     */
    public Path(MarkedNode start, MarkedNode end) {
        this.start = start;
        this.end = end;
        MarkedNode current = end;
        path = new LinkedList<>();
        while(current != null) {
            path.add(current);
            if(current.getPrevious() != null)
                current = current.getPrevious();
            else
                break;
        }
        Collections.reverse(path);
    }

    public void append(List<MarkedNode> path, SimpleWeightedEdge<Node> connectingEdge) {
        this.path.addAll(path);
        end = path.get(path.size() - 1);
    }

    public List<MarkedNode> getReversedPath() {
        LinkedList<MarkedNode> reversed = new LinkedList<>(path);
        Collections.reverse(reversed);
        MarkedNode oldPrevious = reversed.get(0);
        SimpleWeightedEdge<Node> oldEdge = reversed.get(0).getPreviousEdge();
        reversed.get(0).update(0, null, null);
        for (int i = 1; i < reversed.size(); i++) {
            MarkedNode node = reversed.get(i);
            oldPrevious.update(oldPrevious.getDistance(), node, node.getPreviousEdge());
            oldPrevious = node.getPrevious();
            oldEdge = node.getPreviousEdge();
        }
        return reversed;
    }
    public List<MarkedNode> getPath() {
        return path;
    }

    public MarkedNode getStart() {
        return start;
    }

    public MarkedNode getEnd() {
        return end;
    }

    /**
     * Returns the cost of the path.
     * @return the cost of the path
     */
    public long getCost() {
        long cost = 0;
        for (MarkedNode node : path) {
            if(node.getPrevious() != null)
                cost += node.getPreviousEdge().weight();
        }
        return cost;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (MarkedNode node : path) {
            sb.append(node.getNode().id());
            if(node == end)
                break;
            sb.append(" -> ");
        }
        sb.append(" total cost: ").append(getCost());
        return sb.toString();
    }
}
