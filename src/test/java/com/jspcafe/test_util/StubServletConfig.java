package com.jspcafe.test_util;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletContext;
import java.util.Collections;
import java.util.Enumeration;

public class StubServletConfig implements ServletConfig {

  private final ServletContext servletContext;

  public StubServletConfig(ServletContext servletContext) {
    this.servletContext = servletContext;
  }

  @Override
  public String getServletName() {
    return "TestServlet";
  }

  @Override
  public ServletContext getServletContext() {
    return servletContext;
  }

  @Override
  public String getInitParameter(String name) {
    return null;
  }

  @Override
  public Enumeration<String> getInitParameterNames() {
    return Collections.emptyEnumeration();
  }
}
