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
     */
    @Override
    public long weight(N from, N to) {
        Objects.requireNonNull(from);
        Objects.requireNonNull(to);
        return Math.round(Math.sqrt(Math.pow(from.getX() - to.getX(), 2) + Math.pow(from.getY() - to.getY(), 2)));
    }
}
