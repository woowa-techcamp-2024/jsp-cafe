package codesquad.javacafe.common.exception;

public enum ServerErrorCode {
	INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버에 문제가 발생했습니다.");

	private final HttpStatus httpStatus;
	private final String message;

	public HttpStatus getHttpStatus() {
		return httpStatus;
	}
	public int getHttpStatusCode() {
		return httpStatus.getStatusCode();
	}

	public String getMessage() {
		return this.message;
	}

	ServerErrorCode(HttpStatus httpStatus, String message) {
		this.httpStatus = httpStatus;
		this.message = message;
	}

	public CustomException customException(String debugMessage) {
		return new CustomException(getHttpStatus(), name(), getMessage(), debugMessage);
	}
}
