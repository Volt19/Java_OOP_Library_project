package myLibrary;

import myLibrary.command.HelpCommand;


/**
 * Main entry point for the Library Management System.
 * Initializes the system and starts the application.
 */
public class Main {
    public static void main(String[] args) {
        Application app = new Application();
        app.run();
    }
}
