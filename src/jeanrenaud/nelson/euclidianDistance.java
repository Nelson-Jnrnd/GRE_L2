package jeanrenaud.nelson;

import graph.core.EdgeWeighter;

/**
 * {@link EdgeWeighter} implementation describing a euclidian distance weighting function
 * @param <N> Associated node type
 */
class EuclidianDistance<N extends Node> implements EdgeWeighter<N>{

    /**
     * @param from Origin node
     * @param to Destination node
     * @return Euclidian distance between the two nodes
     */
    @Override
    public long weight(N from, N to) {
        return (long) Math.sqrt(Math.pow(from.getX() - to.getX(), 2) + Math.pow(from.getY() - to.getY(), 2));
    }
}
