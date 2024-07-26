package codesquad.javacafe.common.exception;

public interface ErrorCode {
	String name();
	HttpStatus getHttpStatus();
	String getMessage();
}
