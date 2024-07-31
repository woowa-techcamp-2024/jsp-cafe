package com.wootecam.jspcafe.servlet.user;

import com.wootecam.jspcafe.domain.User;
import com.wootecam.jspcafe.service.UserService;
import com.wootecam.jspcafe.servlet.AbstractHttpServlet;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Optional;

public class SignInServlet extends AbstractHttpServlet {

    private final UserService userService;

    public SignInServlet(final UserService userService) {
        this.userService = userService;
    }

    @Override
    protected void doGet(final HttpServletRequest req, final HttpServletResponse resp)
            throws ServletException, IOException {

        req.getRequestDispatcher("/WEB-INF/views/user/login.jsp")
                .forward(req, resp);
    }

    @Override
    protected void doPost(final HttpServletRequest req, final HttpServletResponse resp)
            throws ServletException, IOException {
        Optional<User> user = userService.signIn(
                req.getParameter("userId"),
                req.getParameter("password")
        );

        if (user.isEmpty()) {
            req.getRequestDispatcher("/WEB-INF/views/user/login_failed.jsp")
                    .forward(req, resp);
            return;
        }

        HttpSession session = req.getSession();
        session.setAttribute("signInUser", user.get());

        resp.sendRedirect("/");
    }
}
