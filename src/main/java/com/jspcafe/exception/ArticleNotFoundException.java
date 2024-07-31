package com.jspcafe.exception;

public class ArticleNotFoundException extends RuntimeException {

  public ArticleNotFoundException(final String message) {
    super(message);
  }
}
