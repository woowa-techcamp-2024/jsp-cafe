package codesquad.servlet;

import codesquad.domain.user.User;
import codesquad.domain.user.UserDao;
import codesquad.servlet.annotation.authentication.Authorized;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Optional;

@WebServlet(urlPatterns = {"/users/*", "/users/*/update-form"})
public class UserServlet extends HttpServlet {
    private static final Logger logger = LoggerFactory.getLogger(UserServlet.class);

    private UserDao userDao;

    @Override
    public void init(ServletConfig config) throws ServletException {
        ServletContext servletContext = config.getServletContext();
        userDao = (UserDao) servletContext.getAttribute("userDao");
    }

    /**
     * 유저 수정 폼 요청
     * 유저 정보 요청
     */
    @Authorized
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pathInfo = req.getPathInfo();
        if (pathInfo.endsWith("/update-form")) {
            logger.info("requesting user update form");
            processUpdateForm(req, resp);
        } else {
            logger.info("requesting user info");
            processUserInfo(req, resp);
        }
    }

    private void processUserInfo(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        long userId;
        try {
            userId = getUserId(req.getPathInfo());
        } catch (NumberFormatException e) {
            req.setAttribute("errorMsg", "올바르지 않은 요청입니다.");
            req.getRequestDispatcher("/WEB-INF/views/error/error.jsp").forward(req, resp);
            return;
        }
        Optional<User> findUser = userDao.findById(userId);
        if (findUser.isEmpty()) {
            req.setAttribute("errorMsg", "존재하지 않는 유저입니다.");
            req.getRequestDispatcher("/WEB-INF/views/error/error.jsp").forward(req, resp);
            return;
        }
        req.setAttribute("user", findUser.get());
        req.getRequestDispatcher("/WEB-INF/views/user/profile.jsp").forward(req, resp);
    }

    private void processUpdateForm(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        long userId;
        try {
            userId = getUserId(req.getPathInfo());
        } catch (NumberFormatException e) {
            req.setAttribute("errorMsg", "올바르지 않은 요청입니다.");
            req.getRequestDispatcher("/WEB-INF/views/error/error.jsp").forward(req, resp);
            return;
        }
        Optional<User> findUser = userDao.findById(userId);
        if (findUser.isEmpty()) {
            req.setAttribute("errorMsg", "존재하지 않는 유저입니다.");
            req.getRequestDispatcher("/WEB-INF/views/error/error.jsp").forward(req, resp);
            return;
        }
        req.setAttribute("user", findUser.get());
        req.getRequestDispatcher("/WEB-INF/views/user/update-form.jsp").forward(req, resp);
    }

    private long getUserId(String pathInfo) throws NumberFormatException {
        if (pathInfo == null || !pathInfo.startsWith("/")) {
            throw new NumberFormatException("Invalid path info");
        }
        if (pathInfo.endsWith("/update-form")) {
            return Long.parseLong(pathInfo.substring(1, pathInfo.indexOf("/update-form")));
        }
        return Long.parseLong(pathInfo.substring(1));
    }
}
