package com.jspcafe.user.controller;

import com.jspcafe.user.service.UserService;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet(value = "/users/*")
public class UserController extends HttpServlet {
    private UserService userService;

    @Override
    public void init() throws ServletException {
        ServletContext ctx = getServletContext();
        userService = (UserService) ctx.getAttribute("userService");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = req.getPathInfo();
        if (path.equals("/sign")) {
            forward("signup", req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    }

    private void forward(String fileName, HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/WEB-INF/views/user/" + fileName + ".jsp").forward(req, resp);
    }
}
