package codesquad.auth.handler;

import codesquad.common.exception.AuthenticationException;
import codesquad.common.exception.NoSuchElementException;
import codesquad.common.handler.HttpServletRequestHandler;
import codesquad.common.handler.annotation.RequestMapping;
import codesquad.common.handler.annotation.Response;
import codesquad.user.domain.User;
import codesquad.user.service.SignInService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

@Response
@RequestMapping("/login")
public class LoginHandler extends HttpServletRequestHandler {
    private static final Logger logger = LoggerFactory.getLogger(LoginHandler.class);

    private final SignInService signInService;

    public LoginHandler(SignInService signInService) {
        this.signInService = signInService;
    }

    /**
     * 로그인 폼 요청
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        logger.info("login form serve");
        String userId = req.getParameter("userId");
        String password = req.getParameter("password");
        req.setAttribute("userId", userId);
        req.setAttribute("password", password);
        req.getRequestDispatcher("/WEB-INF/views/user/login.jsp").forward(req, resp);
    }

    /**
     * 로그인 요청
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        logger.info("request for login");
        String userId = req.getParameter("userId");
        String password = req.getParameter("password");
        if (userId == null || userId.trim().isBlank() || password == null || password.trim().isBlank()) {
            req.setAttribute("errorMsg", "잘못된 입력입니다.");
            req.setAttribute("userId", userId);
            req.setAttribute("password", password);
            req.getRequestDispatcher("/WEB-INF/views/user/login.jsp").forward(req, resp);
            return;
        }
        User user;
        SignInService.Command command = new SignInService.Command(userId, password);
        try {
            user = signInService.signIn(command);
        } catch (NoSuchElementException | AuthenticationException e) {
            req.setAttribute("errorMsg", "아이디 또는 비밀번호가 틀립니다. 다시 로그인 해주세요.");
            req.setAttribute("userId", userId);
            req.setAttribute("password", password);
            req.getRequestDispatcher("/WEB-INF/views/user/login.jsp").forward(req, resp);
            return;
        }
        HttpSession session = req.getSession(true);
        session.setAttribute("loginUser", user);
        String newSessionId = req.changeSessionId();
        resp.sendRedirect(req.getContextPath() + "/");
    }
}
