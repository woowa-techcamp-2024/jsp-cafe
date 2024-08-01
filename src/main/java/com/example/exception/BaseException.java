package com.example.exception;

public class BaseException extends RuntimeException {

	private final int statusCode;
	private final String message;

	public BaseException(int statusCode, String message) {
		this.statusCode = statusCode;
		this.message = message;
	}

	public static BaseException exception(int statusCode, String message) {
		return new BaseException(statusCode, message);
	}

	public static BaseException serverException() {
		return new BaseException(500, null);
	}

	// @Override
	// public synchronized Throwable fillInStackTrace() {
	// 	return this;
	// }

	public int getStatus() {
		return statusCode;
	}

	@Override
	public String getMessage() {
		return message;
	}
}
