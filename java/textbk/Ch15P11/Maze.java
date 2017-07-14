import java.util.Scanner;

/**
 * A class of simple command-line maze-exploration games.
 * Each object uses one maze represented by a {@link MazeGraph} object
 * for players to explore; the maze it uses can be changed if needed.
 * The goal for a player is to find the exit in the maze.
 * @author yomishino
 */
public class Maze {
    enum Directions {NORTH, SOUTH, EAST, WEST};

    /** The graph of maze. */
    private MazeGraph maze;
    /** Whether the player has reached the goal room. */
    private boolean finished;
    /** The current position of the player. */
    private String currentRoom;

    /**
     * Constructs a <code>Maze</code> object with an initially empty 
     * maze graph.
     */
    public Maze() {
        maze = new MazeGraph();
        reset();
    }

    /**
     * Constructs a <code>Maze</code> object with an initially empty
     * maze graph, and the intended size of the maze.
     * <p>
     * It is preferable, but not required, that the actual size of 
     * the maze would be of the indicated size.
     * @param size The intended size of the maze.
     */
    public Maze(int size) {
        maze = new MazeGraph(size);
        reset(size);
    }

    /**
     * Constructs a <code>Maze</code> object with the given maze graph.
     * @param maze A graph of maze.
     * @throws IllgealArgumentException if <code>null</code> is passed as
     * the argument.
     */
    public Maze(MazeGraph maze) {
        this();
        useMaze(maze);
    }

    /**
     * Resets the maze graph and clears all relevant gameplay status.
     */
    public void reset() {
        maze.clear();
        finished = false;
        currentRoom = "";
    }

    /**
     * Resets the maze graph, sets a new intended size, 
     * and clears all relevant gameplay status.
     * @param newSize the intended new size of the maze.
     */
    public void reset(int newSize) {
        maze.reset(newSize);
        finished = false;
        currentRoom = "";
    }

    /**
     * Use the given maze graph for this object.
     * This method will also reset all relevant gameplay status.
     * @param maze A graph of maze.
     * @throws IllgealArgumentException if <code>null</code> is passed as
     * the argument.
     */
    public void useMaze(MazeGraph maze) {
        if (maze == null)
            throw new IllegalArgumentException("Null object as a maze graph.");
        reset();
        this.maze = maze;
        currentRoom = maze.startRoom();
    }

    /**
     * Starts the maze exploring by placing the player in the start room
     * of the maze.
     * @throws IllegalStateException If the maze is not ready, that is,
     * the maze graph contains no nodes or the start/goal room of 
     * the maze is not set.
     */
    public void start() {
        if (!maze.isReady())
            throw new IllegalStateException("The maze is not ready.");
        currentRoom = maze.startRoom();
    }

    /**
     * Explores the next room in the given direction, by
     * moving the current position to that room.
     * @param direction A string of direction that can be either
     * the full spelling (such as "east") or the abbreviation
     * (such as "E"); cases are ignored.
     * @throws UnreachableDirectionException If there is no room
     * linking to the current room in that direction.
     * @throws IllegalArgumentException If the given string is
     * not a recognisable direction.
     */
    public void explore(String direction) {
        Directions d = translateDirections(direction);
        switch (d) {
            case NORTH:
                if (!maze.canGoNorth(currentRoom))
                    throw new UnreachableDirectionException();
                currentRoom = maze.northRoom(currentRoom);
                break;
            case SOUTH:
                if (!maze.canGoSouth(currentRoom))
                    throw new UnreachableDirectionException();
                currentRoom = maze.southRoom(currentRoom);
                break;
            case EAST:
                if (!maze.canGoEast(currentRoom))
                    throw new UnreachableDirectionException();
                currentRoom = maze.eastRoom(currentRoom);
                break;
            case WEST:
                if (!maze.canGoWest(currentRoom))
                    throw new UnreachableDirectionException();
                currentRoom = maze.westRoom(currentRoom);
                break;
            default:
                throw new UnknownError();   // Should not be reachable
        }
        if (currentRoom.equals(maze.goalRoom()))
            finished = true;
    }

    /**
     * Checks if the player has reached the goal room.
     * @return true if reached, false otherwise.
     */
    public boolean hasReachedGoal() {
        return finished;
    }

    /**
     * Prints the player's current position and the possible moves
     * on the screen.
     */
    public void printCurrentState() {
        System.out.print("\nYou are in room ");
        System.out.print(currentRoom);
        System.out.print(" of a maze of twisty little passages, all alike. ");
        System.out.print("You can go ");
        String[] moves = possibleMoves();
        for (int i = 0; i < moves.length - 1; i++) {
            System.out.print(moves[i] + ", ");
        }
        System.out.print(moves.length > 1 ? "or " : "");
        System.out.println(moves[moves.length - 1] + ".");
    }

    /**
     * Prints a congratulation on the player's reaching the goal room.
     */
    public void printReached() {
        System.out.print("\nYou are in room ");
        System.out.print(currentRoom);
        System.out.println(" of a maze of twisty little passages.");
        System.out.println("However, the exit of the maze is now "
                            + "right before you.");
        System.out.println("\nYou have reached the exit of the maze. "
                            + "Congrats!\n");
    }

    /**
     * Returns a human-readable string representation of this maze.
     * The string contains the string representation of the maze and 
     * the current position of the player.
     * <p>
     * The string representation will reveal the whole structure of
     * the maze.
     * It is thus recommended not to use it in the normal gameplay 
     * process but in the maze-building process for debugging.
     * @return A string representation of this maze.
     */
    public String toString() {
        String s = maze.toString();
        s += "\nCurrent position: " 
            + (currentRoom.isEmpty() ? "<null>" : currentRoom);
        return s;
    }

    /**
     * Uses the demo maze graph for this object, 
     * and resets all relevant game status.
     */
    void useDemoMaze() {
        useMaze(demoMaze());
    }

    /**
     * Gets all the possible moves from the current position in
     * as strings.
     * Each move is represented by a string of the corresponding direction,
     * such as "east (E)".
     * @return An array of strings of the possible moves.
     */
    private String[] possibleMoves() {
        String[] moves = new String[maze.countLinks(currentRoom)];
        int i = 0;
        if (maze.canGoNorth(currentRoom)) {
            moves[i] = "north (N)";
            i++;
        }
        if (maze.canGoSouth(currentRoom)) {
            moves[i] = "south (S)";
            i++;
        }
        if (maze.canGoEast(currentRoom)) {
            moves[i] = "east (E)";
            i++;
        }
        if (maze.canGoWest(currentRoom)) {
            moves[i] = "west (W)";
        }
        return moves;
    }

    /**
     * Converts a string representation of a direction
     * into a {@link Directions} type constant.
     * <p>
     * Acceptable expressions of a direction are either the
     * full spellings of the directions such as "east", or
     * their abbreviations such as "E". Cases are ignored.
     * @return The {@link Directions} type representation of
     * the direction.
     * @throws IllegalArgumentException if the given string
     * is not a recognisable direction expression.
     */
    private Directions translateDirections(String direction) {
        if (direction.equalsIgnoreCase("north") 
                || direction.equalsIgnoreCase("n"))
            return Directions.NORTH;
        else if (direction.equalsIgnoreCase("south")
                || direction.equalsIgnoreCase("s"))
            return Directions.SOUTH;
        else if (direction.equalsIgnoreCase("east")
                || direction.equalsIgnoreCase("e"))
            return Directions.EAST;
        else if (direction.equalsIgnoreCase("west")
                || direction.equalsIgnoreCase("w"))
            return Directions.WEST;
        else
            throw new IllegalArgumentException("Not a valid direction.");
    }

    /** 
     * Builds the demo maze.
     * @return A new copy of the graph of the demo maze.
     */
    private MazeGraph demoMaze() {
        MazeGraph demo = new MazeGraph(12);
        demo.addRoom("A");
        demo.addRoom("B");
        demo.addRoom("C");
        demo.addRoom("D");
        demo.addRoom("E");
        demo.addRoom("F");
        demo.addRoom("G");
        demo.addRoom("H");
        demo.addRoom("I");
        demo.addRoom("J");
        demo.addRoom("K");
        demo.addRoom("L");

        demo.setStart("A");
        demo.setGoal("L");

        demo.setEastFor("A", "B");
        demo.setSouthFor("A", "E");
        demo.setSouthFor("B", "F");
        demo.setSouthFor("E", "I");
        demo.setEastFor("I", "J");
        demo.setEastFor("F", "G");
        demo.setNorthFor("G", "C");
        demo.setSouthFor("G", "K");
        demo.setEastFor("C", "D");
        demo.setEastFor("G", "H");
        demo.setSouthFor("H", "L");

        return demo;
    }


    public static void main(String[] args) {
        Maze m = new Maze();
        m.useDemoMaze();
        m.start();

        Scanner sc = new Scanner(System.in);
        String input;
        boolean inputValid = false;
        while (!m.hasReachedGoal()) {
            do {
                m.printCurrentState();
                try {
                    input = sc.nextLine();
                    m.explore(input);
                    inputValid = true;
                } 
                catch(UnreachableDirectionException e) {
                    System.out.println("You cannot go there.");
                }
                catch(IllegalArgumentException e) {
                    System.out.println("Please enter a direction.");
                }
            } while (!inputValid);
        }
        m.printReached();
        sc.close();
    }
}