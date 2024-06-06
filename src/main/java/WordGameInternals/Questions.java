package WordGameInternals;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;

import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.Collections;


final class Questions {
    private List<Batch> batchList = new ArrayList<>();
    private int batchIndex = 0;


//------------------------------ Constructors
    /**
    * Constructs a Questions object from a list of Line objects, organizing them into batches.
    * Optionally shuffles the batches and lines within batches.
    *
    * @param lineList the list of Line objects to be organized into batches.
    * @param batchSize the size of each batch.
    * @param shuffle_batches if true, shuffles the batches.
    * @param shuffle_lines if true, shuffles the lines within each batch.
    */
    Questions(List<Line> lineList, int batchSize, boolean shuffle_batches, boolean shuffle_lines){
        if (batchSize > 0){
            for( int i = 0; i< lineList.size(); i+= batchSize){
                batchList.add( new Batch(
                    lineList.subList(i, Math.min(i+batchSize, lineList.size()))
                ));
            }
        } else { batchList.add(new Batch(lineList)); }

        if (shuffle_batches){
            Collections.shuffle( batchList );
        }
        if (shuffle_lines){
            for (int i = 0; i < batchList.size(); i++){
                batchList.set(i, getBatch(i).shuffle() );
            }
        }
    }



    /**
     * Creates a Questions object from a list of file paths, parsing each file into lines,
     * and organizing them into batches.
     *
     * @param filePaths the list of file paths to read and parse.
     * @param batchSize the size of each batch.
     * @param splits the list of strings used to split lines into two parts.
     * @param singleLineComments the list of single-line comment start sequences.
     * @param multiLineComments the list of multi-line comment start and end sequences.
     * @param shuffle_batches if true, shuffles the batches.
     * @param shuffle_lines if true, shuffles the lines within each batch.
     * @return Questions, the created Questions object.
     */
    // make filePaths an args?
    static Questions fromFiles(List<String> filePaths, int batchSize, List<String> splits,
                               List<String> singleLineComments, List<List<String>> multiLineComments,
                               boolean shuffle_batches, boolean shuffle_lines) {
        List<Line> lineList = new ArrayList<>();
        String lines = "";
        for (String file_path : filePaths) {
            try {
                lines += new String(Files.readAllBytes(Paths.get(file_path)), StandardCharsets.UTF_8);

            } catch (FileNotFoundException e){
                System.out.println("Could not find the file: " + file_path);
                System.out.println("Skipping it in the loading process");
                continue;

            } catch (IOException e){
                System.out.println("IO Exception occurred on file: " + file_path);
                System.out.println("Skipping it in the loading process");
            }

            lineList.addAll(parseRawTxt(lines, splits, singleLineComments,
                    multiLineComments));
        }
        return new Questions(lineList, batchSize, shuffle_batches, shuffle_lines);
    }








//------------------------------ Simple Utility
    /**
     * Calculates the total number of lines across all batches in the batch list.
     *
     * @return int The total count of lines across all batches.
     */
    // TODO: this probably should have a "calculate" prefix not "get": fix all similar instances
    int calcLineCount(){
        int count = 0;
        for(Batch batch : batchList){
            count += batch.line_count();
        }
        return count;
    }



    /**
     * Retrieves the total number of batches in the batch list.
     *
     * @return int The total count of batches in the batch list.
     */
    int getBatchCount(){ return batchList.size(); }



    /**
     * Checks if there are more elements in the batch list to process.
     *
     * @return true if more elements are available, false otherwise.
     */
    boolean hasNext() { return batchIndex < batchList.size()-1; }



    /**
     * Moves to the next element in the batch list by incrementing the batch index.
     * This method should only be called after confirming that more elements are available
     * by calling {@link #hasNext()}.
     *
     * @throws NoSuchElementException if there are no more elements to process, which occurs
     *         when {@link #hasNext()} returns false.
     */
    void next() {
        if (this.hasNext() == false){ throw new NoSuchElementException(); }
        batchIndex++;
    }



    /**
     * Retrieves a Batch obj from the batch list at the specified index.
     *
     * @param index the index of the batch to retrieve from the batch list.
     * @return the Batch obj at the specified index.
     * @throws IndexOutOfBoundsException if the index is out of range (index < 0 || index >= batchList.size()).
     */
    Batch getBatch(int index){ return (batchList.get(index)); }



    /**
     * Retrieves the current batch based on the current batch index.
     *
     * @return Batch The current batch as per the batch index.
     */


    Batch getCurrentBatch(){ return getBatch(batchIndex); }



    /**
     * Retrieves the current line from the current batch.
     *
     * @return Line The current line in the current batch.
     */
    Line getCurrentLine(){
        return getCurrentBatch().getCurrentLine();
    }



    /**
     * Returns the index of the current batch.
     *
     * @return int The index of the current batch being processed.
     */
    int getBatchIndex(){ return batchIndex; }



    /**
     * Finds all indices where the target substring begins within the given text.
     *
     * @param text The full text in which to search for the target substring.
     * @param targetSubstring The substring whose indices are to be found.
     * @return List<Integer> A list of starting indices where the substring is found within the text.
     */
    public static List<Integer> list_index_of(String text, String targetSubstring) {
        return IntStream.range(0, text.length() - targetSubstring.length() + 1)
                .filter(i -> text.startsWith(targetSubstring, i))
                .boxed()
                .collect(Collectors.toList());
    }








//------------------------------ String parsing and comments handling
    /**
     * Removes all occurrences of comments from the given string.
     *
     * @param input The string from which to remove comments.
     * @param commentStart The sequence of characters that marks the beginning of a comment.
     * @param commentEnd The sequence of characters that marks the end of a comment.
     * @return String The input string with all comments removed.
     */
    //Ignoring this for now  TODO: Finish this function
    public static String removeComments(String input, String commentStart, String commentEnd) {
        String start = Pattern.quote(commentStart);
        String end = Pattern.quote(commentEnd);

        String regex = start + ".*?\\n.*?" + end;
        String regex_2 = start + ".*?" + end;

        if (commentEnd == "\n") return input.replaceAll(regex_2, "\n");
        input = input.replaceAll(regex, "\n");
        input = input.replaceAll(regex_2, "");

        return input;
    }



    /**
     * Searches for the first occurrence of a specified substring in the given string * that is not preceded by an escape character. *
     * @param toParse The string to search.
     * @param lookFor The substring to find without an escape character before it.
     * @return int The index of the first occurrence of the substring that is not escaped, or -1 if none found.
     */
    // Ignoring for now
    static int first_string_no_escape(String toParse, String lookFor){
        int ocurence = -1;
        for (int index_of: list_index_of(toParse, lookFor)){
            if (index_of >= 1 && toParse.charAt(index_of-1) != '\\'){
                ocurence = index_of;
                break;
            }
        }
        return ocurence;
    }



    /**
     * Removes the escape character before all occurrences of a specified substring
     * within the given string.
     *
     * @param toParse The string from which to remove escapes.
     * @param lookFor The substring from which to remove preceding escape characters.
     * @return String The modified string with escapes removed before the specified substring.
     */
    // Ignoring for now
    static String removeEscapeFromSubstring(String toParse, String lookFor){
        for (int index_of: list_index_of(toParse, "\\" + lookFor)){
            toParse = toParse.substring(0, index_of) + toParse.substring(index_of+1);
        }
        return toParse;
    }



    /**
     * Parses a text file content into a list of Line objects based on specified splitting criteria,
     * while also handling single and multi-line comments.
     *
     * @param txtFileContent The full content of the text file to be parsed.
     * @param splits List of strings that are used as delimiters to split lines into two parts.
     * @param singleLineComments A list of strings, each representing a start sequence for single-line comments.
     * @param multiLineComments A list of lists, where each inner list contains two elements representing
     *                          the start and end sequences for multi-line comments.
     * @return List<Line> A list of Line objects, each containing the left and right parts split by a delimiter,
     *         and the delimiter that was used for splitting.
     */
    static List<Line> parseRawTxt(
            String txtFileContent, List<String> splits,
            List<String> singleLineComments, List<List<String>> multiLineComments) {
        List<Line> lineList = new ArrayList<>();
        String tfc = txtFileContent;
        List<String> slcs = singleLineComments;
        List<List<String>> mlcs = multiLineComments;

        for (List<String> mlc : mlcs){ // TODO: Add a mlc object for ease of use
            tfc = removeComments(tfc, mlc.get(0), mlc.get(1));
        }

        for (String slc : slcs){
            tfc = removeComments(tfc, slc, "\\n");
        }

        String[] strLines = tfc.split("\\r?\\n");

        List<String> stringList = Arrays.asList(strLines);

        for (String strLine : strLines){

            String left = null;
            String right = null;
            String split_used = null;

            for (String split : splits){
               int ocurence = first_string_no_escape(strLine, split);
               if (ocurence == -1){ continue; }

                left = strLine.substring(0, ocurence);
                right = strLine.substring(ocurence + split.length());
                split_used = split;
            }

            if (left == null){ continue; }
            for (String split : splits) {
                left = removeEscapeFromSubstring(left, split);
                right = removeEscapeFromSubstring(right, split);
            }
            lineList.add(new Line(left, right, split_used));
        }

        return lineList;
    }








//------------------------------ Overrides and similar
    /**
     * Compares this Questions to the specified object for full equality.
     *
     * @param obj the object to compare to
     * @return true if the specified object is equal to this Line, false otherwise
     */
    @Override
    public boolean equals(Object obj) {
        if( obj == this)
            return true;
        if( !( obj instanceof Questions) )
            return false;

        Questions q = (Questions) obj;
        if ( q.batchList.size() != batchList.size() )
            return false;

        for( int i = 0; i < q.batchList.size(); i++) {
            var b = q.batchList.get(i);
            var batch = batchList.get(i);
            if ( !( b.equals(batch) ) ) return false;
        }
        return true;
    }



    /**
     * Checks equality using only the most significant field of Line: left, right, split, using
     * partialEquals of each batch in batchList
     *
     * @param obj The Object to be compared
     * @return boolean Are the most significant fields of all Line's
     *                 inside this Questions Object equal to obj's?
     */
    public boolean partialEquals(Object obj) {
        if( obj == this)
            return true;
        if( !( obj instanceof Questions) )
            return false;

        Questions q = (Questions) obj;
        if ( q.batchList.size() != batchList.size() )
            return false;

        for( int i = 0; i < q.batchList.size(); i++) {
            var b = q.batchList.get(i);
            var batch = batchList.get(i);
            if ( !( b.partialEquals(batch) ) ) return false;
        }
        return true;
    }



    @Override
    public String toString() {
        String ret = "Questions: \n";
        for( int i = 0; i < batchList.size(); i++){
            ret += "--- " + String.valueOf(i+1) + ". " + batchList.get(i).toString() + "\n";
        }
        return ret;
    }
}
