package jeanrenaud.nelson;

import graph.core.VertexFactory;
import graph.data.CartesianVertexData;

// TODO : generify to extends node
/**
 * Describes a factory to build any type of user-defined node
 */
public class NodeFactory implements VertexFactory<Node, CartesianVertexData> {

    /**
     * Builds a node
     * @param id Node id
     * @param CartesianVertexData coordinates of the node
     * @return A new node
     */
    @Override
    public Node makeVertex(int id, CartesianVertexData coordiantes) {
        return  new Node(id, coordiantes);
    }
}
