package jeanrenaud.nelson.graph;

import graph.core.EdgeWeighter;

import java.util.Objects;

/**
 * {@link EdgeWeighter} implementation describing a euclidean distance weighting function
 * @param <N> Associated node type
 * @author Nelson Jeanrenaud
 */
public class EuclideanDistance<N extends Node> implements EdgeWeighter<N>{

    /**
     * @param from Origin node
     * @param to Destination node
     * @return Euclidean distance between the two nodes
     * @throws NullPointerException if any of the nodes is null
     */
    @Override
    public long weight(N from, N to) {
        Objects.requireNonNull(from, "from node cannot be null");
        Objects.requireNonNull(to, "to node cannot be null");
        return Math.round(Math.sqrt(Math.pow(from.getX() - to.getX(), 2) + Math.pow(from.getY() - to.getY(), 2)));
    }
}
