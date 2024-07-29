package cafe.handler.users;

import cafe.handler.Handler;
import cafe.service.SessionService;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public class UserSignInHandler implements Handler {
    private final SessionService sessionService;

    public UserSignInHandler(ServletContext servletContext) {
        this.sessionService = (SessionService) servletContext.getAttribute("sessionService");
    }

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        RequestDispatcher dispatcher = req.getRequestDispatcher("/WEB-INF/user/login.jsp");
        dispatcher.forward(req, resp);
    }

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        String userid = req.getParameter("userid");
        String password = req.getParameter("password");
        sessionService.signIn(req.getSession().getId(), userid, password);
        resp.sendRedirect("/");
    }
}
