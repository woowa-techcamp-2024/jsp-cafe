package org.example.util;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import java.util.Optional;

public class SessionUtil {
    public static Optional<String> extractUserId(HttpServletRequest httpServletRequest) {
        HttpSession session = httpServletRequest.getSession();
        if (session == null) {
            return Optional.empty();
        }
        String ret = (String) session.getAttribute("userId");

        return Optional.ofNullable(ret);
    }
}
