package myLibrary.command;

import myLibrary.Library;
import myLibrary.persistence.FileManager;
import myLibrary.service.AuthenticationException;
import myLibrary.service.LibraryService;
import myLibrary.ui.*;

/**
 * Command to login the user.
 */
public class LoginCommand extends AbstractCommand {
    public LoginCommand(Library library, LibraryService libraryService, FileManager fileManager,
                        UserInterface ui, String[] args) {
        super(library, libraryService, fileManager, ui, args);
    }

    @Override
    public void execute() throws AuthenticationException {
        if (libraryService.isLoggedIn()) {
            ui.displayError("You are already logged in.");
            return;
        }
        if (!fileManager.isFileOpen()) {
            ui.displayError("Error: No file is currently open");
            return;
        }
        String username = ui.prompt("Username: ");
        String password = ui.promptPassword("Password: ");
        libraryService.login(username, password);
        ui.displayMessage("Welcome, " + username + "!");
    }
}
