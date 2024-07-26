package cafe.users.servlet;

import cafe.MappingHttpServlet;
import cafe.users.User;
import cafe.users.repository.UserRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

public class UserLoginServlet extends MappingHttpServlet {
    private final UserRepository userRepository;

    @Override
    public List<String> mappings() {
        return List.of("/user/login");
    }

    public UserLoginServlet(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/WEB-INF/views/users/login.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String userId = req.getParameter("userId");
        String password = req.getParameter("password");

        if (userId == null || password == null || userId.isBlank() || password.isBlank()) {
            req.setAttribute("errorMessage", "All fields are required");
            req.getRequestDispatcher("/WEB-INF/views/users/login.jsp").forward(req, resp);
            return;
        }

        User user = userRepository.findByUserId(userId);
        if (user == null || !user.getPassword().equals(password)) {
            req.setAttribute("errorMessage", "Invalid credentials");
            req.getRequestDispatcher("/WEB-INF/views/users/login.jsp").forward(req, resp);
            return;
        }

        req.getSession().setAttribute("user", user);
        resp.sendRedirect("/");
    }
}
