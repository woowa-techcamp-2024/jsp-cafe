package woopaca.jspcafe.error;

public abstract class ClientErrorException extends RuntimeException {

    private final HttpStatus status;

    public ClientErrorException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }

    public HttpStatus getStatus() {
        return status;
    }
}
