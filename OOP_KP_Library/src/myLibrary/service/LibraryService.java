package myLibrary.service;

import myLibrary.models.Book;
import myLibrary.models.Permission;
import myLibrary.models.SearchCriteria;
import myLibrary.models.User;
import myLibrary.repository.BookRepository;
import myLibrary.repository.UserRepository;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

/**
 * Service layer that implements business logic.
 * Follows Single Responsibility Principle.
 */
public class LibraryService {
    private BookRepository bookRepository;
    private UserRepository userRepository;
    private User currentUser;

    public LibraryService(BookRepository bookRepository, UserRepository userRepository) {
        this.bookRepository = bookRepository;
        this.userRepository = userRepository;
        this.currentUser = null;
    }

    public void login(String username, String password) throws AuthenticationException {
        if (currentUser != null) {
            throw new IllegalStateException("User is already logged in");
        }

        if (!userRepository.authenticate(username, password)) {
            throw new AuthenticationException("Incorrect username or password");
        }

        currentUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new AuthenticationException("User not found"));
    }

    public void logout() {
        if (currentUser == null) {
            throw new IllegalStateException("No user is currently logged in");
        }
        currentUser = null;
    }

    public void addBook(Book book) throws UnauthorizedException {
        checkPermission(myLibrary.models.Permission.ADD_BOOK);
        bookRepository.add(book);
    }

    public void removeBook(String isbn) throws UnauthorizedException {
        checkPermission(myLibrary.models.Permission.REMOVE_BOOK);
        bookRepository.remove(isbn);
    }

    public List<Book> getAllBooks() throws UnauthorizedException {
        checkPermission(myLibrary.models.Permission.VIEW_ALL_BOOKS);
        return bookRepository.findAll();
    }

    public Optional<Book> getBookDetails(String isbn) throws UnauthorizedException {
        checkPermission(myLibrary.models.Permission.VIEW_BOOK_DETAILS);
        return bookRepository.findByIsbn(isbn);
    }

    public List<Book> searchBooks(SearchCriteria criteria) throws UnauthorizedException {
        checkPermission(myLibrary.models.Permission.SEARCH_BOOKS);
        return bookRepository.search(criteria);
    }

    public List<Book> sortBooks(Comparator<Book> comparator) throws UnauthorizedException {
        checkPermission(myLibrary.models.Permission.SORT_BOOKS);
        return bookRepository.sort(comparator);
    }

    public void addUser(String username, String password, boolean isAdmin) throws UnauthorizedException {
        checkPermission(myLibrary.models.Permission.ADD_USER);
        User user = isAdmin ?
                User.createAdmin(username, password) :
                User.createRegularUser(username, password);
        userRepository.add(user);
    }

    public void removeUser(String username) throws UnauthorizedException {
        checkPermission(myLibrary.models.Permission.REMOVE_USER);
        userRepository.remove(username);
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public boolean isLoggedIn() {
        return currentUser != null;
    }

    public boolean isAdminLoggedIn() {
        return currentUser != null && currentUser.isAdmin();
    }

    private void checkPermission(Permission permission) throws UnauthorizedException {
        if (currentUser == null) {
            throw new UnauthorizedException("You need to log in first");
        }
        if (!currentUser.hasPermission(permission)) {
            throw new UnauthorizedException("You don't have permission to perform this action");
        }
    }

    /**
     * Updates the repositories (used when loading data from file).
     */
    public void setRepositories(BookRepository bookRepository, UserRepository userRepository) {
        this.bookRepository = bookRepository;
        this.userRepository = userRepository;
        // Reset current user since repositories changed
        this.currentUser = null;
    }
}
