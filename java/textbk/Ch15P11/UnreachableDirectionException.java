/**
 * A runtime exception class to be thrown when an unreachable 
 * direction is given.
 * <p>
 * A direction is unreachable if the room the player is currently in
 * links to no room in that direction.
 * @author yomishino
 */
@SuppressWarnings("serial")
class UnreachableDirectionException extends RuntimeException {
    /**
     * Constructs a default <code>UnreachableDirectionException</code>
     * with no detail messages.
     */
    UnreachableDirectionException() {
        super();
    }

    /**
     * Constructs a <code>UnreachableDirectionException</code>
     * with the specified detail message.
     * @param message The detail message.
     */
    UnreachableDirectionException(String message) {
        super(message);
    }

    /**
     * Constructs a <code>UnreachableDirectionException</code> 
     * with the specified cause and a detail message from the cause.
     * @param cause The cause of the exception; 
     * can be <code>null</code> if unknown or nonexistent.
     */
    UnreachableDirectionException(Throwable cause) {
        super(cause);
    }

    /**
     * Constructs a <code>UnreachableDirectionException</code>
     * with the specified detail message and the cause.
     * @param message The detail message.
     * @param cause The cause of the exception;
     * can be <code>null</code> if unknown or nonexistent.
     */
    UnreachableDirectionException(String message, Throwable cause) {
        super(message, cause);
    }
}