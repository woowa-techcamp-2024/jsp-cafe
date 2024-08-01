package woowa.camp.jspcafe.web.servlet.user;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import woowa.camp.jspcafe.domain.User;
import woowa.camp.jspcafe.domain.exception.UserException;
import woowa.camp.jspcafe.service.UserService;

@WebServlet(name = "userLoginServlet", value = {"/users/login", "/users/login/fail"})
public class UserLoginServlet extends HttpServlet {

    private static final Logger log = LoggerFactory.getLogger(UserLoginServlet.class);
    private UserService userService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        log.info("UserLoginServlet init......");
        ServletContext context = config.getServletContext();
        userService = (UserService) context.getAttribute("userService");
        if (this.userService == null) {
            String errorMessage = "[ServletException] UserLoginServlet -> UserService not initialized";
            log.error(errorMessage);
            throw new ServletException(errorMessage);
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        log.debug("userLoginServlet doGet start");
        if (!req.getRequestURI().contains("fail")) {
            RequestDispatcher requestDispatcher = req.getRequestDispatcher("/WEB-INF/views/user/login.jsp");
            requestDispatcher.forward(req, resp);
            return;
        }
        RequestDispatcher requestDispatcher = req.getRequestDispatcher("/WEB-INF/views/user/login_failed.jsp");
        requestDispatcher.forward(req, resp);
        log.debug("userLoginServlet doGet end");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        log.debug("userLoginServlet doPost start");

        String email = req.getParameter("email");
        String password = req.getParameter("password");
        try {
            User user = userService.login(email, password);
            HttpSession session = req.getSession();
            session.setAttribute("WOOWA_SESSIONID", user);

            Cookie loginCookie = new Cookie("WOOWA_SESSIONID", String.valueOf(user.getId()));
            loginCookie.setPath("/");
            resp.addCookie(loginCookie);
            resp.sendRedirect(req.getContextPath() + "/");
            log.debug("userLoginServlet doPost end");
        } catch (UserException e) {
            resp.sendRedirect(req.getContextPath() + "/users/login/fail");
        }
    }
}
