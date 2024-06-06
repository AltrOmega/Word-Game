package WordGameInternals;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Collections;



final public class Batch {
    private List<Line> lineList;
    private int lineIndex = 0;

    /**
     * Constructs a Batch object with the specified list of lines.
     * @param lineList The list of lines to initialize the Batch.
     */
    Batch(List<Line> lineList){
        this.lineList = lineList;
    }



    /**
     * Returns the count of lines in the Batch.
     * @return The number of lines in the Batch.
     */
    int line_count(){
        return this.lineList.size();
    }



    /**
     * Returns the current index of the line in the Batch.
     * @return The current line index.
     */
    int getLineIndex(){
        return lineIndex;
    }



    /**
     * Removes and returns the line at the specified index from the Batch.
     * @param pop_index The index of the line to be removed.
     * @return The removed line.
     */
    Line popLine(int pop_index) {
        Line pop_line = lineList.get(pop_index);
        lineList.remove(pop_index);
        if (lineIndex >= pop_index) {
            lineIndex--;
        }
        return pop_line;
    }



    /**
     * Removes and returns the current line from the Batch.
     * @return The removed current line.
     */
    Line popCurrentLine(){
        return popLine(lineIndex);
    }



    /**
     * Returns the current line from the Batch.
     * @return The current line.
     */
    Line getCurrentLine(){
        return lineList.get(lineIndex);
    }



    /**
     * Checks if there are lines to repeat in the Batch.
     * @return true if there are lines to repeat, false otherwise.
     */
    boolean hasLinesToRepeat(){
        for (Line line : lineList){
            if (!line.pass && !line.skip){
                return true;
            }
        }
        return false;
    }



    /**
     * Checks if there is a next line in the Batch.
     * @return true if there is a next line, false otherwise.
     */
    boolean hasNext() {
        //if (hasLinesToRepeat()) { return true; } // ignoring for now
        return lineIndex < lineList.size()-1;
    }



    /**
     * Returns the line at the specified index in the Batch.
     * @param index The index of the line to retrieve.
     * @return The line at the specified index.
     */
    Line getLine(int index){
        return lineList.get(index);
    }



    /**
     * Returns the next line in the Batch, iterating over the lines.
     * @return The next line.
     * @throws NoSuchElementException if there is no next line.
     */
    Line next() {
        if (!this.hasNext()){
            throw new NoSuchElementException();
        }
        if(lineIndex >= lineList.size()) lineIndex = 0;
        while (true){
            Line cl = lineList.get(lineIndex++);
            if (!cl.pass) {
                cl.wentOver++;
                cl.pass = true;
                return cl;
            }
        }
    }



    /**
     * Compares this Batch to the specified object for equality.
     * @param obj The object to compare this Batch against.
     * @return true if the specified object is equal to this Batch, false otherwise.
     */
    @Override
    public boolean equals(Object obj) {
        if( obj == this)
            return true;
        if( !( obj instanceof Batch) )
            return false;

        Batch b = (Batch) obj;
        if ( b.lineList.size() != lineList.size() )
            return false;

        for( int i = 0; i < b.lineList.size(); i++){
            var l = b.lineList.get(i);
            var line = lineList.get(i);
            if( !( l.equals(line) ) ) return false;
        }
        return true;
    }



    /**
     * Partially compares this Batch to the specified object for equality.
     * This Batch is considered partially equal if all of the lines it contains are partially equal
     * to those in the compared Batch.
     * @param obj The object to compare this Batch against.
     * @return true if the specified object is partially equal to this Batch, false otherwise.
     */
    public boolean partialEquals(Object obj) {
        if( obj == this)
            return true;
        if( !( obj instanceof Batch) )
            return false;

        Batch b = (Batch) obj;
        if ( b.lineList.size() != lineList.size() )
            return false;

        for( int i = 0; i < b.lineList.size(); i++){
            var l = b.lineList.get(i);
            var line = lineList.get(i);
            if( !( l.partialEquals(line) ) ) return false;
        }
        return true;
    }



    /**
     * Shuffles the lines in the Batch and returns a new Batch with the shuffled lines.
     * @return A new Batch with shuffled lines.
     */
    Batch shuffle(){
        List<Line> shuffledLineList = this.lineList;
        Collections.shuffle(shuffledLineList);
        return new Batch( shuffledLineList );
    }



    /**
     * Returns a string representation of the Batch.
     * @return A string representation of the Batch.
     */
    @Override
    public String toString() {
        String ret = "Batch: \n";
        for( int i = 0; i < lineList.size(); i++){
            ret += String.valueOf(i+1) + ". " + lineList.get(i).toString() + "\n";
        }
        return ret;
    }
}
