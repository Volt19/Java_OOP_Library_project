package myLibrary.command;

import myLibrary.Library;
import myLibrary.persistence.FileManager;
import myLibrary.service.LibraryService;
import myLibrary.ui.UserInterface;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
/**
 * Factory class for creating command objects based on user input.
 */
public class CommandFactory {
    private final Library library;
    private final LibraryService libraryService;
    private final FileManager fileManager;
    private final UserInterface ui;

    private final Map<String, Function<String[], Command>> mainCommands;
    private final Map<String, Function<String[], Command>> booksCommands;
    private final Map<String, Function<String[], Command>> usersCommands;

    public CommandFactory(Library library, LibraryService libraryService,
                          FileManager fileManager, UserInterface ui) {
        this.library = library;
        this.libraryService = libraryService;
        this.fileManager = fileManager;
        this.ui = ui;

        // Initialize main commands map
        this.mainCommands = new HashMap<>();
        initializeMainCommands();

        // Initialize books subcommands map
        this.booksCommands = new HashMap<>();
        initializeBooksCommands();

        // Initialize users subcommands map
        this.usersCommands = new HashMap<>();
        initializeUsersCommands();
    }

    private void initializeMainCommands() {
        mainCommands.put("open", args -> new OpenCommand(library, libraryService, fileManager, ui, args));
        mainCommands.put("save", args -> new SaveCommand(library, libraryService, fileManager, ui, args));
        mainCommands.put("saveas", args -> new SaveAsCommand(library, libraryService, fileManager, ui, args));
        mainCommands.put("close", args -> new CloseCommand(library, libraryService, fileManager, ui, args));
        mainCommands.put("help", args -> new HelpCommand(library, libraryService, fileManager, ui, args));
        mainCommands.put("login", args -> new LoginCommand(library, libraryService, fileManager, ui, args));
        mainCommands.put("logout", args -> new LogoutCommand(library, libraryService, fileManager, ui, args));
        mainCommands.put("exit", args -> new ExitCommand());

        // Commands with nested handlers
        mainCommands.put("books", this::handleBooksCommand);
        mainCommands.put("users", this::handleUsersCommand);
    }

    private void initializeBooksCommands() {
        booksCommands.put("all", args -> new ViewAllBooksCommand(library, libraryService, fileManager, ui, args));
        booksCommands.put("info", args -> new ViewBookDetailsCommand(library, libraryService, fileManager, ui, args));
        booksCommands.put("view", args -> new ViewBookDetailsCommand(library, libraryService, fileManager, ui, args));
        booksCommands.put("find", args -> new SearchBooksCommand(library, libraryService, fileManager, ui, args));
        booksCommands.put("sort", args -> new SortBooksCommand(library, libraryService, fileManager, ui, args));
        booksCommands.put("add", args -> new AddBookCommand(library, libraryService, fileManager, ui, args));
        booksCommands.put("remove", args -> new RemoveBookCommand(library, libraryService, fileManager, ui, args));
    }

    private void initializeUsersCommands() {
        usersCommands.put("add", args -> new AddUserCommand(library, libraryService, fileManager, ui, args));
        usersCommands.put("remove", args -> new RemoveUserCommand(library, libraryService, fileManager, ui, args));
    }

    public Command createCommand(String commandName, String[] args) {
        String lowerCommandName = commandName.toLowerCase();

        Function<String[], Command> commandCreator = mainCommands.get(lowerCommandName);

        if (commandCreator != null) {
            return commandCreator.apply(args);
        }

        throw new IllegalArgumentException("Unknown command: " + commandName);
    }

    private Command handleBooksCommand(String[] args) {
        if (args.length == 0) {
            throw new IllegalArgumentException("Missing books subcommand");
        }

        String subCommand = args[0].toLowerCase();
        String[] subArgs = Arrays.copyOfRange(args, 1, args.length);

        Function<String[], Command> commandCreator = booksCommands.get(subCommand);

        if (commandCreator != null) {
            return commandCreator.apply(subArgs);
        }

        throw new IllegalArgumentException("Unknown books command: " + subCommand);
    }

    private Command handleUsersCommand(String[] args) {
        if (args.length == 0) {
            throw new IllegalArgumentException("Missing users subcommand");
        }

        String subCommand = args[0].toLowerCase();
        String[] subArgs = Arrays.copyOfRange(args, 1, args.length);

        Function<String[], Command> commandCreator = usersCommands.get(subCommand);

        if (commandCreator != null) {
            return commandCreator.apply(subArgs);
        }

        throw new IllegalArgumentException("Unknown users command: " + subCommand);
    }
}