package jeanrenaud.nelson;

import java.util.PriorityQueue;

/**
 * A priority queue that supports Dijkstra's algorithm.
 */
public class DijkstraPriorityQueue extends PriorityQueue<MarkedNode> {
    public DijkstraPriorityQueue(int size) {
        super(size);
    }

    public void update(MarkedNode element) {
        remove(element);
        add(element);
    }

    /**
     * @param o object to be checked for containment in this queue
     * @return true if this queue contains the specified element
     */
    @Override
    public boolean contains(Object o) {
        return !((MarkedNode)o).isShortestPathKnown();
    }

    @Override
    public MarkedNode poll() {
        MarkedNode element = super.poll();
        if(element != null)
            element.shortestPathDiscovered();
        return element;
    }
}
