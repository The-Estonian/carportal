package ee.bcs.carportal.infrastructure.exception;

public class DatabaseConflictException extends RuntimeException {
    public DatabaseConflictException(String errorMessage) {
        super(errorMessage);
    }
}