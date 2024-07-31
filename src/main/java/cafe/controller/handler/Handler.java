package cafe.controller.handler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public interface Handler {
    default void handle(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        String method = req.getMethod();
        switch (method) {
            case "GET" : {
                doGet(req, resp);
                return;
            }
            case "POST": {
                doPost(req, resp);
                return;
            }
            case "PUT": {
                doPut(req, resp);
                return;
            }
            case "DELETE": {
                doDelete(req, resp);
                return;
            }
            default: throw new IllegalArgumentException("Unsupported method: " + method);
        }
    }

    default void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        throw new IllegalArgumentException("Unsupported method: GET");
    }

    default void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        throw new IllegalArgumentException("Unsupported method: POST");
    }

    default void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        throw new IllegalArgumentException("Unsupported method: PUT");
    }

    default void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        throw new IllegalArgumentException("Unsupported method: DELETE");
    }
}
