import java.util.ArrayList;

/**
 * A class of hash tables with chaining.
 * When an item is to be inserted into the table or
 * is to be accessed in the table, 
 * a hash function will be used to find the target.
 * Collisions are resolved by separate chaining.
 * @author yomishino
 */
public class HashTable<T> {

    /**
     * A class for checking and finding prime numbers.
     * This class helps the {@link HashTable<T>} class to decide on what
     * new size to use when rehashing.
     * It keeps track of the primality of numbers at least up to 
     * a certain size of the hash table. 
     * When the checked numbers are greater than the current size of
     * the hash table, the next prime number for use as a new size 
     * can be generated from this class.
     */
    private class PrimeNumber {
        /** A partial list of prime numbers. */
        private final ArrayList<Integer> primes;
        /** The number up to which the primality has been checked. */
        private int checked;
        /** The smallest prime number. */
        private static final int MIN_PRIME = 2;

        /**
         * Constructs a <code>PrimeNumber</code> object containing
         * the list of prime numbers.
         * <p>
         * The list initially records prime numbers up to 
         * the default size of the hash table 
         * {@value HashTable#DEFAULT_SIZE}.
         * The list will grow automatically whenever needed.
         */
        private PrimeNumber() {
            primes = new ArrayList<>(DEFAULT_SIZE);
            primeNumbersUpTo(DEFAULT_SIZE);
        }
    
        /**
         * Generates the next prime number which is the smallest one
         * larger than the current size of the hash table.
         * @return The next prime number.
         */
        private int next() {
            while (lastOne() <= size) {
                primeNumbersUpTo(checked * 2);
            }
            int next = lastOne();
            for (int i = primes.size() - 1; i >= 0; i--) {
                if (primes.get(i) <= size)
                    return next;
                next = primes.get(i);
            }
            return next;
        }

        /**
         * Extends the prime number list to contain all the prime 
         * numbers that are no larger than <code>n</code>.
         * If numbers greater than this upper bound number <code>n</code>
         * has already been checked, then the list will be retained
         * and no further updating actions will be taken.
         * @param n The number at least up to which whose primality 
         * must be checked.
         */
        private void primeNumbersUpTo(int n) {
            if (n < checked)
                return;
            primes.ensureCapacity(n);
            if (primes.size() == 0) {
                primes.add(MIN_PRIME);
                checked = MIN_PRIME;
            }
            int newNum = checked;
            while (newNum < n) {
                newNum++;
                if (isPrime(newNum))
                    primes.add(newNum);
                checked++;
            }
            primes.trimToSize();
        }

        /**
         * Checks if the given integer is a prime number.
         * <p>
         * The checking process makes use of the recorded prime
         * numbers to reduce calculation.
         * <p>
         * Should only call this method inside the routine updating
         * procedure of the prime number lists maintained by this
         * <code>PrimeNumber</code> object (that is, inside
         * the {@link #primeNumbersUpTo(int)} method);
         * otherwise the result is not guaranteed.
         * @param n The number whose primality is to be checked.
         * @return <code>true</code> if the number is prime,
         * <code>false</code> otherwise.
         */
        private boolean isPrime(int n) {
            if (n < 2)
                return false;
            else if (n <= checked) {
                return primes.contains(n);
            }
            else {
                for (int i : primes) {
                    if (n % i == 0)
                        return false;
                }
                return true;
            }
        }

        /**
         * Gets the last prime number recorded so far.
         * @return The last element in the prime number list.
         */
        private int lastOne() {
            return primes.get(primes.size() - 1);
        }
    }   // End of PrimeNumber class


    /** The array of the indexed linked lists storing items. */
    private ArrayList<LinkedList<T>> table;
    /** Size of the hash table, the number of buckets. */
    private int size;
    /** Choices of a new size when rehashing. */
    private final PrimeNumber sizeChoices = new PrimeNumber();
    /** Default initial size of the hash table. */
    private static final int DEFAULT_SIZE = 7;
    /** The maximum load factor before rehashing. */
    private static final double MAX_LOAD = 0.9;

    /**
     * Constructs a hash table.
     * The initial size of the hash table is default 
     * to {@value #DEFAULT_SIZE}.
     */
    public HashTable() {
        this(DEFAULT_SIZE);
    }

    /**
     * Constructs a hash table with the given 
     * initial size.
     * @param size The initial size of the hash table;
     * must be positive.
     * @throws IllegalArgumentException If the given
     * size is not positive.
     */
    public HashTable(int size) {
        if (size <= 0)
            throw new IllegalArgumentException(
                "Non-positive hash table size given.");
        this.size = size;
        table = new ArrayList<>();
        newTable(size);
    }

    /**
     * Inserts the given item into this hash table.
     * @param item The item to be inserted.
     */
    public void insert(T item) {
        int hashValue = hash(item);
        table.get(hashValue).add(item);
        if (isLoadTooHigh())
            rehash();
    }

    /**
     * Removes the given item from this hash table.
     * @param item The item to be removed.
     * @return <code>true</code> if the item is found
     * and removed, <code>false</code> if the item is
     * not found.
     */
    public boolean remove(T item) {
        int hashValue = hash(item);
        return table.get(hashValue).remove(item);
    }

    /**
     * Checks if the given item is in this hash table.
     * @param item The item to be found.
     * @return <code>true</code> if the item is in the
     * hash table, <code>false</code> otherwise.
     */
    public boolean contains(T item) {
        int hashValue = hash(item);
        return table.get(hashValue).contains(item);
    }

    /**
     * Removes all the item inserted into the hash table.
     */
    public void clear() {
        for (LinkedList<T> list : table)
            list.clear();
    }

    /**
     * Makes a copy of this hash table.
     * Items stored in the two tables are identical. 
     * The copy is thus not a truly deep copy.
     * @return The copy of the hash table.
     */
    public HashTable<T> copy() {
        HashTable<T> copyHt = new HashTable<>(size);
        for (int i = 0; i < size; i++)
            copyHt.table.set(i, table.get(i).copy());
        return copyHt;
    }

    /**
     * Returns a string representation of this hash table.
     * If a human-readable representation is desired, 
     * the data stored in the nodes must be of a type that
     * has its own well-defined <code>toString()</code> method.
     * @return A string representation of the hash table.
     */
    @Override
    public String toString() {
        String s = "";
        for (int i = 0; i < size; i++) {
            s += String.valueOf(i) + ": " + table.get(i).toString();
            s += i == size - 1 ? "" : "\n";
        }
        return s;
    }

    /**
     * Checks if the given object is equal to this hash table.
     * @param other The other object.
     * @return <code>true</code> if the two objects are equal,
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
            HashTable<T> ot = (HashTable<T>) other;
            if (size != ot.size)
                return false;
            for (int i = 0; i < size; i++) {
                if (!table.get(i).equals(ot.table.get(i)))
                    return false;
            }
            return true;
        }
    }

    /**
     * Computes the hash value of the item.
     * @param item The item whose hash value is to
     * be computed.
     * @return The hash value in integer.
     */
    private int hash(T item) {
        return Math.abs(item.hashCode()) % size;
    }

    /**
     * Rehashes to store all the items in a larger hash table.
     */
    private void rehash() {
        Object[] old = new Object[countItems()];
        int i = 0;
        for (LinkedList<T> list : table) {
            if (!list.isEmpty()) {
                Object[] arr = list.toArray();
                for (int j = 0; j < list.size(); j++) {
                    old[i] = arr[j];
                    i++;
                }
            }
        }
        while (isLoadTooHigh()) {
            size = sizeChoices.next();
        }
        newTable(size);
        for (Object o : old) {
            @SuppressWarnings("unchecked")
            T item = (T) o;
            insert(item);
        }
    }

    /**
     * Computes the load factor for this hash table.
     * @return The load factor.
     */
    private double load() {
        return (double) countItems() / size;
    }

    /**
     * Checks if the load factor is too high.
     * @return <code>true</code> if it is too high, 
     * <code>false</code> otherwise.
     */
    private boolean isLoadTooHigh() {
        return load() >= MAX_LOAD;
    }

    private int countItems() {
        int count = 0;
        for (LinkedList list : table)
            count += list.size();
        return count;
    }

    /**
     * Initialises a new hash table.
     * @param newSize The initial size of the new table.
     */
    private void newTable(int newSize) {
        size = newSize;
        table.clear();
        table.ensureCapacity(newSize);
        for (int i = 0; i < size; i++)
            table.add(new LinkedList<T>());
    }
}
