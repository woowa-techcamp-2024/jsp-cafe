package com.codesquad.cafe.exception;

public class UnsupportedDBOperationException extends DBException {

	public UnsupportedDBOperationException() {
	}

	public UnsupportedDBOperationException(String message) {
		super(message);
	}

}
