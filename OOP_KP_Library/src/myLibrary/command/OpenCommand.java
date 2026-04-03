package myLibrary.command;

import myLibrary.Library;
import myLibrary.persistence.FileManager;
import myLibrary.service.LibraryService;
import myLibrary.ui.*;

import java.io.File;
import java.io.IOException;

/**
 * Command to open file.
 */
public class OpenCommand extends AbstractCommand {

    public OpenCommand(Library library, LibraryService libraryService,
                       FileManager fileManager, UserInterface ui, String[] args) {
        super(library, libraryService, fileManager, ui, args);
    }

    @Override
    public void execute() {
        if (fileManager.isFileOpen()) {
            ui.displayError("Error: A file is already open. Close it first.");
            return;
        }

        String filePath;
        if (args.length > 0) {
            filePath = args[0];
        } else {
            filePath = ui.prompt("Enter file path: ");
        }

        if (filePath == null || filePath.trim().isEmpty()) {
            ui.displayError("Error: File path cannot be empty");
            return;
        }

        try {
            // Open file and get data
            FileManager.LibraryData data = fileManager.open(filePath);

            // Load data into repositories
            library.loadData(data);

            // Optional: Add default books if repository is empty
            if (library.getBookRepository().isEmpty()) {
                library.getBookRepository().addDefaultBooks();
                ui.displayMessage("Added default books to new library");
            }

            String fileName = new File(filePath).getName();
            ui.displayMessage("   Successfully opened: " + fileName);
            ui.displayMessage("   Books: " + library.getBookRepository().count());
            ui.displayMessage("   Users: " + library.getUserRepository().findAll().size());

        } catch (Exception e) {
            ui.displayError("Error opening file: " + e.getMessage());
        }
    }
}
