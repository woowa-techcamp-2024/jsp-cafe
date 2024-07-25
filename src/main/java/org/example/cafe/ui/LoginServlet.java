package org.example.cafe.ui;

import static org.example.cafe.utils.LoggerFactory.getLogger;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import org.example.cafe.application.AuthService;
import org.example.cafe.application.dto.LoginDto;
import org.slf4j.Logger;

@WebServlet(name = "LoginServlet", urlPatterns = "/login")
public class LoginServlet extends HttpServlet {

    private static final Logger log = getLogger(LoginServlet.class);

    private AuthService authService;

    @Override
    public void init() {
        this.authService = (AuthService) getServletContext().getAttribute("AuthService");
        log.debug("Init servlet: {}", this.getClass().getSimpleName());
    }

    /**
     * login 페이지를 반환한다.
     *
     * @param request  an {@link HttpServletRequest} object that contains the request the client has made of the
     *                 servlet
     * @param response an {@link HttpServletResponse} object that contains the response the servlet sends to the client
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/WEB-INF/user/login.jsp").forward(request, response);
    }

    /**
     * 아이디, 비밀번호 기반으로 로그인한다. 인증 성공 시 메인 페이지로 이동한다. 인증 실패 시 로그인 실패 페이지로 이동한다.
     *
     * @param request
     * @param response
     * @throws IOException
     */
    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String userId = request.getParameter("userId");
        String password = request.getParameter("password");
        LoginDto loginDto = new LoginDto(userId, password);

        if (!authService.authenticate(loginDto)) {
            request.getRequestDispatcher("/WEB-INF/user/login_failed.jsp").forward(request, response);
            return;
        }

        HttpSession session = request.getSession(true);
        session.setAttribute("userId", userId);
        Cookie cookie = new Cookie("SESSION", session.getId());
        response.addCookie(cookie);

        response.sendRedirect("/");
    }
}