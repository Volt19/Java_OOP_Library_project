package myLibrary.command;

import myLibrary.Library;
import myLibrary.service.LibraryService;
import myLibrary.persistence.FileManager;
import myLibrary.ui.UserInterface;

import java.util.Arrays;
/**
 * Command to display help information about available commands.
 */
public class HelpCommand extends AbstractCommand {

    public HelpCommand(Library library, LibraryService libraryService, FileManager fileManager,
                       UserInterface ui, String[] args) {
        super(library, libraryService, fileManager, ui, args);
    }

    @Override
    public void execute() {
        displayHelp();
    }

    private void displayHelp() {
        StringBuilder helpText = new StringBuilder();

        helpText.append("\n=== Library Management System Help ===\n\n");

        helpText.append("AVAILABLE COMMANDS:\n");
        helpText.append("====================\n\n");

        // File Operations
        helpText.append("FILE OPERATIONS:\n");
        helpText.append("  open [filepath]       - Open a library file\n");
        helpText.append("  save                  - Save to current file\n");
        helpText.append("  saveas [filepath]     - Save to specified file\n");
        helpText.append("  close                 - Close current file\n");
        helpText.append("  help                  - Display this help message\n");
        helpText.append("  exit                  - Exit the program\n\n");

        // Authentication
        helpText.append("AUTHENTICATION:\n");
        helpText.append("  login                 - Log in to the system\n");
        helpText.append("  logout                - Log out of the system\n\n");

        // Book Operations
        helpText.append("BOOK OPERATIONS:\n");
        helpText.append("  books all             - List all books\n");
        helpText.append("  books info <isbn>     - Show details for a book\n");
        helpText.append("  books view <isbn>     - Show details for a book\n");
        helpText.append("  books find <criteria> <value> - Search for books\n");
        helpText.append("      <criteria>: title, author, tag, genre\n");
        helpText.append("  books sort <criteria> [asc|desc] - Sort books\n");
        helpText.append("      <criteria>: title, author, year, rating\n");
        helpText.append("      [order]: asc (ascending, default) or desc (descending)\n");
        helpText.append("  books add             - Add a new book (admin only)\n");
        helpText.append("  books remove <isbn>   - Remove a book (admin only)\n\n");

        // User Management (Admin only)
        helpText.append("USER MANAGEMENT (Admin only):\n");
        helpText.append("  users add <username> <password> - Add a new user\n");
        helpText.append("  users remove <username>         - Remove a user\n\n");

        // Examples
        helpText.append("EXAMPLES:\n");
        helpText.append("=========\n");
        helpText.append("  open library.dat\n");
        helpText.append("  login\n");
        helpText.append("  books find author \"Stephen King\"\n");
        helpText.append("  books sort year desc\n");
        helpText.append("  books info 978-3-16-148410-0\n");
        helpText.append("  users add john secret123\n");
        helpText.append("  saveas backup.dat\n");
        helpText.append("  exit\n\n");

        // Permissions info
        helpText.append("PERMISSIONS:\n");
        helpText.append("============\n");
        helpText.append("  ⚠ Requires login: books, users commands\n");
        helpText.append("  ⚠ Admin only: books add/remove, users add/remove\n");
        helpText.append("  No login needed: open, close, save, saveas, help, exit\n\n");

        ui.displayMessage(helpText.toString());
    }
}
