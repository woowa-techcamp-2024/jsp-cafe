package codesquad.javacafe.common;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public interface SubController {
    void doProcess(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException;
}
