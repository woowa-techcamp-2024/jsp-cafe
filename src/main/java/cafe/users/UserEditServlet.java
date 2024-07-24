package cafe.users;

import cafe.MappingHttpServlet;
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
        User user = userRepository.findById(id);
        req.setAttribute("user", user);

        req.getRequestDispatcher("/WEB-INF/views/users/editProfile.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String userId = req.getParameter("userId");
        String email = req.getParameter("email");
        String username = req.getParameter("username");
        String password = req.getParameter("password");
        String confirmPassword = req.getParameter("confirmPassword");
        req.setAttribute("user", new User(userId, email, username, password));

        if (userId == null || email == null|| username == null || password == null || confirmPassword == null
                || userId.isBlank() || email.isBlank() || username.isBlank() || password.isBlank() || confirmPassword.isBlank()
        ) {
            req.setAttribute("errorMessage", "All fields are required");
            req.getRequestDispatcher("/WEB-INF/views/users/editProfile.jsp").forward(req, resp);
            return;
        }

        Long id = Long.valueOf(req.getPathInfo().substring(1));
        User user = userRepository.findById(id);

        if (user == null) {
            req.setAttribute("errorMessage", "User not found");
            req.getRequestDispatcher("/WEB-INF/views/users/editProfile.jsp").forward(req, resp);
            return;
        }


        if (!password.equals(confirmPassword)) {
            req.setAttribute("errorMessage", "Passwords do not match");
            req.getRequestDispatcher("/WEB-INF/views/users/editProfile.jsp").forward(req, resp);
            return;
        }


        if (!userId.equals(user.getUserId())) {
            req.setAttribute("errorMessage", "User ID cannot be changed");
            req.getRequestDispatcher("/WEB-INF/views/users/editProfile.jsp").forward(req, resp);
            return;
        }

        userRepository.save(user.withEmail(email).withUsername(username).withPassword(password));
        resp.sendRedirect("/users");
    }
}
