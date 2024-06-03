package WordGameInternals;
import java.util.Objects;

public class Line {
    String left;
    String right;
    String split;
    int wentOver = 0;
    int mistake = 0;
    boolean pass = false;
    boolean skip = false;

    Line(String left, String right, String split){
        this.left = left;
        this.right = right;
        this.split = split;
    }

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

    @Override
    public boolean equals(Object obj) {
        if( partialEquals(obj) == false ) return false;

        Line l = (Line)obj;
        return (
            l.wentOver == wentOver &&
            l.mistake == mistake &&
            l.pass == pass &&
            l.skip == skip
        );
    }

    @Override
    public int hashCode() {
        return ( Objects.hash( left, right, split, wentOver, mistake, pass, skip ));
    }

    @Override
    public String toString() {
        return "Line: " + left + split + right;
    }
}
