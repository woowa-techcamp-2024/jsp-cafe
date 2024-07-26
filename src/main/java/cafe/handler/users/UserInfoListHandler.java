package cafe.handler.users;

import cafe.domain.entity.User;
import cafe.handler.Handler;
import cafe.service.UserService;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public class UserInfoListHandler implements Handler {
    private UserService userService;

    public UserInfoListHandler(ServletContext context) {
        userService = (UserService) context.getAttribute("userService");
    }

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        req.setAttribute("users", userService.findAll());
        RequestDispatcher dispatcher = req.getRequestDispatcher("/WEB-INF/user/list.jsp");
        dispatcher.forward(req, resp);
    }

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        doGet(req, resp);
    }
}
