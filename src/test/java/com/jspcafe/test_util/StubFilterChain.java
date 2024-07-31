package com.jspcafe.test_util;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import java.io.IOException;

public class StubFilterChain implements FilterChain {

  private boolean doFilterCalled = false;

  @Override
  public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse)
      throws IOException, ServletException {
    doFilterCalled = true;
  }

  public boolean isDoFilterCalled() {
    return doFilterCalled;
  }
}
