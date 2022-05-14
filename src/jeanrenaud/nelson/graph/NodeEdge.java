package jeanrenaud.nelson.graph;

import graph.core.Edge;

public class NodeEdge <N extends Node> implements Edge<N>{

    private N from;
    private N to;

    public NodeEdge(N from, N to) {
        this.from = from;
        this.to = to;
    }

    @Override
    public N from() {
        
        return null;
    }

    @Override
    public N to() {
        
        return null;
    }
    
}
