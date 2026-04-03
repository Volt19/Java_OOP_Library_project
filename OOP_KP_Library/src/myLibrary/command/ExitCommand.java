package myLibrary.command;

/**
 * Command to exit the application.
 */
// Exit command
public class ExitCommand implements Command {
    @Override
    public void execute() {
        throw new ApplicationExitException();
    }
}
