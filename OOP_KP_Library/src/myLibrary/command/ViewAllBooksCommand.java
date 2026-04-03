package myLibrary.command;

import myLibrary.Library;
import myLibrary.models.Book;
import myLibrary.persistence.FileManager;
import myLibrary.service.LibraryService;
import myLibrary.service.UnauthorizedException;
import myLibrary.ui.*;

import java.util.List;

 /**
 *Command to display all books in the library.
 */
public class ViewAllBooksCommand extends AbstractCommand {
    public ViewAllBooksCommand(Library library, LibraryService libraryService, FileManager fileManager,
                               UserInterface ui, String[] args) {
        super(library, libraryService, fileManager, ui, args);
    }

    @Override
    public void execute() throws UnauthorizedException {
        if (!fileManager.isFileOpen()) {
            ui.displayError("Error: No file is currently open");
            return;
        }

        List<Book> books = libraryService.getAllBooks();
        if (books.isEmpty()) {
            ui.displayMessage("No books available");
        } else {
            books.forEach(book -> ui.displayMessage(book.toString()));
        }
    }
}
