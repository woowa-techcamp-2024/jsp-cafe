package org.example.mock;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletContext;

import java.util.Enumeration;

public class TestServletConfig implements ServletConfig {
    private final ServletContext servletContext;

    public TestServletConfig(ServletContext servletContext) {
        this.servletContext = servletContext;
    }

    @Override
    public ServletContext getServletContext() {
        return servletContext;
    }

    // not impl

    @Override
    public String getServletName() {
        return "";
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
