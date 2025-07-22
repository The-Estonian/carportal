package ee.bcs.carportal.infrastructure;

import ee.bcs.carportal.infrastructure.exception.DatabaseConflictException;
import ee.bcs.carportal.infrastructure.exception.ResourceNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

        @ExceptionHandler(MethodArgumentNotValidException.class)
        protected ResponseEntity<ApiError> handleValidation(MethodArgumentNotValidException ex,
                        HttpServletRequest request) {
                String errorMessage = ex.getBindingResult()
                                .getFieldErrors()
                                .get(0)
                                .getDefaultMessage();
                ApiError apiError = new ApiError(
                                HttpStatus.BAD_REQUEST,
                                HttpStatus.BAD_REQUEST.value(),
                                errorMessage,
                                request.getRequestURI());
                return new ResponseEntity<>(apiError, apiError.getStatus());
        }

        @ExceptionHandler(ResourceNotFoundException.class)
        protected ResponseEntity<ApiError> handleNotFound(ResourceNotFoundException ex,
                        HttpServletRequest request) {
                ApiError apiError = new ApiError(
                                HttpStatus.NOT_FOUND,
                                HttpStatus.NOT_FOUND.value(),
                                ex.getError().getMessage(),
                                request.getRequestURI());
                return new ResponseEntity<>(apiError, apiError.getStatus());
        }

        @ExceptionHandler(DatabaseConflictException.class)
        protected ResponseEntity<ApiError> handleConflict(DatabaseConflictException ex,
                        HttpServletRequest request) {
                ApiError apiError = new ApiError(
                                HttpStatus.CONFLICT,
                                HttpStatus.CONFLICT.value(),
                                ex.getError().getMessage(),
                                request.getRequestURI());
                return new ResponseEntity<>(apiError, apiError.getStatus());
        }

}