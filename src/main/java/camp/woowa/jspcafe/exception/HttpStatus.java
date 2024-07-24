package camp.woowa.jspcafe.exception;

public enum HttpStatus {
    // default status
    OK(200, "OK", ""),
    CREATED(201, "Created", ""),
    NO_CONTENT(204, "No Content", ""),
    BAD_REQUEST(400, "Bad Request", ""),
    UNAUTHORIZED(401, "Unauthorized", ""),
    FORBIDDEN(403, "Forbidden", ""),
    NOT_FOUND(404, "Not Found", ""),
    INTERNAL_SERVER_ERROR(500, "Internal Server Error", ""),

    // custom status
    INVALID_PASSWORD(400, "Invalid Password", "Password is invalid"),

    ;
    private final int statusCode;
    private final String message;
    private final String description;

    HttpStatus(int statusCode, String message, String description) {
        this.statusCode = statusCode;
        this.message = message;

        this.description = description;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public String getMessage() {
        return message;
    }

    public String getDescription() {
        return description;
    }
}
