import java.util.Scanner;

/**
 * A program for simulating the princess Eve's way of choosing a suitor,
 * which is lining up all suitors in a circle and eliminating every third
 * suitor, and for deciding which is the best position for the user so
 * that he (or she?) can cheat other suitors and marry the princess.
 * @author yomishino
 * @version 1.0
 */
public class SuitorChoice {
    /** The list of suitors. */
    private CircularLinkedList<Integer> suitors;
    /** The number to be skipped when deciding the next unlucky suitor. */
    private static final int STEP = 3;


    /**
     * Constructs the object with a list of suitors of size ``numSuitors``.
     * Each suitor is represented by their index number, counting from 1.
     * If 0 is given, this is equivalent to calling {@link #SuitorChoice()}.
     * @param numSuitors The total number of suitors; must be positive.
     */
    public SuitorChoice(int numSuitors) {
        if (numSuitors <= 0)
            throw new IllegalArgumentException("Invalid number of suitors.");
        else {
            suitors = new CircularLinkedList<>();
            for (int i = 1; i <= numSuitors; i++) {
                suitors.append(i);
            }
        }
    }

    /**
     * Simulates the whole elimination process of suitors.
     * @return The index number of the lucky suitor that has survived
     * the elimination process.
     */
    public int play() {
        CircularLinkedList<Integer>.CircularLinkedListIterator itr
            = suitors.iterator();
        while (suitors.size() > 1) {
            System.out.println();
            System.out.println(this);
            onepass(itr);
        }
        return itr.next();
    }

    /**
     * Simulates one turn of elimination, from starting/continuing
     * counting to eliminate one particular suitor.
     */
    private void onepass(
            CircularLinkedList<Integer>.CircularLinkedListIterator itr) {
        System.out.println("Counting from " + itr.next() + "...");
        for (int i = 1; i < STEP - 1; i++) {
            // ``STEP - 1`` because already called next() once.
            itr.next();
        }
        int removed = itr.removeNode();
        System.out.println("Suitor " + removed + " is eliminated.");
    }

    /**
     * Returns a string representation of the current state of suitor
     * choosing.
     * @return A human-readable string representation.
     */
    @Override
    public String toString() {
        return "Remaining suitors: " + suitors.toString();
    }


    public static void main(String[] args) {
        SuitorChoice choice = null;
        Scanner sc = new Scanner(System.in);
        int numSuitors = 0;
        boolean valid = false;
        do {
            try {
                System.out.print("\nHow many suitors are there?  ");
                numSuitors = Integer.parseInt(sc.nextLine());
                choice = new SuitorChoice(numSuitors);
                valid = true;
                sc.close();
            } catch (NumberFormatException e) {
                System.out.println("\nPlease enter a positive integer.");
            } catch (IllegalArgumentException e) {
                System.out.println("\nPlease enter a positive integer.");
            }
        } while (!valid);
        int winner = choice.play();
        System.out.println("\nElimination done.");
        System.out.println("Suitor " + winner + " is the lucky winner.");
        System.out.println("\nYou should stand in Position " + winner 
                            + " to marry the princess, if there are "
                            + numSuitors + " suitors.\n");
    }
}