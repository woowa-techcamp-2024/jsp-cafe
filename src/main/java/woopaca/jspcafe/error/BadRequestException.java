package woopaca.jspcafe.error;

public class BadRequestException extends ClientErrorException {

    public BadRequestException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}
