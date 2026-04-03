package myLibrary.ui;

import java.util.Scanner;

/**
 * Console-based implementation of UserInterface.
 */
public class ConsoleUserInterface implements UserInterface {
    private final Scanner scanner;

    public ConsoleUserInterface() {
        this.scanner = new Scanner(System.in);
    }

    @Override
    public String getUserInput() {
        System.out.print("> ");
        return scanner.nextLine().trim();
    }

    @Override
    public String prompt(String message) {
        System.out.print(message);
        return scanner.nextLine().trim();
    }

    @Override
    public String promptPassword(String message) {
        System.out.print(message);
        // For now, simple implementation. In real app, use Console.readPassword()
        return scanner.nextLine().trim();
    }

    @Override
    public void displayMessage(String message) {
        System.out.println(message);
    }

    @Override
    public void displayError(String error) {
        System.err.println("Error: " + error);
    }

    @Override
    public void displayWelcome() {
        System.out.println("=== Library Management System ===");
        System.out.println("Type 'help' for available commands");
    }
}
