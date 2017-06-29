import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

/**
 * A class of a Predator-prey simulator.
 * The simulated world is composed of a 2D grid of cells of designated size,
 * which is enclosed so that no organisms can move off the edges.
 * Time is simulated in time steps.
 * @author yomishino
 * @version 1.0
 */
public class PPSimulator {

    /** The simulation world composed of a grid of cells. */
    private Organism[][] world;
    /** The collection of ants in this simulation world. */
    private ArrayList<Ant> ants;
    /** The collection of doodlebugs in this simulation world. */
    private ArrayList<Doodlebug> bugs;
    /** The total number of ants in this simulation world. */
    private int numAnts;
    /** The total number of doodlebugs in this simulation world. */
    private int numBugs;
    /** The total number of time steps passed in this simulation world. */
    private int time = 0;
    /** The default height of the grid. */
    private static final int DEFAULT_GRID_HEIGHT = 20;
    /** The default width of the grid. */
    private static final int DEFAULT_GRID_WIDTH = 20;
    /** The default initial number of ants. */
    private static final int DEFAULT_INIT_NUM_ANTS = 100;
    /** The default initial number of doodlebugs. */
    private static final int DEFAULT_INIT_NUM_BUGS = 5;
    /** The ASCII character representing a cell with no organisms. */
    private static final char EMPTY_CELL = '.';


    /** Constructs a Predator-prey simulator with the default settings. */
    public PPSimulator() {
        this(DEFAULT_INIT_NUM_ANTS, DEFAULT_INIT_NUM_BUGS);
    }

    /**
     * Constructs a Predator-prey simulator with the given initial numbers
     * of organisms and the default size of grid of cells.
     * The total number of organisms cannot exceed 
     * {@value #DEFAULT_GRID_HEIGHT} * {@value #DEFAULT_GRID_WIDTH}.
     * @param numAnts An <code>int</code> of the initial number of ants.
     * @param numBugs An <code>int</code> of the initial number of bugs.
     */
    public PPSimulator(int numAnts, int numBugs) {
        this(DEFAULT_GRID_HEIGHT, DEFAULT_GRID_WIDTH,
             numAnts, numBugs);
    }

    /**
     * Constructs a Predator-prey simulator with the given size of grid of
     * cells and initial numbers of organisms.
     * The total number of organisms cannot exceed 
     * {@value #DEFAULT_GRID_HEIGHT} * {@value #DEFAULT_GRID_WIDTH}.
     * @param h An <code>int</code> of the height of the grid.
     * @param w An <code>int</code> of the width of the grid.
     * @param numAnts An <code>int</code> of the initial number of ants.
     * @param numBugs An <code>int</code> of the initial number of bugs.
     */
    public PPSimulator(int h, int w, int numAnts, int numBugs) 
                    throws IllegalArgumentException {
        if (numAnts + numBugs > h * w)
            throw new IllegalArgumentException(
                "Too many organisms for the world.");
        world = new Organism[h][w];
        this.numAnts = numAnts;
        this.numBugs = numBugs;
        ants = new ArrayList<>(numAnts);
        bugs = new ArrayList<>(numBugs);
        populate(numAnts, numBugs);
    }


    /**
     * Draws the world on the screen, using corresponding ASCII characters
     * to represent the organisms.
     */
    public void drawWorld() {
        System.out.println();
        for (int row = 0; row < world.length; row++) {
            System.out.print("\t");
            for (Organism o : world[row]) {
                if (o == null)
                    System.out.print(EMPTY_CELL);
                else 
                    o.draw();
                System.out.print(" ");
            }
            System.out.println();
        }
        System.out.println();
        System.out.println("Time steps: " + time);
        System.out.println("Total number of ants alive: " + numAnts);
        System.out.println("Total number of doodlebugs alive: " + numBugs);
        System.out.println();
    }

    /**
     * Runs the simulation.
     */
    public void simulate() {
        String exitFlag = "q";
        String input;
        Scanner keyboard = new Scanner(System.in);
        do {
            drawWorld();
            System.out.println("Press enter to continue,");
            System.out.println("or enter " + exitFlag 
                                + " to exit.");
            input = keyboard.nextLine();
            advanceTime();
            oneStep();
        } while (!input.equals(exitFlag));
    }

    /**
     * Simulates what happens in the world during one time step.
     */
    private void oneStep() {
        // bug
        doodlebugsOneStep();
        clearDeadDoodlebugs();
        clearDeadAnts();
        // ant
        antsOneStep();
    }

    /**
     * Simulates what happens to the doodlebugs in the world during one time
     * step.
     */
    private void doodlebugsOneStep() {
        int wh = world.length, ww = world[0].length;
        ArrayList<Doodlebug> newBugs = new ArrayList<>();
        int[] pos, oldPos;
        Doodlebug newBug;
        Organism prey;
        for (Doodlebug bug : bugs) {
            bug.grow();
            oldPos = bug.getPosition();
            // move
            pos = bug.findPrey(adjacentCells(oldPos[0], oldPos[1]));
            if (pos != null) {
                prey = world[pos[0]][pos[1]];
                bug.move(pos[0], pos[1]);
                bug.eat(prey);
                world[pos[0]][pos[1]] = null;
                swapCells(oldPos, pos);
                numAnts--;
            }
            else {
                if (hasEmptyNeighbour(oldPos[0], oldPos[1])) {
                    do {
                        pos = bug.randomNearbyPosition(wh, ww);
                    } while (!isCellAvailable(pos[0], pos[1]));
                    bug.move(pos[0], pos[1]);
                    swapCells(pos, oldPos);
                }
            }
            oldPos = bug.getPosition();
            // starve
            if (bug.isStarving()) {
                bug.starve();
                numBugs--;
                world[oldPos[0]][oldPos[1]] = null;
            }
            // breed
            if (bug.isAlive() && bug.isReadyForBreeding()
                    && hasEmptyNeighbour(oldPos[0], oldPos[1])) {
                do {
                    pos = bug.randomNearbyPosition(wh, ww);
                } while (!isCellAvailable(pos[0], pos[1]));
                newBug = (Doodlebug) bug.breed(pos[0], pos[1]);
                world[pos[0]][pos[1]] = newBug;
                numBugs++;
                newBugs.add(newBug);
            }
        }
        bugs.addAll(newBugs);
    }


    /**
     * Simulates what happens to the ants in the world during one time step.
     */
    private void antsOneStep() {
        int wh = world.length, ww = world[0].length;
        ArrayList<Ant> newAnts = new ArrayList<>();
        int[] pos, oldPos;
        Ant newAnt;
        for (Ant ant : ants) {
            ant.grow();
            // move
            oldPos = ant.getPosition();
            if (hasEmptyNeighbour(oldPos[0], oldPos[1])) {
                do {
                    pos = ant.randomNearbyPosition(wh, ww);
                } while (!isCellAvailable(pos[0], pos[1]));
                ant.move(pos[0], pos[1]);
                swapCells(pos, oldPos);
            }
            // breed
            oldPos = ant.getPosition();
            if (hasEmptyNeighbour(oldPos[0], oldPos[1])
                        && ant.isReadyForBreeding()) {
                do {
                    pos = ant.randomNearbyPosition(wh, ww);
                } while (!isCellAvailable(pos[0], pos[1]));
                newAnt = (Ant) ant.breed(pos[0], pos[1]);
                world[pos[0]][pos[1]] = newAnt;
                numAnts++;
                newAnts.add(newAnt);
            }
        }
        ants.addAll(newAnts);
    }

    /**
     * Removes the dead ants from the collection.
     */
    private void clearDeadAnts() {
        ArrayList<Ant> aliveAnts = new ArrayList<>(ants.size());
        for (Ant a : ants) {
            if (a.isAlive())
                aliveAnts.add(a);
        }
        aliveAnts.trimToSize();
        ants = aliveAnts;
    }

    /**
     * Removes the dead doodlebugs from the collection.
     */
    private void clearDeadDoodlebugs() {
        ArrayList<Doodlebug> aliveBugs = new ArrayList<>(bugs.size());
        for (Doodlebug d : bugs) {
            if (d.isAlive())
                aliveBugs.add(d);
        }
        aliveBugs.trimToSize();
        bugs = aliveBugs;
    }


    /**
     * Lists the contents of all adjacent cells of the given position.
     * @param r An <code>int</code> of the row index of the position.
     * @param c An <code>int</code> of the column index of the position.
     * @return An array of <code>Organism</code> objects from the
     * adjacent cells, in the order of UP, DOWN, LEFT, RIGHT; 
     * elements can be <code>null</code>.
     */
    private Organism[] adjacentCells(int r, int c) {
        Organism[] neighbours = {null, null, null, null};
        // UP
        if (!isOutside(r - 1, c) && hasOrganism(r - 1, c))
            neighbours[0] = world[r - 1][c];
        // DOWN
        if (!isOutside(r + 1, c) && hasOrganism(r + 1, c))
            neighbours[1] = world[r + 1][c];
        // LEFT
        if (!isOutside(r, c - 1) && hasOrganism(r, c - 1))
            neighbours[2] = world[r][c - 1];
        // RIGHT
        if (!isOutside(r, c + 1) && hasOrganism(r, c + 1))
            neighbours[3] = world[r][c + 1];
        return neighbours;
    }

    /**
     * Finds an empty cell randomly among all empty ones and return its
     * row and column indexes.
     * @return A two-element array whose first element is the row index
     * of the empty cell and the second is the column index;
     * <code>null</code> if no empty cell.
     */
    private int[] findEmptyCell() {
        if (isFull()) return null;
        Random rand = new Random();
        int orgSize = numAnts + numBugs;
        int gridSize = world.length * world[0].length;
        if (orgSize >= 2 * gridSize / 3) {     // dense
            int[][] empty = new int[gridSize - orgSize][2];
            int i = 0;
            for (int row = 0; row < world.length; row++) {
                for (int col = 0; col < world[0].length; col++) {
                    if (isCellAvailable(row, col)) {
                        empty[i][0] = row;
                        empty[i][1] = col;
                        i++;
                    }
                }
            }
            return empty[rand.nextInt(empty.length)];
        }
        else {      // sparse
            int r, c;
            do {
                r = rand.nextInt(world.length);
                c = rand.nextInt(world[0].length);
            } while(!isCellAvailable(r, c));
            int[] pos = {r, c};
            return pos;
        }
    }

    /**
     * Swaps the contents of the two specified cells.
     * @param pos1 A two-element array of <code>int</code> whose first
     * element is the row index of the first cell and the second the
     * column index.
     * @param pos2 A two-element array of <code>int</code> similar to
     * <code>pos1</code> but for the position of the second cell.
     */
    private void swapCells(int[] pos1, int[] pos2) {
        Organism o = world[pos1[0]][pos1[1]];
        world[pos1[0]][pos1[1]] = world[pos2[0]][pos2[1]];
        world[pos2[0]][pos2[1]] = o;
    }

    /**
     * Checks if the cell at the given position is available.
     * A cell is available if it is empty and not outside of the world.
     * @param r The row index of the position.
     * @param c The column index of the position.
     * @return A <code>boolean</code> indicating whether the cell is emtpy.
     */
    private boolean isCellAvailable(int r, int c) {
        return !isOutside(r, c) && world[r][c] == null;
    }

    /**
     * Checks if the given position is outside of the world.
     * @param r The row index of the position.
     * @param c The column index of the position.
     * @return A <code>boolean</code> indicating whether the position is
     * outside of the world.
     */
    private boolean isOutside(int r, int c) {
        return r < 0 || c < 0 || r >= world.length || c >= world[0].length;
    }

    /**
     * Checks if there is any adjacent cell of the cell at the given 
     * position that is empty.
     * @param r The row index of the position.
     * @param c The column index of the position.
     * @return A <code>boolean</code> indicating whether there is any empty
     * adjacent cell.
     */
    private boolean hasEmptyNeighbour(int r, int c) {
        return isCellAvailable(r - 1, c) || isCellAvailable(r + 1, c)
                || isCellAvailable(r, c - 1) || isCellAvailable(r, c + 1);
    }

    /**
     * Checks if the cell at the given position contains an organism.
     * @param r The row index of the position.
     * @param c The column index of the position.
     * @return A <code>boolean</code> indicating whether the cell has an
     * organism in it.
     */
    private boolean hasOrganism(int r, int c) {
        if (isOutside(r, c)) return false;
        return world[r][c] != null;
    }

    /**
     * Checks if the world is full of organisms.
     * @return A <code>boolean</code> indicating the fullness of the 
     * world.
     */
    private boolean isFull() {
        return numAnts + numBugs >= world.length * world[0].length;
    }

    /**
     * Advances the time step by one.
     */
    private void advanceTime() {
        time++;
    }

    /** 
     * Populates the world with the given number of ants and doodlebugs,
     * and updates the collections of the ants and of the doodlebugs.
     * @param numAnts An <code>int</code> of the number of ants.
     * @param numBugs An <code>int</code> of the number of doodlebugs.
     */
    private void populate(int numAnts, int numBugs) {
        int[] pos;
        Ant a;
        Doodlebug b;
        for (int i = 0; i < numAnts; i++) {
            pos = findEmptyCell();
            a = new Ant();
            a.move(pos[0], pos[1]);
            world[pos[0]][pos[1]] = a;
            ants.add(a);
        }
        for (int i = 0; i < numBugs; i++) {
            pos = findEmptyCell();
            b = new Doodlebug();
            b.move(pos[0], pos[1]);
            world[pos[0]][pos[1]] = b;
            bugs.add(b);
        }
    }


    public static void main(String[] args) {
        PPSimulator p = new PPSimulator();
        p.simulate();
    }
}
