package com.wootecam.jspcafe.servlet.user;

import com.wootecam.jspcafe.domain.User;
import com.wootecam.jspcafe.service.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

public class UserEditServlet extends HttpServlet {

    private final UserService userService;

    public UserEditServlet(final UserService userService) {
        this.userService = userService;
    }

    @Override
    protected void doGet(final HttpServletRequest req, final HttpServletResponse resp)
            throws ServletException, IOException {
        Long id = parseSuffixPathVariable(req.getPathInfo());
        User user = userService.read(id);

        req.setAttribute("user", user);
        req.getRequestDispatcher("/WEB-INF/views/user/update_form.jsp")
                .forward(req, resp);
    }

    private Long parseSuffixPathVariable(final String pathInfo) {
        String[] splitPaths = pathInfo.split("/");

        return Long.parseLong(splitPaths[splitPaths.length - 1].trim());
    }

    @Override
    protected void doPost(final HttpServletRequest req, final HttpServletResponse resp)
            throws ServletException, IOException {
        Long id = parseSuffixPathVariable(req.getPathInfo());

        userService.edit(
                id,
                req.getParameter("originalPassword"),
                req.getParameter("newPassword"),
                req.getParameter("name"),
                req.getParameter("email")
        );
        User editedUser = userService.read(id);

        req.setAttribute("user", editedUser);
        req.getRequestDispatcher("/WEB-INF/views/user/profile.jsp")
                .forward(req, resp);
    }
}
