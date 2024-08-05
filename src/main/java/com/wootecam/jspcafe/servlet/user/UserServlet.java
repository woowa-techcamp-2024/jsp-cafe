package com.wootecam.jspcafe.servlet.user;

import com.wootecam.jspcafe.domain.User;
import com.wootecam.jspcafe.service.UserService;
import com.wootecam.jspcafe.servlet.AbstractHttpServlet;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class UserServlet extends AbstractHttpServlet {

    private final UserService userService;

    public UserServlet(final UserService userService) {
        this.userService = userService;
    }

    @Override
    protected void doGet(final HttpServletRequest req, final HttpServletResponse resp)
            throws ServletException, IOException {
        List<User> users = userService.readAll();

        req.setAttribute("users", users);

        req.getRequestDispatcher("/WEB-INF/views/user/list.jsp")
                .forward(req, resp);
    }

    @Override
    protected void doPost(final HttpServletRequest req, final HttpServletResponse resp)
            throws ServletException, IOException {
        userService.signup(
                req.getParameter("userId"),
                req.getParameter("password"),
                req.getParameter("name"),
                req.getParameter("email")
        );

        resp.sendRedirect("/users");
    }
}
