package jeanrenaud.nelson.graph;
import graph.core.Vertex;
import graph.data.CartesianVertexData;

/**
 * {@link Vertex} implementation describing a node in a orthonormal graph
 * @author Nelson Jeanrenaud
 */
public class Node implements Vertex {
    
    /** Coordinates of the node */
    private final int x;
    private final int y;
    /** Node identifier */
    private final int id;
    
    /**
     * Builds a node
     */
    public Node(final int id, final int x, final int y) {
        this.x = x;
        this.y = y;
        this.id = id;
    }
    
    /**
     * @return Coordinate on the X axis
     */
    public int getX() {
        return x;
    }
    /**
     * @return Coordinate on the Y axis
     */
    public int getY() {
        return y;
    }

    /**
     * @return Node identifier
     */
    @Override
    public int id() {
        return id;
    }
}
