package camp.woowa.jspcafe.exception;

public class CustomException extends RuntimeException {
    private final int statusCode;
    private final String message;
    private final String description;

    public CustomException(final HttpStatus httpStatus) {
        this.statusCode = httpStatus.getStatusCode();
        this.message = httpStatus.getMessage();
        this.description = httpStatus.getDescription();
    }

    public CustomException(final int statusCode, final String message, final String description) {
        this.statusCode = statusCode;
        this.message = message;
        this.description = description;
    }

    public int getStatusCode() {
        return statusCode;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public String getDescription() {
        return description;
    }
}
