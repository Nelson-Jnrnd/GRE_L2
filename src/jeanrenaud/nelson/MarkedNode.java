package jeanrenaud.nelson;

public class MarkedNode implements Comparable<MarkedNode> {
    private final int graphIndex;
    private final Node node;
    private int distance;

    public Node getNode() {
        return node;
    }

    public int getDistance() {
        return distance;
    }

    public MarkedNode getPrevious() {
        return previous;
    }

    private MarkedNode previous;

    public MarkedNode(int graphIndex, Node node, int distance, MarkedNode previous) {
        this.graphIndex = graphIndex;
        this.node = node;
        this.distance = distance;
        this.previous = previous;
    }

    @Override
    public int compareTo(MarkedNode o) {
        return Integer.compare(distance, o.distance);
    }

    public void update(int distance, MarkedNode previous) {
        this.distance = distance;
        this.previous = previous;
    }

    @Override
    public String toString() {
        return "MarkedNode{" +
                "graphIndex=" + graphIndex +
                ", node=" + node.id() +
                ", distance=" + distance +
                ", previous=" + (previous == null ? null : previous.node.id()) +
                '}';
    }
}
