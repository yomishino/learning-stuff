/**
 * A class of waypoints with timestamps recorded together.
 * The class is designed to be immutable.
 * @author yomishino
 * @version 1.0
 */
class WaypointTimestamped extends GPSWaypoint {
    /** Timestamp in seconds, in integer format. */
    int timestamp;

    /** 
     * Constructs a <code>WaypointTimestamped</code> object, with the
     * given coordinates and a timestamp.
     * @param x A <code>double</code> of the <i>x</i>-coordinate.
     * @param y A <code>double</code> of the <i>y</i>-coordinate.
     * @param timestamp A <code>int</code> of the timestamp in seconds.
     * @throws NegativeTimestampException if the added timestamp is a negative number.
     */
    WaypointTimestamped(double x, double y, int timestamp) {
        super(x, y);
        this.timestamp = timestamp;
    }

    /** 
     * Gets the timestamp in seconds, as an integer.
     * @return An <code>int</code> of the time.
     */
    int getTimestamp() {
        return timestamp;
    }

    /** 
     * Returns a string representation of the timestamped waypoints, in the form of
     * {@literal <(x, y), timestamp>}.
     * @return A <code>String</code> representing the timestamped waypoints.
     */
    @Override
    public String toString() {
        return "<" + super.toString() + ", " + timestamp + ">";
    }
}
