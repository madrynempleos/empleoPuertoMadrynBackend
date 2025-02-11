package ozoriani.empleomadrynbackend.errors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import ozoriani.empleomadrynbackend.errors.exception.InvalidOperationException;
import ozoriani.empleomadrynbackend.errors.exception.ResourceNotFoundException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiError> handleResourceNotFoundException(
            ResourceNotFoundException ex, WebRequest request) {
        ApiError apiError = new ApiError(
            HttpStatus.NOT_FOUND.value(),
            "Resource Not Found",
            ex.getMessage(),
            request.getDescription(false)
        );
        return new ResponseEntity<>(apiError, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(InvalidOperationException.class)
    public ResponseEntity<ApiError> handleInvalidOperationException(
            InvalidOperationException ex, WebRequest request) {
        ApiError apiError = new ApiError(
            HttpStatus.BAD_REQUEST.value(),
            "Invalid Operation",
            ex.getMessage(),
            request.getDescription(false)
        );
        return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleGlobalException(
            Exception ex, WebRequest request) {
        ApiError apiError = new ApiError(
            HttpStatus.INTERNAL_SERVER_ERROR.value(),
            "Internal Server Error",
            ex.getMessage(),
            request.getDescription(false)
        );
        return new ResponseEntity<>(apiError, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
