package org.example.cafe.ui;

import static org.example.cafe.utils.LoggerFactory.getLogger;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import org.slf4j.Logger;

@WebServlet(name = "LogoutServlet", urlPatterns = "/logout")
public class LogoutServlet extends HttpServlet {

    private static final Logger log = getLogger(LogoutServlet.class);

    @Override
    public void init() {
        log.debug("Init servlet: {}", this.getClass().getSimpleName());
    }

    /**
     * 아이디, 비밀번호 기반으로 로그인한다. 인증 성공 시 메인 페이지로 이동한다. 인증 실패 시 로그인 실패 페이지로 이동한다.
     *
     * @param request
     * @param response
     * @throws IOException
     */
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession();
        if (session == null || session.getAttribute("userId") == null) {
            response.sendRedirect("/login");
            return;
        }

        session.invalidate();
        response.addCookie(new Cookie("SESSION", ""));
        response.sendRedirect("/");
    }
}