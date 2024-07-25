package org.example.jspcafe.user.controller;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.jspcafe.user.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

import static org.example.jspcafe.common.StringUtils.isNumeric;

@WebServlet(name = "UserServlet", value = "/users/*")
public class UserServlet extends HttpServlet {
    private UserService userService;

    private Logger logger = LoggerFactory.getLogger(UserServlet.class);

    @Override
    public void init(ServletConfig config) {
        ServletContext context = config.getServletContext();
        this.userService = (UserService) context.getAttribute("UserService");
        logger.info("UserServlet init");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        Long id = extractPathVariable(req);
        req.setAttribute("user", userService.findById(id));
        req.getRequestDispatcher("/user/edit.jsp").forward(req, res);
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse res) throws IOException {
        Long id = extractPathVariable(req);

        String userId = req.getParameter("userId");
        String nickname = req.getParameter("nickname");
        String password = req.getParameter("password");
        String email = req.getParameter("email");

        userService.updateUser(id, userId, nickname, password, email);

        res.setStatus(HttpServletResponse.SC_OK);
        res.addHeader("Location", "/users/" + id);
    }

    private static Long extractPathVariable(HttpServletRequest req) throws IOException {
        String pathInfo = getPathInfo(req);

        String substring = pathInfo.substring(1);
        if (!isNumeric(substring)) {
            throw new IllegalArgumentException("Invalid pathInfo");
        }

        return Long.valueOf(substring);
    }

    private static String getPathInfo(HttpServletRequest req) {
        String pathInfo = req.getPathInfo();
        if (pathInfo == null || pathInfo.length() <= 1) {
            throw new IllegalArgumentException("Invalid pathInfo");
        }
        return pathInfo;
    }
}
