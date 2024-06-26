package WordGameInternals;

import java.util.List;

public final class GameEngine { // TODO: Make it a Singleton
    Questions questions;
    public GameSettings gameSettings = new GameSettings();
    private GameState gameState = GameState.NOT_INITIALIZED;

    Side currentQuestionSide = Side.RANDOM;
    private boolean answerSubmitted = false;


//------------------------------ Constructors
    /**
     * Initializes a new game using a list of lines, setting up the questions and
     * batches according to the game settings.
     *
     * @param lineList The list of lines to be used for the game.
     */
    void newGameFromLineList(List<Line> lineList){
        int bs = gameSettings.batchSize.getValue();
        questions = new Questions(lineList, bs,
                gameSettings.randomBatch.getValue(), gameSettings.randomLineInBatch.getValue());

        answerSubmitted = false;
        gameState = GameState.ONGOING;
    }



    /**
     * Starts a new game using a list of file paths that contain question data.
     *
     * @param fileList List of relative or absolute file paths containing the questions.
     */
    public void newGameFromFileList(List<String> fileList){
        questions = Questions.fromFiles(fileList,
                gameSettings.batchSize.getValue(), gameSettings.splits.getValue(),
                gameSettings.slComments.getValue(), gameSettings.mlComments.getValue(),
                gameSettings.randomBatch.getValue(), gameSettings.randomLineInBatch.getValue());

        answerSubmitted = false;
        gameState = GameState.ONGOING;
    }








//------------------------------ Functions
    /**
     * Updates the `currentQuestionSide` based on the randomization setting.
     */
    void updateCurrentQuestionSide(){
        if (gameSettings.fromSide.getValue() != Side.RANDOM) {
            this.currentQuestionSide = gameSettings.fromSide.getValue();
            return;
        }
        if( this.currentQuestionSide == Side.RANDOM){
            this.currentQuestionSide = (Math.random() < 0.5 ? Side.LEFT : Side.RIGHT);
        }
    }



    /**
     * Determines and retrieves the current question side as a string.
     *
     * @return the question string from the current side (left or right).
     */
    public String getCurrentQuestionSideStr(){
        this.updateCurrentQuestionSide();
        if (this.currentQuestionSide == Side.LEFT) { return questions.getCurrentLine().left; }
        return questions.getCurrentLine().right;
    }



    /**
     * Determines and retrieves the current answer side as a string.
     *
     * @return the answer string from the opposite side of
     * the current question side (left or right).
     */
    public String getCurrentAnswerSideStr(){
        this.updateCurrentQuestionSide();
        if (this.currentQuestionSide == Side.LEFT) { return questions.getCurrentLine().right; }
        return questions.getCurrentLine().left;
    }



    /** //TODO: Change this description
     * Calculates the number of lines containing mistakes across all batches.
     * Aka, the number of unique mistakes. If a mistake was made more than once on a
     * given line, it will be counted only once.
     *
     * @return the total number of lines with mistakes across all batches.
     */
    public int getUniqueMistakeCount(){
        int mistakeCount = 0;
        for (int i = 0; i < questions.getBatchCount(); i++){
            Batch batch = questions.getBatch(i);
            for (int j = 0; j < batch.line_count(); j++){
                Line line = batch.getLine(j);

                if (line.mistake > 0){ mistakeCount++; }
            }
        }
        return mistakeCount;
    }



    /**
     * Calculates the total count of all mistakes across all batches.
     *
     * @return the sum of all mistakes from all lines across all batches.
     */
    public int getTotalMistakeCount(){
        int mistakeCount = 0;
        for (int i = 0; i < questions.getBatchCount(); i++){
            Batch batch = questions.getBatch(i);
            for (int j = 0; j < batch.line_count(); j++){
                Line line = batch.getLine(j);

                if (line.mistake > 0){ mistakeCount += line.mistake; }
            }
        }
        return mistakeCount;
    }



    /**
     * Calculates the percentage of lines with unique mistakes relative to the total number of lines.
     *
     * @return the percentage of lines without mistakes, rounded to two decimal places.
     */
    public float getCorrectPercent() {
        int mc = this.getUniqueMistakeCount();
        int ls = questions.calcLineCount();
        if (mc == 0) { return (float) 100; }
        float percent = (float) 100 * Math.max(ls - mc, 0) / ls;
        return Math.round(percent * 100) / 100;
    }



    /**
     * Generates a question string to be presented to the user
     *
     * @param split the delimiter of the line
     * @return the full question string, the first stage of questioning the user.
     */
    public String getQuestionStage(String split){
        String question = this.getCurrentQuestionSideStr();

        return question + split;
    }



    /**
     * Generates the expected answer string to be presented to the user
     *
     * @param split the delimiter of the line
     * @return the full answer string, the second stage of questioning the user
     */
    public String getAnswerStage(String split){
        String question = this.getCurrentQuestionSideStr();
        String answer = this.getCurrentAnswerSideStr();

        return question + split + answer;
    }



    /**
     * Moves the game to the next question. This involves checking and managing the progression
     * through lines and batches of questions. It also sets the game state to ENDED if there
     * are no more questions available.
     */
    public void nextQuestion(){
        Batch currentBatch = questions.getCurrentBatch();
        boolean hasNextLine = currentBatch.hasNext();
        boolean hasNextBatch = questions.hasNext();
        answerSubmitted = false;

        if (hasNextBatch == false && hasNextLine == false){
            this.gameState = GameState.ENDED;
            return;
        }

        if(hasNextBatch == true && hasNextLine == false){
            questions.next();
            return;
        }

        if(hasNextLine == true){
            currentBatch.next();
            return;
        }

        throw new IllegalStateException("Unhandled nextQuestion state");
    }



    /**
     * Submits an answer for the current question and checks if it is correct.
     * Throws an exception if an answer has already been submitted for the current question.
     *
     * @param answer The answer to be validated against the correct answer.
     * @throws IllegalStateException If an answer has already been submitted for the current question.
     */
    public void submitAnswer(String answer){
        if (answerSubmitted) { throw new IllegalStateException("Answer can be submitted only once per question."); }

        if(gameSettings.typingMode.getValue() == true){
            String correct_answer = getCurrentAnswerSideStr();
            if(gameSettings.whiteSpaceSensitive.getValue() == false){
                answer = answer.replaceAll("\\s+", "");
                correct_answer = correct_answer.replaceAll("\\s+", "");
            }

            if(gameSettings.caseSensitive.getValue() == false){
                answer = answer.toLowerCase();
                correct_answer = correct_answer.toLowerCase();
            }

            if (answer.equals(correct_answer) == false){
                // TODO: Marker
                makeMistake();
            }
        } else{
            if(answer.equals("") == false) {
                makeMistake();
            }
        }

        this.currentQuestionSide = this.gameSettings.fromSide.getValue(); // Marker
        answerSubmitted = true;
    }



    /**     //TODO: Change this description when makeRepeat is finished
     * Applies the rules for repeating a question based on the current game settings.
     * If a mistake is made, and repeating is enabled, the current line is marked to not pass,
     * potentially triggering a repeat based on the game's configuration.
     */
    void makeRepeat(){
        RepeatAtPoint repeatAt = this.gameSettings.repeatAtPoint.getValue();
        Line cl = questions.getCurrentLine();
        cl.pass = false;
        switch (repeatAt){
            //case CURRENT_BATCH:
                // skip has been set nothing else to do
                //break;
            //TODO: Implement the rest of repeatAt options
        }
    }



    /**
     * Records a mistake for the current line and triggers repeating mechanisms
     * if the appropriate Setting is on.
     */
    void makeMistake(){
        Line cl = questions.getCurrentLine();
        cl.mistake += 1;
        if (this.gameSettings.repeatAtPoint.getValue() != RepeatAtPoint.DO_NOT_REPEAT){
            this.makeRepeat();
        }
    }








//------------------------------ Simple Utility
    @Override
    public String toString() {
        return this.questions.toString();
    }

    public int calcTotalLineCount(){ return questions.calcLineCount(); }

    public GameState getGameState(){ return this.gameState; }

    public int getBatchCount(){
        return questions.getBatchCount();
    }

    public int getBatchIndex(){
        return questions.getBatchIndex();
    }

    public int getCurrentBatchLineCount(){
        return questions.getCurrentBatch().line_count();
    }

    public int getCurrentInBatchLinePos() { return questions.getCurrentBatch().getLineIndex(); }

    public int getGlobalLinePos(){
        int batchSize = this.gameSettings.batchSize.getValue();
        int batchIndex = getBatchIndex();
        int ciblp = getCurrentInBatchLinePos();
        return (batchIndex) * batchSize + ciblp + 1;
    }
}
