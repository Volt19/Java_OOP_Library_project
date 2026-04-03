package myLibrary.command;

import myLibrary.Library;
import myLibrary.models.Book;
import myLibrary.persistence.FileManager;
import myLibrary.service.LibraryService;
import myLibrary.service.UnauthorizedException;
import myLibrary.ui.UserInterface;

import java.util.Comparator;
import java.util.List;

/**
 * Command to sort books by specified criteria.
 */
public class SortBooksCommand extends AbstractCommand {

    public SortBooksCommand(Library library, LibraryService libraryService,
                            FileManager fileManager, UserInterface ui, String[] args) {
        super(library, libraryService, fileManager, ui, args);
    }

    @Override
    public void execute() {
        if (!fileManager.isFileOpen()) {
            ui.displayError("Error: No file is currently open");
            return;
        }

        if (args.length < 1) {
            ui.displayError("Error: Missing sort criteria. Usage: books sort <criteria> [asc|desc]");
            return;
        }

        String criteria = args[0].toLowerCase();
        boolean ascending = true; // Default to ascending

        if (args.length > 1) {
            String order = args[1].toLowerCase();
            if (order.equals("desc")) {
                ascending = false;
            } else if (!order.equals("asc")) {
                ui.displayError("Error: Invalid sort order. Use 'asc' or 'desc'");
                return;
            }
        }

        try {
            Comparator<Book> comparator = createComparator(criteria, ascending);
            List<Book> sortedBooks = libraryService.sortBooks(comparator);

            if (sortedBooks.isEmpty()) {
                ui.displayMessage("No books available to sort");
            } else {
                displaySortedBooks(sortedBooks, criteria, ascending);
            }

        } catch (IllegalArgumentException e) {
            ui.displayError("Error: " + e.getMessage());
        } catch (UnauthorizedException e) {
            ui.displayError("Error: " + e.getMessage());
        } catch (Exception e) {
            ui.displayError("Error sorting books: " + e.getMessage());
        }
    }

    private Comparator<Book> createComparator(String criteria, boolean ascending) {
        Comparator<Book> comparator;

        switch (criteria) {
            case "title":
                comparator = Comparator.comparing(Book::getTitle, String.CASE_INSENSITIVE_ORDER);
                break;
            case "author":
                comparator = Comparator.comparing(Book::getAuthor, String.CASE_INSENSITIVE_ORDER);
                break;
            case "year":
                comparator = Comparator.comparingInt(Book::getPublicationYear);
                break;
            case "rating":
                comparator = Comparator.comparingDouble(Book::getRating);
                break;
            default:
                throw new IllegalArgumentException(
                        "Invalid sort criteria. Use: title, author, year, or rating");
        }

        if (!ascending) {
            comparator = comparator.reversed();
        }

        return comparator;
    }

    private void displaySortedBooks(List<Book> books, String criteria, boolean ascending) {
        String order = ascending ? "ascending" : "descending";
        ui.displayMessage("\nBooks sorted by " + criteria + " (" + order + "):");
        ui.displayMessage("=" .repeat(50));

        for (int i = 0; i < books.size(); i++) {
            Book book = books.get(i);
            String line = String.format("%3d. %s by %s (%d) - Rating: %.1f/5.0 - ISBN: %s",
                    i + 1,
                    book.getTitle(),
                    book.getAuthor(),
                    book.getPublicationYear(),
                    book.getRating(),
                    book.getIsbn());
            ui.displayMessage(line);
        }

        ui.displayMessage("=" .repeat(50));
        ui.displayMessage("Total books: " + books.size());
    }
}
