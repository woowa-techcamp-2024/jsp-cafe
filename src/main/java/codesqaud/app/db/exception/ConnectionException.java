package codesqaud.app.db.exception;

public class ConnectionException extends UncheckedSQLException {
    public ConnectionException(String message) {
        super(message);
    }
}
