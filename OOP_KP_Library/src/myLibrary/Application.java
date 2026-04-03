package myLibrary;
import myLibrary.command.*;
import myLibrary.ui.*;

import java.util.Scanner;

/**
 * Main application controller that orchestrates the system.
 * Implements the Facade pattern to simplify system usage.
 */
public class Application {
    private final Library library;
    private final CommandParser commandParser;
    private final UserInterface ui;
    private boolean isRunning;

    /**
     * Constructs a new Application with all necessary components.
     */
    public Application() {
        this.library = new Library();
        this.ui = new ConsoleUserInterface();
        this.commandParser = new CommandParser(library, ui);
        this.isRunning = true;
    }

    /**
     * Starts the main application loop.
     */
    public void run() {
        ui.displayWelcome();

        while (isRunning) {
            try {
                String input = ui.getUserInput();
                Command command = commandParser.parse(input);
                command.execute();
            } catch (ApplicationExitException e) {
                isRunning = false;
                ui.displayMessage("Exiting the system...");
            } catch (Exception e) {
                ui.displayError("Error: " + e.getMessage());
            }
        }
    }
}
