package org.example.mock;

import jakarta.servlet.RequestDispatcher;


public class TestRequestDispatcher implements RequestDispatcher {
    private boolean forwarded = false;
    private String forwardedPath;

    @Override
    public void forward(jakarta.servlet.ServletRequest request, jakarta.servlet.ServletResponse response) {
        forwarded = true;
        forwardedPath = ((TestHttpServletRequest) request).getRequestURI();
    }

    @Override
    public void include(jakarta.servlet.ServletRequest request, jakarta.servlet.ServletResponse response) {
        // 구현 필요 없음
    }

    public boolean isForwarded() {
        return forwarded;
    }

    public String getForwardedPath() {
        return forwardedPath;
    }
}
