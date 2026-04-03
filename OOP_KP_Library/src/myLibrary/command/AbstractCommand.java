package myLibrary.command;

import myLibrary.Library;
import myLibrary.service.LibraryService;
import myLibrary.persistence.FileManager;
import myLibrary.ui.UserInterface;

/**
 * Base abstract command with common functionality.
 */
public abstract class AbstractCommand implements Command {
    protected final Library library;  // Added
    protected final LibraryService libraryService;
    protected final FileManager fileManager;
    protected final UserInterface ui;
    protected final String[] args;

    protected AbstractCommand(Library library, LibraryService libraryService,
                              FileManager fileManager, UserInterface ui, String[] args) {
        this.library = library;
        this.libraryService = libraryService;
        this.fileManager = fileManager;
        this.ui = ui;
        this.args = args;
    }

    // Convenience method to create LibraryData
    protected FileManager.LibraryData createLibraryData() {
        return library.createLibraryData();
    }
}