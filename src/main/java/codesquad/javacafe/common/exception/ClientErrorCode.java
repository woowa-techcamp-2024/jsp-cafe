package codesquad.javacafe.common.exception;

public enum ClientErrorCode implements ErrorCode{
	USER_ALREADY_EXIST(HttpStatus.CONFLICT, "해당 아이디는 이미 사용중입니다."),
	USER_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 사용자입니다."),
	INVALID_PASSWORD(HttpStatus.UNAUTHORIZED, "비밀번호가 일치하지 않습니다.");

	private final HttpStatus httpStatus;
	private final String message;

	ClientErrorCode(HttpStatus httpStatus, String message) {
		this.httpStatus = httpStatus;
		this.message = message;
	}

	public CustomException customException(String debugMessage) {
		return new CustomException(getHttpStatus(), name(), getMessage(), debugMessage);
	}

	@Override
	public HttpStatus getHttpStatus() {
		return httpStatus;
	}

	@Override
	public String getMessage() {
		return message;
	}
}
