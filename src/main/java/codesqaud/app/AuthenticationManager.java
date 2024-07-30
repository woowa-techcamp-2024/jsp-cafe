package codesqaud.app;

import codesqaud.app.exception.HttpException;
import codesqaud.app.model.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
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
        Object isLogin = request.getAttribute("isLogin");
        if (Boolean.TRUE.equals(isLogin)) {
            HttpSession session = request.getSession(false);
            if (session != null) {
                return Optional.of((User) session.getAttribute("loginUser"));
            }
        }
        return Optional.empty();
    }

    public static User getLoginUserOrElseThrow(HttpServletRequest request) {
        return getLoginUser(request)
                .orElseThrow(() -> new HttpException(HttpServletResponse.SC_UNAUTHORIZED, "로그인 후 이용해 주세요"));
    }

    public static HttpSession getAuthSession(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null) {
            throw new HttpException(HttpServletResponse.SC_UNAUTHORIZED, "세션이 만료되었습니다. 다시 로그인 해주세요.");
        }
        return session;
    }
}
