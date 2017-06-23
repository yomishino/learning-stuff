// This is the answer to the book Ch.14 Self-Test Ex.9.
import java.util.ArrayList;

/**
 * This class implements the selection sort for an <code>ArrayList</code> 
 * of <code>String</code>s.
 * @author yomishino
 * @version 1.0
 */
public class StringSelectionSort {
    /**
     * Sorts the strings into lexicographic order, using selection sort.
     * @param a An <code>ArrayList</code> of <code>String</code>s to be sorted.
     */
    public static void sort(ArrayList<String> a) {
        int sIndex;
        for (int i = 0; i < a.size(); i++) {
            sIndex = smallestIndexOf(a, i);
            interchange(a, i, sIndex);
        }
    }

    /**
     * Finds the index of the smallest element, 
     * starting from the given index position.
     * @param a An <code>ArrayList</code> of <code>String</code>s.
     * @param i An <code>int</code> of the starting index specifying 
     * the range of <code>a</code>
     * among which the index of the smallest element is to be found.
     * @return An <code>int</code> of the index of the smallest element.
     */
     private static int smallestIndexOf(ArrayList<String> a, int i) {
        int sIndex = i;
        String minVal = a.get(i);
        for (int j = i + 1; j < a.size(); j++) {
            if (a.get(j).compareTo(minVal) < 0) {
                sIndex = j;
                minVal = a.get(j);
            }
        }
        return sIndex;
    }

    /**
     * Swaps the two specified elements.
     * @param a An <code>ArrayList</code> of <code>String</code>s.
     * @param i An <code>int</code> of the index of the first element.
     * @param j An <code>int</code> of the index of the second element.
     */
    private static void interchange(ArrayList<String> a, int i, int j) {
        String temp = a.set(i, a.get(j));
        a.set(j, temp);
    }
}
