package ee.bcs.carportal.infrastructure.exception;

import ee.bcs.carportal.infrastructure.Error;

public class ResourceNotFoundException extends RuntimeException {
    private final Error error;

    public ResourceNotFoundException(Error error) {
        super(error.getMessage());
        this.error = error;
    }

    public Error getError() {
        return error;
    }
}