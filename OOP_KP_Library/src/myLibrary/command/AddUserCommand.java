package myLibrary.command;

import myLibrary.Library;
import myLibrary.persistence.FileManager;
import myLibrary.service.LibraryService;
import myLibrary.service.UnauthorizedException;
import myLibrary.ui.UserInterface;

/**
 * Command to add a new user to the system (admin only).
 */
public class AddUserCommand extends AbstractCommand {

    public AddUserCommand(Library library, LibraryService libraryService,
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
            // Check if admin is logged in
            if (!libraryService.isAdminLoggedIn()) {
                ui.displayError("Error: Only administrators can add users");
                return;
            }

            String username;
            String password;

            // Parse arguments or prompt for input
            if (args.length >= 2) {
                username = args[0];
                password = args[1];
            } else {
                ui.displayMessage("\n=== ADD NEW USER ===\n");
                username = getUsernameInput();
                password = getPasswordInput();
            }

            // Ask for admin privileges
            boolean isAdmin = getAdminStatusInput();

            // Add the user
            libraryService.addUser(username, password, isAdmin);

            String role = isAdmin ? "administrator" : "regular user";
            ui.displayMessage("\nUser '" + username + "' added successfully as " + role + "!");

        } catch (UnauthorizedException e) {
            ui.displayError("Error: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            ui.displayError("Error: " + e.getMessage());
        } catch (Exception e) {
            ui.displayError("Error adding user: " + e.getMessage());
        }
    }

    private String getUsernameInput() {
        while (true) {
            String username = ui.prompt("Username: ");
            if (username == null || username.trim().isEmpty()) {
                ui.displayError("Username cannot be empty!");
            } else if (username.equalsIgnoreCase("admin")) {
                ui.displayError("Username 'admin' is reserved!");
            } else {
                return username.trim();
            }
        }
    }

    private String getPasswordInput() {
        while (true) {
            String password = ui.promptPassword("Password: ");
            if (password == null || password.trim().isEmpty()) {
                ui.displayError("Password cannot be empty!");
            } else if (password.length() < 3) {
                ui.displayError("Password must be at least 3 characters!");
            } else {
                return password;
            }
        }
    }

    private boolean getAdminStatusInput() {
        while (true) {
            String response = ui.prompt("Is this user an administrator? (yes/no): ");
            if (response == null) {
                return false;
            }

            response = response.trim().toLowerCase();
            if (response.equals("yes") || response.equals("y")) {
                return true;
            } else if (response.equals("no") || response.equals("n")) {
                return false;
            } else {
                ui.displayError("Please answer 'yes' or 'no'");
            }
        }
    }
}