package myLibrary.command;

import myLibrary.Library;
import myLibrary.persistence.FileManager;
import myLibrary.service.LibraryService;
import myLibrary.service.UnauthorizedException;
import myLibrary.ui.UserInterface;

/**
 * Command to remove a book from the library (admin only).
 */
public class RemoveBookCommand extends AbstractCommand {

    public RemoveBookCommand(Library library, LibraryService libraryService,
                             FileManager fileManager, UserInterface ui, String[] args) {
        super(library, libraryService, fileManager, ui, args);
    }

    @Override
    public void execute() {
        if (!fileManager.isFileOpen()) {
            ui.displayError("Error: No file is currently open");
            return;
        }
        if (!libraryService.isLoggedIn()) {
            ui.displayError("Error: No user is currently logged in");
            return;
        }
        if (!libraryService.isAdminLoggedIn()) {
            ui.displayError("Error: Only administrators can remove users");
            return;
        }

        if (args.length < 1) {
            ui.displayError("Error: Missing ISBN. Usage: books remove <isbn>");
            return;
        }

        String isbn = args[0];

        try {
            if (!libraryService.isAdminLoggedIn()) {
                ui.displayError("Error: Only administrators can remove books");
                return;
            }

            // Optional: Ask for confirmation
            String response = ui.prompt("Are you sure you want to remove book with ISBN " +
                    isbn + "? (yes/no): ");

            if (response != null && (response.equalsIgnoreCase("yes") ||
                    response.equalsIgnoreCase("y"))) {
                libraryService.removeBook(isbn);
                ui.displayMessage("Book with ISBN " + isbn + " removed successfully");
            } else {
                ui.displayMessage("Book removal cancelled");
            }

        } catch (UnauthorizedException e) {
            ui.displayError("Error: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            ui.displayError("Error: " + e.getMessage());
        } catch (Exception e) {
            ui.displayError("Error removing book: " + e.getMessage());
        }
    }
}