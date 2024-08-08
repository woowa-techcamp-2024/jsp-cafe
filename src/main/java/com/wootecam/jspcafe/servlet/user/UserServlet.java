package com.wootecam.jspcafe.servlet.user;

import com.wootecam.jspcafe.domain.User;
import com.wootecam.jspcafe.service.UserService;
import com.wootecam.jspcafe.servlet.AbstractHttpServlet;
import com.wootecam.jspcafe.servlet.dto.UsersPageResponse;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

public class UserServlet extends AbstractHttpServlet {

    private final UserService userService;

    public UserServlet(final UserService userService) {
        this.userService = userService;
    }

    @Override
    protected void doGet(final HttpServletRequest req, final HttpServletResponse resp)
            throws ServletException, IOException {
        int size = parseSize(req.getParameter("size"));
        int page = parsePage(req.getParameter("page"));

        int userCount = userService.countAll();
        List<User> users = userService.readAll(page, size);
        UsersPageResponse response = UsersPageResponse.of(userCount, page, users);

        req.setAttribute("users", response);

        req.getRequestDispatcher("/WEB-INF/views/user/list.jsp")
                .forward(req, resp);
    }

    private int parseSize(final String size) {
        if (Objects.isNull(size)) {
            return 15;
        }
        return Integer.parseInt(size);
    }

    private int parsePage(final String page) {
        if (Objects.isNull(page)) {
            return 1;
        }
        return Integer.parseInt(page);
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

        resp.sendRedirect("/");
    }
}
