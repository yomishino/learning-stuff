/**
 * A class of ants living in the Predator-prey simulation world.
 * @author yomishino
 * @version 1.0
 */
class Ant extends Organism {

    /** Default frequency of breeding of an ant. */
    public static final int DEFAULT_BREED_CYCLE = 3;
    /** The character representing the ant in the cell-world. */
    public static final char REP_CHAR = 'o';


    /** 
     * Constructs an <code>Ant</code> object that represents an ant.
     */
    Ant() {
        super();
    }


    /**
     * Gets the frequency of breeding, that is the time span
     * between two times of breeding behaviour.
     * @return A <code>int</code> of the breeding cycle.
     */
    @Override
    int getBreedingCycle() {
        return DEFAULT_BREED_CYCLE;
    }

    /**
     * Draws the ant on the screen using a predefined representation.
     */
    @Override
    void draw() {
        System.out.print(REP_CHAR);
    }
}
