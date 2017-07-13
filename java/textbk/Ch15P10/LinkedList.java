import java.util.NoSuchElementException;

/**
 * A class of linked lists. 
 * A linked list maintains indefinite number of nodes.
 * Each node stores data of the specified type,
 * and each node has a link to the node next to it in the list,
 * except for the last node.
 * @author yomishino
 */
public class LinkedList<T> {

    /** A class of nodes of the linked list. */
    private class Node<T> {
        private T value;
        private Node<T> link;

        /**
         * Constructs an empty node that stores no data and links
         * to no nodes.
         */
        private Node() {
            value = null;
            link = null;
        }

        /**
         * Constructs a node that stores the given data and links to
         * the given other node.
         * @param value Data to be stored in the node.
         * @param link The node to which this node links.
         */
        private Node(T value, Node<T> link) {
            this.value = value;
            this.link = link;
        }
    }    // End of Node class


    /** An iterator class for the linked list. */
    public class LinkedListIterator {
        /** The current position in the list. */
        private Node<T> position;
        /** The node that links to {@link #position}. */
        private Node<T> prev;

        /**
         * Constructs an iterator for {@link LinkedList<T>}.
         */
        public LinkedListIterator() {
            reset();
        }

        /**
         * Resets the iterator so that it starts again from
         * the head node in the list.
         */
        public void reset() {
            position = head;
            prev = null;
        }

        /**
         * Moves the current position of the iterator to the next node.
         * @return The data stored in the node the iterator was originally
         * at before calling this method.
         * @throws NoSuchElementException If there is no next node, either
         * because the linked list is empty or the iterator is at the
         * end position.
         */
        public T next() {
            if (position == null) 
                throw new NoSuchElementException("No next node.");
            T item = position.value;
            prev = position;
            position = position.link;
            return item;
        }

        /**
         * Gets the item stored in the node at the current position.
         * @return The data stored in the current node.
         * @throws NoSuchElementException If there is no node at the
         * current position, either because the iterator is at the end
         * position or the linked list is empty.
         */
        public T get() {
            if (position == null) 
                throw new NoSuchElementException("No node here.");
            else
                return position.value;
        }

        /**
         * Removes the node at the current position.
         * After removal, the iterator will be at the node that was
         * originally the next node to the removed one.
         * @return The data stored in the removed node.
         * @throws NoSuchElementException If there is no node to remove.
         */
        public T removeNode() {
            if (position == null)
                throw new NoSuchElementException("No node to remove.");
            T item = position.value;
            if (position == head)
                head = position.link;
            else
                prev.link = position.link;
            position = position.link;
            return item;
        }

        /**
         * Adds a node at the current position that stores the given data.
         * The new node will be the previous node of the node that was
         * originally at the current position.
         * @param item The data to be stored in the new node.
         */
        public void addNode(T item) {
            Node<T> newNode = new Node<>(item, null);
            newNode.link = position;
            if (position == head)
                head = newNode;
            else
                prev.link = newNode;
            position = newNode;
        }

        /**
         * Checks if the iterator can move to the next position.
         * @return <code>true</code> if there is a next position;
         * <code>false</code> if the iterator is at the end position
         * or the linked list is empty.
         */
        public boolean hasNext() {
            return position != null;
        }
    }    // End of iterator class


    /** The first node in the linked list. */
    private Node<T> head;

    /** Constructs an empty linked list that is innitially empty. */
    public LinkedList() {
        head = null;
    }

    /**
     * Returns an iterator for this linked list.
     * @return A new iterator for the list.
     */
    public LinkedListIterator iterator() {
        return new LinkedListIterator();
    }

    /**
     * Makes a copy of this linked list.
     * Items stored in the nodes in the two lists are identical. 
     * The copy is thus not a truly deep copy.
     * @return The copy of the linked list.
     */
    public LinkedList<T> copy() {
        LinkedList<T> copyList = new LinkedList<>();
        Object[] items = toArray();
        for (int i = items.length - 1; i >= 0; i--) {
            @SuppressWarnings("unchecked")
            T item = (T) items[i];
            copyList.add(item);
        }
        return copyList;
    }

    /** Removes all nodes in the list and make it empty. */
    public void clear() {
        head = null;
    }

    /**
     * Adds a node to the <b>start</b> of this linked list with
     * the given data.
     * @param item The data to be stored in the node.
     */
    public void add(T item) {
        Node<T> newNode = new Node<>(item, null);
        if (head == null)
            head = newNode;
        else {
            newNode.link = head;
            head = newNode;
        }
    }

    /**
     * Removes the first of the nodes storing the given data.
     * @param item The data stored in the node.
     * @return <code>true</code> if the node is deleted,
     * <code>false</code> if the node is not found or cannot be deleted.
     */
    public boolean remove(T item) {
        if (head == null)
            return false;
        LinkedListIterator itr = iterator();
        while (itr.hasNext()) {
            if (itr.get().equals(item)) {
                itr.removeNode();
                return true;
            }
            itr.next();
        }
        return false;
    }

    /**
     * Returns the size of this linked list.
     * @return The total number of nodes in the list.
     */
    public int size() {
        int count = 0;
        Node<T> current = head;
        while (current != null) {
            count++;
            current = current.link;
        }
        return count;
    }

    /**
     * Checks if the given item is in this linked list.
     * @param item The item to be found.
     * @return <code>true</code> if the item is in the linked list,
     * <code>false</code> otherwise.
     */
    public boolean contains(T item) {
        if (head == null)
            return false;
        Node<T> current = head;
        while (current != null) {
            if (current.value.equals(item))
                return true;
            current = current.link;
        }
        return false;
    }

    /**
     * Checks if this linked list is empty.
     * @return <code>true</code> if the list is empty;
     * <code>false</code> otherwise.
     */
    public boolean isEmpty() {
        return head == null;
    }

    /**
     * Returns an array that contains all the data stored in
     * this linked list.
     * Each element of the array is an object stored in a node 
     * at the corresponding position in the linked list,
     * typecasted into an {@link Object} type.
     * @return An Object array coverted from this linked list.
     */
    public Object[] toArray() {
        Object[] arr = new Object[size()];
        int i = 0;
        Node<T> current = head;
        while (current != null) {
            arr[i] = current.value;
            i++;
            current = current.link;
        }
        return arr;
    }

    /**
     * Returns a string representation of this linked list.
     * If a human-readable representation is desired, 
     * the data stored in the nodes must be of a type that
     * has its own well-defined <code>toString()</code> method.
     * @return A string representation of the list.
     */
    @Override
    public String toString() {
        Node<T> current = head;
        String s = "";
        while (current != null) {
            s += current.value == null ? "<null>" : current.value.toString();
            s += " ";
            current = current.link;
        } 
        return s;
    }

    /**
     * Checks if the given object is equal to this linked list.
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
            LinkedList<T> otherList = (LinkedList<T>) other;
            if (size() != otherList.size())
                return false;
            Node<T> pos1 = head, pos2 = otherList.head;
            while (pos1 != null) {
                if (pos1.value == null) {
                    if (pos2.value != null) 
                        return false;
                } else {
                    if (!pos1.value.equals(pos2.value))
                        return false;
                }
                pos1 = pos1.link;
                pos2 = pos2.link;
            }
            return true;
        }
    }
}
