package codesquad.global.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public class MockFilterChain implements FilterChain {
    private int counter = 0;
    private HttpServletRequest request;
    private HttpServletResponse response;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response) throws IOException, ServletException {
        this.request = (HttpServletRequest) request;
        this.response = (HttpServletResponse) response;
        counter++;
    }

    public int isCalled() {
        return counter;
    }

    public HttpServletRequest getRequest() {
        return request;
    }

    public HttpServletResponse getResponse() {
        return response;
    }
}
