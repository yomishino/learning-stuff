import java.util.HashSet;

/**
 * A simple class of users for storing user data. 
 * @author yomishino
 */
public class User {
    private String username;
    private HashSet<User> friends;

    /**
     * Constructs a new user object.
     * @param username The username of the user; 
     * should not contain whitespaces.
     * @throw IllegalArgumentException If the username contains whitespaces.
     */
    public User(String username) {
        this.username = username;
        this.friends = new HashSet<>();
    }

    /**
     * Adds a friend for this user.
     * @param u The User object representing the friend.
     * @return true if the friend is added; 
     * false if the friend already exists and cannot be added.
     * @throws IllegalArgumentException If <code>u</code> is null.
     */
    public boolean addFriend(User u) {
        if (u == null)
            throw new IllegalArgumentException("``null`` given as a User.");
        return friends.add(u);
    }

    /**
     * Returns a set of the usernames of this user's friends.
     * @return A set of usernames of friends.
     */
    public HashSet<String> friends() {
        HashSet<String> names = new HashSet<>();
        for (User f : friends)
            names.add(f.username);
        return names;
    }

    /**
     * Returns a string representation of this object.
     * @return A string representation.
     */
    @Override
    public String toString() {
        String s = username + ":  ";
        for (User f : friends)
            s += f.username + ",  ";
        return s;
    }

    /**
     * Calculates and returns a hash code for this object.
     * @return A hash code.
     */
    @Override
    public int hashCode() {
        return toString().hashCode();
    }

    /**
     * Checks if the given object is equal to this object.
     * @param obj The other object to be checked.
     * @return true if the two objects are equal, false otherwise.
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == null)
            return false;
        else if (getClass() != obj.getClass())
            return false;
        else {
            User other = (User) obj;
            return toString().equals(other.toString());
        }
    }
}