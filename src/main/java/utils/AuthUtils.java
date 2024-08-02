package utils;

import exception.LoginRequiredException;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

public class AuthUtils {

    public static final String LOGIN_MEMBER = "loginMember";

    public static void checkLogin(HttpSession session) {
        if (!isLoginUser(session)) {
            throw new LoginRequiredException("로그인이 필요합니다.");
        }
    }

    private static boolean isLoginUser(HttpSession session) {
        return session != null && session.getAttribute(LOGIN_MEMBER) != null;
    }

}
