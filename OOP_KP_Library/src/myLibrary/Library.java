package myLibrary;

import myLibrary.persistence.FileManager;
import myLibrary.repository.BookRepository;
import myLibrary.repository.UserRepository;
import myLibrary.service.LibraryService;

/**
 * Main library facade that provides access to all components.
 */
public class Library {
    private BookRepository bookRepository;
    private UserRepository userRepository;
    private final LibraryService libraryService;
    private final FileManager fileManager;

    public Library() {
        this.bookRepository = new BookRepository();
        this.userRepository = new UserRepository();
        this.libraryService = new LibraryService(bookRepository, userRepository);
        this.fileManager = new FileManager();
    }

    /**
     * Loads data from a LibraryData object into repositories.
     * This is called when a file is opened.
     */
    public void loadData(FileManager.LibraryData data) {
        if (data == null) {
            // Create new empty repositories
            this.bookRepository = new BookRepository();
            this.userRepository = new UserRepository();
        } else {
            // Use the repositories from the loaded data
            this.bookRepository = data.getBookRepository();
            this.userRepository = data.getUserRepository();
        }

        // Update the service with new repositories
        this.libraryService.setRepositories(bookRepository, userRepository);
    }

    /**
     * Clears all data from repositories.
     */
    public void clearData() {
        this.bookRepository = new BookRepository();
        this.userRepository = new UserRepository();
        this.libraryService.setRepositories(bookRepository, userRepository);
    }

    public BookRepository getBookRepository() {
        return bookRepository;
    }

    public UserRepository getUserRepository() {
        return userRepository;
    }

    public LibraryService getLibraryService() {
        return libraryService;
    }

    public FileManager getFileManager() {
        return fileManager;
    }

    /**
     * Creates LibraryData from current repositories.
     */
    public myLibrary.persistence.FileManager.LibraryData createLibraryData() {
        return new myLibrary.persistence.FileManager.LibraryData(bookRepository, userRepository);
    }
}