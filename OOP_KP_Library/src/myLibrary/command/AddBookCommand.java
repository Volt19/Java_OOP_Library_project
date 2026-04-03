package myLibrary.command;

import myLibrary.Library;
import myLibrary.models.Book;
import myLibrary.persistence.FileManager;
import myLibrary.service.LibraryService;
import myLibrary.service.UnauthorizedException;
import myLibrary.ui.UserInterface;

import java.util.Arrays;
import java.util.List;

/**
 * Command to add a new book to the library (admin only).
 */
public class AddBookCommand extends AbstractCommand {

    public AddBookCommand(Library library, LibraryService libraryService,
                          FileManager fileManager, UserInterface ui, String[] args) {
        super(library, libraryService, fileManager, ui, args);
    }

    @Override
    public void execute() {
        try {
            if (!fileManager.isFileOpen()) {
                ui.displayError("Error: No file is currently open");
                return;
            }
            if (!libraryService.isLoggedIn()) {
                ui.displayError("Error: No user is currently logged in");
                return;
            }
            if (!libraryService.isAdminLoggedIn()) {
                ui.displayError("Error: Only administrators can add books");
                return;
            }

            ui.displayMessage("\n=== ADD NEW BOOK ===\n");

            // Collect book information
            String isbn = getInput("ISBN: ", true);
            String title = getInput("Title: ", true);
            String author = getInput("Author: ", true);
            String genre = getInput("Genre: ", false);
            String description = getInput("Description: ", false);
            int year = getYearInput();
            List<String> tags = getTagsInput();
            double rating = getRatingInput();

            // Create book using Builder pattern
            Book book = new Book.Builder(isbn, title, author)
                    .genre(genre)
                    .description(description)
                    .publicationYear(year)
                    .tags(tags)
                    .rating(rating)
                    .build();

            // Add book to library
            libraryService.addBook(book);

            ui.displayMessage("\nBook added successfully!");
            ui.displayMessage("Title: " + title);
            ui.displayMessage("Author: " + author);
            ui.displayMessage("ISBN: " + isbn);

        } catch (UnauthorizedException e) {
            ui.displayError("Error: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            ui.displayError("Error: " + e.getMessage());
        } catch (Exception e) {
            ui.displayError("Error adding book: " + e.getMessage());
        }
    }

    private String getInput(String prompt, boolean required) {
        while (true) {
            String input = ui.prompt(prompt);
            if (required && (input == null || input.trim().isEmpty())) {
                ui.displayError("This field is required!");
            } else {
                return input != null ? input.trim() : "";
            }
        }
    }

    private int getYearInput() {
        while (true) {
            try {
                String input = ui.prompt("Publication Year: ");
                if (input == null || input.trim().isEmpty()) {
                    return 0; // Default value
                }
                int year = Integer.parseInt(input.trim());
                if (year > 0 && year <= java.time.Year.now().getValue()) {
                    return year;
                } else {
                    ui.displayError("Please enter a valid year (1 - " +
                            java.time.Year.now().getValue() + ")");
                }
            } catch (NumberFormatException e) {
                ui.displayError("Please enter a valid number for year");
            }
        }
    }

    private List<String> getTagsInput() {
        String input = ui.prompt("Tags (comma separated, optional): ");
        if (input == null || input.trim().isEmpty()) {
            return Arrays.asList();
        }
        return Arrays.asList(input.trim().split("\\s*,\\s*"));
    }

    private double getRatingInput() {
        while (true) {
            try {
                String input = ui.prompt("Rating (0.0 - 5.0, optional): ");
                if (input == null || input.trim().isEmpty()) {
                    return 0.0; // Default value
                }
                double rating = Double.parseDouble(input.trim());
                if (rating >= 0.0 && rating <= 5.0) {
                    return rating;
                } else {
                    ui.displayError("Rating must be between 0.0 and 5.0");
                }
            } catch (NumberFormatException e) {
                ui.displayError("Please enter a valid number for rating");
            }
        }
    }
}
