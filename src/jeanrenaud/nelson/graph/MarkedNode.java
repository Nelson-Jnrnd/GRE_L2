package jeanrenaud.nelson.graph;

public class MarkedNode implements Comparable<MarkedNode> {
    private final Node node;
    private MarkedNode previous;
    private long distance;
    private boolean shortestPathKnown;

    public Node getNode() {
        return node;
    }

    public MarkedNode getPrevious() {
        return previous;
    }

    public long getDistance() {
        return distance;
    }

    public boolean isShortestPathKnown() {
        return shortestPathKnown;
    }

    public void shortestPathDiscovered() {
        shortestPathKnown = true;
    }

    public MarkedNode(Node node, long distance, MarkedNode previous) {
        this.node = node;
        this.distance = distance;
        this.previous = previous;
        this.shortestPathKnown = false;
    }

    @Override
    public int compareTo(MarkedNode o) {
        return Long.compare(distance, o.distance);
    }

    public void update(long distance, MarkedNode previous) {
        this.distance = distance;
        this.previous = previous;
    }

    @Override
    public String toString() {
        return node.id() + ": (" + distance + ", " + (previous == null ? null : previous.node.id()) + ")" + (shortestPathKnown ? "*" : "");
    }
}
