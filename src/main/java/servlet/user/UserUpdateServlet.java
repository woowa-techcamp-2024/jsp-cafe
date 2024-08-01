package servlet.user;

import domain.User;
import exception.UnAuthorizedException;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.UserService;
import utils.AuthUtils;

import java.io.IOException;


@WebServlet("/users/*")
public class UserUpdateServlet extends HttpServlet {

    private UserService userService;
    Logger log = LoggerFactory.getLogger(UserServlet.class);

    @Override
    public void init(ServletConfig config) {
        ServletContext context = config.getServletContext();
        this.userService = (UserService) context.getAttribute("userService");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        log.info("ProfileServlet doGet");

        String[] pathParts = req.getPathInfo().split("/");
        Long userId = Long.parseLong(pathParts[1]);

        HttpSession session = req.getSession(false);
        AuthUtils.checkLogin(session);
        checkAuthorization(session, userId, "수정");

        User user = userService.findById(userId);
        req.setAttribute("user", user);

        req.getRequestDispatcher("/user/updateForm.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        log.info("ProfileServlet doPost");

        String userId = req.getParameter("userId");
        String password = req.getParameter("password");
        String newPassword = req.getParameter("newPassword");
        String name = req.getParameter("name");
        String email = req.getParameter("email");

        AuthUtils.checkLogin(req.getSession(false));
        checkAuthorization(req.getSession(false), Long.parseLong(userId), "수정");

        log.info("userId: {}, password: {}, newPassword: {}, name: {}, email: {}", userId, password, newPassword, name, email);
        userService.changeProfile(userId, newPassword, name, email, password);
        resp.sendRedirect("/users");
    }

    private void checkAuthorization(HttpSession session, Long userId, String action) {
        // 수정(삭제) 대상과 로그인 유저가 다르면 예외 발생
        User loginUser = (User) session.getAttribute(AuthUtils.LOGIN_MEMBER);
        if (!loginUser.getId().equals(userId)) {
            throw new UnAuthorizedException("다른 사용자의 정보를 " + action + "할 수 없습니다.");
        }
    }

}
