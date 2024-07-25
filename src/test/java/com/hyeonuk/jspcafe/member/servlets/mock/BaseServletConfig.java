package com.hyeonuk.jspcafe.member.servlets.mock;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletContext;

import java.util.Enumeration;

public class BaseServletConfig implements ServletConfig {
    private final ServletContext servletContext;
    public BaseServletConfig(ServletContext servletContext) {
        this.servletContext = servletContext;
    }
    @Override
    public String getServletName() {
        return "";
    }

    @Override
    public ServletContext getServletContext() {
        return this.servletContext;
    }

    @Override
    public String getInitParameter(String s) {
        return "";
    }

    @Override
    public Enumeration<String> getInitParameterNames() {
        return null;
    }
}
