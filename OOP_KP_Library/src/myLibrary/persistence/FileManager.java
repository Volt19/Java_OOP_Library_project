package myLibrary.persistence;

import myLibrary.models.*;
import myLibrary.repository.BookRepository;
import myLibrary.repository.UserRepository;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Handles file operations for persistence.
 * Follows Dependency Inversion Principle.
 */
public class FileManager {
    private String currentFilePath;

    /**
     * Opens a library file and returns the data.
     *
     * @param filePath the path to the file to open
     * @return LibraryData object containing repositories
     * @throws IOException if there's an error reading the file
     * @throws ClassNotFoundException if the file format is invalid
     */
    public LibraryData open(String filePath) throws IOException, ClassNotFoundException {
        if (currentFilePath != null) {
            throw new IllegalStateException("A file is already open");
        }

        File file = new File(filePath);
        LibraryData data = null;

        try {
            if (!file.exists() || file.length() == 0) {
                // Case 1: File doesn't exist or is empty
                data = createNewLibraryDataWithDefaultData();
                currentFilePath = filePath;

                // Ensure directory exists before saving
                ensureDirectoryExists(file);

                // Save the newly created data
                save(data, filePath);

                System.out.println("Created new library file with default data: " + filePath);
            } else {
                // Case 2: File exists - try to load it
                try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filePath))) {
                    data = (LibraryData) ois.readObject();
                    currentFilePath = filePath;
                    System.out.println("Successfully loaded library file: " + filePath);
                } catch (EOFException | StreamCorruptedException e) {
                    // File is corrupted or has wrong format
                    System.out.println("File is corrupted or empty. Creating new library with default data...");
                    data = createNewLibraryDataWithDefaultData();
                    currentFilePath = filePath;

                    // Save over the corrupted file
                    save(data, filePath);
                    System.out.println("Created new library data and saved to: " + filePath);
                }
            }
        } catch (IOException e) {
            // Clean up on error
            currentFilePath = null;
            throw new IOException("Failed to open file '" + filePath + "': " + e.getMessage(), e);
        }

        return data;
    }

    /**
     * Ensures the directory for the file exists.
     */
    private void ensureDirectoryExists(File file) throws IOException {
        File parentDir = file.getParentFile();
        if (parentDir != null && !parentDir.exists()) {
            if (!parentDir.mkdirs()) {
                throw new IOException("Failed to create directory: " + parentDir.getAbsolutePath());
            }
        }
    }

    /**
     * Creates new library data with default admin and sample books.
     */
    private LibraryData createNewLibraryDataWithDefaultData() {
        // Create repositories
        BookRepository bookRepo = new BookRepository();
        UserRepository userRepo = new UserRepository();

        // UserRepository constructor should already add default admin
        // If not, add it:
        if (!userRepo.findByUsername("admin").isPresent()) {
            User defaultAdmin = new User("admin", "iamsa", User.UserRole.ADMIN);
            userRepo.add(defaultAdmin);
        }

        // Add sample books to the repository
        bookRepo.addDefaultBooks();

        return new LibraryData(bookRepo, userRepo);
    }

    /**
     * Saves library data to a specified file.
     *
     * @param data the library data to save
     * @param filePath the path where to save the file
     * @throws IOException if there's an error writing to the file
     */
    public void save(LibraryData data, String filePath) throws IOException {
        // Create directory if it doesn't exist
        File file = new File(filePath);
        File parentDir = file.getParentFile();
        if (parentDir != null && !parentDir.exists()) {
            parentDir.mkdirs();
        }

        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filePath))) {
            oos.writeObject(data);
        }
    }

    /**
     * Saves library data to the currently open file.
     *
     * @param data the library data to save
     * @throws IOException if there's an error writing to the file
     * @throws IllegalStateException if no file is currently open
     */
    public void save(LibraryData data) throws IOException {
        if (currentFilePath == null) {
            throw new IllegalStateException("No file is currently open");
        }
        save(data, currentFilePath);
    }

    /**
     * Closes the current file.
     */
    public void close() {
        currentFilePath = null;
    }

    /**
     * Checks if a file is currently open.
     *
     * @return true if a file is open, false otherwise
     */
    public boolean isFileOpen() {
        return currentFilePath != null;
    }

    /**
     * Gets the path of the currently open file.
     *
     * @return the file path, or null if no file is open
     */
    public String getCurrentFilePath() {
        return currentFilePath;
    }



    /**
     * Creates a new empty library data object without any users.
     *
     * @return new LibraryData with empty repositories
     */
    public static LibraryData createNewEmptyLibraryData() {
        return new LibraryData(new BookRepository(), new UserRepository());
    }

    /**
     * Data transfer object for library persistence.
     */
    public static class LibraryData implements Serializable {
        private final BookRepository bookRepository;
        private final UserRepository userRepository;

        public LibraryData(BookRepository bookRepository, UserRepository userRepository) {
            this.bookRepository = bookRepository;
            this.userRepository = userRepository;
        }

        public BookRepository getBookRepository() {
            return bookRepository;
        }

        public UserRepository getUserRepository() {
            return userRepository;
        }
    }
}