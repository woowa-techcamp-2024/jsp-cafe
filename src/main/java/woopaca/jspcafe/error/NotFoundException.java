package woopaca.jspcafe.error;

public class NotFoundException extends ClientErrorException {

    public NotFoundException(String message) {
        super(message, HttpStatus.NOT_FOUND);
    }
}
