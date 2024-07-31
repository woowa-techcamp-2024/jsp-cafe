package org.example.jspcafe.user.controller;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.jspcafe.user.User;
import org.example.jspcafe.user.service.UserService;

import java.io.IOException;

import static org.example.jspcafe.common.RequestUtil.getPathInfo;


@WebServlet("/auth/*")
public class AuthServlet extends HttpServlet {

    private UserService userService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        ServletContext context = config.getServletContext();
        this.userService = (UserService) context.getAttribute("UserService");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/user/login.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pathInfo = getPathInfo(req);
        String path = pathInfo.split("\\/")[1];
        System.out.println(path);
        if (path.equals("login")) {
            String userId = req.getParameter("userId");
            String password = req.getParameter("password");
            User user = userService.findByIdAndPw(userId, password);
            req.getSession().setAttribute("user", user);
            resp.sendRedirect("/");
            return;
        }
        if (path.equals("logout")) {
            req.getSession().invalidate();  // 세션 무효화
            resp.sendRedirect("/");
            return;
        }

        super.doPost(req, resp);
    }
}
