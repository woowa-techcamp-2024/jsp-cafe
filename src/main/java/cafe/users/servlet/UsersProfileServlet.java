package cafe.users.servlet;

import cafe.MappingHttpServlet;
import cafe.users.User;
import cafe.users.repository.UserRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;

public class UsersProfileServlet extends MappingHttpServlet {
    private static final Logger log = Logger.getLogger(UsersProfileServlet.class.getName());
    private final UserRepository userRepository;

    @Override
    public List<String> mappings() {
        return List.of("/users/*");
    }

    public UsersProfileServlet(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Long id = Long.parseLong(req.getPathInfo().substring(1));
        User user = userRepository.findById(id);
        log.info("User: " + user);
        if (user == null) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        req.setAttribute("user", user);
        req.getRequestDispatcher("/WEB-INF/views/users/profile.jsp").forward(req, resp);
    }
}
