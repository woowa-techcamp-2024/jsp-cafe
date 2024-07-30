package org.example.jspcafe.common;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.example.jspcafe.user.User;

import java.io.IOException;

import static org.example.jspcafe.common.StringUtils.isNumeric;

public class RequestUtil {
    public static Long extractLongPathVariable(HttpServletRequest req) throws IOException {
        String pathInfo = getPathInfo(req);
        String substring = pathInfo.substring(1);
        if (!isNumeric(substring)) {
            throw new IllegalArgumentException("Invalid pathInfo");
        }
        return Long.valueOf(substring);
    }

    public static String extractStringPathVariable(HttpServletRequest req) throws IOException {
        String pathInfo = getPathInfo(req);
        return pathInfo.substring(1);
    }

    public static String getPathInfo(HttpServletRequest req) {
        String pathInfo = req.getPathInfo();
        if (pathInfo == null || pathInfo.length() <= 1) {
            throw new IllegalArgumentException("Invalid pathInfo");
        }
        return pathInfo;
    }

    public static User getUserFromSession(HttpServletRequest req) {
        HttpSession session = req.getSession(false); // false: 세션이 없으면 null을 반환
        return  (User) (session != null ? session.getAttribute("user") : null);
    }
}
