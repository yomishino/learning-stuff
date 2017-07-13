/**
 * A class of unordered sets. 
 * A set contains only one copy of an element.
 * Here the sets are implemented use both the hash table and 
 * the linked list, to optimise different operations by trading off 
 * space efficency.
 * @author yomishino
 */
public class Set<T> {

    /** The linked list representation of the set. */
    private LinkedList<T> linkedlist;
    /** The iterator for the linked list. */
    private LinkedList<T>.LinkedListIterator listIterator;
    /** The hash table representation of the set. */
    private HashTable<T> hashtable;

    /** Constructs an initially empty set. */
    public Set() {
        linkedlist = new LinkedList<>();
        hashtable = new HashTable<>();
        listIterator = linkedlist.iterator();
    }

    /**
     * Makes a copy of this set.
     * Items stored in the two sets are identical. 
     * The copy is thus not a truly deep copy.
     * @return The copy of the set.
     */
    public Set<T> copy() {
        Set<T> copySet = new Set<>();
        copySet.linkedlist = linkedlist.copy();
        copySet.hashtable = hashtable.copy();
        copySet.listIterator = copySet.linkedlist.iterator();
        return copySet;
    }

    /**
     * Adds an item into the set.
     * @param item The item to be added.
     * @return <code>true</code> if the item is successfully added,
     * <code>false</code> if the item is already in the set and thus
     * is not added again.
     */
    public boolean add(T item) {
        if (contains(item))
            return false;
        else {
            linkedlist.add(item);
            hashtable.insert(item);
            return true;
        }
    }

    /**
     * Removes the given item from the set.
     * @param item The item to be removed.
     * @return <code>true</code> if the item is successfully removed,
     * <code>false</code> if the item is not in the set and thus cannot
     * be removed.
     */
    public boolean remove(T item) {
        if (!contains(item))
            return false;
        else
            return linkedlist.remove(item) & hashtable.remove(item);
    }

    /**
     * Removes all elements from the set, making it empty.
     */
    public void clear() {
        linkedlist.clear();
        hashtable.clear();
    }

    /**
     * Returns the size of this set.
     * @return The total number of elements in the set.
     */
    public int size() {
        return linkedlist.size();
    }

    /**
     * Checks if this set is an empty set.
     * @return <code>true</code> if the set is empty, 
     * <code>false</code> otherwise.
     */
    public boolean isEmpty() {
        return linkedlist.isEmpty();
    }

    /**
     * Checks if the set contains the given item.
     * @param item The item to be checked.
     * @return <code>true</code> if the set contains the given item,
     * <code>false</code> otherwise.
     */
    public boolean contains(T item) {
        return hashtable.contains(item);
    }


    /**
     * Performs the union operation on this set and the other set.
     * The resulting set contains elements appear in either set.
     * @param other The other set to be unioned with this set.
     * @return The set resulting from union.
     */
    public Set<T> union(Set<T> other) {
        Set<T> uSet = new Set<>();
        listIterator.reset();
        while (listIterator.hasNext()) {
            uSet.add(listIterator.next());
        }
        other.listIterator.reset();
        while (other.listIterator.hasNext()) {
            uSet.add(other.listIterator.next());
        }
        return uSet;
    }

    /**
     * Performs the intersection operation on this set and the other set.
     * The resulting set contains only elements common to both sets.
     * @param other The other set to be intersected with this set.
     * @return The set resulting from intersection.
     */
    public Set<T> intersection(Set<T> other) {
        Set<T> iSet = new Set<>();
        if (isEmpty() || other.isEmpty())
            return iSet;
        T item;
        listIterator.reset();
        while (listIterator.hasNext()) {
            item = listIterator.next();
            if (other.contains(item))
                iSet.add(item);
        }
        return iSet;
    }

    /**
     * Performs the difference operation on this set and the other set.
     * The resulting set contains elements that are in this set
     * but not in the other set.
     * @param other The other set agains which the difference is to be
     * calculated.
     * @return The set resulting from difference.
     */
    public Set<T> difference(Set<T> other) {
        Set<T> dSet = new Set<>();
        if (isEmpty())
            return dSet;
        else if (other.isEmpty())
            return copy();
        else {
            T item;
            listIterator.reset();
            while (listIterator.hasNext()) {
                item = listIterator.next();
                if (!other.contains(item))
                    dSet.add(item);
            }
        }
        return dSet;
    }

    /**
     * Checks if the other object is equal to this set.
     * @param other The other object to be checked.
     * @return <code>true</code> if the two are equal,
     * <code>false</code> otherwise.
     */
    @Override
    public boolean equals(Object other) {
        if (other == null)
            return false;
        else if (getClass() != other.getClass())
            return false;
        else {
            @SuppressWarnings("unchecked")
            Set<T> oSet = (Set<T>) other;
            return linkedlist.equals(oSet.linkedlist);
        }
    }

    /**
     * Returns a string representation of this set.
     * If a human-readable representation is desired, 
     * the items in the set must be of a type that has its own
     * well-defined <code>toString()</code> method.
     * @return A string representation of the hash table.
     */
    @Override
    public String toString() {
        return linkedlist.toString();
    }
}