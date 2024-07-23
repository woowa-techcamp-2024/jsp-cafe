package codesqaud.mock;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;

import java.io.IOException;

public class MockRequestDispatcher implements RequestDispatcher {
    private int forwardCounter = 0;
    private String forwardedPath;

    public MockRequestDispatcher(String forwardedPath) {
        this.forwardedPath = forwardedPath;
    }

    public int getForwardCount() {
        return forwardCounter;
    }

    public String getForwardedPath() {
        return forwardedPath;
    }

    @Override
    public void forward(ServletRequest request, ServletResponse response) throws ServletException, IOException {
        forwardCounter++;
    }

    @Override
    public void include(ServletRequest request, ServletResponse response) throws ServletException, IOException {

    }
}
