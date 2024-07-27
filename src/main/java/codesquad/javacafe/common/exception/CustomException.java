package codesquad.javacafe.common.exception;

public class CustomException extends RuntimeException{
	private final HttpStatus httpStatus;
	private final String errorName;
	private final String errorMessage;
	private final String debugMessage;

	public CustomException(HttpStatus httpStatus,String errorName, String errorMessage) {
		super(errorMessage);
		this.errorName = errorName;
		this.httpStatus = httpStatus;
		this.errorMessage = errorMessage;
		this.debugMessage = null;
	}

	public CustomException(HttpStatus httpStatus,String errorName, String errorMessage,
		String debugMessage) {
		super(errorMessage);
		this.errorName = errorName;
		this.httpStatus = httpStatus;
		this.errorMessage = errorMessage;
		this.debugMessage = debugMessage;
	}

	public HttpStatus getHttpStatus() {
		return httpStatus;
	}

	public String getErrorName() {
		return errorName;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public String getDebugMessage() {
		return debugMessage;
	}
}
