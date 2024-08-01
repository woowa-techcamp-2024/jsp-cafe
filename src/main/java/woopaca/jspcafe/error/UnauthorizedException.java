package woopaca.jspcafe.error;

public class UnauthorizedException extends ClientErrorException {

    public UnauthorizedException(String message) {
        super(message, HttpStatus.UNAUTHORIZED);
    }
}
