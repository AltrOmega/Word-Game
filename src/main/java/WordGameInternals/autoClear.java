package WordGameInternals;

import java.io.IOException;

// Does not render properly in the IDE, but works if you run it in the Windows terminal
// Yes, i tested this
public final class autoClear {
    public static void cls() {
        try {
            if (System.getProperty("os.name").contains("Windows")) {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            }
            else {
                System.out.print("\033\143");
            }
        } catch (IOException | InterruptedException ex) {}
    }


    // move this to a unit test or smthn
    public static void main(String[] args) {
        System.out.println("foo");
        cls();
        //System.out.println("\f");
        System.out.println("Terminal cleared!");
    }
}
