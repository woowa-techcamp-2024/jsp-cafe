package codesquad.global.servlet;

import codesquad.common.exception.AuthenticationException;
import codesquad.common.exception.NoSuchElementException;
import codesquad.user.domain.User;
import codesquad.user.service.SignInService;
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

import java.io.IOException;

@WebServlet(urlPatterns = "/login")
public class LoginServlet extends HttpServlet {
    private static final Logger logger = LoggerFactory.getLogger(LoginServlet.class);

    private SignInService signInService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        ServletContext servletContext = config.getServletContext();
        signInService = (SignInService) servletContext.getAttribute("SignInService");
        logger.info("LoginServlet initialized");
    }

    /**
     * 로그인 폼 요청
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
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
