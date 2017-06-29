/**
 * A class of doodlebugs living in the Predator-prey simulation world.
 * <p>
 * Doodlebug is the name some North Americans give to the 
 * disgusting-looking larva of an antlion. 
 * Fortunately, doodlebugs only appear as harmless ASCII characters
 * in the simulation world.
 * Hopefully these lovely little characters would not cause any kind of 
 * suffering to users even if they outbreed the whole simulation world.
 * @author yomishino
 * @version 1.0
 */
public class Doodlebug extends Organism {

    /** Number of time steps the doodlebug has lived without eating. */
    private int timeHungary;
    /** List of the name of the organisms a doodlebug eats. */
    private static final String[] PREYS = {"Ant"};
    /** Default frequency of breeding of a doodlebug. */
    public static final int DEFAULT_BREED_CYCLE = 8;
    /** Default number of time steps before the doodlebug starves. */
    public static final int DEFAULT_STARVE_TIME = 3;
    /** The character representing the doodlebug in the cell-world. */
    public static final char REP_CHAR = 'X';


    /**
     * Constructs a <code>Doodlebug</code> object that represents a
     * doodlebug.
     */
    public Doodlebug() {
        super();
        fed();
    }


    /** 
     * Simulates the death from starvation of a doodlebug.
     */
    public void starve() {
        die();
    }

    /**
     * Simulates the eating behaviour of a doodlebug.
     * @param prey An <code>Organism</code> object representing
     * the organism to be eaten by this doodlebug.
     */
    public void eat(Organism prey) {
        prey.die();
        fed();
    }

    /**
     * Makes the doodlebug one time step older.
     * Usually to be called at the beginning of the time step.
     */
    @Override
    public void grow() {
        timeHungary++;
        super.grow();
    }

    /**
     * Gets the frequency of breeding, that is, the time span
     * between two times of breeding behaviour.
     * @return A <code>int</code> of the breeding cycle.
     */
    @Override
    public int getBreedingCycle() {
        return DEFAULT_BREED_CYCLE;
    }

    /**
     * Draws the doodlebug on the screen using a char representation.
     */
    @Override
    public void draw() {
        System.out.print(REP_CHAR);
    }

    /**
     * Looks for preys in the adjacent cells.
     * @param neighbours An four-element array of <code>Organism</code>
     * object of the four adjacent cells, in the order of UP, DOWN, LEFT,
     * RIGHT. 
     * @return A two-element array of <code>int</code> of the position
     * of the prey (only one if many preys exist); <code>null</code> if
     * no preys exist in the adjacent cells. 
     */
    public int[] findPrey(Organism[] neighbours) {
        int[] pos = new int[2];
        for (Organism o : neighbours) {
            if (o != null && isPrey(o)) {
                return o.getPosition();
            }
        }
        return null;
    }

    /**
     * Checks if the given organism can be eaten by a doodlebug.
     * @param o A <code>Organism</code> object to be checked.
     * @return A <code>boolean</code> indicating whether a doodlebug
     * will eat the given organism.
     */
    private boolean isPrey(Organism o) {
        if (o == null) return false;
        String name = o.getClass().getSimpleName();
        for (String s : PREYS) {
            if (s.equals(name)) return true;
        }
        return false;
    }

    /**
     * Checks if this doodlebug is starving and will soon die.
     * <p>
     * If a doodlebug has not eaten for time steps of
     * {@value #DEFAULT_STARVE_TIME}, it is doomed to die.
     * @return A <code>boolean</code> indicating whether the
     * doodlebug is starving.
     */
    public boolean isStarving() {
        return timeHungary >= DEFAULT_STARVE_TIME;
    }
    
    /**
     * Simulates the doodlebug's being fed.
     */
    private void fed() {
        timeHungary = 0;
    }
}
