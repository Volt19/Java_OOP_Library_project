package myLibrary.persistence;

import myLibrary.repository.BookRepository;
import myLibrary.repository.UserRepository;
import java.io.Serializable;

/**
 * Data Transfer Object for library persistence.
 * Contains all data that needs to be saved/loaded.
 */
public class LibraryData implements Serializable {
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