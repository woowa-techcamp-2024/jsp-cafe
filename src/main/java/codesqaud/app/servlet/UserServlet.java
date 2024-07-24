package codesqaud.app.servlet;

import codesqaud.app.dao.UserDao;
import codesqaud.app.model.User;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@WebServlet(value = "/users/*")
public class UserServlet extends HttpServlet {
    Pattern USER_PROFILE_PATTERN = Pattern.compile("/users/([1-9][\\d]{0,9})");
    private UserDao userDao;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        this.userDao = (UserDao) config.getServletContext().getAttribute("userDao");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (req.getRequestURI().equals("/users")) {
            handleUserList(req, resp);
            return;
        }

        if (req.getRequestURI().equals("/users/login")) {
            handleLoginForm(req, resp);
            return;
        }

        if (req.getRequestURI().equals("/users/form")) {
            handleSignUpForm(req, resp);
            return;
        }


        Matcher matcher = USER_PROFILE_PATTERN.matcher(req.getRequestURI());
        if (matcher.matches()) {
            long id = Long.parseLong(matcher.group(1));
            handleUserProfile(req, resp, id);
        }
    }

    private void handleSignUpForm(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        RequestDispatcher requestDispatcher = req.getRequestDispatcher("/WEB-INF/user/form.jsp");
        requestDispatcher.forward(req,resp);
    }

    private void handleLoginForm(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        RequestDispatcher requestDispatcher = req.getRequestDispatcher("/WEB-INF/user/login.jsp");
        requestDispatcher.forward(req,resp);
    }

    private void handleUserProfile(HttpServletRequest req, HttpServletResponse resp, long id) throws ServletException, IOException {
        User user = userDao.findById(id).orElseThrow(
                () -> new IllegalArgumentException("해당 아이디를 가진 사용자는 찾을 수 없습니다.")
        );

        req.setAttribute("user", user);
        RequestDispatcher dispatcher = req.getRequestDispatcher("/WEB-INF/user/profile.jsp");
        dispatcher.forward(req, resp);
    }

    private void handleUserList(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<User> uesrs = userDao.findAll();
        req.setAttribute("users", uesrs);

        RequestDispatcher dispatcher = req.getRequestDispatcher("/WEB-INF/user/list.jsp");
        dispatcher.forward(req, resp);
    }


    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        handleSignUp(req, resp);
    }

    private void handleSignUp(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String userId = req.getParameter("userId");
        String password = req.getParameter("password");
        String name = req.getParameter("name");
        String email = req.getParameter("email");

        User user = new User(userId, password, name, email);
        userDao.save(user);
        resp.setStatus(HttpServletResponse.SC_FOUND);
        resp.sendRedirect("/users");
    }
}
