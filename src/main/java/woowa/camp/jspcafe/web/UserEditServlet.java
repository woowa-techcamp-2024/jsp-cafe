package woowa.camp.jspcafe.web;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import woowa.camp.jspcafe.domain.User;
import woowa.camp.jspcafe.repository.dto.UserUpdateRequest;
import woowa.camp.jspcafe.service.UserService;
import woowa.camp.jspcafe.utils.PathVariableExtractor;

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
        log.debug("userUpdateServlet doGet start");

        Map<String, String> pathVariables = PathVariableExtractor.extractPathVariables("/users/edit/{userId}",
                req.getRequestURI());
        Long userId = Long.parseLong(pathVariables.get("userId"));
        User user = userService.findById(userId);
        req.setAttribute("user", user);

        RequestDispatcher requestDispatcher = req.getRequestDispatcher("/WEB-INF/views/user/update_form.jsp");
        requestDispatcher.forward(req, resp);
        log.debug("userUpdateServlet doGet end");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        log.debug("userUpdateServlet doPost start");

        Map<String, String> pathVariables = PathVariableExtractor.extractPathVariables("/users/edit/{userId}",
                req.getRequestURI());
        Long userId = Long.parseLong(pathVariables.get("userId"));
        String password = req.getParameter("password");
        log.debug("기존 비밀번호 : {}", password);
        String newNickname = req.getParameter("newNickname");
        String newPassword = req.getParameter("newPassword");
        UserUpdateRequest updateRequest = new UserUpdateRequest(newPassword, newNickname);

        userService.updateUserInfo(userId, password, updateRequest);
        String contextPath = req.getContextPath();
        resp.sendRedirect(contextPath + "/users");

        log.debug("userUpdateServlet doPost end");
    }
}
