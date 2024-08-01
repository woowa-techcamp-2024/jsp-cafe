package codesquad.ui;

import codesquad.domain.user.User;
import codesquad.domain.user.UserDao;
import codesquad.exception.DuplicateIdException;
import codesquad.exception.IncorrectPasswordException;
import codesquad.ui.annotation.authentication.Authorized;
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
import java.util.List;
import java.util.Optional;

@WebServlet(urlPatterns = "/users")
public class UsersServlet extends HttpServlet {
    private static final Logger logger = LoggerFactory.getLogger(UsersServlet.class);

    private UserDao userDao;

    @Override
    public void init(ServletConfig config) throws ServletException {
        ServletContext servletContext = config.getServletContext();
        userDao = (UserDao) servletContext.getAttribute("userDao");
        logger.info("UsersServlet initialized");
    }

    /**
     * 유저 리스트 요청
     */
    @Authorized
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        logger.info("Listing all users");
        List<User> users = userDao.findAll();
        req.setAttribute("users", users);
        req.getRequestDispatcher("/WEB-INF/views/user/list.jsp").forward(req, resp);
    }

    /**
     * 유저 등록 요청
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        logger.info("Registering user");
        String id = req.getParameter("userId");
        String password = req.getParameter("password");
        String name = req.getParameter("name");
        String email = req.getParameter("email");
        try {
            userDao.save(new User(id, password, name, email));
            resp.sendRedirect(req.getContextPath() + "/users");
        } catch (DuplicateIdException e) {
            req.setAttribute("errorMsg", e.getMessage());
            req.setAttribute("userId", id);
            req.setAttribute("password", password);
            req.setAttribute("name", name);
            req.setAttribute("email", email);
            req.getRequestDispatcher("/WEB-INF/views/user/form.jsp").forward(req, resp);
        }
    }

    /**
     * 유저 수정 요청
     */
    @Authorized
    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        logger.info("Updating user info");
        String id = req.getParameter("id");
        String password = req.getParameter("password");
        String name = req.getParameter("name");
        String email = req.getParameter("email");

        Optional<User> findUser = userDao.findById(Long.parseLong(id));
        if (findUser.isEmpty()) {
            req.setAttribute("errorMsg", "존재하지 않는 유저입니다.");
            req.getRequestDispatcher("/WEB-INF/views/error/error.jsp").forward(req, resp);
            return;
        }
        User user = findUser.get();
        try {
            user.update(password, name, email);
        } catch (IncorrectPasswordException e) {
            req.setAttribute("errorMsg", "잘못된 비밀번호 입니다.");
            req.setAttribute("user", user);
            resp.sendRedirect(req.getContextPath() + "/users/" + user.getId() + "/update-form");
            return;
        }
        userDao.update(user);
        resp.sendRedirect(req.getContextPath() + "/users");
    }
}
