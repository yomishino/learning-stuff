import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.*;

/**
 * A class for storing and analysing a social network.
 * @author yomishino
 */
public class SocialNetwork {
    /** Mapping username to the user object. */
    private HashMap<String, User> users;
    /** The tag of comment lines in the text file. */
    private static final String COMMENT_TAG = "#";
    /** The default path to the text file of friendship list. */
    private static final String FILEPATH = "friendship.txt";

    /**
     * Constructs an empty social network.
     */
    public SocialNetwork() {
        users = new HashMap<>();
    }

    /**
     * Constrcuts a social network,
     * with the data from the given file.
     * @param path The path to the text file of friendship list.
     * @throws InvalidFriendListFormatException If the file contains
     * lines with an invalid format.
     * @throws FileNotFoundException If the file cannot be found.
     */
    public SocialNetwork(String path) 
            throws InvalidFriendListFormatException, FileNotFoundException {
        this();
        readFromFile(path);
    }

    /**
     * Adds a new user to this social network.
     * @param username The username of the new user; should not be empty.
     * @throws IllegalArgumentException If the given username is empty.
     */
    public void addUser(String username) {
        if (username.isEmpty())
            throw new IllegalArgumentException("Empty username.");
        User u = new User(username);
        users.put(username, u);
    }

    /**
     * Adds a friend for a user.
     * @param thisUser The username of the user.
     * @param friend The username of the user's friend.
     * @return true if the friend is added;
     * false if the friend has already been added before this call.
     * @throws NoSuchElementException If the user or the friend
     * has not been created in this social network yet.
     */
    public boolean addFriend(String thisUser, String friend) {
        User user = users.get(thisUser);
        if (user == null)
            throw new NoSuchElementException("The user '" + thisUser
                                                + "' does not exist.");
        User fr = users.get(friend);
        if (fr == null)
            throw new NoSuchElementException("The user '" + friend
                                                + "' does not exist.");
        return user.addFriend(fr);
    }

    /**
     * Checks if the social network has a valid user with this username.
     * @param username The username to be checked.
     * @return true if the user exists, false otherwise.
     */
    public boolean contains(String username) {
        return users.get(username) != null;
    }

    /**
     * Finds all the users that are <code>k</code> links away 
     * from the specified user.
     * @param user The username of the source user.
     * @param k The distance: how many links away.
     * @return A set of the usernames that are <code>k</code> 
     * links away.
     * @throws NoSuchElementException If the user does not exist.
     * @throws IllegalArgumentException If the given distance
     * <code>k</code> is less than 1.
     */
    public HashSet<String> kLinksAway(String user, int k) {
        if (k < 1)
            throw new IllegalArgumentException("Invalid distance.");
        User u = users.get(user);
        if (u == null)
            throw new NoSuchElementException("The user '" + user
                                            + "' does not exist.");
        if (k == 1)
            return u.friends();
        else {
            HashSet<String> friends = new HashSet<>();
            for (String f : u.friends()) 
                friends.addAll(kLinksAway(f, k - 1));
            return friends;
        }
    }

    /**
     * Reads a friendship list from the given text file.
     * @param path The path to the text file.
     * @throws InvalidFriendListFormatException If the text file contains
     * lines in an invalid format.
     * @throws FileNotFoundException If the file cannot be found.
     */
    public void readFromFile(String path) 
            throws InvalidFriendListFormatException, FileNotFoundException {
        Scanner filesc = new Scanner(new FileInputStream(path));
        StringTokenizer tokenizer;
        String line, user, friend;
        while (filesc.hasNextLine()) {
            line = filesc.nextLine().trim();
            if (line.startsWith(COMMENT_TAG) || line.isEmpty())
                continue;
            tokenizer = new StringTokenizer(line);
            if (tokenizer.countTokens() != 2) {
                filesc.close();
                throw new InvalidFriendListFormatException(line);
            }
            user = tokenizer.nextToken();
            friend = tokenizer.nextToken();
            if (!users.containsKey(user))
                addUser(user);
            if (!users.containsKey(friend))
                addUser(friend);
            addFriend(user, friend);
        }
        filesc.close();
    }

    /**
     * Prompts the user to enter a username and number of links away
     * from that user for analysis, and displays the results.
     * @param sc The {@link Scanner} object for handling user input.
     */
    public void promptAnalysis(Scanner sc) {
        sc.reset();
        System.out.println("\nPlease enter a username for analysis:");
        String username = sc.next();
        if (!contains(username))
            System.out.println("The user '" + username + "' does not exist.");
        else {
            int k = 0;
            HashSet<String> friends = new HashSet<>();
            boolean notValid = true;
            while (notValid) {
                try {
                    sc.nextLine();  // clear buffer
                    System.out.println("\nHow many links away the users"
                                        + " are from this user are you "
                                        + "interested in: ");
                    k = sc.nextInt();
                    friends = kLinksAway(username, k);
                    notValid = false;
                }
                catch (InputMismatchException | IllegalArgumentException e) {
                    System.err.println("Please enter a positive integer.");
                }
            }
            System.out.println("\nAll users that are " + k + " links "
                                + "away from the user '" + username + "': ");
            for (String f : friends)
                System.out.print(f + ",  ");
            System.out.println("\nDone.\n");
        }

    }

    /**
     * Returns a string representation for this social network.
     * @return A string representation.
     */
    @Override
    public String toString() {
        String s = "";
        for (User u : users.values())
            s += u.toString() + "\n";
        return s;
    }

    private static void demo(String path) {
        SocialNetwork sn = null;
        Scanner sc = new Scanner(System.in);
        try {
            sn = new SocialNetwork(path);
            while (true) {
                sn.promptAnalysis(sc);
            }
        }
        catch (FileNotFoundException e) {
            System.err.println("Cannot find " + path +".");
        }
        catch (InvalidFriendListFormatException e) {
            System.err.println("The file " + path + " has invalid format.");
        }
        finally {
            sc.close();
        }
    }

    public static void main(String[] args) {
        demo(SocialNetwork.FILEPATH);
    }
}