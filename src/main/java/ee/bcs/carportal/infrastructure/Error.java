package ee.bcs.carportal.infrastructure;

import lombok.AllArgsConstructor;
import lombok.Getter;
@AllArgsConstructor
@Getter

public enum Error {
    RESOURCE_NOT_FOUND("Resource not found"),
    CAR_EXISTS("Car already exists");

    private final String message;
}