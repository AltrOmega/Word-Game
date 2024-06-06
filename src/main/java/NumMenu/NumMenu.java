package NumMenu;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

/**
 * A class representing a numbered menu that allows users to choose actions by typing numbers.
 */
public final class NumMenu {
    private String MENU_TEXT;
    private List<Option> OPTIONS;

    /**
     * Constructs a new NumMenu with default menu text.
     */
    public NumMenu() {
        this.MENU_TEXT = "Type a number to choose an action: ";
        this.OPTIONS = new ArrayList<>();
    }



    /**
     * Constructs a new NumMenu with custom menu text.
     *
     * @param menuText the custom text to display at the top of the menu
     */
    public NumMenu(String menuText) {
        this.MENU_TEXT = menuText;
        this.OPTIONS = new ArrayList<>();
    }









    /**
     * Adds an option to the menu with the specified text and action.
     *
     * @param optionText the text of the menu option
     * @param action the action to perform when the option is selected
     */
    public void addOption(String optionText, Runnable action) {
        this.OPTIONS.add(new Option(optionText, action));
    }



    /**
     * Adds an option to the menu with the specified text, action, and whether it allows returning to a previous menu.
     *
     * @param optionText the text of the menu option
     * @param action the action to perform when the option is selected
     * @param getBack if true, selecting this option returns to the previous menu after the action is performed
     * Can be chained.
     */
    public void addOption(String optionText, Runnable action, boolean getBack) {
        this.OPTIONS.add(new Option(optionText, action, getBack));
    }



    /**
     * Adds an exit option to the menu with the specified text.
     *
     * @param optionText the text of the menu option
     */
    public void addOption(String optionText) {
        this.OPTIONS.add(new Option(optionText));
    }









    /**
     * A class representing an individual menu option.
     */
    private static class Option {
        private String OPTION_TEXT;
        private Runnable ACTION;
        private boolean exit = false;
        private boolean getBack = false;



        /**
         * Constructs a new Option with the specified text and action.
         *
         * @param optionText the text of the menu option
         * @param action the action to perform when the option is selected
         */
        public Option(String optionText, Runnable action) {
            this.OPTION_TEXT = optionText;
            this.ACTION = action;
        }



        /**
         * Constructs a new Option with the specified text, action, and whether it automatically returns to the previous menu.
         *
         * @param optionText the text of the menu option
         * @param action the action to perform when the option is selected
         * @param getBack if true, selecting this option returns to the previous menu after performing the action
         */
        public Option(String optionText, Runnable action, boolean getBack) {
            this.OPTION_TEXT = optionText;
            this.ACTION = action;
            this.getBack = getBack;
        }



        /**
         * Constructs a new exit Option with the specified text.
         *
         * @param optionText the text of the menu option
         */
        public Option(String optionText) {
            this(optionText, () -> {});
            this.exit = true;
        }

        @Override
        public String toString() {
            return this.OPTION_TEXT;
        }
    }









    /**
     * Prompts the user to choose an action and ensures the choice is valid.
     *
     * @param max_inclusive the maximum valid option number
     * @return the chosen option number
     */
    private static int force_chose_action(int max_inclusive) {
        int ret_action_num = 0;
        while (true) {
            try {
                Scanner scanner = new Scanner(System.in);
                ret_action_num = scanner.nextInt();
                if (ret_action_num >= 1 && ret_action_num <= max_inclusive) {
                    return ret_action_num;
                }
            } catch (InputMismatchException e) {
                // Continue to next iteration to prompt again
            }
            System.out.println("Your choice must be a number between 1 and " + max_inclusive);
        }
    }



    /**
     * Displays the menu and processes user choices in a loop until an exit option is chosen,
     * or and option with getBack is run
     */
    public void loopMenu() {
        boolean exit = false;
        while (!exit) {
            System.out.println(this.MENU_TEXT);
            for (int i = 0; i < this.OPTIONS.size(); i++) {
                System.out.println((i + 1) + ". " + this.OPTIONS.get(i).OPTION_TEXT);
            }

            Option option = this.OPTIONS.get(force_chose_action(this.OPTIONS.size()) - 1);
            option.ACTION.run();

            if (option.getBack) {
                return;
            }

            exit = option.exit;
        }
    }
}
