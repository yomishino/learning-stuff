/**
 * A checked exception thrown when the input text file of 
 * friendship list is not in the required format.
 * @author yomishino
 */
public class InvalidFriendListFormatException extends Exception {
    private static final long serialVersionUID = 1L;

    /**
     * Constructs an <code>InvalidFriendListFormatException</code>
     * with no detail messages.
     */
    public InvalidFriendListFormatException() {
        super();
    }

    /**
     * Constructs an <code>InvalidFriendListFormatException</code>
     * with the given message.
     * @param message The detail message.
     */
    public InvalidFriendListFormatException(String message) {
        super(message);
    }

    /**
     * Constructs an <code>InvalidFriendListFormatException</code>
     * with the specified cause and the detail message from the cause.
     * @param cause The cause of the exception.
     */
    public InvalidFriendListFormatException(Throwable cause) {
        super(cause);
    }

    /**
     * Constructs an <code>InvalidFriendListFormatException</code>
     * with the specified cause and the given message.
     * @param message The detail message.
     * @param cause The cause of the exception.
     */
    public InvalidFriendListFormatException(String message, Throwable cause) {
        super(message, cause);
    }
}