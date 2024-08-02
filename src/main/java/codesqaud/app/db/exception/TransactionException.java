package codesqaud.app.db.exception;

public class TransactionException extends UncheckedSQLException {
    public TransactionException(String message) {
        super(message);
    }
}
