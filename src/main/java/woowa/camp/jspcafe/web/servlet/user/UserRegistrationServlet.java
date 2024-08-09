package woowa.camp.jspcafe.web.servlet.user;

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
import woowa.camp.jspcafe.service.dto.request.RegistrationRequest;

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
        RequestDispatcher requestDispatcher = req.getRequestDispatcher("/WEB-INF/views/user/form.jsp");
        requestDispatcher.forward(req, resp);
    }

    // 회원가입 처리
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String email = req.getParameter("email");
        String password = req.getParameter("password");
        String nickname = req.getParameter("nickname");

        RegistrationRequest registrationRequest = new RegistrationRequest(email, nickname, password);
        userService.registration(registrationRequest);

        String contextPath = req.getContextPath();
        resp.sendRedirect(contextPath + "/users");
    }
}
