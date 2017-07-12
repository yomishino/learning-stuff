/**
 * A generic class of circular linked lists.
 * It is a linked list in which each node refers to another node,
 * and the last node in the list refers back to the first node,
 * so that the list can be walked through in a circular manner.
 * @author yomishino
 * @version 1.0
 */
public class CircularLinkedList<T> {
    
    /**
     * A class of nodes, each of which contains a reference to 
     * another node.
     * For use in the <code>CircularLinkedList<T></code>.
     */
    private class Node<T> {
        private T data;
        private Node<T> link;

        private Node() {
            data = null;
            link = null;
        }

        private Node(T data, Node<T> link) {
            this.data = data;
            this.link = link;
        }
    }   // End of node class


    /**
     * An iterator class of a circular linked list.
     */
    public class CircularLinkedListIterator {
        /** The current position in the list. */
        private Node<T> position;
        /** The previous position in the list. */
        private Node<T> prev;

        /**
         * Constructs an iterator for the circular linked list.
         */
        public CircularLinkedListIterator() {
            reset();
        }

        /**
         * Moves to the next node in the list.
         * @return The item stored in the previous node.
         */
        public T next() {
            if (position == null)
                throw new NullPointerException("The list is empty.");
            T item = position.data;
            prev = position;
            position = position.link;
            return item;
        }

        /**
         * Resets the iterator so that it begins at the first node in
         * the list.
         */
        public void reset() {
            position = head;
            setPrevOfHead();
        }

        /**
         * Removes the node at the current position in the list.
         * @return The item stored in the removed node.
         */
        public T removeNode() {
            if (position == null)
                throw new NullPointerException("The list is empty.");
            T item = position.data;
            if (position == head)
                head = position.link;
            position = position.link;
            prev.link = position;
            return item;
        }

        /**
         * Sets the {@link #prev} to be the previous node of the head node.
         */
        private void setPrevOfHead() {
            if (head == null)
                prev = null;
            else {
                Node<T> current = head;
                while (current.link != head) 
                    current = current.link;
                prev = current;
            }
        }
    }   // End of iterator class


    /** The first node in this list; nominal since the list is circular. */
    private Node<T> head;

    /**
     * Constructs a circular linked list that is initially empty.
     */
    public CircularLinkedList() {
        head = null; 
    }

    /**
     * Returns an iterator for the list.
     * @return The iterator for this list.
     */
    public CircularLinkedListIterator iterator() {
        return new CircularLinkedListIterator();
    }

    /**
     * Appends a node to the <b>end</b> of the list.
     * @param item The item to be stored in the node.
     */
    public void append(T item) {
        Node<T> newNode = new Node<T>(item, null);
        if (head == null) {
            head = newNode;
        }
        else {
            Node<T> current = head;
            while (current.link != head)
                current = current.link;
            // ``current`` is the currently last node
            current.link = newNode;
        }
        newNode.link = head;
    }

    /**
     * Clears the list, making it empty.
     */
    public void clear() {
        head = null;
    }

    /**
     * Returns the size of this list.
     * @return The total number of the nodes in the list.
     */
    public int size() {
        if (head == null)
            return 0;
        int count = 0;
        Node<T> current = head;
        do {
            count++;
            current = current.link;
        } while (current != head);
        return count;
    }

    /**
     * Checks if the list is empty.
     * @return A <code>boolean</code> indicating if the list is empty.
     */
    public boolean isEmpty() {
        return head == null;
    }

    /**
     * Returns a string representation of the circular linked list.
     * Requires the class of the items stored in the nodes to have
     * a suitably defined <code>toString()</code> method in order to
     * give a human-readable string representation.
     * @return The string representation.
     */
    @Override
    public String toString() {
        if (isEmpty())
            return "";
        Node<T> current = head;
        String s = "";
        do {
            s += current.data == null ? "<null>" : current.data.toString();
            s += " ";
            current = current.link;
        } while (current != head);
        return s; 
    }

    /**
     * Checks if the other object is equal to this object.
     * Requires the data type of the item stored in the node has a 
     * well-defined <code>equals</code> method.
     * @param other An object to be checked.
     * @return A <code>boolean</code> indicating whether the other
     * object is equal to this object.
     */
    @Override
    public boolean equals(Object other) {
        if (other == null)
            return false;
        else if (getClass() != other.getClass())
            return false;
        else {
            @SuppressWarnings("unchecked")
            CircularLinkedList<T> otherList = (CircularLinkedList<T>) other;
            if (size() != otherList.size())
                return false;
            if (isEmpty() && otherList.isEmpty())
                return true;
            Node<T> pos1 = head, pos2 = otherList.head;
            do {
                if (pos1.data == null) {
                    if (pos2.data != null)
                        return false;
                }
                else if (!pos1.data.equals(pos2.data))
                    return false;
                pos1 = pos1.link;
                pos2 = pos2.link;
            } while (pos1 != head);
            return true;
        }            
    }

    private static void test1() {
        CircularLinkedList<String> l1 = new CircularLinkedList<>();
        l1.append("apple");
        l1.append("banana");
        System.out.println("List => " + l1);

        CircularLinkedList<String>.CircularLinkedListIterator itr1
            = l1.iterator();
        System.out.println("Iterator ...");
        System.out.print(itr1.next()+ " ");
        System.out.println(itr1.next());
        System.out.println();

        l1.append("cherry");
        l1.append("durian");
        System.out.println("List => " + l1);
        itr1.reset();
        System.out.println("Delete the 2nd element");
        for (int i = 1; i < 2; i++)
            itr1.next();
        System.out.println("Removed: " + itr1.removeNode());
        System.out.println("List => " + l1);
        System.out.println();

        l1.append("eggfruit");
        System.out.println("List => " + l1);
        System.out.println("Delete the 1st element");
        itr1.reset();
        System.out.println("Removed: " + itr1.removeNode());
        System.out.println("List => " + l1);
        System.out.println("Iterator ...");
        itr1.reset();
        for (int i = 0; i < l1.size(); i++)
            System.out.print(itr1.next() + " ");
        System.out.println(" ... and returns to " + itr1.next());
        System.out.println();

        System.out.println("List => " + l1);
        System.out.println("Delete the last element");
        itr1.reset();
        for (int i = 1; i < l1.size(); i++)
            itr1.next();
        System.out.println("Removed: " + itr1.removeNode());
        System.out.println("List => " + l1);
        System.out.println("Iterator ...");
        itr1.reset();
        for (int i = 0; i < l1.size(); i++)
            System.out.print(itr1.next() + " ");
        System.out.println(" ... and returns to " + itr1.next());
        System.out.println();
    }


    public static void main(String[] args) {
        test1();
    }
}
