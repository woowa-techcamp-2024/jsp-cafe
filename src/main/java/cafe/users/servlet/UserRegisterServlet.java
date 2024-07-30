package cafe.users.servlet;

import cafe.MappingHttpServlet;
import cafe.users.User;
import cafe.users.repository.UserRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

public class UserRegisterServlet extends MappingHttpServlet {
    private final UserRepository userRepository;

    @Override
    public List<String> mappings() {
        return List.of("/user/register");
    }

    public UserRegisterServlet(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/WEB-INF/views/users/register.jsp").forward(req, resp);
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String userId = request.getParameter("userId");
        String email = request.getParameter("email");
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String confirmPassword = request.getParameter("confirmPassword");

        if (username == null || email == null || password == null || confirmPassword == null) {
            response.sendRedirect("/user/register");
            return;
        }

        // 간단한 회원가입 처리 로직 (예: 유효성 검사, 사용자 저장 등)
        if (password.equals(confirmPassword)) {
            // 회원가입 성공 로직
            User user = new User(userId, email, username, password);
            userRepository.save(user);
            response.sendRedirect("/users");
        } else {
            // 회원가입 실패 로직
            request.setAttribute("errorMessage", "Passwords do not match");
            request.setAttribute("username", username);
            request.setAttribute("email", email);
            request.getRequestDispatcher("/WEB-INF/views/users/userList.jsp").forward(request, response);
        }
    }
}
