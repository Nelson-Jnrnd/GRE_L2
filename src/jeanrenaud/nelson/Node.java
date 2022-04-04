package jeanrenaud.nelson;
import graph.core.Vertex;
import graph.data.CartesianVertexData;

/**
 * {@link Vertex} implementation describing a node in a orthonormal graph
 * @author Nelson Jeanrenaud
 */
public class Node implements Vertex {
    
    /** Coordinates of the node */
    private final CartesianVertexData coordinates;
    /** Node identifier */
    private int id;
    
    /**
     * Builds a node
     * @param coordinates Coordinates of the node
     */
    public Node(final int id, final CartesianVertexData coordinates) {
        this.coordinates = coordinates;
        this.id = id;
    }
    
    /**
     * @return Coordinate on the X axis
     */
    public int getX() {
        return coordinates.x;
    }
    /**
     * @return Coordinate on the Y axis
     */
    public int getY() {
        return coordinates.y;
    }

    /**
     * @return Node identifier
     */
    @Override
    public int id() {
        return id;
    }
}
