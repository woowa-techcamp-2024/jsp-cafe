package cafe.controller.handler.users;

import cafe.controller.handler.Handler;
import cafe.service.UserService;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public class UserSignUpHandler implements Handler {
    private final UserService userService;

    public UserSignUpHandler(ServletContext context) {
        userService = (UserService) context.getAttribute("userService");
    }

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        RequestDispatcher dispatcher = req.getRequestDispatcher("/WEB-INF/user/form.jsp");
        dispatcher.forward(req, resp);
    }

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String id = req.getParameter("userid");
        String name = req.getParameter("name");
        String password = req.getParameter("password");
        String email = req.getParameter("email");

        userService.save(id, name, password, email);
        resp.sendRedirect("/users");
    }
}
