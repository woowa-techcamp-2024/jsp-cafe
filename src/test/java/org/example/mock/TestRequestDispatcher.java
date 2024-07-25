package org.example.mock;

import jakarta.servlet.RequestDispatcher;


public class TestRequestDispatcher implements RequestDispatcher {
    private boolean forwarded = false;
    private String forwardedPath;

    public TestRequestDispatcher() {
    }

    public TestRequestDispatcher(String forwardedPath) {
        this.forwardedPath = forwardedPath;
    }

    @Override
    public void forward(jakarta.servlet.ServletRequest request, jakarta.servlet.ServletResponse response) {
        forwarded = true;
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
