package codesquad.mock.http;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;

import java.io.IOException;

public class MockRequestDispatcher implements RequestDispatcher {
    private String path;
    private boolean isForwarded = false;
    private boolean isIncluded = false;

    @Override
    public void forward(ServletRequest request, ServletResponse response) throws ServletException, IOException {
        isForwarded = true;
    }

    @Override
    public void include(ServletRequest request, ServletResponse response) throws ServletException, IOException {
        isIncluded = true;
    }

    public String getPath() {
        return path;
    }

    public boolean isForwarded() {
        return isForwarded;
    }

    public boolean isIncluded() {
        return isIncluded;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
