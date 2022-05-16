package jeanrenaud.nelson.graph;

import graph.core.VertexFactory;
import graph.data.CartesianVertexData;

import java.util.Objects;

/**
 * Describes a factory to build any type of user-defined node
 * @author Nelson Jeanrenaud
 */
public class NodeFactory implements VertexFactory<Node, CartesianVertexData> {

    /**
     * Builds a node
     * @param id Node id of the node to build
     * @param coordinates CartesianVertexData coordinates of the node to build
     * @return The newly built node
     * @throws NullPointerException if the coordinates are null
     */
    @Override
    public Node makeVertex(int id, CartesianVertexData coordinates) {
        Objects.requireNonNull(coordinates, "coordinates cannot be null");
        return  new Node(id, coordinates.x, coordinates.y);
    }
}
