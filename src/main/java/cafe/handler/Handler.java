package cafe.handler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public interface Handler {
    default void handle(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        String method = req.getMethod();
        switch (method) {
            case "GET": doGet(req, resp);
            case "POST": doPost(req, resp);
            default: throw new IllegalArgumentException("Unsupported method: " + method);
        }
    }

    default void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        throw new IllegalArgumentException("Unsupported method: GET");
    }

    default void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        throw new IllegalArgumentException("Unsupported method: POST");
    }
}
