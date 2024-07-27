package com.woowa.hyeonsik.application.servlet;

import com.woowa.hyeonsik.application.domain.User;
import com.woowa.hyeonsik.application.service.UserService;
import com.woowa.hyeonsik.application.util.SendPageUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoginServlet extends HttpServlet {
    private static final Logger logger = LoggerFactory.getLogger(LoginServlet.class);
    private final UserService userService;

    public LoginServlet(final UserService userService) {
        this.userService = userService;
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String userId = request.getParameter("userId");
        String password = request.getParameter("password");

        try {
            userService.validateUser(userId, password);
            final User foundUser = userService.findByUserId(userId);

            HttpSession session = request.getSession();
            session.setAttribute("user", foundUser);
            logger.debug("세션을 통해 유저 정보를 저장합니다. {}: {}", session.getId(), foundUser.getUserId());
            SendPageUtil.redirect("", getServletContext(), response);
        } catch (IllegalArgumentException e) {
            SendPageUtil.redirect("/auth/login_failed.jsp", getServletContext(), response);
        }
    }
}
