package jeanrenaud.nelson.graph;

import graph.core.EdgeWeighter;
import jeanrenaud.nelson.graph.Node;

/**
 * {@link EdgeWeighter} implementation describing a euclidian distance weighting function
 * @param <N> Associated node type
 */
public class EuclidianDistance<N extends Node> implements EdgeWeighter<N>{

    /**
     * @param from Origin node
     * @param to Destination node
     * @return Euclidian distance between the two nodes
     */
    @Override
    public long weight(N from, N to) {
        return Math.round(Math.sqrt(Math.pow(from.getX() - to.getX(), 2) + Math.pow(from.getY() - to.getY(), 2)));
    }
}
