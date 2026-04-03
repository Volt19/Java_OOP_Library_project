package myLibrary.service;

/**
 * Custom exception for authorization failures.
 */
public class UnauthorizedException extends Exception {
    public UnauthorizedException(String message) {
        super(message);
    }
}
