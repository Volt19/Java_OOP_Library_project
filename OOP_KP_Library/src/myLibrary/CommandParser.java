package myLibrary;

import myLibrary.command.Command;
import myLibrary.command.CommandFactory;
import myLibrary.ui.UserInterface;

/**
 * Parses user input and creates appropriate command objects.
 */
public class CommandParser {
    private final CommandFactory commandFactory;

    public CommandParser(Library library, UserInterface ui) {
        var libraryService = library.getLibraryService();
        var fileManager = library.getFileManager();

        this.commandFactory = new CommandFactory(library, libraryService, fileManager, ui);
    }

    public Command parse(String input) {
        if (input == null || input.trim().isEmpty()) {
            throw new IllegalArgumentException("Empty command");
        }

        String[] parts = input.trim().split("\\s+");
        String commandName = parts[0];
        String[] args = parts.length > 1 ?
                java.util.Arrays.copyOfRange(parts, 1, parts.length) :
                new String[0];

        return commandFactory.createCommand(commandName, args);
    }
}