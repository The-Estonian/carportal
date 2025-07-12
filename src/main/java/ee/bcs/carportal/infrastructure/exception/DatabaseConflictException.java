package ee.bcs.carportal.infrastructure.exception;

import ee.bcs.carportal.infrastructure.Error;

public class DatabaseConflictException extends RuntimeException {
    private final Error error;

    public DatabaseConflictException(Error error) {
        super(error.getMessage());
        this.error = error;
    }

    public Error getError() {
        return error;
    }
}