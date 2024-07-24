package codesquad.servlet;

import codesquad.exception.DuplicateIdException;
import codesquad.user.InMemoryUserDao;
import codesquad.user.User;
import codesquad.user.UserDao;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;

@WebServlet(urlPatterns = "/users")
public class UsersServlet extends HttpServlet {
    private static final Logger logger = LoggerFactory.getLogger(UsersServlet.class);

    private final UserDao userDao = new InMemoryUserDao();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        logger.info("Listing all users");
        List<User> users = userDao.findAll();
        req.setAttribute("users", users);
        req.getRequestDispatcher("/user/list.jsp").forward(req, resp);
    }

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
            req.getRequestDispatcher("/user/form.jsp").forward(req, resp);
        }
    }
}
