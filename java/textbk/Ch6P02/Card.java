/**
 * A class of cards used in the memory matching game.
 * Each card has two sides: one side shows the value of this card
 * and the other displays a common back card.
 * An object of <code>Card</code> records both its value and its face
 * (whether facing up or down).
 * @author yomishino
 * @version 1.0
 */
public class Card {
    /** The value of the card. */
    private char value;
    /** A boolean indicating whether the card is facing up. */
    private boolean faceup;
    /** A <code>char</code> representing the back of all cards. */
    private static final char BACK = '*';

    /**
     * Constructs a <code>Card</code> object with a given value.
     * @param value A <code>char</code> of the value of the card.
     */
    public Card(char value) {
        this.value = value;
        turnFaceDown();
    }

    /**
     * Constructs a <code>Card</code> object from another
     * <code>Card</code> object.
     * This method is essentially a copy constructor that makes
     * a deep copy of the original card.
     * @param c A <code>Card</code> object from which a new
     * <code>Card</code> object is to be constructed.
     */
    public Card(Card c) {
        this(c.value);
    }

    /**
     * Gets the value of the card.
     * @return A <code>char</code> of the value of the card.
     */
    public char getValue() {
        return value;
    }

    /**
     * Gets the char according to whether the card is facing up
     * or down: if up, the value; otherwise the back.
     * @return A <code>char</code> of the face of the card.
     */
    public char getShownFace() {
        return faceup ? value : BACK;
    }

    /**
     * Checks if the card is facing up.
     * @return A <code>boolean</code> indicating facing up or not.
     */
    public boolean isFacingUp() {
        return faceup;
    }

    /**
     * Turns the card face up.
     */
    public void turnFaceUp() {
        faceup = true;
    }

    /**
     * Turns the card face down.
     */
    public void turnFaceDown() {
        faceup = false;
    }

    /**
     * Turns the card face up if it is facing down;
     * otherwise turns it face down.
     */
    public void reverseFace() {
        faceup = !faceup;
    }

    /**
     * Checks if the given object is equal to the card.
     * That is, if the object is a <code>Card</code> and has
     * the same value as this <code>Card</code>, regardless
     * of whether it is facing up or down.
     * @param other An object to be compared with this 
     * <code>Card</code>.
     */
    @Override
    public boolean equals(Object other) {
        if (other == null) return false;
        else if (getClass() != other.getClass()) return false;
        else return equals((Card) other);
    }

    /**
     * Checks if the two cards have the same value,
     * regardless of their facing up or down.
     * @param other A <code>Card</code> representing
     * another card.
     */
    public boolean equals(Card other) {
        return other != null && value == other.value;
    }
}