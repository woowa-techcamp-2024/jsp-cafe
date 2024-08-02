package codesqaud.util;

import codesqaud.app.dao.user.UserDao;
import codesqaud.app.model.User;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.http.HttpServletRequest;

import java.util.concurrent.atomic.AtomicLong;

public class LoginUtils {
    private static User user;
    private static AtomicLong value = new AtomicLong(0);

    public static void signupAndLogin(ServletConfig config, HttpServletRequest req) {
        UserDao userDao = (UserDao) config.getServletContext().getAttribute("userDao");
        long additionalValue = value.incrementAndGet();
        user = new User(
                "testUser" + additionalValue,
                "password",
                "Test User" + additionalValue,
                "test@example.com"
        );
        userDao.save(user);
        user = userDao.findByUserId(user.getUserId()).get();

        req.setAttribute("isLogin", true);
        req.getSession(true).setAttribute("loginUser", user);
    }

    public static User getLoginUser() {
        return user;
    }
}
