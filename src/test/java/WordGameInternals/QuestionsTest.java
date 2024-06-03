package WordGameInternals;

import org.junit.jupiter.api.*;

import java.util.List;
import java.util.NoSuchElementException;

class QuestionsTest {

    static List<Line> LINES_LIST_1 = List.of(
            new Line("jeden", "one", " - "),
            new Line("dwa", "two", " - "),
            new Line("trzy", "three", " - "),
            new Line("cztery", "four", " - "),
            new Line("pięć", "five", " - "),
            new Line("sześć", "six", " - "),
            new Line("siedem", "seven", " - "),
            new Line("osiem", "eight", " - "),
            new Line("dziewięć", "nine", " - "),
            new Line("dziesięć", "ten", " - "),
            new Line("jedenaście", "eleven", " - "),
            new Line("dwanaście", "twelve", " - "),
            new Line("trzynaście", "thirteen", " - "),
            new Line("czternaście", "fourteen", " - "),
            new Line("piętnaście", "fifteen", " - "),
            new Line("szesnaście", "sixteen", " - "),
            new Line("siedemnaście", "seventeen", " - "),
            new Line("osiemnaście", "eighteen", " - "),
            new Line("dziewiętnaście", "nineteen", " - "),
            new Line("dwadzieścia", "twenty", " - ")
    );

    @Test
    void fromFiles() {
    }

    @Test
    @DisplayName("Verify getLineCount returns appropriate amount of Lines in Questions")
    void getLineCount() {
        Questions questions_1 = new Questions(LINES_LIST_1, 5);
        Questions questions_2 = new Questions(LINES_LIST_1, 3);
        Questions questions_3 = new Questions(LINES_LIST_1, 30);
        Questions questions_4 = new Questions(LINES_LIST_1, 0);
        Questions questions_5 = new Questions(LINES_LIST_1, -1);

        Assertions.assertEquals(20, questions_1.calcLineCount());
        Assertions.assertEquals(20, questions_2.calcLineCount());
        Assertions.assertEquals(20, questions_3.calcLineCount());
        Assertions.assertEquals(20, questions_4.calcLineCount());
        Assertions.assertEquals(20, questions_5.calcLineCount());
    }

    @Test
    @DisplayName("Verify getBatchCount returns the appropriate amount of batches")
    void getBatchCount() {
        Questions questions_1 = new Questions(LINES_LIST_1, 5);
        Questions questions_2 = new Questions(LINES_LIST_1, 3);
        Questions questions_3 = new Questions(LINES_LIST_1, 30);
        Questions questions_4 = new Questions(LINES_LIST_1, 0);
        Questions questions_5 = new Questions(LINES_LIST_1, -1);

        Assertions.assertEquals(4, questions_1.getBatchCount());
        Assertions.assertEquals(7, questions_2.getBatchCount());
        Assertions.assertEquals(20, questions_3.calcLineCount());
        Assertions.assertEquals(20, questions_4.calcLineCount());
        Assertions.assertEquals(20, questions_5.calcLineCount());
    }

    @Test
    @DisplayName("Ensure hasNext correctly reports the existence of the next batch")
    void hasNext() {
        Questions questions = new Questions(LINES_LIST_1, 10);

        Assertions.assertEquals(true, questions.hasNext()); // Line: 1-10/20, Batch 1/2
        questions.next();

        Assertions.assertEquals(false, questions.hasNext());  // Line: 11-20/20, Batch 2/2


    }

    @Test
    @DisplayName("Verify that .next() increments batchIndex correctly and throws a " +
                 "NoSuchElementException at the end")
    void next() {
        Questions questions = new Questions(LINES_LIST_1, 10);

        Assertions.assertEquals(0, questions.getBatchIndex()); // Line: 1-10/20, Batch 1/2
        questions.next();

        Assertions.assertEquals(1, questions.getBatchIndex());  // Line: 11-20/20, Batch 2/2

        Assertions.assertThrows(NoSuchElementException.class, () -> questions.next()); // End of lines
    }

    @Test
    void getBatch() { // Basic Test
        Questions questions = new Questions(LINES_LIST_1, 1);

        //System.out.println( new Questions(LINES_LIST_1, 5) );

        Assertions.assertEquals(
                new Batch( List.of(new Line("jeden", "one", " - ")) ),
                questions.getBatch(0)
        );
    }

    @Test
    void getCurrentBatch() { // Basic Test
        Questions questions = new Questions(LINES_LIST_1, 1);

        Assertions.assertEquals(
                new Batch( List.of(new Line("jeden", "one", " - ")) ),
                questions.getCurrentBatch()
        );
    }

    @Test
    void getCurrentLine() { // Basic Test
        Questions questions = new Questions(LINES_LIST_1, 1);

        Assertions.assertEquals(
                new Line("jeden", "one", " - "),
                questions.getCurrentLine()
        );
    }

    // Pretty much the same as next() test, might want to think of something better
    @Test
    void getBatchIndex() {
        Questions questions = new Questions(LINES_LIST_1, 5);

        Assertions.assertEquals(0, questions.getBatchIndex());
        questions.next();

        Assertions.assertEquals(2, questions.getBatchIndex());
        questions.next();

        Assertions.assertEquals(3, questions.getBatchIndex());

    }

    @Test
    void list_index_of() {
        String text = "The quick brown dog jumps over another quick dog. The dog is not lazy";

        Assertions.assertEquals(List.of(4, 39), Questions.list_index_of(text, "quick"));
        Assertions.assertEquals(List.of(0, 50), Questions.list_index_of(text, "The"));
        Assertions.assertEquals(List.of(16, 45, 54), Questions.list_index_of(text, "dog"));
    }

    @Test
    //Tests if remove comments works with different multi line comments
    void removeComments_1() {// TODO: test its working with mulitple of the same comment
        String text =
            "jeden -/* foo */ one\n" +
            "dwa - two\"\"\" ba\n" +
            "r\"\"\"trzy - three\n" +
            "cztery - four# yee\n" +
            "pięć - five\n";


        String tartgetText_1 =
            "jeden - one\n" +
            "dwa - two\"\"\" ba\n" +
            "r\"\"\"trzy - three\n" +
            "cztery - four# yee\n" +
            "pięć - five\n";

        String tartgetText_2 =
            "jeden - one\n" +
            "dwa - two\n" +
            "trzy - three\n" +
            "cztery - four# yee\n" +
            "pięć - five\n";

        String cleanText =
            "jeden - one\n" +
            "dwa - two\n" +
            "trzy - three\n" +
            "cztery - four\n" +
            "pięć - five\n";

        Assertions.assertEquals(tartgetText_1, Questions.removeComments(text, "/*", "*/"));
        Assertions.assertEquals(tartgetText_2, Questions.removeComments(tartgetText_1, "\"\"\"", "\"\"\""));
        Assertions.assertEquals(cleanText, Questions.removeComments(tartgetText_2, "#", "\n"));
    }

    @Test
    //Tests if remove comments works with one comment multiple times
    void removeComments_2() {
        String text =
            "jeden -/* foo */ one\n" +
            "dwa - two/* ba\n" +
            "r*/trzy - three\n" +
            "cztery - four/* yee\n" +
            "pięć - five\n*/";

        String expecterText =
            "jeden - one\n" +
            "dwa - two\n" +
            "trzy - three\n" +
            "cztery - four\n";

        Assertions.assertEquals(expecterText, Questions.removeComments(text, "/*", "*/"));
    }

    @Test
    void first_string_no_escape() {
    }

    @Test
    void removeEscapeFromSubstring() {
    }

    @Test
    void parseRawTxt() {
        String text =
            "jeden - one\n" +
            "dwa - two\n" +
            "trzy - three\n" +
            "cztery - four\n" +
            "pięć - five\n";

        List<Line> LINES_LIST_2 = List.of(
            new Line("jeden", "one", " - "),
            new Line("dwa", "two", " - "),
            new Line("trzy", "three", " - "),
            new Line("cztery", "four", " - "),
            new Line("pięć", "five", " - ")
        );

        GameEngine ge = new GameEngine();
        Assertions.assertEquals(LINES_LIST_2, Questions.parseRawTxt(text,
            ge.gameSettings.splits.getValue(), ge.gameSettings.slComments.getValue(),
            ge.gameSettings.mlComments.getValue())
        );

    }


}