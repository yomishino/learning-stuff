import java.util.ArrayList;
import java.util.ListIterator;

/**
 * A simulation of Laura's suitor elimination, 
 * letting the user cheat others in order to marry Laura.
 * @author yomishino
 */
public class SuitorEliminator {
    private ArrayList<Integer> suitors;
    private ListIterator<Integer> itr;
    /** Eliminates the {@value #STEP}-th suitor each turn. */
    private static final int STEP = 5;

    /**
     * Constructs a <code>SuitorEliminator</code> object.
     * @param numSuitors Total number of suitors.
     * @throws IllegalArgumentException If the given number is less than 1.
     */
    public SuitorEliminator(int numSuitors) {
        if (numSuitors < 1)
            throw new IllegalArgumentException(
                "There must be at least one suitor.");
        suitors = new ArrayList<>(numSuitors);
        for (int i = 1; i <= numSuitors; i++)
            suitors.add(i);
        itr = suitors.listIterator();
    }
    
    /**
     * Returns a string representation of this object.
     * @return A string representation.
     */
    @Override
    public String toString() {
        String s = "";
        for (int i : suitors)
            s += String.valueOf(i) + " ";
        return s;
    }

    /**
     * Kills all suitors but one!
     */
    public void kill() {
        outputInitial();
        while (!isDone()) {
            oneTurn();
        }
        outputWinner();
    }

    /**
     * Simulates the elimination process for one turn.
     * Eliminates the {@value #STEP}-th suitor and
     * returns his/her number.
     * @return The assigned number of the suitor eliminated;
     * -1 if no one is eliminated.
     */
    private int oneTurn() {
        int toRemove = -1;
        if (suitors.size() > 1) {
            for (int i = 0; i < STEP; i++) {
                if (!itr.hasNext())
                    itr = suitors.listIterator();
                toRemove = itr.next();
            }
            itr.remove();
            System.out.println("Suitor " + toRemove + " eliminated.");
        }
        outputCurrent();
        return toRemove;
    }

    /**
     * Outputs the initial status of suitors on the screen.
     */
    private void outputInitial() {
        System.out.println("Initial list of suitors: " + this);
        System.out.println();
    }

    /**
     * Outputs the current status of suitors on the screen.
     */
    private void outputCurrent() {
        System.out.println("Remaining suitors: " + this);
        System.out.println();
    }

    /**
     * Announces the winner who survives the elimination.
     */
    private void outputWinner() {
        System.out.println("The lucky winner is " + suitors.get(0));
    }

    /**
     * Chceks if only one suitor left.
     * @return true if the elimination process is done, false otherwise.
     */
    private boolean isDone() {
        return suitors.size() <= 1;
    }


    /**
     * A demo for this class, with 56 suitors in total.
     */
    private static void demo() {
        SuitorEliminator e = new SuitorEliminator(56);
        e.kill();
    }

    public static void main(String[] args) {
        demo();
    }
}