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
import woowa.camp.jspcafe.service.UserService;
import woowa.camp.jspcafe.service.dto.RegistrationRequest;

@WebServlet(name = "userRegistrationServlet", value = "/users/registration")
public class UserRegistrationServlet extends HttpServlet {

    private static final Logger log = LoggerFactory.getLogger(UserRegistrationServlet.class);

    private UserService userService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        ServletContext context = config.getServletContext();
        this.userService = (UserService) context.getAttribute("userService");
        if (this.userService == null) {
            String errorMessage = "[ServletException] UserRegistrationServlet -> UserService not initialized";
            log.error(errorMessage);
            throw new ServletException(errorMessage);
        }
    }

    // 회원가입 폼을 반환
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        log.debug("userRegistrationServlet doGet start");
        RequestDispatcher requestDispatcher = req.getRequestDispatcher("/WEB-INF/views/user/form.jsp");
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

        RegistrationRequest registrationRequest = new RegistrationRequest(userId, password, name, email);
        userService.registration(registrationRequest);

        resp.sendRedirect("/users");
        log.debug("userRegistrationServlet doPost end");
    }
}
