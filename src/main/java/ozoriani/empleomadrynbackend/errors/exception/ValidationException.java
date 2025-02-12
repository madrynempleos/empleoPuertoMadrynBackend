package ozoriani.empleomadrynbackend.errors.exception;

public class ValidationException extends RuntimeException {
    public ValidationException(String message) {
        super(message);
    }
} 