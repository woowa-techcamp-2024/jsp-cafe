package cafe.users.servlet;

import cafe.MappingHttpServlet;
import cafe.users.repository.UserRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;

public class UsersServlet extends MappingHttpServlet {
    private static final Logger log = Logger.getLogger(UsersServlet.class.getName());
    private final UserRepository userRepository;

    @Override
    public List<String> mappings() {
        return List.of("/users");
    }

    public UsersServlet(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setAttribute("userList", userRepository.findAll());
        req.getRequestDispatcher("/WEB-INF/views/users/userList.jsp").forward(req, resp);
    }
}
