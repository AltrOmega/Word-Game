package TerminalWordGame;
import NumMenu.NumMenu;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import WordGameInternals.*;



public class TerminalWordGame {
//------------------------------ Game loop logic

    private static void gameLoop(GameEngine gameEngine){
        GameEngine ge = gameEngine;
        //System.out.println(ge);
        while(ge.getGameState() != GameState.ENDED){
            Scanner scanner = new Scanner(System.in);
            String stage_1 = ge.getQuestionStage(" - ");
            String stage_2 = ge.getAnswerStage(" - ");

            boolean showPos = ge.gameSettings.showPosition.getValue();
            boolean showMis = ge.gameSettings.showMistakeCount.getValue();
            boolean autoClear = ge.gameSettings.autoClear.getValue();
            String stats = "";
            if (autoClear){};

            // Since no new lines are added while the game is running
            // it's possible to make this more efficient by stashing some of the data here TODO: add it
            if (showPos){// TODO: split global and in batch pos into two diff settings
                stats += "G: " + ge.getGlobalLinePos() + "/" + ge.calcTotalLineCount() + " | ";
                stats += "B: " + String.valueOf(ge.getCurrentInBatchLinePos()+1) + "/" + ge.getCurrentBatchLineCount() +
                         " | " + String.valueOf(ge.getBatchIndex()+1) + "/" + ge.getBatchCount() + "\n";
            }
            if (showMis) {// Todo split and handle show score
                stats += "M: " + ge.getUniqueMistakeCount() + " | " + ge.getCorrectPercent() + "%";
            }

            if (stats != "") { System.out.println(stats);}

            boolean PLACEHOLDER_typing_mode = false;
            if (PLACEHOLDER_typing_mode){
                System.out.print(stage_1);
                ge.submitAnswer(scanner.nextLine());
                System.out.println(stage_2);
            } else {
                System.out.print(stage_1);
                scanner.nextLine();
                System.out.println(stage_2);
                String s = scanner.nextLine();
                ge.submitAnswer(s);
            }
            ge.nextQuestion();
        }
    }



    private static void newGameLoop(GameEngine gameEngine){
        // TODO: add a handle for multiple files and white / blacklist
        System.out.println("Type a path to a .txt file with lines: ");
        Scanner scanner = new Scanner(System.in);
        gameEngine.newGameFromFileList(List.of(scanner.nextLine()));
        gameLoop(gameEngine);
    }





//----------------------------------- Menus

//-------------------- Change a setting menus

    private static void intSettingChangeMenu(Setting setting){
        Scanner scanner = new Scanner(System.in);

        int value;
        while(true){
            System.out.println(setting.getValue().toString());
            System.out.println("What to change: " + setting.getName() + ": " +
                    setting.getValue().toString() + " to: ");

            value = scanner.nextInt();
            if(setting.valueIsLegal(value)) break;
            System.out.println("This is a illegal value, try again");
            System.out.println(setting.getName() + ": " + setting.get_description());
            // Todo: Here is a random pause. pls fix
        }
        setting.setValue(value);
        System.out.println("Change successful");
    }



    private static void booleanSettingChangeMenu(Setting setting){
        NumMenu menu = new NumMenu();
        menu.addOption(
            "true",
            ()-> {
                setting.setValue(true);
                System.out.println("Change successful");
            }, true);

        menu.addOption(
            "false",
            ()-> {
                setting.setValue(false);
                System.out.println("Change successful");
            }, true);

        menu.addOption("Exit this submenu");

        menu.loopMenu();
    }



    private static Runnable repeatAtPointSettingChangeMenu(Setting setting){
        return () -> {
            NumMenu menu = new NumMenu();

            for(RepeatAtPoint value : RepeatAtPoint.values()){
                menu.addOption(
                    value.name(),
                    () -> {
                        setting.setValue(value);
                        System.out.println("Change successful");
                    },
                    true);
            }

            menu.addOption("Exit this submenu");

            menu.loopMenu();
            };
    }



    private static Runnable sideSettingChangeMenu(Setting setting){
        return () -> {
            NumMenu menu = new NumMenu();

            for(Side value : Side.values()){
                menu.addOption(
                    value.name(),
                    () -> {
                        setting.setValue(value);
                        System.out.println("Change successful");
                    },
                    true
                    );
            }

            menu.addOption("Exit this submenu");

            menu.loopMenu();
        };
    }







//-------------------- Take action functions

    private static String changeActionString(){
        Scanner scanner = new Scanner(System.in);
        String setTo = "";

        while (setTo.equals("")){
            System.out.print("What to set it to?: ");
            setTo = scanner.nextLine();
            System.out.println();
            if(setTo.equals("")){
                System.out.println("Can't be set to nothing");
            }
        }

        return setTo;
    }



    private static Runnable listSettingChangeAction(Setting setting, int settingPos){
        return () -> {
            List<?> tempValueList = (List<?>) setting.getValue();
            if( tempValueList.get(0) instanceof String){
                ArrayList<String> valueList = new ArrayList( (List)setting.getValue() );
                String setTo = changeActionString();

                valueList.set(settingPos, setTo);
                setting.setValue(valueList);
            } else {
                ArrayList<List<String>> valueList = new ArrayList( (List<String>)setting.getValue() );

                System.out.println("Start comment:");
                String setTo1 = changeActionString();

                System.out.println("End comment:");
                String setTo2 = changeActionString();

                valueList.set(settingPos, List.of(setTo1, setTo2));
                setting.setValue(valueList);
            }

        };
    }



    private static Runnable listSettingDeleteAction(Setting setting, int settingPos) {
        return () -> {
            List<?> tempValueList = (List<?>) setting.getValue();
            if ( tempValueList.size() <= 1 ){
                System.out.println("Can't delete the last one, change it instead.");
                return;
            }
            if( tempValueList.get(0) instanceof String){
                ArrayList<String> valueList = new ArrayList( (List)setting.getValue() );
                valueList.remove(settingPos);
                setting.setValue(valueList);
            } else {
                ArrayList<List<String>> valueList = new ArrayList( (List<String>)setting.getValue() );
                valueList.remove(settingPos);
                setting.setValue(valueList);
            }
        };
    }



    private static Runnable listSettingAddAction(Setting setting){
        return () -> {
            List<?> tempValueList = (List<?>) setting.getValue();
            if( tempValueList.get(0) instanceof String){
                ArrayList<String> valueList = new ArrayList( (List)setting.getValue() );
                String addWhat = changeActionString();

                valueList.add(addWhat);
                setting.setValue(valueList);
            } else {        // Error of list indexing here?
                ArrayList<List<String>> valueList = new ArrayList( (List<String>)setting.getValue() );

                System.out.println("Start comment:");
                String addWhat1 = changeActionString();

                System.out.println("End comment:");
                String addWhat2 = changeActionString();

                valueList.add(List.of(addWhat1, addWhat2));
                setting.setValue(valueList);
            }

        };
    }



    private static Runnable listSettingChoseAction(Setting setting, int settingPos){
        return () -> {
            NumMenu menu = new NumMenu();
            menu.addOption("Change", listSettingChangeAction(setting, settingPos), true);
            menu.addOption("Delete", listSettingDeleteAction(setting, settingPos), true);
            menu.addOption("Exit this submenu");
            menu.loopMenu();
        };
    }



    private static Runnable listSettingChangeMenu(Setting setting){
        return () -> {
            NumMenu menu = new NumMenu();

            List<?> valueList = (List<?>) setting.getValue();

            for (int i = 0; i < valueList.size(); i++){
                menu.addOption(valueList.get(i).toString(), listSettingChoseAction(setting, i), true);
            }

            menu.addOption("Add a new one", listSettingAddAction(setting), true);
            menu.addOption("Exit this submenu");

            menu.loopMenu();
        };
    }







//----------------------------------- Main Menu

    private static void settingsLoop(GameSettings gs) {
        NumMenu menu = new NumMenu("What setting to change?: ");

        List<Setting> settingsList = gs.toList();
        for (int i = 1; i <= settingsList.size(); i++){
            Setting setting = settingsList.get(i-1);

            Runnable func = ()->{};

            Object value = setting.getValue();
            if( value instanceof Integer ){
                func = ()->{ intSettingChangeMenu(setting); };

            } else if( value instanceof Boolean){
                func = ()->{ booleanSettingChangeMenu(setting); };

            } else if( value instanceof RepeatAtPoint){
                func = repeatAtPointSettingChangeMenu(setting);

            } else if( value instanceof Side){
                func = sideSettingChangeMenu(setting);

            } else if( value instanceof List){
                func = listSettingChangeMenu(setting);
            }

            menu.addOption(
                setting.getName() + ": " + setting.getValue().toString(),
                func,
                true
            );

            if ( i == settingsList.size() ){
                menu.addOption("Show descriptions",
                () -> {
                    List<Setting> sl = gs.toList();
                    String print_ = "";
                    for (int j = 1; j <= sl.size(); j++){
                        Setting setting_ = sl.get(j-1);
                        print_ += j + ". " + setting_.getName() + ": " + setting_.getValue() + "\n" +
                            setting_.get_description() + "\n-----\n";
                    }
                    System.out.println(print_);
                });
                menu.addOption("Exit this submenu");
            }
        }

        menu.loopMenu();
        }



    public static void mainMenuLoop(GameEngine gameEngine){
        NumMenu menu = new NumMenu();
            menu.addOption("Start a new game", ()-> {newGameLoop(gameEngine);});
            menu.addOption("Settings", ()-> {settingsLoop(gameEngine.gameSettings);});
            menu.addOption("Exit the program");
        menu.loopMenu();
    }



    public static void main(String[] args) {
        GameEngine gameEngine = new GameEngine();
        gameEngine.gameSettings.showPosition.setValue(true);
        gameEngine.gameSettings.showMistakeCount.setValue(true);
        mainMenuLoop(gameEngine);
    }
}