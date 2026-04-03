package myLibrary.command;

import myLibrary.Library;
import myLibrary.persistence.FileManager;
import myLibrary.service.LibraryService;
import myLibrary.ui.UserInterface;

/**
 * Command to close the currently open file.
 */
public class CloseCommand extends AbstractCommand {

    public CloseCommand(Library library, LibraryService libraryService, FileManager fileManager,
                        UserInterface ui, String[] args) {
        super(library, libraryService, fileManager, ui, args);
    }

    @Override
    public void execute() {
        if (!fileManager.isFileOpen()) {
            ui.displayError("Error: No file is currently open");
            return;
        }

        // Check if there are unsaved changes (optional enhancement)
        // if (hasUnsavedChanges()) {
        //     String response = ui.prompt("You have unsaved changes. Close anyway? (yes/no): ");
        //     if (!response.equalsIgnoreCase("yes")) {
        //         ui.displayMessage("Close operation cancelled.");
        //         return;
        //     }
        // }

//        // Logout user if logged in
//        if (libraryService.isLoggedIn()) {
//            ui.displayMessage("Automatically logging out user: " +
//                    libraryService.getCurrentUser().getUsername());
//            try {
//                libraryService.logout();
//            } catch (Exception e) {
//                // Ignore logout errors during close
//            }
//        }

        String closedFilePath = fileManager.getCurrentFilePath();
        fileManager.close();
        library.clearData();

        if (closedFilePath != null) {
            ui.displayMessage("Successfully closed: " + closedFilePath);
        } else {
            ui.displayMessage("File closed.");
        }
    }

    /**
     * Optional: Check for unsaved changes.
     * This would require tracking changes in your application.
     */
    private boolean hasUnsavedChanges() {
        // Implementation depends on how you track changes
        // For example, you could have a boolean flag in Application
        // that gets set to true when any modifying command is executed
        // and set to false after save.
        return false; // Placeholder
    }
}