package myLibrary.command;

import myLibrary.Library;
import myLibrary.persistence.FileManager;
import myLibrary.service.LibraryService;
import myLibrary.ui.UserInterface;

import java.io.IOException;

/**
 * Command to save the current library data to the opened file.
 */
public class SaveCommand extends AbstractCommand {

    public SaveCommand(Library library, LibraryService libraryService, FileManager fileManager,
                       UserInterface ui, String[] args) {
        super(library, libraryService, fileManager, ui, args);
    }

    @Override
    public void execute() {
        if (!fileManager.isFileOpen()) {
            ui.displayError("Error: No file is currently open");
            return;
        }

        try {
            // Create LibraryData from current repositories
            FileManager.LibraryData data = createLibraryData();

            // Save to current file
            fileManager.save(data);

            String filePath = fileManager.getCurrentFilePath();
            ui.displayMessage("Successfully saved to " + filePath);

        } catch (IOException e) {
            ui.displayError("Error saving file: " + e.getMessage());
        } catch (Exception e) {
            ui.displayError("Error: " + e.getMessage());
        }
    }
}