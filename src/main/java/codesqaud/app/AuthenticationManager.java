package codesqaud.app;

import codesqaud.app.model.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

public class AuthenticationManager {
    private static final Logger log = LoggerFactory.getLogger(AuthenticationManager.class);

    public static boolean isMe(HttpServletRequest request, User user) {
        Optional<User> loginUser = getLoginUser(request);
        if(loginUser.isEmpty()) {
            return false;
        }
        Long loginUserId = loginUser.get().getId();
        return loginUserId.equals(user.getId());
    }

    public static Optional<User> getLoginUser(HttpServletRequest request) {
        boolean isLogin = (Boolean) request.getAttribute("isLogin");
        if (isLogin) {
            HttpSession session = request.getSession(false);
            if (session != null) {
                return Optional.of((User) session.getAttribute("user"));
            }
        }
        return Optional.empty();
    }
}
