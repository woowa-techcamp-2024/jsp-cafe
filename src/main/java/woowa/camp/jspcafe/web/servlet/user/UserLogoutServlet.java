package woowa.camp.jspcafe.web.servlet.user;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@WebServlet(name = "userLogoutServlet", value = "/users/logout")
public class UserLogoutServlet extends HttpServlet {

    private static final Logger log = LoggerFactory.getLogger(UserLoginServlet.class);

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        if (session != null) {
            if (session.getAttribute("WOOWA_SESSIONID") != null) {
                log.info("loggedInUser 세션이 존재합니다");
                session.removeAttribute("WOOWA_SESSIONID");
            }
        }

        Cookie[] cookies = req.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("WOOWA_SESSIONID".equals(cookie.getName())) {
                    cookie.setValue("");
                    cookie.setPath("/");
                    cookie.setMaxAge(0);
                    resp.addCookie(cookie);
                    break;
                }
            }
        }

        resp.sendRedirect(req.getContextPath() + "/");
    }

}
