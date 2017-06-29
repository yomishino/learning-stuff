import java.util.Random;

/**
 * A class of organisms that encompsulates basic data common to all organisms in
 * the predator-prey simulation.
 * @author yomishino
 * @version 1.0
 */
public abstract class Organism {

    /** Total time the organism has lived. */
    private int age;
    /** Whether the organism is alive or dead. */
    private boolean alive;
    /** The time span of the organism not breeding. */
    private int timeNotBreed;
    /** The current position of the organism. */
    private int[] position;
    /** A random number generator used when simulating random behaviours. */
    private final Random rand = new Random();
    

    /**
     * Constructs an <code>Organism</code> object that represents an organism.
     */
    public Organism() {
        age = 0;    // do not increment here
        alive = true;
        resetBreedTime();
        position = new int[2];
    }


    /**
     * Simulates the organism's moving to a specified position.
     * @param r An <code>int</code> of the row index of the position.
     * @param c An <code>int</code> of the column index of the position.
     * @return A two-element array of <code>int</code> of the position
     * the organism has moved to.
     */
    public int[] move(int r, int c) {
        setPosition(r, c);
        return getPosition();
    }

    /**
     * Simulates the breeding behaviour of an organism.
     * By default, a new organism will be created and returnd,
     * and will be set to the given position.
     * @param r An <code>int</code> of the row index of the position.
     * @param c An <code>int</code> of the column index of the position.
     * @return An <code>Organism</code> object that is the new organism. 
     */
    public Organism breed(int r, int c) {
        Organism newOrg = null;
        try {
            newOrg = getClass().newInstance();
            newOrg.move(r, c);
            resetBreedTime();
        }
        catch (Exception e) {
            System.err.println("Cannot instantiate a new "
                                + getClass().getSimpleName());
        }
        return newOrg;
    }
    
    /**
     * Chooses a position from the four directions randomly.
     * The choice is made bindly and thus can be a position that is 
     * unavailable temporarily (such as an occupied cell).
     * @param height An <code>int</code> of the height of the grid.
     * @param width An <code>int</code> of the width of the grid.
     * @return A two-element array of <code>int</code> of the chosen
     * position.
     */
    public int[] randomNearbyPosition(int height, int width) {
        int choice, r, c;
        do {
            r = getPosition()[0];
            c = getPosition()[1];
            choice = rand.nextInt(4);
            switch (choice) {
                case 0:     // up
                    r--;
                    break;
                case 1:     // down
                    r++;
                    break;
                case 2:     // left
                    c--;
                    break;
                case 3:     // right
                    c++;
                    break;
            }
        } while (r < 0 || c < 0 || r >= height || c >= width);
        int[] p = {r, c};
        return p;
    }

    /**
     * Sets the position of the organism.
     * @param r An <code>int</code> of the row index of the position.
     * @param c An <code>int</code> of the column index of the position.
     */
    public void setPosition(int r, int c) {
        position[0] = r;
        position[1] = c;
    }

    /** 
     * Gets the current position of the organism in the world. 
     * Note that modifying the returned position will not change the 
     * position of this organism.
     * @return A two-element array of the position of the organism.
     */
    public int[] getPosition() {
        int[] p = new int[2];
        p[0] = position[0];
        p[1] = position[1];
        return p;
    }

    /**
     * Kills the organism.
     */
    public void die() {
        alive = false;
    }

    /**
     * Checks if the organism is alive.
     * @return A <code>boolean</code> indicating if the organism
     * is alive.
     */
    public boolean isAlive() {
        return alive;
    }

    /**
     * Makes the organism one time step older.
     * Usually to be called at the beginning of the time step.
     */
    public void grow() {
        age++;
        timeNotBreed++;
    }

    /**
     * Gets the current age of the organism.
     * @return A <code>int</code> of the age.
     */
    public int getAge() {
        return age;
    }

    /**
     * Gets the frequency of breeding, that is the time span
     * between two times of breeding behaviour.
     * To be overriden by subclasses.
     * @return A <code>int</code> of the breeding cycle.
     */
    public abstract int getBreedingCycle();

    /**
     * Checks if the organism is ready to breed, regardless of whether 
     * there is empty space for it to be born.
     * @return A <code>boolean</code> indicating whether the organism 
     * is ready for breeding.
     */
    public boolean isReadyForBreeding() {
        return timeNotBreed >= getBreedingCycle();
    }

    /**
     * Draws the organism on the screen.
     * To be overriden by subclasses according to their own char 
     * representation.
     */
    public abstract void draw();

    /** 
     * Resets the time span the organism has not bred. 
     */
    private void resetBreedTime() {
        timeNotBreed = 0;
    }

    /**
     * Returns a string representation of this <code>Organism</code> 
     * object.
     * @return A <code>String</code> of the representation.
     */
    @Override
    public String toString() {
        String s = getClass().getSimpleName() + ": ";
        s += "Age " + String.valueOf(getAge()) + ", ";
        s += isAlive() ? "alive, " : "dead, ";
        s += "at (" + String.valueOf(getPosition()[0]) + ", "
                    + String.valueOf(getPosition()[1]) + ")";
        return s;
    }
}
