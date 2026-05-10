package ui;

public class AppWrapper {
    public static void main(String[] args) {
        // This wrapper circumvents the "JavaFX runtime components are missing" error
        // when running JavaFX 11+ applications without modules or VM options.
        Main.main(args);
    }
}
