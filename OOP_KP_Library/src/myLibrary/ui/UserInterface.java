package myLibrary.ui;

/**
 * Interface for user interaction.
 * Allows different implementations (console, GUI, etc.)
 */
public interface UserInterface {
    String getUserInput();

    String prompt(String message);

    String promptPassword(String message);

    void displayMessage(String message);

    void displayError(String error);

    void displayWelcome();
}
