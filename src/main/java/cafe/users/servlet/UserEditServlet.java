package cafe.users.servlet;

import cafe.MappingHttpServlet;
import cafe.users.User;
import cafe.users.repository.UserRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

public class UserEditServlet extends MappingHttpServlet {
    private final UserRepository userRepository;

    @Override
    public List<String> mappings() {
        return List.of("/users/edit/*");
    }

    public UserEditServlet(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Long id = Long.valueOf(req.getPathInfo().substring(1));
        User loginUser = (User) req.getSession().getAttribute("user");

        if (loginUser == null || !loginUser.getId().equals(id)) {
            resp.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        User user = userRepository.findById(id);
        req.setAttribute("user", user);
        req.getRequestDispatcher("/WEB-INF/views/users/editProfile.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String userId = req.getParameter("userId");
        String email = req.getParameter("email");
        String username = req.getParameter("username");
        String currentPassword = req.getParameter("currentPassword");
        String newPassword = req.getParameter("newPassword");
        String confirmPassword = req.getParameter("confirmPassword");
        Long id = Long.valueOf(req.getPathInfo().substring(1));
        req.setAttribute("user", new User(userId, email, username, newPassword).withId(id));

        if (userId == null || email == null || username == null
                || userId.isBlank() || email.isBlank() || username.isBlank()
        ) {
            req.setAttribute("errorMessage", "All fields are required");
            req.getRequestDispatcher("/WEB-INF/views/users/editProfile.jsp").forward(req, resp);
            return;
        }

        User user = userRepository.findById(id);

        if (user == null) {
            req.setAttribute("errorMessage", "User not found");
            req.getRequestDispatcher("/WEB-INF/views/users/editProfile.jsp").forward(req, resp);
            return;
        }

        if (!user.getPassword().equals(currentPassword)) {
            req.setAttribute("errorMessage", "Current password is incorrect");
            req.getRequestDispatcher("/WEB-INF/views/users/editProfile.jsp").forward(req, resp);
            return;
        }

        if (newPassword != null && confirmPassword != null && !newPassword.isBlank() && !confirmPassword.isBlank()) {
            if (newPassword.equals(confirmPassword)) {
                currentPassword = newPassword;
            }
        }

        if (!userId.equals(user.getUserId())) {
            req.setAttribute("errorMessage", "User ID cannot be changed");
            req.getRequestDispatcher("/WEB-INF/views/users/editProfile.jsp").forward(req, resp);
            return;
        }

        userRepository.save(user.withEmail(email).withUsername(username).withPassword(currentPassword));
        resp.sendRedirect("/users");
    }
}