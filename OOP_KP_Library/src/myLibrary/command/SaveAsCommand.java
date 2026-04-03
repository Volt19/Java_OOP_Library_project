package myLibrary.command;

import myLibrary.Library;
import myLibrary.persistence.FileManager;
import myLibrary.service.LibraryService;
import myLibrary.ui.UserInterface;

import java.io.File;
import java.io.IOException;

/**
 * Command to save the current library data to a new file.
 */
public class SaveAsCommand extends AbstractCommand {

    public SaveAsCommand(Library library, LibraryService libraryService, FileManager fileManager,
                         UserInterface ui, String[] args) {
        super(library, libraryService, fileManager, ui, args);
    }

    @Override
    public void execute() {
        if (!fileManager.isFileOpen()) {
            ui.displayError("Error: No file is currently open. Use 'open' command first.");
            return;
        }

        String newFilePath;
        if (args.length > 0) {
            newFilePath = args[0];
        } else {
            newFilePath = ui.prompt("Enter new file path: ");
        }

        if (newFilePath == null || newFilePath.trim().isEmpty()) {
            ui.displayError("Error: File path cannot be empty");
            return;
        }

        // Check if file already exists
        File newFile = new File(newFilePath);
        if (newFile.exists()) {
            String response = ui.prompt("File already exists. Overwrite? (yes/no): ");
            if (response == null || !(response.equalsIgnoreCase("yes") || response.equalsIgnoreCase("y"))) {
                ui.displayMessage("Save operation cancelled.");
                return;
            }
        }

        try {
            // Create LibraryData from current repositories
            FileManager.LibraryData data = createLibraryData();

            // Save to new file
            fileManager.save(data, newFilePath);

            ui.displayMessage("Successfully saved as " + newFilePath);

        } catch (IOException e) {
            ui.displayError("Error saving file: " + e.getMessage());
        } catch (Exception e) {
            ui.displayError("Error: " + e.getMessage());
        }
    }
}