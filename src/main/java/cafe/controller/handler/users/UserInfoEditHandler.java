package cafe.controller.handler.users;

import cafe.controller.handler.Handler;
import cafe.domain.entity.User;
import cafe.service.SessionService;
import cafe.service.UserService;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public class UserInfoEditHandler implements Handler {
    private final UserService userService;
    private final SessionService sessionService;

    public UserInfoEditHandler(ServletContext context) {
        userService = (UserService) context.getAttribute("userService");
        sessionService = (SessionService) context.getAttribute("sessionService");
    }

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        String session = req.getSession(true).getId();
        User user = sessionService.findUserBySession(session);
        userService.verifyUserId(user, req.getRequestURI());

        req.setAttribute("user", userService.find(req.getRequestURI()));
        RequestDispatcher dispatcher = req.getRequestDispatcher("/WEB-INF/user/update_form.jsp");
        dispatcher.forward(req, resp);
    }

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String session = req.getSession(true).getId();
        User user = sessionService.findUserBySession(session);
        userService.verifyUserId(user, req.getRequestURI());

        String uri = req.getRequestURI();
        String name = req.getParameter("name");
        String password = req.getParameter("password");
        String email = req.getParameter("email");
        String beforePassword = req.getParameter("before-password");

        userService.update(uri, name, password, email, beforePassword);
        resp.sendRedirect("/users");
    }
}
