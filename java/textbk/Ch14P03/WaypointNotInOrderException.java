/**
 * An exception class, thrown when the waypoint to be added is out of order.
 * @author yomishino
 * @version 1.0
 */
public class WaypointNotInOrderException extends RuntimeException {
    /**
     * Constructs a <code>WaypointNotInOrderException</code> with a default message.
     */
    public WaypointNotInOrderException() {
        super("The new waypoint is not in order.");
    }

    /**
     * Constructs a <code>WaypointNotInOrderException</code> with a given message.
     * @param message A <code>String</code> of the customised exception message.
     */
    public WaypointNotInOrderException(String message) {
        super(message);
    }
}

