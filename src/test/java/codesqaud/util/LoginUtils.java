package codesqaud.util;

import codesqaud.app.dao.user.UserDao;
import codesqaud.app.model.User;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.http.HttpServletRequest;

public class LoginUtils {
    private static User user;

    public static void login(ServletConfig config, HttpServletRequest req) {
        UserDao userDao = (UserDao) config.getServletContext().getAttribute("userDao");
        user = new User("testUser", "password", "Test User", "test@example.com");
        userDao.save(user);

        req.setAttribute("isLogin", true);
        req.getSession().setAttribute("loginUser", user);
    }

    public static User getLoginUser() {
        return user;
    }
}
