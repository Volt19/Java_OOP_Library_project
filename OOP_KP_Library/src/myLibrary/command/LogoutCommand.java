package myLibrary.command;

import myLibrary.Library;
import myLibrary.persistence.FileManager;
import myLibrary.service.LibraryService;
import myLibrary.ui.UserInterface;

/**
 * Command to logout the current user.
 */
public class LogoutCommand extends AbstractCommand {

    public LogoutCommand(Library library, LibraryService libraryService, FileManager fileManager,
                         UserInterface ui, String[] args) {
        super(library, libraryService, fileManager, ui, args);
    }

    @Override
    public void execute() throws Exception {
        if (!libraryService.isLoggedIn()) {
            ui.displayError("Error: No user is currently logged in");
            return;
        }

        String username = libraryService.getCurrentUser().getUsername();
        libraryService.logout();
        ui.displayMessage("Goodbye, " + username + "!");
    }
}