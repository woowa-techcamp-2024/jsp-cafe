package codesqaud.app.db.exception;

public class DbConstraintException extends UncheckedSQLException {
    public DbConstraintException(String message) {
        super(message);
    }

    public DbConstraintException(Throwable cause) {
        super(cause);
    }
}
