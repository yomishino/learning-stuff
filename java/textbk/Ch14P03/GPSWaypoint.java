/**
 * A class of GPS waypoints. 
 * Each point is represented as a pair of doubles.
 * This class is designed to be an immutable class such that, once
 * constructed, the values of the object cannot be changed.
 * @author yomishino
 * @version 1.0
 */
public class GPSWaypoint {
    /** The x-coordinate of the waypoint. */
    private double x;
    /** The y-coordinate of the waypont. */
    private double y;

    /** 
     * Constructs a <code>Waypoint</code> object with the given coordinate.
     * @param x A <code>double</code> of the <i>x</i>-coordinate.
     * @param y A <code>dobule</code> of the <i>y</i>-coordinate.
     */
    public GPSWaypoint(double x, double y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Gets the <i>x</i>-coordinate of the waypoint.
     * @return A <code>double</code> of the <i>x</i>-coordinate.
     */
    public double getX() {
        return x;
    }

    /**
     * Gets the <i>y</i>-coordinate of the waypoint.
     * @return A <code>double</code> of the <i>y</i> coordinate.
     */
    public double getY() {
        return y;
    }

    /**
     * Returns a string representation of the waypoint, in the form of (x, y).
     * @return A <code>String</code> representing the waypoint.
     */
    @Override
    public String toString() {
        return "(" + x + ", " + y + ")";
    }

   /**
    * Computes the Euclidean distance between this waypoint and the other 
    * given waypoint.
    * The distance is computed in the coordinate unit, so two points (1, 0) 
    * and (2, 0) will have distance of 1, regardless how many metres/miles
    * it might represent.
    * @param other A <code>GPSWaypoint</code> object of the other waypoint.
    * @return A <code>double</code> of the distance between two waypoints.
    * @throws NullPointerException if the given waypoint is <code>null</code>.  
    */
    public double distance(GPSWaypoint other) throws NullPointerException {
        if (other == null) 
            throw new NullPointerException(
                "Null given for calculating the distance between waypoints.");
        return Math.sqrt(Math.pow((x - other.x), 2) + Math.pow((y - other.y), 2));
    }
}
