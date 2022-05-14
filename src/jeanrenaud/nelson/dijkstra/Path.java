package jeanrenaud.nelson.dijkstra;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Represents a path between two nodes.
 */
public class Path{
    /** First node of the path */
    private final MarkedNode start;
    /** Last node of the path */
    private final MarkedNode end;

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
        while(current != null && current != start) {
            path.add(current);
            current = current.getPrevious();
        }
        Collections.reverse(path);
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
        return end.getDistance();
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
