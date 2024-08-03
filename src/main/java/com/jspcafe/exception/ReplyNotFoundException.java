package com.jspcafe.exception;

public class ReplyNotFoundException extends RuntimeException {

  public ReplyNotFoundException(final String message) {
    super(message);
  }
}
