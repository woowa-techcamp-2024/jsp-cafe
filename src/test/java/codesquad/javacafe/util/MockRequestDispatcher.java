package codesquad.javacafe.util;

import codesquad.javacafe.util.CustomHttpServletResponse;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import java.io.IOException;

public class MockRequestDispatcher implements RequestDispatcher {
    private String path;

    public MockRequestDispatcher(String path) {
        this.path = path;
    }

    @Override
    public void forward(ServletRequest request, ServletResponse response) throws ServletException, IOException {
        ((CustomHttpServletResponse) response).setForwardedUrl(path);
    }

    @Override
    public void include(ServletRequest request, ServletResponse response) throws ServletException, IOException {
        // No-op for this mock implementation
    }
}