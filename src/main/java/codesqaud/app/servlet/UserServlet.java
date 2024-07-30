package codesqaud.app.servlet;

import codesqaud.app.AuthenticationManager;
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
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static jakarta.servlet.http.HttpServletResponse.*;

@WebServlet(value = "/users/*")
public class UserServlet extends HttpServlet {
    private static final Pattern PROFILE_PATTERN = Pattern.compile("^/users/profile/([1-9][\\d]{0,9})$");
    private static final String LIST_URI = "/users";
    private static final String LOGIN_URI = "/users/login";
    private static final String USER_PROFILE_FORM_URI = "/users/profile";

    private UserDao userDao;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        this.userDao = (UserDao) config.getServletContext().getAttribute("userDao");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (req.getRequestURI().equals(LIST_URI)) {
            handleUserList(req, resp);
            return;
        }

        if (req.getRequestURI().equals(LOGIN_URI)) {
            handleLoginForm(req, resp);
            return;
        }

        if (req.getRequestURI().equals("/users/form")) {
            handleSignUpForm(req, resp);
            return;
        }

        if (req.getRequestURI().equals(USER_PROFILE_FORM_URI)) {
            handleProfileForm(req, resp);
            return;
        }

        Matcher matcher = PROFILE_PATTERN.matcher(req.getRequestURI());
        if (matcher.matches()) {
            long id = Long.parseLong(matcher.group(1));
            handleUserProfile(req, resp, id);
            return;
        }

        throw new HttpException(HttpServletResponse.SC_NOT_FOUND);
    }

    private void handleProfileForm(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
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
        if (req.getRequestURI().equals("/users")) {
            handleSignUp(req, resp);
            return;
        }

        if (req.getRequestURI().equals("/users/login")) {
            handleLogin(req, resp);
            return;
        }

        if (req.getRequestURI().equals("/users/logout")) {
            handleLogout(req, resp);
            return;
        }

        if (req.getRequestURI().equals(USER_PROFILE_FORM_URI)) {
            handleProfileUpdate(req, resp);
            return;
        }

        super.doPost(req, resp);
    }

    private void handleLogout(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        HttpSession session = req.getSession();
        session.invalidate();
        resp.sendRedirect(req.getHeader("Referer"));
    }

    private void handleLogin(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String userId = req.getParameter("userId");
        String password = req.getParameter("password");

        Optional<User> user = userDao.findByUserId(userId);
        if (user.isEmpty() || !user.get().getPassword().equals(password)) {
            req.setAttribute("isFailed", true);
            req.getRequestDispatcher("/WEB-INF/user/login.jsp").forward(req, resp);
            return;
        }

        HttpSession session = req.getSession(true);
        session.setAttribute("loginUser", user.get());
        resp.sendRedirect("/");
    }

    private void handleSignUp(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String userId = req.getParameter("userId");
        String password = req.getParameter("password");
        String name = req.getParameter("name");
        String email = req.getParameter("email");

        User user = new User(userId, password, name, email);
        userDao.save(user);
        resp.sendRedirect("/users");
    }

    private void handleProfileUpdate(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        HttpSession session = AuthenticationManager.getAuthSession(req);

        if (!isCheckedPassword(session)) {
            processPasswordCheck(req, resp, session);
        } else {
            processProfileUpdate(req, resp, session);
        }
    }

    private boolean isCheckedPassword(HttpSession session) {
        Object checkPassword = session.getAttribute("checkPassword");
        return Boolean.TRUE.equals(checkPassword);
    }

    private void processPasswordCheck(HttpServletRequest req, HttpServletResponse resp, HttpSession session) throws ServletException, IOException {
        User loginUser = AuthenticationManager.getLoginUserOrElseThrow(req);

        String password = req.getParameter("password");
        if (!loginUser.getPassword().equals(password)) {
            session.setAttribute("checkPassword", false);
            req.setAttribute("isFailed", true);
        } else {
            session.setAttribute("checkPassword", true);
        }
        req.getRequestDispatcher("/WEB-INF/user/profile_form.jsp").forward(req, resp);
    }

    private void processProfileUpdate(HttpServletRequest req, HttpServletResponse resp, HttpSession session) throws IOException {
        User loginUser = AuthenticationManager.getLoginUserOrElseThrow(req);

        loginUser.setEmail(req.getParameter("email"));
        loginUser.setName(req.getParameter("name"));

        session.removeAttribute("checkPassword");
        userDao.update(loginUser);
        resp.sendRedirect("/users/profile/" + loginUser.getId());
    }
}
