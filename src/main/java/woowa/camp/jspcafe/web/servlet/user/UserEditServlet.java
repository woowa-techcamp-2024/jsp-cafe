package woowa.camp.jspcafe.web.servlet.user;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import woowa.camp.jspcafe.domain.User;
import woowa.camp.jspcafe.repository.dto.request.UserUpdateRequest;
import woowa.camp.jspcafe.service.UserService;
import woowa.camp.jspcafe.web.utils.PathVariableExtractor;

@WebServlet(name = "userUpdateServlet", value = "/users/edit/*")
public class UserEditServlet extends HttpServlet {

    private static final Logger log = LoggerFactory.getLogger(UserEditServlet.class);
    private UserService userService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        log.info("UserUpdateServlet init......");
        ServletContext context = config.getServletContext();
        userService = (UserService) context.getAttribute("userService");
        if (this.userService == null) {
            String errorMessage = "[ServletException] UserUpdateServlet -> UserService not initialized";
            log.error(errorMessage);
            throw new ServletException(errorMessage);
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Map<String, String> pathVariables = PathVariableExtractor.extractPathVariables("/users/edit/{userId}",
                req.getRequestURI());
        Long userId = Long.parseLong(pathVariables.get("userId"));

        HttpSession session = req.getSession(false);
        User sessionUser = (User) session.getAttribute("WOOWA_SESSIONID");
        if (isUnAuthorization(sessionUser, userId)) {
            log.warn("[UnAuthorization] 비인가 사용자입니다. - {}, {}", userId, sessionUser);
            resp.sendRedirect(req.getContextPath() + "/");
            return;
        }

        User user = userService.findById(userId);
        req.setAttribute("user", user);

        RequestDispatcher requestDispatcher = req.getRequestDispatcher("/WEB-INF/views/user/update_form.jsp");
        requestDispatcher.forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Map<String, String> pathVariables = PathVariableExtractor.extractPathVariables("/users/edit/{userId}",
                req.getRequestURI());
        Long userId = Long.parseLong(pathVariables.get("userId"));

        HttpSession session = req.getSession(false);
        User sessionUser = (User) session.getAttribute("WOOWA_SESSIONID");
        if (isUnAuthorization(sessionUser, userId)) {
            log.warn("[UnAuthorization] 비인가 사용자입니다. - {}, {}", userId, sessionUser);
            resp.sendRedirect(req.getContextPath() + "/");
            return;
        }

        String password = req.getParameter("password");
        String newNickname = req.getParameter("newNickname");
        String newPassword = req.getParameter("newPassword");
        UserUpdateRequest updateRequest = new UserUpdateRequest(newPassword, newNickname);

        userService.updateUserProfile(userId, password, updateRequest);
        String contextPath = req.getContextPath();
        resp.sendRedirect(contextPath + "/users");
    }

    private boolean isUnAuthorization(User sessionUser, Long userId) throws IOException {
        return !sessionUser.getId().equals(userId);
    }

}
