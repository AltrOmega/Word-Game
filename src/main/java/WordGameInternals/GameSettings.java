package WordGameInternals;

import java.util.List;

public final class GameSettings{
    Setting<RepeatAtPoint> repeatAtPoint = new Setting<>( // Not implemented
            "Repeat mistakes",
            "Toggles if you want to repeat mistakes, and if so where.",
            RepeatAtPoint.DO_NOT_REPEAT
    );

    Setting<Integer> batchSize = new Setting<>( // Implemented
            "Batch size",
            "Size of a learning batch. If set to 0 disables batch mode.",
            0,
            value -> value >= 0
    );

    Setting<Boolean> randomBatch = new Setting<>( // Implemented
            "Random batch",
            "Toggles if you want to randomise the order of batches.",
            false
    );

    Setting<Boolean> randomLineInBatch = new Setting<>( // Implemented
            "Random line in batch",
            "Toggles if you want to randomise the order of lines inside a batch.",
            false
    );

    Setting<Side> fromSide = new Setting<>( // Implemented
            "From side",
            "The side from which the question is asked.",
            Side.RANDOM
    );

    Setting<Boolean> typingMode = new Setting<>( // Not implemented
            "Typing mode",
            "Toggles typing mode. You will have to type out the answer.",
            false
    );

    Setting<Boolean> caseSensitive = new Setting<>( // Not implemented
            "Case sensitive typing",
            "Toggles if the typing mode is case sensitive or not.",
            false
    );

    Setting<Boolean> whiteSpaceSensitive = new Setting<>( // Not implemented
            "White space sensitive typing",
            "Toggles if the typing mode is white space sensitive or not.",
            false
    );

    //----------

    public Setting<Boolean> showPosition = new Setting<>( // Implemented
            "Show position",
            "Toggles the display of the current questions position.",
            true
    );

    public Setting<Boolean> showMistakeCount = new Setting<>( // Implemented
            "Show mistake count",
            "Toggles the display of the number of mistakes made.",
            true
    );

    // ignoring this setting for now
    // showMisCount shows score as well for now
    Setting<Boolean> showScore = new Setting<>( // Implemented
            "Show score",
            "Toggles the display of the right answer percentage.",
            true
    );

    public Setting<Boolean> autoClear = new Setting<>( // Not implemented
            "Auto clear",
            "Toggles the  automatic clearing of the terminal after each question.",
            true
    );

    Setting<List<String>> splits = new Setting<>( // Partly implemented
            "Split Line Strings",
            "Split the line on the first occurrence of one of these when reading the .txt file.",
            List.of(" - ")
    );

    Setting<List<String>> slComments = new Setting<>( // Not implemented
            "Single line comments",
            "Those strings will be accepted as a single line comment.",
            List.of("//")
    );

    Setting<List<List<String>>> mlComments = new Setting<>( // Not implemented
            "Multi line comments",
            "Those strings will be accepted as a multi line comment.",
            List.of(List.of("/*", "*/"))
    );



    public List<Setting> toList() {
        return List.of(
                //repeatAtPoint,
                batchSize,
                randomBatch,
                randomLineInBatch,
                fromSide,
                //typingMode,
                //caseSensitive,
                //whiteSpaceSensitive,
                showPosition,
                showMistakeCount,
                showScore,
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
// add show descriptions to setttings TODO