import java.util.ArrayList;
import java.util.LinkedList;
import java.util.NoSuchElementException;

/**
 * A class of graphs, each representing the structure of a maze.
 * A graph consist of nodes representing rooms in the maze
 * and edges between nodes representing direct passages from
 * one node to another.
 * @author yomishino
 */
public class MazeGraph {
    
    /**
     * A class of nodes for the {@link MazeGraph},
     * representing a room in the maze.
     * Each node can links to at most four other nodes,
     * representing the rooms to the four directions of 
     * the current room that have direct passages to this room.
     */
    private class MazeNode {
        /** The label of the room. */
        private String label;
        /** The node linked to this node in the north. */
        private MazeNode north;
        /** The node linked to this node in the south. */
        private MazeNode south;
        /** The node linked to this node in the east. */
        private MazeNode east;
        /** The node linked to this node in the west. */
        private MazeNode west;

        /**
         * Constructs a node with a given label, 
         * but is initially isolated from other nodes.
         * @param label The label of the room; cannot be empty.
         * @throws IllegalArgumentException If the label is the
         * empty string.
         */
        private MazeNode(String label) {
            if (label.length() == 0)
                throw new IllegalArgumentException("Label is empty.");
            this.label = label;
        }

        /**
         * Constructs a node for the {@link MazeGraph} object.
         * @param label The label of the room.
         * @param north The node linked to this node in the north.
         * @param south The node linked to this node in the south.
         * @param east The node linked to this node in the east.
         * @param west The node linked to this node in the west.
         * @throws IllegalArgumentException If the label is the
         * empty string.
         */
        private MazeNode(String label, MazeNode north, MazeNode south, 
                                    MazeNode east, MazeNode west) {
            if (label.length() == 0)
                throw new IllegalArgumentException("Label is empty.");
            this.label = label;
            this.north = north;
            this.south = south;
            this.east = east;
            this.west = west;
        }

        // /**
        //  * Returns a human-readable string representation of this node,
        //  * showing its label and the the rooms it links to.
        //  * @return A string representation of this node.
        //  */
        // @Override
        // @Deprecated
        // public String toString() {
        //     String s = label + ": {";
        //     if (north != null)
        //         s += " N: " + north.label + ",";
        //     if (south != null)
        //         s += " S: " + south.label + ",";
        //     if (east != null)
        //         s += " E: " + east.label + ",";
        //     if (west != null)
        //         s += " W: " + east.label + " }";
        //     return s;
        // }
    }    // End of MazeNode class


    /** The start room, where a player starts in the maze. */
    private MazeNode start;
    /** The room with the exit of the maze: the goal room. */
    private MazeNode goal;
    /** All room nodes in the maze. */
    private ArrayList<MazeNode> rooms;



    /** Constructs a graph representing a maze, which is initially empty. */
    public MazeGraph() {
        start = null;
        goal = null;
        rooms = new ArrayList<>();
    }

    /**
     * Constructs a graph representing a maze, with the initial size set.
     * <p>
     * The choice of an initial size does not put a restriction on
     * the actual maze size, and the actual size can be expanded any time
     * if needed, although the choice of size may have an impact on 
     * efficency.
     * @param size The (initial) number of rooms in the maze.
     */
    public MazeGraph(int size){
        this(size, null, null);
    }

    /**
     * Constructs a graph representing a maze, with the initial size and
     * the start and the goal room set.
     * <p>
     * The choice of an initial size does not put a restriction on
     * the actual maze size, and the actual size can be expanded any time
     * if needed, although the choice of size may have an impact on 
     * efficency.
     * @param size The (initial) number of rooms in the maze.
     * @param start The start room.
     * @param goal The goal room.
     */
    public MazeGraph(int size, MazeNode start, MazeNode goal) {
        rooms = new ArrayList<>(size);
        this.start = start;
        this.goal = goal;
    }

    /**
     * Sets the start room to be the room with the given label.
     * If the maze has a room with this label, this room will be
     * set as the start room; otherwise a new room node will be
     * created and set as the start room.
     * @param label The label of the room.
     * @throws IllegalArgumentException If the given label is
     * the empty string.
     */
    public void setStart(String label) {
        if (label.length() == 0)
            throw new IllegalArgumentException("Label is empty");
        start = getNode(label);
        if (start == null)
            start = new MazeNode(label);
    }

    /**
     * Returns the label of the start room.
     * @return The label of the start room, 
     * {@literal <null>} if the start room is not set.
     */
    public String startRoom() {
        if (start == null)
            return "<null>";
        else
            return start.label;
    }

    /**
     * Sets the goal room to be the room with the given label.
     * If the maze has a room with this label, this room will be
     * set as the goal room; otherwise a new room node will be
     * created and set as the goal room.
     * @param label The label of the room.
     * @throws IllegalArgumentException If the given label is
     * the empty string.
     */
    public void setGoal(String label) {
        if (label.length() == 0)
            throw new IllegalArgumentException("Label is empty.");
        goal = getNode(label);
        if (goal == null)
            goal = new MazeNode(label);
    }

    /**
     * Returns the label of the goal room.
     * @return The label of the goal room, 
     * {@literal <null>} if the goal room is not set.
     */
    public String goalRoom() {
        if (goal == null)
            return "<null>";
        else
            return goal.label;
    }

    /**
     * Adds a new room with the given label into the maze.
     * A new room with an empty label is not permitted.
     * @param label The label of the new room; cannot be
     * the empty string
     * @return true if the room is added, false if the room cannot
     * be added, or there is already a room with the same label.
     * @throws IllegalArgumentException If <code>label</code> is
     * the empty string.
     */
    public boolean addRoom(String label) {
        if (label.length() == 0)
            throw new IllegalArgumentException("Label is empty.");
        if (contains(label))
            return false;
        return rooms.add(new MazeNode(label));        
    }

    /**
     * Adds a new room with the given label into the maze,
     * and set the corresponding links to the rooms with 
     * the specified labels.
     * A new room with an empty label is not permitted.
     * <p>
     * However, if any of the labels of other four rooms is left 
     * empty, this will be taken as that the new room does not
     * link to another room in this direction.
     * @param label The label of the new room; cannot be the 
     * empty string.
     * @param north The label of the room linked to the north;
     * the empty string if no room to the north.
     * @param south The label of the room linked to the south;
     * the empty string if no room to the south.
     * @param east The label of the room linked to the east;
     * the empty string if no room to the east.
     * @param west The label of the room linked to the west;
     * the empty string if no room to the west.
     * @return true if the room is added, false if the room cannot
     * be added, the given label is empty,
     * or there is already a room with the same label.
     * @throws IllegalArgumentException If <code>label</code> is
     * the empty string.
     */
    public boolean addRoom(String label, String north, String south,
                                String east, String west) {
        if (label.length() == 0)
            throw new IllegalArgumentException("Label is empty.");
        if (contains(label))
            return false;
        Node<T> newRoom = new MazeNode(label);
        if (rooms.add(newRoom)) {
            setNorthFor(label, north);
            setSouthFor(label, south);
            setEastFor(label, east);
            setWestFor(label, west);
            return true;
        }
        return false;
    }

    /**
     * Removes the room with the specified label from the maze.
     * Any room that previously linked to the removed room will have
     * the corresponding link set to null automatically.
     * <p>
     * Since a room with the empty string as its label is not permitted,
     * if the empty string is passed as the argument,
     * this method will simply return false.
     * @param label The label of the room to be removed.
     * @return true if the room is removed, 
     * false if the given label is empty or the a room with the given
     * label cannot be found.
     */
    public boolean removeRoom(String label) {
        if (label.length() == 0)
            return false;
        MazeNode toRm = getNode(label);
        if (toRm == null)
            return false;
        if (toRm.north != null)
            toRm.north.south = null;
        if (toRm.south != null)
            toRm.south.north = null;
        if (toRm.east != null)
            toRm.east.west = null;
        if (toRm.west != null)
            toRm.west.east = null;
        return rooms.remove(toRm);
    }

    /**
     * Sets the room whose label is given in <code>thisRoom</code> 
     * to link to the room whose label is given in <code>north</code>
     * in the north direction. 
     * If the north room does not exist, a new room node with the given 
     * label will be created.
     * This method will automatically set the north room to link to
     * <code>thisRoom</code> in the south.
     * An empty string of <code>north</code> indicates that the room
     * will not link to any room in the north direction.
     * @param thisRoom The label of the room whose north link is to be set.
     * @param north The label of the room which will be set to the north;
     * the empty string if no room to link in this direction.
     * @throws NoSuchElementException If there is no room in the maze
     * that has a label as given in <code>thisRoom</code>.
     * @throws IllegalArgumentException If <code>thisRoom</code> is 
     * the empty string.
     */
    public void setNorthFor(String thisRoom, String north) {
        if (thisRoom.length() == 0)
            throw new IllegalArgumentException("Label is empty.");
        MazeNode current = getNode(thisRoom);
        if (current == null)
            throw new NoSuchElementException("No such a room: " + thisRoom);
        if (north.length() == 0)
            current.north = null;
        else {
            MazeNode other = getNode(north);
            if (other == null)
                other = new MazeNode(north);
            current.north = other;
            other.south = current;
        }
    }

    /**
     * Sets the room whose label is given in <code>thisRoom</code> 
     * to link to the room whose label is given in <code>south</code>
     * in the south direction. 
     * If the south room does not exist, a new room node with the given 
     * label will be created.
     * This method will automatically set the south room to link to
     * <code>thisRoom</code> in the north.
     * An empty string of <code>south</code> indicates that the room
     * will not link to any room in the south direction.
     * @param thisRoom The label of the room whose south link is to be set.
     * @param south The label of the room which will be set to the south;
     * the empty string if no room to link in this direction.
     * @throws NoSuchElementException If there is no room in the maze
     * that has a label as given in <code>thisRoom</code>.
     * @throws IllegalArgumentException If <code>thisRoom</code> is 
     * the empty string.
     */
    public void setSouthFor(String thisRoom, String south) {
        if (thisRoom.length() == 0)
            throw new IllegalArgumentException("Label is empty.");
        MazeNode current = getNode(thisRoom);
        if (current == null)
            throw new NoSuchElementException("No such a room: " + thisRoom);
        if (south.length() == 0)
            current.south = null;
        else {
            MazeNode other = getNode(south);
            if (other == null)
                other = new MazeNode(south);
            current.south = other;
            other.north = current;
        }
    }

    /**
     * Sets the room whose label is given in <code>thisRoom</code> 
     * to link to the room whose label is given in <code>east</code>
     * in the east direction. 
     * If the east room does not exist, a new room node with the given 
     * label will be created.
     * This method will automatically set the east room to link to
     * <code>thisRoom</code> in the west.
     * An empty string of <code>east</code> indicates that the room
     * will not link to any room in the east direction.
     * @param thisRoom The label of the room whose east link is to be set.
     * @param east The label of the room which will be set to the east;
     * the empty string if no room to link in this direction.
     * @throws NoSuchElementException If there is no room in the maze
     * that has a label as given in <code>thisRoom</code>.
     * @throws IllegalArgumentException If <code>thisRoom</code> is 
     * the empty string.
     */
    public void setEastFor(String thisRoom, String east) {
        if (thisRoom.length() == 0)
            throw new IllegalArgumentException("Label is empty.");
        MazeNode current = getNode(thisRoom);
        if (current == null)
            throw new NoSuchElementException("No such a room: " + thisRoom);
        if (east.length() == 0)
            current.east = null;
        else {
            MazeNode other = getNode(east);
            if (other == null)
                other = new MazeNode(east);
            current.east = other;
            other.west = current;
        }
    }

    /**
     * Sets the room whose label is given in <code>thisRoom</code> 
     * to link to the room whose label is given in <code>west</code>
     * in the west direction. 
     * If the west room does not exist, a new room node with the given 
     * label will be created.
     * This method will automatically set the west room to link to
     * <code>thisRoom</code> in the east.
     * An empty string of <code>east</code> indicates that the room
     * will not link to any room in the west direction.
     * @param thisRoom The label of the room whose west link is to be set.
     * @param west The label of the room which will be set to the west;
     * the empty string if no room to link in this direction.
     * @throws NoSuchElementException If there is no room in the maze
     * that has a label as given in <code>thisRoom</code>.
     * @throws IllegalArgumentException If <code>thisRoom</code> is 
     * the empty string.
     */
    public void setWestFor(String thisRoom, String west) {
        if (thisRoom.length() == 0)
            throw new IllegalArgumentException("Label is empty.");
        MazeNode current = getNode(thisRoom);
        if (current == null)
            throw new NoSuchElementException("No such a room: " + thisRoom);
        if (west.length() == 0)
            current.west = null;
        else {
            MazeNode other = getNode(west);
            if (other == null)
                other = new MazeNode(west);
            current.west = other;
            other.east = current;
        }
    }

    /**
     * Resets the north link of the room with the given label,
     * making it links to no room in the north direction.
     * <p>
     * If there was previously a room linking to this room in the north, 
     * the south link of that room will also be reset.
     * @param label The label of the room.
     * @throws NoSuchElementException If there is no room in the maze
     * that has the given label.
     * @throws IllegalArgumentException If <code>label</code> is the
     * empty string.
     */
    public void resetNorthFor(String label) {
        setNorthFor(label, "");
    }

    /**
     * Resets the south link of the room with the given label,
     * making it links to no room in the south direction.
     * <p>
     * If there was previously a room linking to this room in the south,
     * the north link of that room will also be reset.
     * @param label The label of the room.
     * @throws NoSuchElementException If there is no room in the maze
     * that has the given label.
     * @throws IllegalArgumentException If <code>label</code> is the
     * empty string.
     */
    public void resetSouthFor(String label) {
        setSouthFor(label, "");
    }

    /**
     * Resets the east link of the room with the given label,
     * making it links to no room in the east direction.
     * <p>
     * If there was previously a room linking to this room in the east,
     * the west link of that room will also be reset.
     * @param label The label of the room.
     * @throws NoSuchElementException If there is no room in the maze
     * that has the given label.
     * @throws IllegalArgumentException If <code>label</code> is the
     * empty string.
     */
    public void resetEastFor(String label) {
        setEastFor(label, "");
    }

    /**
     * Resets the west link of the room with the given label,
     * making it links to no room in the west direction.
     * <p>
     * If there was previously a room linking to this room in the west,
     * the east link of that room will also be reset.
     * @param label The label of the room.
     * @throws NoSuchElementException If there is no room in the maze
     * that has the given label.
     * @throws IllegalArgumentException If <code>label</code> is the
     * empty string.
     */
    public void resetWestFor(String label) {
        setWestFor(label, "");
    }

    /**
     * Resets all links of the room with the given label,
     * making it isolated from any other node in the maze.
     * <p>
     * If there was previously a room linking to this room,
     * the link of that room in the corresponding direction will 
     * also be reset.
     * @param label The label of the room.
     * @throws NoSuchElementException If there is no room in the maze
     * that has the given label.
     * @throws IllegalArgumentException If <code>label</code> is the
     * empty string.
     */
    public void resetLinksFor(String label) {
        resetNorthFor(label);
        resetSouthFor(label);
        resetEastFor(label);
        resetWestFor(label);
    }

    /**
     * Checks if the maze graph contains a node with the given label.
     * @param label The label of the room node.
     * @return true if the graph contains the node, false otherwise.
     */
    public boolean contains(String label) {
        if (label.length() == 0)
            return false;
        for (MazeNode r : rooms) {
            if (r.label.equals(label))
                return true;
        }
        return false;
    }

    /**
     * Removes all room nodes from the maze.
     */
    public void clear() {
        rooms.clear();
    }

    /**
     * Resets the links for all room nodes, making them
     * isolated from each other.
     */
    public void resetAllLinks() {
        for (MazeNode r : rooms) {
            r.north = null;
            r.south = null;
            r.east = null;
            r.west = null;
        }
    }

    /**
     * TODO: implements the ? algo to find a path out of the maze.
     * @return A string representation of path found; the empty string
     * if there is no path out of the maze.
     */
    public String shortestPath() {
        // TODO
        return "";
    }

    /**
     * TODO: Check if the current maze is solvable, via shortestPath().
     */
    public boolean isSolvable() {
        // TODO
        return true;
    }

    /**
     * TODO: Implements a randomwalk strategy.
     * The length will only be up to a certain number,
     * if the maze is unsolvable.
     * @param length The length of the walk.
     * @return A string representation of the path walked.
     */
    public String randomWalk(int length) {
        // TODO
        return "";
    }

    /**
     * Gets the node with the given label from the ArrayList of nodes.
     * @param label The label of the room
     * @return The reference to the node; <code>null</code> if there
     * is no room node with the given label.
     */
    private MazeNode getNode(String label) {
        if (label.length() == 0)
            return null;
        for (MazeNode r : rooms) {
            if (r.label.equals(label))
                return r;
        }
        return null;
    }

}