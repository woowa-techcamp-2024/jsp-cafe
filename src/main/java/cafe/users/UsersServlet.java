package cafe.users;

import cafe.MappingHttpServlet;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;

public class UsersServlet extends MappingHttpServlet {
    private static final Logger log = Logger.getLogger(UsersServlet.class.getName());
    private final UserRepository userRepository;

    @Override
    public List<String> mappings() { return List.of("/users"); }

    public UsersServlet(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setAttribute("userList", userRepository.findAll());
        req.getRequestDispatcher("/WEB-INF/views/users/userList.jsp").forward(req, resp);
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("username");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String confirmPassword = request.getParameter("confirmPassword");

        log.info("username: " + username);
        log.info("email: " + email);
        log.info("password: " + password);
        log.info("confirmPassword: " + confirmPassword);

        if (username == null || email == null || password == null || confirmPassword == null) {
            response.sendRedirect("/user/register");
            return;
        }

        // 간단한 회원가입 처리 로직 (예: 유효성 검사, 사용자 저장 등)
        if (password.equals(confirmPassword)) {
            // 회원가입 성공 로직
            User user = new User(email, username, password);
            userRepository.save(user);
            response.sendRedirect("/users");
        } else {
            // 회원가입 실패 로직
            request.setAttribute("errorMessage", "Passwords do not match");
            request.setAttribute("username", username);
            request.setAttribute("email", email);
            request.getRequestDispatcher("/WEB-INF/views/users.jsp").forward(request, response);
        }
    }
}
