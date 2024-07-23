package org.example.jspcafe.user.controller;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.jspcafe.user.repository.UserRepository;
import org.example.jspcafe.user.service.UserService;

import java.io.IOException;

@WebServlet(name = "UserServlet", value = "/users/*")
public class UserServlet extends HttpServlet {
    private UserService userService;

    @Override
    public void init(ServletConfig config){
        ServletContext context = config.getServletContext();
        this.userService = (UserService) context.getAttribute("UserService");
    }


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res)
            throws IOException, ServletException {
        String pathInfo = req.getPathInfo(); // /123 형태로 반환됨

        if (pathInfo == null && pathInfo.length() <= 1) {
            res.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid user ID");
            return;
        }

        Long id = Long.valueOf(pathInfo.substring(1)); // 앞의 '/' 제거
        // userId를 사용하여 필요한 로직 수행

        req.setAttribute("user", userService.findById(id));
        req.getRequestDispatcher("/user/profile.jsp").forward(req, res);
    }
}
