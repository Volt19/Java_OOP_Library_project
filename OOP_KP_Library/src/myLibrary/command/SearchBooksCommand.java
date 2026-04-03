package myLibrary.command;

import myLibrary.Library;
import myLibrary.models.Book;
import myLibrary.models.SearchCriteria;
import myLibrary.persistence.FileManager;
import myLibrary.service.LibraryService;
import myLibrary.service.UnauthorizedException;
import myLibrary.ui.*;
import java.util.Arrays;
import java.util.List;

/**
 * Command to Search books command
 */
public class SearchBooksCommand extends AbstractCommand {
    public SearchBooksCommand(Library library, LibraryService libraryService, FileManager fileManager,
                              UserInterface ui, String[] args) {
        super(library, libraryService, fileManager, ui, args);
    }

    @Override
    public void execute() throws UnauthorizedException {
        if (!fileManager.isFileOpen()) {
            ui.displayError("Error: No file is currently open");
            return;
        }

        if (args.length < 2) {
            throw new IllegalArgumentException("Usage: books find <criteria> <value>");
        }

        SearchCriteria.SearchType type;
        try {
            type = SearchCriteria.SearchType.valueOf(args[0].toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid search criteria. Use: title, author, tag, genre");
        }

        String value = String.join(" ", Arrays.copyOfRange(args, 1, args.length));
        SearchCriteria criteria = new SearchCriteria(type, value);

        List<Book> results = libraryService.searchBooks(criteria);
        if (results.isEmpty()) {
            ui.displayMessage("No books found");
        } else {
            results.forEach(book -> ui.displayMessage(book.toString()));
        }
    }
}
