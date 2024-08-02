package codesquad.global.handler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public interface RequestHandler {
    void handle(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException;
}
