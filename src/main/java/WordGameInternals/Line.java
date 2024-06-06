package WordGameInternals;

import java.util.Objects;

/**
 * Represents a line in the word game with additional attributes for game mechanics.
 *
 * <p>The class fields are as follows:</p>
 * <ul>
 *   <li>{@code left} and {@code right} are the strings containing words.</li>
 *   <li>{@code split} is the string that splits a line into two parts, positioned between {@code left} and {@code right} strings.</li>
 *   <li>{@code wentOver} represents how many times this line has been viewed by the user.</li>
 *   <li>{@code mistake} is the total number of mistakes made by the user across all instances of viewing this line.</li>
 *   <li>{@code pass} indicates whether this line should be ignored for the purpose of repeating.</li>
 *   <li>{@code skip} indicates whether this line should never be shown again in the current game.</li>
 * </ul>
 */
public class Line {
    String left;
    String right;
    String split;
    int wentOver = 0;
    int mistake = 0;
    boolean pass = false;
    boolean skip = false;

    /**
     * Constructs a Line with specified left, right, and split strings.
     *
     * @param left  the left part of the line
     * @param right the right part of the line
     * @param split the split string separating left and right parts
     */
    Line(String left, String right, String split) {
        this.left = left;
        this.right = right;
        this.split = split;
    }



    /**
     * Compares this Line to the specified object for partial equality.
     * Only compares the left, right, and split fields.
     *
     * @param obj the object to compare to
     * @return true if the specified object is equal to this Line, false otherwise
     */
    public boolean partialEquals(Object obj) {
        if (obj == this)
            return true;
        if (!(obj instanceof Line))
            return false;

        Line l = (Line) obj;
        return (
                l.left.equals(left) &&
                        l.right.equals(right) &&
                        l.split.equals(split)
        );
    }



    /**
     * Compares this Line to the specified object for full equality.
     * Compares all fields including wentOver, mistake, pass, and skip.
     *
     * @param obj the object to compare to
     * @return true if the specified object is equal to this Line, false otherwise
     */
    @Override
    public boolean equals(Object obj) {
        if (partialEquals(obj) == false) return false;

        Line l = (Line) obj;
        return (
                l.wentOver == wentOver &&
                        l.mistake == mistake &&
                        l.pass == pass &&
                        l.skip == skip
        );
    }



    @Override
    public int hashCode() {
        return Objects.hash(left, right, split, wentOver, mistake, pass, skip);
    }

    @Override
    public String toString() {
        return "Line: " + left + split + right;
    }
}
