/**
 * An exception class, thrown when the given timestamp is a negative number.
 * @author yomishino
 * @version 1.0
 */
public class NegativeTimestampException extends RuntimeException {
    /**
     * Constructs a <code>NegativeTimestampException</code> with a default message.
     */
    public NegativeTimestampException() {
        super("The given timestamp is a negative number.");
    }

    /**
     * Constructs a <code>NegativeTimestampException</code> with a specified meesage.
     * @param message A <code>String</code> of the customised exception message.
     */
    public NegativeTimestampException(String message) {
        super(message);
    }
}