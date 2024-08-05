package com.wootecam.jspcafe.servlet.user;

import com.wootecam.jspcafe.domain.User;
import com.wootecam.jspcafe.service.UserService;
import com.wootecam.jspcafe.servlet.AbstractHttpServlet;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

public class UserProfileServlet extends AbstractHttpServlet {

    private final UserService userService;

    public UserProfileServlet(final UserService userService) {
        this.userService = userService;
    }

    @Override
    protected void doGet(final HttpServletRequest req, final HttpServletResponse resp)
            throws ServletException, IOException {
        Long id = parseSuffixPathVariable(req.getPathInfo());
        User user = userService.read(id);

        req.setAttribute("user", user);
        req.getRequestDispatcher("/WEB-INF/views/user/profile.jsp").forward(req, resp);
    }

    private Long parseSuffixPathVariable(final String pathInfo) {
        String[] splitPaths = pathInfo.split("/");

        return Long.parseLong(splitPaths[splitPaths.length - 1].trim());
    }
}
