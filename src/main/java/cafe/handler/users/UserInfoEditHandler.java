package cafe.handler.users;

import cafe.handler.Handler;
import cafe.service.UserService;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public class UserInfoEditHandler implements Handler {
    private final UserService userService;

    public UserInfoEditHandler(ServletContext context) {
        userService = (UserService) context.getAttribute("userService");
    }

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        req.setAttribute("user", userService.find(req.getRequestURI()));
        RequestDispatcher dispatcher = req.getRequestDispatcher("/WEB-INF/user/update_form.jsp");
        dispatcher.forward(req, resp);
    }

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String uri = req.getRequestURI();
        String name = req.getParameter("name");
        String password = req.getParameter("password");
        String email = req.getParameter("email");
        String beforePassword = req.getParameter("before-password");

        userService.update(uri, name, password, email, beforePassword);
        resp.sendRedirect("/users");
    }
}
