package woowa.camp.jspcafe.web.servlet.user;

import static woowa.camp.jspcafe.web.utils.PathVariableExtractor.extractPathVariables;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import woowa.camp.jspcafe.domain.User;
import woowa.camp.jspcafe.service.UserService;
import woowa.camp.jspcafe.service.dto.UserResponse;

@WebServlet(name = "userServlet", value = {"/users", "/users/*"})
public class UserServlet extends HttpServlet {

    private static final Logger log = LoggerFactory.getLogger(UserServlet.class);
    private UserService userService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        log.info("UserServlet init......");
        ServletContext context = config.getServletContext();
        userService = (UserService) context.getAttribute("userService");
        if (this.userService == null) {
            String errorMessage = "[ServletException] UserServlet -> UserService not initialized";
            log.error(errorMessage);
            throw new ServletException(errorMessage);
        }
    }

    // 회원 목록 조회, 회원 프로필 조회
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        log.debug("userServlet doGet start");

        String pathInfo = req.getPathInfo();
        log.info("pathInfo - {}", pathInfo);

        if (pathInfo == null || pathInfo.equals("/")) {
            handleUserList(req, resp);
            log.debug("userServlet doGet end");
            return;
        }

        String contextPath = req.getContextPath();
        Map<String, String> pathVariables = extractPathVariables(contextPath + "/users/{id}", req.getRequestURI());
        long id = Long.parseLong(pathVariables.get("id"));

        handleUserProfile(req, resp, id);
        log.debug("userServlet doGet end");
    }

    private void handleUserList(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<UserResponse> users = userService.findAll();
        req.setAttribute("users", users);

        RequestDispatcher requestDispatcher = req.getRequestDispatcher("/WEB-INF/views/user/list.jsp");
        requestDispatcher.forward(req, resp);
    }

    private void handleUserProfile(HttpServletRequest req, HttpServletResponse resp, Long id)
            throws ServletException, IOException {

        User user = userService.findById(id);
        UserResponse userResponse = UserResponse.of(user);

        req.setAttribute("user", userResponse);
        RequestDispatcher requestDispatcher = req.getRequestDispatcher("/WEB-INF/views/user/profile.jsp");
        requestDispatcher.forward(req, resp);
    }

}
