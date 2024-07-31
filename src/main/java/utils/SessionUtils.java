package utils;

import camp.woowa.jspcafe.model.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

public class SessionUtils {
    private SessionUtils() {}
    public static User getSessionUser(HttpServletRequest req, HttpServletResponse resp) {
        HttpSession session = req.getSession(false);

        if (session == null) {
            return null;
        }

        return (User) session.getAttribute("user");
    }
}
