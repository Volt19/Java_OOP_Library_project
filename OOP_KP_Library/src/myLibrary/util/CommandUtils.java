package myLibrary.util;

import myLibrary.Library;
import myLibrary.persistence.FileManager;
import myLibrary.repository.*;

/**
 * Utility class for common command operations.
 */
public class CommandUtils {

    /**
     * Creates LibraryData from the current application state.
     */
    public static FileManager.LibraryData createLibraryDataFromCurrentState(Library library) {
        if (library == null) {
            throw new IllegalArgumentException("Library cannot be null");
        }

        // Use Library's method to create data
        return library.createLibraryData();
    }

    /**
     * Alternative: Direct access to repositories.
     */
    public static FileManager.LibraryData createLibraryDataFromRepositories(
            BookRepository bookRepo, UserRepository userRepo) {
        return new FileManager.LibraryData(bookRepo, userRepo);
    }
}
