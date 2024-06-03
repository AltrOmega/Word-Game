package WordGameInternals;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.function.Function;
import java.util.function.Predicate;

final public class Batch {
    private List<Line> lineList;
    private int lineIndex = 0;

    Batch(List<Line> lineList){ this.lineList = lineList; }

    int line_count(){ return this.lineList.size(); }

    int getLineIndex(){ return lineIndex; };

    Line popLine(int pop_index) {
        Line pop_line = lineList.get(pop_index);
        lineList.remove(pop_index);
        if (lineIndex >= pop_index) { lineIndex--; }
        return pop_line;
    }

    Line popCurrentLine(){ return popLine(lineIndex); }

    Line getCurrentLine(){ return lineList.get(lineIndex); }

    boolean hasLinesToRepeat(){
        for (Line line : lineList){
            if (line.pass == false && line.skip == false){ return true; }
        }
        return false;
    }

    boolean hasNext() {
        //if (hasLinesToRepeat()) { return true; } // ignoring for now
        return lineIndex < lineList.size()-1;
    }

    Line getLine(int index){ return lineList.get(index); }

    Line next() {
        if (this.hasNext() == false){ throw new NoSuchElementException(); }
        if(lineIndex >= lineList.size()) lineIndex = 0;
        while (true){
            Line cl = lineList.get(lineIndex++);
            if (cl.pass == false) {
                cl.wentOver++;
                cl.pass = true;
                return cl;
            }
        }
    }



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


    @Override
    public String toString() {
        String ret = "Batch: \n";
        for( int i = 0; i < lineList.size(); i++){
            ret += String.valueOf(i+1) + ". " + lineList.get(i).toString() + "\n";
        }
        return ret;
    }

};