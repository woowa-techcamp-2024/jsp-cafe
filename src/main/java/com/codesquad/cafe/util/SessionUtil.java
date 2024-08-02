package com.codesquad.cafe.util;

import com.codesquad.cafe.model.UserPrincipal;
import com.codesquad.cafe.servlet.LoginServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

public final class SessionUtil {
    private SessionUtil() {
    }

    public static UserPrincipal getUserPrincipal(HttpServletRequest request) {
        HttpSession session = request.getSession();
        if (session == null) {
            return null;
        }
        return ((UserPrincipal) session.getAttribute(
                LoginServlet.SESSION_USER_PRINCIPAL_KEY));
    }

}
