package WordGameInternals;

import java.util.List;

public final class GameSettings{
    public Setting<RepeatAtPoint> repeatAtPoint = new Setting<>( // Not implemented
            "Repeat mistakes",
            "Toggles if you want to repeat mistakes, and if so where.",
            RepeatAtPoint.DO_NOT_REPEAT
    );

    public Setting<Integer> batchSize = new Setting<>( // Implemented
            "Batch size",
            "Size of a learning batch. If set to 0 disables batch mode.",
            0,
            value -> value >= 0
    );

    public Setting<Boolean> randomBatch = new Setting<>( // Implemented
            "Random batch",
            "Toggles if you want to randomise the order of batches.",
            false
    );

    public Setting<Boolean> randomLineInBatch = new Setting<>( // Implemented
            "Random line in batch",
            "Toggles if you want to randomise the order of lines inside a batch.",
            false
    );

    public Setting<Side> fromSide = new Setting<>( // Implemented
            "From side",
            "The side from which the question is asked.",
            Side.RANDOM
    );

    public Setting<Boolean> typingMode = new Setting<>( // Implemented
            "Typing mode",
            "Toggles typing mode. You will have to type out the answer.",
            false
    );

    public Setting<Boolean> caseSensitive = new Setting<>( // Implemented
            "Case sensitive typing mode",
            "Toggles if the typing mode is case sensitive or not.",
            false
    );

    public Setting<Boolean> whiteSpaceSensitive = new Setting<>( // Implemented
            "White space sensitive typing mode",
            "Toggles if the typing mode is white space sensitive or not.",
            false
    );

    //----------

    public Setting<Boolean> showGlobalPosition = new Setting<>( // Implemented
            "Show global position",
            "Toggles the display of the current questions global position.",
            true
    );

    public Setting<Boolean> showBatchPosition = new Setting<>( // Implemented
            "Show batch position",
            "Toggles the display of the current questions in batch and batch position.",
            true
    );

    public Setting<Boolean> showMistakeCount = new Setting<>( // Implemented
            "Show mistake count",
            "Toggles the display of the number of mistakes made.",
            true
    );

    public Setting<Boolean> showScore = new Setting<>( // Implemented
            "Show score",
            "Toggles the display of the right answer percentage.",
            true
    );

    public Setting<Boolean> examMode = new Setting<>( // Implemented
            "Exam mode",
            "When true, will show statistics only at the end of the game",
            false
    );

    public Setting<Boolean> autoClear = new Setting<>( // Not implemented
            "Auto clear",
            "Toggles the  automatic clearing of the terminal after each question.",
            true
    );

    public Setting<List<String>> splits = new Setting<>( // Partly implemented
            "Split Line Strings",
            "Split the line on the first occurrence of one of these when reading the .txt file.",
            List.of(" - ")
    );

    public Setting<List<String>> slComments = new Setting<>( // Not implemented
            "Single line comments",
            "Those strings will be accepted as a single line comment.",
            List.of("//")
    );

    public Setting<List<List<String>>> mlComments = new Setting<>( // Not implemented
            "Multi line comments",
            "Those strings will be accepted as a multi line comment.",
            List.of(List.of("/*", "*/"))
    );


    /**
     * Converts the GameSettings to a list for easy iteration over each setting.
     * Only includes settings that are currently implemented.
     *
     * @return A list containing the implemented GameSettings
     */
    public List<Setting> toList() {
        return List.of(
                //repeatAtPoint,
                batchSize,
                randomBatch,
                randomLineInBatch,
                fromSide,
                typingMode,
                caseSensitive,
                whiteSpaceSensitive,
                showGlobalPosition,
                showBatchPosition,
                showMistakeCount,
                showScore,
                examMode,
                //autoClear,
                splits
                //slComments,
                //mlComments
        );
    }



    @Override
    public String toString() {
        List<Setting> sl = this.toList();
        String ret = "";
        for (int i = 1; i <= sl.size(); i++){
            ret += i + ". " + sl.get(i-1).getName() + ": " + sl.get(i-1).getValue() + "\n";
        }
        return ret;
    }
}