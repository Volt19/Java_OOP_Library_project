package myLibrary.command;

import myLibrary.Library;
import myLibrary.persistence.FileManager;
import myLibrary.service.LibraryService;
import myLibrary.service.UnauthorizedException;
import myLibrary.ui.UserInterface;

/**
 * Command to remove a user from the system (admin only).
 */
public class RemoveUserCommand extends AbstractCommand {

    public RemoveUserCommand(Library library, LibraryService libraryService,
                             FileManager fileManager, UserInterface ui, String[] args) {
        super(library, libraryService, fileManager, ui, args);
    }

    @Override
    public void execute() {
        try {
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

            String username;

            // Parse argument or prompt for input
            if (args.length >= 1) {
                username = args[0];
            } else {
                ui.displayMessage("\n=== REMOVE USER ===\n");
                username = getUsernameInput();
            }

            // Check if trying to remove admin
            if (username.equalsIgnoreCase("admin")) {
                ui.displayError("Error: Cannot remove the default admin user");
                return;
            }

            // Check if user exists
            if (!userExists(username)) {
                ui.displayError("Error: User '" + username + "' not found");
                return;
            }

            // Ask for confirmation
            String response = ui.prompt("Are you sure you want to remove user '" +
                    username + "'? (yes/no): ");

            if (response != null && (response.equalsIgnoreCase("yes") ||
                    response.equalsIgnoreCase("y"))) {
                libraryService.removeUser(username);
                ui.displayMessage(" User '" + username + "' removed successfully");
            } else {
                ui.displayMessage(" User removal cancelled");
            }

        } catch (UnauthorizedException e) {
            ui.displayError("Error: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            ui.displayError("Error: " + e.getMessage());
        } catch (Exception e) {
            ui.displayError("Error removing user: " + e.getMessage());
        }
    }

    private String getUsernameInput() {
        while (true) {
            String username = ui.prompt("Username to remove: ");
            if (username == null || username.trim().isEmpty()) {
                ui.displayError("Username cannot be empty!");
            } else {
                return username.trim();
            }
        }
    }

    private boolean userExists(String username) {
        // This method should check if the user exists
        // You'll need to implement this based on your UserRepository
        // For now, we'll assume the LibraryService handles this check
        return true; // Placeholder
    }
}