package jeanrenaud.nelson.graph;

import graph.core.VertexFactory;
import graph.data.CartesianVertexData;

/**
 * Describes a factory to build any type of user-defined node
 */
public class NodeFactory implements VertexFactory<Node, CartesianVertexData> {

    /**
     * Builds a node
     * @param id Node id
     * @return A new node
     */
    @Override
    public Node makeVertex(int id, CartesianVertexData coordiantes) {
        return  new Node(id, coordiantes.x, coordiantes.y);
    }
}
