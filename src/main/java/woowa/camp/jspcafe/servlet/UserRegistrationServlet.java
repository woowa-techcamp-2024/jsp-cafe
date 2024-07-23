package woowa.camp.jspcafe.servlet;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import woowa.camp.jspcafe.domain.User;
import woowa.camp.jspcafe.repository.UserRepository;

@WebServlet(name = "userRegistrationServlet", value = "/users/registration")
public class UserRegistrationServlet extends HttpServlet {

    private static final Logger log = LoggerFactory.getLogger(UserRegistrationServlet.class);
    private UserRepository userRepository;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        ServletContext context = config.getServletContext();
        this.userRepository = (UserRepository) context.getAttribute("userRepository");
        if (this.userRepository == null) {
            throw new ServletException("UserRegistrationServlet -> UserRepository not initialized");
        }
    }

    // 회원가입 폼을 반환
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        log.debug("userRegistrationServlet doGet start");
        RequestDispatcher requestDispatcher = req.getRequestDispatcher("/static/user/form.html");
        requestDispatcher.forward(req, resp);
        log.debug("userRegistrationServlet doGet end");
    }

    // 회원가입 처리
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        log.debug("userRegistrationServlet doPost start");
        String userId = req.getParameter("userId");
        String password = req.getParameter("password");
        String name = req.getParameter("name");
        String email = req.getParameter("email");

        log.info("userId - {}", userId);
        log.info("password - {}", password);
        log.info("name - {}", name);
        log.info("email - {}", email);

        User user = new User(userId, password, name, email);
        userRepository.save(user);

        resp.sendRedirect("/users");
        log.debug("userRegistrationServlet doPost end");
    }
}
