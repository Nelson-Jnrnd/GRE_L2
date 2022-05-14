package jeanrenaud.nelson.graph;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class Path{
    private final MarkedNode start;
    private final MarkedNode end;

    private final LinkedList<MarkedNode> path;

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
