package cafe.controller.handler.users;

import cafe.controller.handler.Handler;
import cafe.service.SessionService;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public class UserSignOutHandler implements Handler {
    private final SessionService sessionService;

    public UserSignOutHandler(ServletContext servletContext) {
        this.sessionService = (SessionService) servletContext.getAttribute("sessionService");
    }

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        sessionService.signOut(req.getSession().getId());
        resp.sendRedirect("/");
    }
}
