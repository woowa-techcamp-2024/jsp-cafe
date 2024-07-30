package woopaca.jspcafe.error;

public class ForbiddenException extends ClientErrorException {

    public ForbiddenException(String message) {
        super(message, HttpStatus.FORBIDDEN);
    }
}
