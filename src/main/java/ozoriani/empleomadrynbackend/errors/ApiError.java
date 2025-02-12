package ozoriani.empleomadrynbackend.errors;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ApiError {
    private LocalDateTime timestamp;
    private int statusCode;
    private String message;
    private String path;
    private String error;

    public ApiError(int statusCode, String message, String path, String error) {
        this.timestamp = LocalDateTime.now();
        this.statusCode = statusCode;
        this.message = message;
        this.path = path;
        this.error = error;
    }
}
