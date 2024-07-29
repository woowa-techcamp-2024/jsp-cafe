package cafe.handler.users;

import cafe.handler.Handler;
import cafe.service.UserService;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public class UserInfoHandler implements Handler {
    private final UserService userService;

    public UserInfoHandler(ServletContext context) {
        userService = (UserService) context.getAttribute("userService");
    }

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        req.setAttribute("user", userService.find(req.getRequestURI()));
        RequestDispatcher dispatcher = req.getRequestDispatcher("/WEB-INF/user/profile.jsp");
        dispatcher.forward(req, resp);
    }
}
