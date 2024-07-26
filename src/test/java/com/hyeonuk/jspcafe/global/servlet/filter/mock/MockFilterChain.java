package com.hyeonuk.jspcafe.global.servlet.filter.mock;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;

import java.io.IOException;

public class MockFilterChain implements FilterChain {
    private int callCount = 0;
    private ServletRequest servletRequest;
    public int getCallCount(){
        return callCount;
    }

    public ServletRequest getRequest(){
        return servletRequest;
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse) throws IOException, ServletException {
        callCount++;
        this.servletRequest = servletRequest;
    }
}
