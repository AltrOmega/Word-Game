package WordGameInternals;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

class GameEngineTest {

    List<Line> fiveQuestionSetup(){
        return List.of(
            new Line("jeden", "one", " - "),
            new Line("dwa", "two", " - "),
            new Line("trzy", "three", " - "),
            new Line("cztery", "four", " - "),
            new Line("pięć", "five", " - ")
        );
    }

    @Test
    void SomeTest() {
        GameEngine ge = new GameEngine();


    }


    // Not imprtant - skipping for now
    @Test
    void getGameState() {
    }

    @Test
    void updateCurrentQuestionSide() {
        GameEngine ge = new GameEngine();
        ge.gameSettings.fromSide.setValue(Side.LEFT);

        ge.updateCurrentQuestionSide();
        Assertions.assertEquals(Side.LEFT, ge.currentQuestionSide);


        ge.gameSettings.fromSide.setValue(Side.RIGHT);
        ge.updateCurrentQuestionSide();
        Assertions.assertEquals(Side.RIGHT, ge.currentQuestionSide);


        ge.gameSettings.fromSide.setValue(Side.RANDOM);
        ge.updateCurrentQuestionSide();
        Assertions.assertNotEquals(Side.RANDOM, ge.currentQuestionSide);
    }

    @Test
    void getCurrentQuestionSideStr() {
        GameEngine ge = new GameEngine();
        ge.newGameFromLineList(List.of(new Line("left", "right", " - ")));

        ge.gameSettings.fromSide.setValue(Side.LEFT);
        Assertions.assertEquals("left", ge.getCurrentQuestionSideStr());

        ge.gameSettings.fromSide.setValue(Side.RIGHT);
        Assertions.assertEquals("right", ge.getCurrentQuestionSideStr());
    }

    @Test
    // error on get curr answ TODO: fix
    void getCurrentAnswerSideStr() {
        GameEngine ge = new GameEngine();
        ge.newGameFromLineList(List.of(new Line("left", "right", " - ")));

        ge.gameSettings.fromSide.setValue(Side.LEFT);
        Assertions.assertEquals("right", ge.getCurrentAnswerSideStr());

        ge.gameSettings.fromSide.setValue(Side.RIGHT);
        Assertions.assertEquals("left", ge.getCurrentAnswerSideStr());
    }

    GameEngine mistakeTestSetup(){
        List<Line> ll = fiveQuestionSetup();
        ll.get(0).mistake = 5;
        ll.get(1).mistake = 0;
        ll.get(2).mistake = 1;
        ll.get(3).mistake = 2;
        ll.get(4).mistake = 0;

        GameEngine ge = new GameEngine();
        ge.newGameFromLineList(ll);
        return ge;
    }

    @Test
    void getUniqueMistakeCount() {
        GameEngine ge = mistakeTestSetup();
        Assertions.assertEquals(3, ge.getUniqueMistakeCount());
    }

    @Test
    void getTotalMistakeCount() {
        GameEngine ge = mistakeTestSetup();
        Assertions.assertEquals(8, ge.getTotalMistakeCount());
    }

    @Test
    void getCorrectPercent() {
        GameEngine ge = mistakeTestSetup();
        Assertions.assertEquals(40, ge.getCorrectPercent());
    }

    @Test
    void getQuestionStage(){
        GameEngine ge = new GameEngine();
        ge.newGameFromLineList(List.of(new Line("left", "right", " - ")));
        ge.gameSettings.fromSide.setValue(Side.LEFT);

        Assertions.assertEquals("left - ", ge.getQuestionStage(" - "));
    }

    @Test
    void getAnswerStage(){
        GameEngine ge = new GameEngine();
        ge.newGameFromLineList(List.of(new Line("left", "right", " - ")));
        ge.gameSettings.fromSide.setValue(Side.LEFT);

        Assertions.assertEquals("left - right", ge.getAnswerStage(" - "));
    }

    //TODO: Test each if's in nextQuestion and make sure it cant throw,
    // then delete throw and optimise ifs
    @Test
    void nextQuestion() {
    }

    @Test
    void submitAnswer() {
        List<Line> ll = fiveQuestionSetup();
        GameEngine ge = new GameEngine();
        ge.gameSettings.fromSide.setValue(Side.LEFT);
        // Typing mode: On
        ge.gameSettings.typingMode.setValue(true);
        ge.newGameFromLineList(ll);

        ge.submitAnswer("one");
        Assertions.assertEquals(0, ge.getUniqueMistakeCount());

        Assertions.assertThrows(IllegalStateException.class, () -> {ge.submitAnswer("");});
        ge.nextQuestion();
        ge.submitAnswer("not two");
        Assertions.assertEquals(1, ge.getUniqueMistakeCount());

        // Typing mode: Off
        ge.gameSettings.typingMode.setValue(false);
        ll = fiveQuestionSetup();
        ge.newGameFromLineList(ll);

        ge.submitAnswer("");// one
        Assertions.assertEquals(0, ge.getUniqueMistakeCount());

        Assertions.assertThrows(IllegalStateException.class, () -> {ge.submitAnswer("");});
        ge.nextQuestion();
        ge.submitAnswer("f");// not two
        Assertions.assertEquals(1, ge.getUniqueMistakeCount());

    }

    @Test   // Not fully written yet
    void makeRepeat() {
    }

    @Test   // No need to test this
    void makeMistake() {}

    // If no other previous test failed this works properly so no need to test
    @Test
    void newGameFromLineList() {
    }

    // This one will need testing but  TODO: will do it some other day
    @Test
    void newGameFromFileList() {
    }
}