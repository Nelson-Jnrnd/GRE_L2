package jeanrenaud.nelson.dijkstra;

import java.util.PriorityQueue;

/**
 * A priority queue that supports Dijkstra's algorithm.
 * @author Nelson Jeanrenaud
 * @see Dijkstra
 */
public class DijkstraPriorityQueue extends PriorityQueue<MarkedNode> {
    public DijkstraPriorityQueue(int size) {
        super(size);
    }

    /**
     * Update the priority of the specified element.
     * @param element
     */
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

    /**
     * @return the smallest node in the queue and mark it as shortest path known
     */
    @Override
    public MarkedNode poll() {
        MarkedNode element = super.poll();
        if(element != null)
            element.shortestPathDiscovered();
        return element;
    }
}
