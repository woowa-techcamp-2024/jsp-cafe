package codesquad.servlet;

import codesquad.domain.user.User;
import codesquad.domain.user.UserDao;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.Optional;

@WebServlet(urlPatterns = "/login")
public class LoginServlet extends HttpServlet {
    private UserDao userDao;

    @Override
    public void init(ServletConfig config) throws ServletException {
        ServletContext servletContext = config.getServletContext();
        userDao = (UserDao) servletContext.getAttribute("userDao");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String userId = req.getParameter("userId");
        String password = req.getParameter("password");
        req.setAttribute("userId", userId);
        req.setAttribute("password", password);
        req.getRequestDispatcher("/WEB-INF/views/user/login.jsp").forward(req, resp);
    }

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
        Optional<User> findUser = userDao.findByUserId(userId);
        if (findUser.isEmpty()) {
            req.setAttribute("errorMsg", "아이디 또는 비밀번호가 틀립니다. 다시 로그인 해주세요.");
            req.setAttribute("userId", userId);
            req.setAttribute("password", password);
            req.getRequestDispatcher("/WEB-INF/views/user/login.jsp").forward(req, resp);
            return;
        }
        User user = findUser.get();
        boolean matches = user.matches(password);
        if (matches) {
            HttpSession session = req.getSession(true);
            session.setAttribute("loginUser", user);
            String newSessionId = req.changeSessionId();
            resp.sendRedirect(req.getContextPath() + "/");
            return;
        }
        req.setAttribute("errorMsg", "아이디 또는 비밀번호가 틀립니다. 다시 로그인 해주세요.");
        req.setAttribute("userId", userId);
        req.setAttribute("password", password);
        req.getRequestDispatcher("/WEB-INF/views/user/login.jsp").forward(req, resp);
    }
}
