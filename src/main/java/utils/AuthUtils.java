package utils;

import jakarta.servlet.http.HttpSession;

public class AuthUtils {

    public static boolean isLoginUser(HttpSession session) {
        return session != null && session.getAttribute("loginMember") != null;
    }

}
