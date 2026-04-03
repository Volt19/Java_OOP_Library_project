package myLibrary.command;

/**
 * Custom exception for application exit
 */
public class ApplicationExitException extends RuntimeException {
    public ApplicationExitException() {
        super("Application exit requested");
    }
}
