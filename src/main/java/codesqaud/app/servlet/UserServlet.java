package codesqaud.app.servlet;

import codesqaud.app.dao.user.UserDao;
import codesqaud.app.exception.HttpException;
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

import static jakarta.servlet.http.HttpServletResponse.*;

@WebServlet(value = "/users/*")
public class UserServlet extends HttpServlet {
    Pattern USER_PROFILE_PATTERN = Pattern.compile("^/users/([1-9][\\d]{0,9})$");
    Pattern USER_PROFILE_FORM_PATTERN = Pattern.compile("^/users/profile/([1-9][\\d]{0,9})$");
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
            return;
        }

        matcher = USER_PROFILE_FORM_PATTERN.matcher(req.getRequestURI());
        if (matcher.matches()) {
            //TODO: 로그인한 사용자만 수정 가능하도록 변경
            long id = Long.parseLong(matcher.group(1));
            handleProfileForm(req, resp, id);
            return;
        }

        throw new HttpException(HttpServletResponse.SC_NOT_FOUND);
    }

    private void handleProfileForm(HttpServletRequest req, HttpServletResponse resp, long id) throws ServletException, IOException {
        User user = userDao.findById(id).orElseThrow(() -> new HttpException(SC_NOT_FOUND));
        req.setAttribute("user", user);
        req.getRequestDispatcher("/WEB-INF/user/profile_form.jsp").forward(req, resp);
    }

    private void handleSignUpForm(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        RequestDispatcher requestDispatcher = req.getRequestDispatcher("/WEB-INF/user/form.jsp");
        requestDispatcher.forward(req, resp);
    }

    private void handleLoginForm(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        RequestDispatcher requestDispatcher = req.getRequestDispatcher("/WEB-INF/user/login.jsp");
        requestDispatcher.forward(req, resp);
    }

    private void handleUserProfile(HttpServletRequest req, HttpServletResponse resp, long id) throws ServletException, IOException {
        User user = userDao.findById(id).orElseThrow(
                () -> new HttpException(SC_NOT_FOUND, "해당 아이디를 가진 사용자는 찾을 수 없습니다.")
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
        if(req.getRequestURI().equals("/users")) {
            handleSignUp(req, resp);
            return;
        }

        Matcher matcher = USER_PROFILE_PATTERN.matcher(req.getRequestURI());
        if (matcher.matches()) {
            //TODO: 로그인한 사용자만 수정 가능하도록 변경
            long id = Long.parseLong(matcher.group(1));
            handleProfileUpdate(req, resp, id);
            return;
        }
    }

    private void handleSignUp(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String userId = req.getParameter("userId");
        String password = req.getParameter("password");
        String name = req.getParameter("name");
        String email = req.getParameter("email");

        User user = new User(userId, password, name, email);
        userDao.save(user);
        resp.setStatus(SC_FOUND);
        resp.sendRedirect("/users");
    }

    private void handleProfileUpdate(HttpServletRequest req, HttpServletResponse resp, long id) throws IOException {
        User user = userDao.findById(id)
                .orElseThrow(() -> new HttpException(SC_NOT_FOUND, "해당 사용자를 찾을 수 없습니다."));

        user.setEmail(req.getParameter("email"));
        user.setName(req.getParameter("name"));
        user.setPassword(req.getParameter("password"));

        userDao.update(user);

        resp.sendRedirect("/users/" + id);
    }
}
