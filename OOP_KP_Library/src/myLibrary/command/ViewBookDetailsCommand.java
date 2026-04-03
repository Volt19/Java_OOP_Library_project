package myLibrary.command;

import myLibrary.Library;
import myLibrary.models.Book;
import myLibrary.persistence.FileManager;
import myLibrary.service.LibraryService;
import myLibrary.service.UnauthorizedException;
import myLibrary.ui.UserInterface;

import java.util.Optional;

/**
 * Command to view detailed information about a specific book.
 */
public class ViewBookDetailsCommand extends AbstractCommand {

    public ViewBookDetailsCommand(Library library, LibraryService libraryService,
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
            ui.displayError("Error: Missing ISBN. Usage: books info <isbn>");
            return;
        }

        String isbn = args[0];

        try {
            Optional<Book> bookOpt = libraryService.getBookDetails(isbn);

            if (bookOpt.isPresent()) {
                Book book = bookOpt.get();
                displayBookDetails(book);
            } else {
                ui.displayError("Error: Book with ISBN " + isbn + " not found");
            }

        } catch (UnauthorizedException e) {
            ui.displayError("Error: " + e.getMessage());
        } catch (Exception e) {
            ui.displayError("Error retrieving book details: " + e.getMessage());
        }
    }

    private void displayBookDetails(Book book) {
        StringBuilder details = new StringBuilder();

        details.append("\n=== BOOK DETAILS ===\n");
        details.append("ISBN: ").append(book.getIsbn()).append("\n");
        details.append("Title: ").append(book.getTitle()).append("\n");
        details.append("Author: ").append(book.getAuthor()).append("\n");
        details.append("Genre: ").append(book.getGenre()).append("\n");
        details.append("Publication Year: ").append(book.getPublicationYear()).append("\n");
        details.append("Rating: ").append(String.format("%.1f/5.0", book.getRating())).append("\n");
        details.append("Description: ").append(book.getDescription()).append("\n");

        if (!book.getTags().isEmpty()) {
            details.append("Tags: ").append(String.join(", ", book.getTags())).append("\n");
        }

        details.append("===================\n");

        ui.displayMessage(details.toString());
    }
}