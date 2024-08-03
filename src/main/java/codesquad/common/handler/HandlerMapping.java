package codesquad.common.handler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.regex.Pattern;

public class HandlerMapping {
    private final Pattern pattern;
    private final RequestHandler handler;

    public HandlerMapping(Pattern pattern, RequestHandler handler) {
        this.pattern = pattern;
        this.handler = handler;
    }

    public boolean matches(String url) {
        return pattern.matcher(url).matches();
    }

    public void handle(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        handler.handle(request, response);
    }
}
