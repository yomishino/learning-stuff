import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Random;

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

        /**
         * Checks if the given object is equal to this node.
         * @param other An object to be checked.
         * @return true if they are equal; false otherwise.
         */
        @Override
        public boolean equals(Object other) {
            if (other == null)
                return false;
            else if (getClass() != other.getClass())
                return false;
            else {
                MazeNode node = (MazeNode) other;
                if (!label.equals(node.label))
                    return false;
                return north == node.north && south == node.south
                        && east == node.east && west == node.west;
                    // references to same node or not
            }
        }

        /**
         * Returns a human-readable string representation of
         * this node.
         * @return The string representation of this node.
         */
        @Override
        public String toString() {
            String s = label + ": {  ";
            s += north == null ? "" : ("[N] " + north.label + "  ");
            s += south == null ? "" : ("[S] " + south.label + "  ");
            s += east == null ? "" : ("[E] " + east.label + "  ");
            s += west == null ? "" : ("[W] " + west.label + "  ");
            s += "}";
            return s;
        }
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
     * The choice of an initial size is only an indication of how
     * large the maze would probably be after adding all room nodes.
     * There is no need for the actual maze size to be the same as
     * the specified initial size, although this is preferable.
     * @param size The (initial) number of rooms in the maze.
     */
    public MazeGraph(int size) {
        start = null;
        goal = null;
        rooms = new ArrayList<>(size);
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
        MazeNode newRoom = new MazeNode(label);
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
     * If the room to be removed is the start or the goal room of
     * the maze, then the start or goal room will also be reset.
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
        if (start == toRm)
            start = null;
        if (goal == toRm)
            goal = null;
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
     */
    public void setNorthFor(String thisRoom, String north) {
        MazeNode current = getNode(thisRoom);
        if (current == null)
            throw new NoSuchElementException("No such a room: " + thisRoom);
        if (north.length() == 0) {
            if (current.north != null)
                current.north.south = null;
            current.north = null;
        }
        else {
            addRoom(north);      // will add only if not exist
            MazeNode other = getNode(north);
            removeLinkBetween(thisRoom, north);
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
     */
    public void setSouthFor(String thisRoom, String south) {
        MazeNode current = getNode(thisRoom);
        if (current == null)
            throw new NoSuchElementException("No such a room: " + thisRoom);
        if (south.length() == 0) {
            if (current.south != null)
                current.south.north = null;
            current.south = null;
        }
        else {
            addRoom(south);      // will add only if not exist
            MazeNode other = getNode(south);
            removeLinkBetween(thisRoom, south);
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
     */
    public void setEastFor(String thisRoom, String east) {
        MazeNode current = getNode(thisRoom);
        if (current == null)
            throw new NoSuchElementException("No such a room: " + thisRoom);
        if (east.length() == 0) {
            if (current.east != null)
                current.east.west = null;
            current.east = null;
        }
        else {
            addRoom(east);      // will add only if not exist
            MazeNode other = getNode(east);
            removeLinkBetween(thisRoom, east);
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
     */
    public void setWestFor(String thisRoom, String west) {
        MazeNode current = getNode(thisRoom);
        if (current == null)
            throw new NoSuchElementException("No such a room: " + thisRoom);
        if (west.length() == 0) {
            if (current.west != null)
                current.west.east = null;
            current.west = null;
        }
        else {
            addRoom(west);      // will add only if not exist
            MazeNode other = getNode(west);
            removeLinkBetween(thisRoom, west);
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
     */
    public void resetLinksFor(String label) {
        resetNorthFor(label);
        resetSouthFor(label);
        resetEastFor(label);
        resetWestFor(label);
    }

    /**
     * Removes the link between the two rooms with the given label.
     * @param room1 The label for the first room.
     * @param room2 The label for the second room.
     * @throws NoSuchElementException if no room in the maze that
     * has either of the given label.
     */
    public void removeLinkBetween(String room1, String room2) {
        MazeNode n1 = getNode(room1);
        MazeNode n2 = getNode(room2);
        if (n1 == null)
            throw new NoSuchElementException("No such a room: " + room1);
        if (n2 == null)
            throw new NoSuchElementException("No such a room: " + room2);
        if (n1.north == n2) {
            n1.north = null;
            n2.south = null;
        }
        if (n1.south == n2) {
            n1.south = null;
            n2.north = null;
        }
        if (n1.east == n2) {
            n1.east = null;
            n2.west = null;
        }
        if (n1.west == n2) {
            n1.west = null;
            n2.east = null;
        }
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
        start = null;
        goal = null;
    }

    /**
     * Removes all room nodes from the maze and indicates a
     * new size for the maze.
     * @param size The new size of the maze; it is preferable but
     * not required to have the actual size of the maze to be
     * of this indicated size.
     */
    public void reset(int size) {
        rooms.clear();
        start = null;
        goal = null;
        rooms.trimToSize();
        rooms.ensureCapacity(size);
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
     * Checks if the room has a link to another room in 
     * the north direction.
     * @param room The label of the room to be checked.
     * @return true if the room has a north room, false otherwise.
     * @throws NoSuchElementException if no room with the given label
     * can be found in the maze.
     */
    public boolean canGoNorth(String room) {
        MazeNode current = getNode(room);
        if (current == null)
            throw new NoSuchElementException("No such a room: " + room);
        return current.north != null;
    }

    /**
     * Checks if the room has a link to another room in 
     * the south direction.
     * @param room The label of the room to be checked.
     * @return true if the room has a south room, false otherwise.
     * @throws NoSuchElementException if no room with the given label
     * can be found in the maze.
     */
    public boolean canGoSouth(String room) {
        MazeNode current = getNode(room);
        if (current == null)
            throw new NoSuchElementException("No such a room: " + room);
        return current.south != null;
    }

    /**
     * Checks if the room has a link to another room in 
     * the east direction.
     * @param room The label of the room to be checked.
     * @return true if the room has an east room, false otherwise.
     * @throws NoSuchElementException if no room with the given label
     * can be found in the maze.
     */
    public boolean canGoEast(String room) {
        MazeNode current = getNode(room);
        if (current == null)
            throw new NoSuchElementException("No such a room: " + room);
        return current.east != null;
    }

    /**
     * Checks if the room has a link to another room in 
     * the west direction.
     * @param room The label of the room to be checked.
     * @return true if the room has a west room, false otherwise.
     * @throws NoSuchElementException if no room with the given label
     * can be found in the maze.
     */
    public boolean canGoWest(String room) {
        MazeNode current = getNode(room);
        if (current == null)
            throw new NoSuchElementException("No such a room: " + room);
        return current.west != null;
    }

    /**
     * Gets the label of the room to the north of this room as specified
     * by the given label.
     * @param thisRoom The label of the room to be checked.
     * @return The label of the north room; {@literal "<null>"} if there
     * is no room linked to this room in the north direction.
     * @throws NoSuchElementException if no room with the given label
     * can be found in the maze.
     */
    public String northRoom(String thisRoom) {
        MazeNode current = getNode(thisRoom);
        if (current == null)
            throw new NoSuchElementException("No such a room: " + thisRoom);
        return current.north == null ? "<null>" : current.north.label;
    }

    /**
     * Gets the label of the room to the south of this room as specified
     * by the given label.
     * @param thisRoom The label of the room to be checked.
     * @return The label of the south room; {@literal "<null>"} if there
     * is no room linked to this room in the south direction.
     * @throws NoSuchElementException if no room with the given label
     * can be found in the maze.
     */
    public String southRoom(String thisRoom) {
        MazeNode current = getNode(thisRoom);
        if (current == null)
            throw new NoSuchElementException("No such a room: " + thisRoom);
        return current.south == null ? "<null>" : current.south.label;
    }

    /**
     * Gets the label of the room to the east of this room as specified
     * by the given label.
     * @param thisRoom The label of the room to be checked.
     * @return The label of the east room; {@literal "<null>"} if there
     * is no room linked to this room in the east direction.
     * @throws NoSuchElementException if no room with the given label
     * can be found in the maze.
     */
    public String eastRoom(String thisRoom) {
        MazeNode current = getNode(thisRoom);
        if (current == null)
            throw new NoSuchElementException("No such a room: " + thisRoom);
        return current.east == null ? "<null>" : current.east.label;
    }

    /**
     * Gets the label of the room to the west of this room as specified
     * by the given label.
     * @param thisRoom The label of the room to be checked.
     * @return The label of the west room; {@literal "<null>"} if there
     * is no room linked to this room in the west direction.
     * @throws NoSuchElementException if no room with the given label
     * can be found in the maze.
     */
    public String westRoom(String thisRoom) {
        MazeNode current = getNode(thisRoom);
        if (current == null)
            throw new NoSuchElementException("No such a room: " + thisRoom);
        return current.west == null ? "<null>" : current.west.label;
    }

    /**
     * Counts how many links to other rooms this room as specified
     * by the given label has.
     * @param thisRoom The label of the room to be checked.
     * @return The number of links.
     * @throws NoSuchElementException if no room with the given label
     * can be found in the maze.
     */
    public int countLinks(String thisRoom) {
        MazeNode current = getNode(thisRoom);
        if (current == null)
            throw new NoSuchElementException("No such a room: " + thisRoom);
        int count = 0;
        if (current.north != null)
            count++;
        if (current.south != null)
            count++;
        if (current.east != null)
            count++;
        if (current.west != null)
            count++;
        return count;
    }

    /**
     * Checks if the maze is ready to be played.
     * The maze is ready if it contains a valid maze graph that
     * has at least one room node, 
     * and the start and the goal room are set to valid room nodes.
     * @return true if the maze is readty, false otherwise.
     */
    public boolean isReady() {
        if (rooms.isEmpty())
            return false;
        if (start == null || !rooms.contains(start))
            return false;
        if (goal == null || !rooms.contains(goal))
            return false;
        return true;
    }

    /**
     * Finds a path out of the maze by depth-first search on the graph.
     * <p>
     * This method may not find the optimal solution to the maze, if any.
     * The returned path may have detours, 
     * since all rooms visited are recorded.
     * @return A string representation of path found; the empty string
     * if there is no path out of the maze.
     * @throws IllegalStateException if the maze is not ready, 
     * as indicated by {@link #isReady()}.
     */
    public String findPath() {
        if (!isReady())
            throw new IllegalStateException("Maze not ready.");
        String path = "";
        LinkedList<MazeNode> explored = new LinkedList<>();
        LinkedList<MazeNode> toExplore = new LinkedList<>();
        toExplore.addFirst(start);
        MazeNode current = null;
        while (!toExplore.isEmpty()) {
            current = toExplore.pop();
            explored.add(current);
            if (current == goal)
                break;
            if (current.north != null 
                    && !explored.contains(current.north))
                toExplore.addFirst(current.north);
            if (current.south != null 
                    && !explored.contains(current.south))
                toExplore.addFirst(current.south);
            if (current.east != null 
                    && !explored.contains(current.east))
                toExplore.addFirst(current.east);
            if (current.west != null
                    && !explored.contains(current.west))
                toExplore.addFirst(current.west);
        }
        if (current == goal) {
            while (explored.size() > 1) {
                path += explored.pop().label;
                path += " -> ";
            }
            path += explored.pop().label;
        }
        return path;
    }

    /**
     * Checks if the current maze is solvable, that is, 
     * if there is a path out of the maze.
     * @return true if the maze is solvable, false otherwise.
     * @throws IllegalStateException if the maze is not ready,
     * as indicated by {@link #isReady()}.
     */
    public boolean isSolvable() {
        return !findPath().isEmpty();
    }

    /**
     * Returns a path that is generated by random walk of
     * the given length.
     * @param length The length of the walk.
     * @param stopAtGoal A boolean to indicate whether the walk
     * should stop when it reaches the goal room: true if it stops.
     * @return A string representation of the path walked.
     * @throws IllegalStateException if the maze is not ready,
     * as indicated by {@link #isReady()}.
     * @throws IllegalArgumentException if the given length is negative.
     */
    public String randomWalk(int length, boolean stopAtGoal) {
        if (length < 0)
            throw new IllegalArgumentException("Negative length.");
        if (!isReady())
            throw new IllegalStateException("Maze not ready.");
        Random rand = new Random();
        String path = "";
        MazeNode current = start;
        char[] possible = new char[4];
        char chosen;
        int j;
        for (int i = 0; i < length; i++) {
            path += current.label + " -> ";
            j = 0;
            if (current.north != null) {
                possible[j] = 'n';
                j++;
            }
            if (current.south != null) {
                possible[j] = 's';
                j++;
            }
            if (current.east != null) {
                possible[j] = 'e';
                j++;
            }
            if (current.west != null) {
                possible[j] = 'w';
                j++;
            }
            if (j > 0) {
                chosen = possible[rand.nextInt(j)];
                switch (chosen) {
                    case 'n':
                        current = current.north;
                        break;
                    case 's':
                        current = current.south;
                        break;
                    case 'e':
                        current = current.east;
                        break;
                    case 'w':
                        current = current.west;
                        break;
                    default:
                        throw new UnknownError();   // should be impossible
                }
            }
            if (current == goal && stopAtGoal)
                break;
        }
        path += current.label;
        return path;
    }

    /*
     * Checks if the given object is equal to this maze graph.
     * @param other An object to be checked.
     * @return true if they are equal, false otherwise.
     */
    @Override
    public boolean equals(Object other) {
        if (other == null)
            return false;
        else if (getClass() != other.getClass())
            return false;
        else {
            MazeGraph g = (MazeGraph) other;
            if (start == null && g.start != null)
                return false;
            else if (!start.equals(g.start))
                return false;
            if (goal == null && g.goal != null)
                return false;
            else if (!goal.equals(g.goal))
                return false;
            return rooms.equals(g.rooms);
        }
    }

    /**
     * Returns a human-redable string representation of this maze graph.
     * @return The string representation of this graph.
     */
    @Override
    public String toString() {
        String s = "";
        for (MazeNode node : rooms) {
            s += node.toString();
            s += "\n";
        }
        s += "Start: " + startRoom() + "\n";
        s += "Goal: " + goalRoom();
        return s;
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